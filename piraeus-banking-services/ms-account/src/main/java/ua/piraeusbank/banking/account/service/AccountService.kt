package ua.piraeusbank.banking.account.service

import org.javamoney.moneta.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import ua.piraeusbank.banking.account.TransactionProcessingException
import ua.piraeusbank.banking.account.repository.*
import ua.piraeusbank.banking.domain.entity.*
import ua.piraeusbank.banking.domain.entity.TransactionStatus.*
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode.CHECK
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode.TRANSFER
import ua.piraeusbank.banking.domain.model.AccountCreationRequest
import ua.piraeusbank.banking.domain.model.AccountMoneyTransferMessage
import ua.piraeusbank.banking.domain.model.MoneyTransferRequest
import java.time.Instant
import java.time.LocalDateTime
import javax.annotation.PostConstruct

interface AccountService {

    fun checkCurrentBalance(accountId: Long): Money

    fun transferMoney(request: MoneyTransferRequest)

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
                balance = Money.of(0, DEFAULT_CURRENCY),
                currency = currencyRepository.getByCurrencyCode(request.currencyCode).get(),
                customer = accountRepository.getCustomerReference(request.customerId)
        )

        return accountRepository.save(newAccount)
    }

    @Transactional
    override fun checkCurrentBalance(accountId: Long) = txExecutor.execute(context(CHECK)) {
        accountRepository.getAccountBalance(accountId)
    } ?: throw TransactionProcessingException(TRANSACTION_EXECUTION_ERROR_MSG)

    @Transactional
    override fun transferMoney(request: MoneyTransferRequest) = txExecutor.execute(context(TRANSFER, request)) {
        accountRepository.transferMoney(request.sourceAccountId, request.sourceAccountId, request.amount)
    } ?: throw TransactionProcessingException(TRANSACTION_EXECUTION_ERROR_MSG)

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
            val sourceAccount = transferRequest?.sourceAccountId?.let { accountRepository.getOne(it) }?.let {
                if (type == TRANSFER) {
                    throw IllegalStateException("sourceAccountId hasn't found")
                }
                it
            }
            val targetAccount = transferRequest?.targetAccountId?.let { accountRepository.getOne(it) }?.let {
                if (type == TRANSFER) {
                    throw IllegalStateException("targetAccountId hasn't found")
                }
                it
            }

            val entity = txRepository.saveAndFlush(
                    TransactionEntity(
                            status = STARTED,
                            timestamp = Instant.now(),
                            type = transactionType,
                            amount = transferRequest?.amount,
                            sourceAccount = sourceAccount,
                            targetAccount = targetAccount,
                            description = transferRequest?.description))
            try {
                val result = txTemplate.execute {
                    execution()
                }
                val completedTransaction = txRepository.saveAndFlush(
                        TransactionEntity(
                                id = entity.id,
                                status = COMPLETED,
                                timestamp = Instant.now(),
                                type = transactionType))
                jmsTemplate.convertAndSend(
                        "account",
                        AccountMoneyTransferMessage(completedTransaction))
                return result
            } catch (e: Throwable) {
                val failedTransaction = TransactionEntity(
                        id = entity.id,
                        status = FAILED,
                        timestamp = Instant.now(),
                        errorMessage = e.message,
                        type = transactionType)
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