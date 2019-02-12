package ua.piraeusbank.banking.account.repository

import org.javamoney.moneta.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.account.NotEnoughMoneyException
import ua.piraeusbank.banking.account.TransactionProcessingException
import ua.piraeusbank.banking.domain.entity.*
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.LockModeType
import javax.persistence.PersistenceContext

interface TransactionTypeRepository : JpaRepository<TransactionTypeEntity, Long> {
    fun findByCode(code: TransactionTypeCode): Optional<TransactionTypeEntity>
}

interface BaseAccountRepository : JpaRepository<AccountEntity, Long>

interface AccountRepository {

    fun getOne(accountId: Long): AccountEntity

    fun getAccountBalance(accountId: Long): Money

    fun transferMoney(sourceAccountId: Long, targetAccountId: Long, amount: Money)

    fun getCustomerReference(customerId: Long): CustomerEntity

    fun save(newAccount: AccountEntity): Long
}

@Repository
class AccountRepositoryImpl(
        @Autowired val baseAccountRepository: BaseAccountRepository)
    : AccountRepository {

    override fun save(newAccount: AccountEntity): Long {
        return baseAccountRepository.saveAndFlush(newAccount).accountId!!
    }

    override fun getOne(accountId: Long): AccountEntity = baseAccountRepository.getOne(accountId)


    override fun getAccountBalance(accountId: Long): Money = baseAccountRepository.getOne(accountId).balance

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun transferMoney(sourceAccountId: Long,
                               targetAccountId: Long,
                               amount: Money) {
        val query = em.createQuery(
                "select a from Account a where a.id in (:sourceAccountId, :targetAccountId)")
        query.setParameter("sourceAccountId", sourceAccountId)
        query.setParameter("targetAccountId", targetAccountId)
        query.lockMode = LockModeType.PESSIMISTIC_WRITE
        em.flush()

        if (query.resultList.size < 2) {
            throw TransactionProcessingException("Unable to lock!")
        }

        val sourceAccount = em.find(AccountEntity::class.java, sourceAccountId)
        val targetAccount = em.find(AccountEntity::class.java, targetAccountId)


        if (sourceAccount.status == AccountStatus.BLOCKED) {
            throw TransactionProcessingException("This account has been blocked!")
        }

        if (sourceAccount.status == AccountStatus.CLOSED) {
            throw TransactionProcessingException("This account has been closed!")
        }

        val sourceAccountBalance = sourceAccount.balance
        val targetAccountBalance = targetAccount.balance

        if (sourceAccountBalance.isLessThan(amount)) {
            throw NotEnoughMoneyException("Not enough money in the account!")
        }

        em.persist(sourceAccount.copy(balance = sourceAccountBalance.subtract(amount)))

        em.persist(targetAccount.copy(balance = targetAccountBalance.add(amount)))
    }

    override fun getCustomerReference(customerId: Long): CustomerEntity {
        return em.getReference(CustomerEntity::class.java, customerId)
    }

}