package ua.piraeusbank.banking.card.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.card.service.CardServiceImpl
import ua.piraeusbank.banking.card.service.Empty
import ua.piraeusbank.banking.domain.entity.*
import java.time.LocalDate
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

internal interface BaseCardRepository : JpaRepository<BankCard, Long>

internal interface CardRepository {

    fun getOne(cardId: Long): BankCard

    fun findAll(): List<BankCard>

    fun save(params: BankCardParams)

    fun save(card: BankCard)
}

@Repository
internal class CardRepositoryImpl(@Autowired private val baseCardRepository: BaseCardRepository) : CardRepository {

    @PersistenceContext
    private lateinit var em: EntityManager
    
    override fun getOne(cardId: Long): BankCard = baseCardRepository.getOne(cardId)

    override fun findAll(): List<BankCard> = baseCardRepository.findAll()

    override fun save(params: BankCardParams) {
        val newBankCard = BankCard(
                status = params.status!!,
                type = params.type!!,
                pinCode = params.pinCode!!,
                securityCode = params.securityCode!!,
                expirationDate = params.expirationDate!!,
                cardholder = em.getReference(CustomerEntity::class.java, params.customerId),
                network = params.network!!,
                binCode = params.binCode!!,
                number = params.number!!,
                account = em.getReference(AccountEntity::class.java, params.accountId)
        )
        em.persist(newBankCard)
        em.flush()
    }

    override fun save(card: BankCard) {
        baseCardRepository.saveAndFlush(card)
    }

}
