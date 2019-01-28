package ua.piraeusbank.banking.account.service

import org.javamoney.moneta.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import ua.piraeusbank.banking.account.TransactionProcessingException
import ua.piraeusbank.banking.account.domain.AccountTransaction
import ua.piraeusbank.banking.account.domain.AccountTransactionType
import ua.piraeusbank.banking.account.domain.DEFAULT_CURRENCY
import ua.piraeusbank.banking.account.repository.AccountTransactionRepository

@Service
class AccountServiceImpl(
        @Autowired val transactionExecutor: AccountTransactionExecutor,
        @Autowired val accountTransactionRepository: AccountTransactionRepository) {

    companion object {
        private const val TRANSACTION_EXECUTION_ERROR_MSG = "An error occurred during the execution of the transaction"
    }

    @Transactional
    fun checkCurrentBalance(accountId: Long) =
            transactionExecutor.execute(AccountTransactionType.CHECK) {
                Money.of(0, DEFAULT_CURRENCY)
            } ?: throw TransactionProcessingException(TRANSACTION_EXECUTION_ERROR_MSG)

    @Transactional
    fun transferMoney(request: MoneyTransferRequest) {

        transactionExecutor.execute(AccountTransactionType.TRANSFER) {

        }

    }

    @Transactional(readOnly = true)
    fun getTransaction(transactionId: Long) = accountTransactionRepository.getOne(transactionId)

    @Transactional(readOnly = true)
    fun getAccountTransactions(accountId: Long) = accountTransactionRepository.findAll()
}

@Service
class AccountTransactionExecutor(
        val transactionTemplate: TransactionTemplate) {

    fun <T> execute(transactionType: AccountTransactionType, execution: () -> T): T? {
        try {
            return transactionTemplate.execute {
                execution()
            }
        } catch (e: Throwable) {

        } finally {

        }
        return null
    }
}

data class MoneyTransferRequest(
        val sourceAccountId: String,
        val targetAccountId: String,
        val amount: Money,
        val description: String?)