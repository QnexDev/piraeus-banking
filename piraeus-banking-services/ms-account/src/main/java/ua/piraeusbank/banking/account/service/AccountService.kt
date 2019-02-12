package ua.piraeusbank.banking.account.service

import org.javamoney.moneta.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import ua.piraeusbank.banking.account.repository.*
import ua.piraeusbank.banking.domain.entity.*
import ua.piraeusbank.banking.domain.entity.TransactionStatus.COMPLETED
import ua.piraeusbank.banking.domain.entity.TransactionStatus.FAILED
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode.CHECK
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode.TRANSFER
import ua.piraeusbank.banking.domain.model.AccountCreationRequest
import ua.piraeusbank.banking.domain.model.AccountMoneyTransferMessage
import ua.piraeusbank.banking.domain.model.MoneyTransferRequest
import java.time.Instant
import java.time.LocalDateTime
import javax.annotation.PostConstruct

interface AccountService {

    fun checkCurrentBalance(accountId: Long): Money?

    fun transferMoney(request: MoneyTransferRequest): Unit?

    fun getTransaction(transactionId: Long): TransactionEntity

    fun getOutgoingTransactions(accountId: Long): List<TransactionEntity>

    fun getIncomingTransactions(accountId: Long): List<TransactionEntity>

    fun createAccount(request: AccountCreationRequest): Long
}

@Service
class AccountServiceImpl(
        @Autowired val txExecutor: TransactionExecutor,
        @Autowired val txRepository: TransactionRepository,
        @Autowired val accountRepository: AccountRepository,
        @Autowired val accountTypeRepository: AccountTypeRepository,
        @Autowired val currencyRepository: CurrencyRepository,
        @Autowired val txTypeRepository: TransactionTypeRepository) : AccountService {

    @PostConstruct
    @Transactional
    fun init() {
        accountTypeRepository.findByName("CURRENT").orElseGet {
            accountTypeRepository.saveAndFlush(
                    AccountTypeEntity(name = "CURRENT", description = "current account type"))
        }
        txTypeRepository.findByCode(CHECK).orElseGet {
            txTypeRepository.saveAndFlush(TransactionTypeEntity(code = CHECK, description = "check transaction type"))
        }

        txTypeRepository.findByCode(TRANSFER).orElseGet {
            txTypeRepository.saveAndFlush(TransactionTypeEntity(code = TRANSFER, description = "transfer transaction type"))
        }
        currencyRepository.getByCurrencyCode("UAH").orElseGet {
            currencyRepository.saveAndFlush(CurrencyEntity(currencyCode = "UAH", numericCode = 1))
        }
    }

    companion object {
        private const val TRANSACTION_EXECUTION_ERROR_MSG = "An error occurred during the execution of the transaction"
    }

    @Transactional
    override fun createAccount(request: AccountCreationRequest): Long {
        val accountType = accountTypeRepository.findByName("CURRENT")
                .orElseThrow { IllegalArgumentException("Wrong account type") }

        val newAccount = AccountEntity(
                status = AccountStatus.OPENED,
                accountType = accountType,
                creationDate = LocalDateTime.now(),
                balance = request.balance,
                currency = currencyRepository.getByCurrencyCode(request.currencyCode).get(),
                customer = accountRepository.getCustomerReference(request.customerId)
        )

        return accountRepository.save(newAccount)
    }

    @Transactional
    override fun checkCurrentBalance(accountId: Long) = txExecutor.execute(context(CHECK)) {
        accountRepository.getAccountBalance(accountId)
    }

    @Transactional
    override fun transferMoney(request: MoneyTransferRequest) {
        txExecutor.execute(context(TRANSFER, request)) {
            accountRepository.transferMoney(request.sourceAccountId, request.targetAccountId, request.amount)
        }
    }

    @Transactional(readOnly = true)
    override fun getTransaction(transactionId: Long): TransactionEntity = txRepository.getOne(transactionId)

    @Transactional(readOnly = true)
    override fun getOutgoingTransactions(accountId: Long) = txRepository.getOutgoingTransactionsByAccountId(accountId)

    @Transactional(readOnly = true)
    override fun getIncomingTransactions(accountId: Long) = txRepository.getIncomingTransactionsByAccountId(accountId)
}

interface TransactionExecutor {

    fun <T> execute(context: TransactionExecutorContext, execution: () -> T): T?

}

@Service("txExecutor")
class TransactionExecutorImpl(
        @Autowired val txTemplate: TransactionTemplate,
        @Autowired val txRepository: TransactionRepository,
        @Autowired val txTypeRepository: TransactionTypeRepository,
        @Autowired val accountRepository: AccountRepository,
        @Autowired val jmsTemplate: JmsTemplate) : TransactionExecutor {

    override fun <T> execute(context: TransactionExecutorContext, execution: () -> T): T? {
        try {
            val (type, transferRequest) = context

            val transactionType = txTypeRepository.findByCode(type)
                    .orElseThrow { IllegalStateException("Wrong transaction code!") }
            val sourceAccount = transferRequest?.sourceAccountId?.let {
                accountRepository.findById(it).orElseGet {
                    if (type == TRANSFER) {
                        throw IllegalStateException("sourceAccountId hasn't found")
                    }
                    null
                }
            }
            val targetAccount = transferRequest?.targetAccountId?.let {
                accountRepository.findById(it).orElseGet {
                    if (type == TRANSFER) {
                        throw IllegalStateException("targetAccountId hasn't found")
                    }
                    null
                }
            }

//            val entity = txRepository.saveAndFlush(
//                    TransactionEntity(
//                            status = STARTED,
//                            timestamp = Instant.now(),
//                            type = transactionType,
//                            amount = transferRequest?.amount,
//                            sourceAccount = sourceAccount,
//                            targetAccount = targetAccount,
//                            description = transferRequest?.description))
            try {
                execution()
                val completedTransaction = txRepository.saveAndFlush(
                        TransactionEntity(
//                                id = entity.id,
                                status = COMPLETED,
                                timestamp = Instant.now(),
                                type = transactionType,
                                description = transferRequest?.description,
                                amount = transferRequest?.amount,
                                sourceAccount = sourceAccount,
                                targetAccount = targetAccount))
                jmsTemplate.convertAndSend(
                        "account",
                        AccountMoneyTransferMessage(completedTransaction))
                return null
            } catch (e: Throwable) {
                val failedTransaction = TransactionEntity(
//                        id = entity.id,
                        status = FAILED,
                        timestamp = Instant.now(),
                        errorMessage = e.message,
                        type = transactionType,
                        description = transferRequest?.description,
                        amount = transferRequest?.amount,
                        sourceAccount = sourceAccount,
                        targetAccount = targetAccount)
                txRepository.saveAndFlush(
                        failedTransaction)
                jmsTemplate.convertAndSend(
                        "account",
                        AccountMoneyTransferMessage(failedTransaction))
                return null
            }
        } catch (e: Throwable) {
            return null
        }
    }
}

fun context(type: TransactionTypeCode,
            moneyTransferRequest: MoneyTransferRequest? = null) =
        TransactionExecutorContext(type, moneyTransferRequest)


data class TransactionExecutorContext(val type: TransactionTypeCode,
                                      val moneyTransferRequest: MoneyTransferRequest? = null)