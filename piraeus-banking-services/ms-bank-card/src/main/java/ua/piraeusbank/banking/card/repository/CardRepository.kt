package ua.piraeusbank.banking.card.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ua.piraeusbank.banking.domain.entity.AccountEntity
import ua.piraeusbank.banking.domain.entity.BankCardEntity
import ua.piraeusbank.banking.domain.entity.BankCardParams
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

internal interface BaseCardRepository : JpaRepository<BankCardEntity, Long> {

    fun findCardsByCustomerId(customerId: Long): List<BankCardEntity>
}

internal interface CardRepository {

    fun getOne(cardId: Long): BankCardEntity

    fun findCardsByCustomerId(customerId: Long): List<BankCardEntity>

    fun save(params: BankCardParams)

    fun save(card: BankCardEntity)
}

@Repository
internal class CardRepositoryImpl(@Autowired private val baseCardRepository: BaseCardRepository) : CardRepository {


    @PersistenceContext
    private lateinit var em: EntityManager
    
    override fun getOne(cardId: Long): BankCardEntity = baseCardRepository.getOne(cardId)

    override fun findCardsByCustomerId(customerId: Long): List<BankCardEntity> = baseCardRepository.findCardsByCustomerId(customerId)

    override fun save(params: BankCardParams) {
        val newBankCard = BankCardEntity(
                name = "Payment Card",
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

    override fun save(card: BankCardEntity) {
        baseCardRepository.saveAndFlush(card)
    }

}
