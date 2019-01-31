package ua.piraeusbank.banking.account.service

import org.javamoney.moneta.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import ua.piraeusbank.banking.account.TransactionProcessingException
import ua.piraeusbank.banking.account.repository.AccountRepository
import ua.piraeusbank.banking.account.repository.TransactionRepository
import ua.piraeusbank.banking.account.repository.TransactionTypeRepository
import ua.piraeusbank.banking.common.config.AccountMoneyTransferMessage
import ua.piraeusbank.banking.domain.entity.TransactionEntity
import ua.piraeusbank.banking.domain.entity.TransactionStatus.*
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode
import ua.piraeusbank.banking.domain.entity.TransactionTypeCode.*
import java.time.Instant

interface AccountService {

    fun checkCurrentBalance(accountId: Long): Money

    fun transferMoney(request: MoneyTransferRequest)

    fun getTransaction(transactionId: Long): TransactionEntity

    fun getOutgoingTransactions(accountId: Long): List<TransactionEntity>

    fun getIncomingTransactions(accountId: Long): List<TransactionEntity>
}

@Service
class AccountServiceImpl(
        @Autowired val txExecutor: TransactionExecutor,
        @Autowired val txRepository: TransactionRepository,
        @Autowired val accountRepository: AccountRepository) : AccountService {

    companion object {
        private const val TRANSACTION_EXECUTION_ERROR_MSG = "An error occurred during the execution of the transaction"
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

@Service("checkTxExecutor")
class CheckTransactionExecutor(
        @Autowired val txTemplate: TransactionTemplate,
        @Autowired val txRepository: TransactionRepository,
        @Autowired val txTypeRepository: TransactionTypeRepository,
        @Autowired val jmsTemplate: JmsTemplate) : TransactionExecutor {

    override fun <T> execute(context: TransactionExecutorContext, execution: () -> T): T? {
        try {
            val (type, transferRequest) = context

            val transactionType = txTypeRepository.findByCode(type)

            val entity = txRepository.saveAndFlush(
                    TransactionEntity(
                            status = STARTED,
                            timestamp = Instant.now(),
                            type = transactionType,
                            amount = transferRequest?.amount,
                            sourceAccountId = transferRequest?.sourceAccountId,
                            targetAccountId = transferRequest?.targetAccountId,
                            description = transferRequest?.description))
            try {
                val result = txTemplate.execute {
                    execution()
                }
                txRepository.saveAndFlush(
                        TransactionEntity(
                                id = entity.id,
                                status = COMPLETED,
                                timestamp = Instant.now(),
                                type = transactionType))
                return result
            } catch (e: Throwable) {
                txRepository.saveAndFlush(
                        TransactionEntity(
                                id = entity.id,
                                status = FAILED,
                                timestamp = Instant.now(),
                                errorMessage = e.message,
                                type = transactionType))
                return null
            } finally {
                jmsTemplate.convertAndSend(
                        "account",
                        AccountMoneyTransferMessage(requireNotNull(entity.id)))
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


data class MoneyTransferRequest(
        val sourceAccountId: Long,
        val targetAccountId: Long,
        val amount: Money,
        val description: String?)