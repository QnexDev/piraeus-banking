package ua.piraeusbank.banking.card.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.card.CardProcessingException
import ua.piraeusbank.banking.card.repository.CardNetworkRepository
import ua.piraeusbank.banking.card.repository.CardRepository
import ua.piraeusbank.banking.common.domain.ChangePinCodeRequest
import ua.piraeusbank.banking.domain.entity.BankCardEntity
import ua.piraeusbank.banking.domain.entity.BankCardNetworkEntity
import ua.piraeusbank.banking.domain.entity.BankCardParams
import ua.piraeusbank.banking.domain.entity.BankCardStatus.*
import ua.piraeusbank.banking.domain.entity.BankCardType.DEBIT
import ua.piraeusbank.banking.domain.entity.CardNetworkCode
import ua.piraeusbank.banking.domain.model.OrderCardRequest
import java.time.LocalDate
import javax.annotation.PostConstruct

internal interface CardService {

    fun getCardById(cardId: Long): BankCardEntity

    fun findCardsByCustomerId(customerId: Long): List<BankCardEntity>

    fun orderCard(request: OrderCardRequest)

    fun closeCard(cardId: Long)

    fun blockCard(cardId: Long)

    fun unblockCard(cardId: Long)

    fun changePinCode(cardId: Long, request: ChangePinCodeRequest)

    fun getSecurityCode(cardId: Long): Short
}


@Service
internal class CardServiceImpl(
        @Autowired private val cardRepository: CardRepository,
        @Autowired private val cardNetworkRepository: CardNetworkRepository,
        @Qualifier("pinCodeGenerator")
        private val pinCodeGenerator: PinCodeGeneratorAlias,
        @Qualifier("securityCodeGenerator")
        @Autowired
        private val securityCodeGenerator: SecurityCodeGeneratorAlias,
        @Qualifier("cardNumberGenerator")
        @Autowired
        private val cardNumberGenerator: CardNumberGeneratorAlias) : CardService {

    companion object {
        const val SERVICE_DURATION_IN_YEAR = 3L
        const val PIN_CODE_DIGITS_LENGTH = 4
    }

    @PostConstruct
    fun init() {
        cardNetworkRepository.findByCode(CardNetworkCode.VISA).orElseGet {
            cardNetworkRepository.saveAndFlush(BankCardNetworkEntity(
                    description = "VISA",
                    code = CardNetworkCode.VISA,
                    name = "VISA",
                    prefixNumbers = 4))
        }
        cardNetworkRepository.findByCode(CardNetworkCode.MASTERCARD).orElseGet {
            cardNetworkRepository.saveAndFlush(BankCardNetworkEntity(
                    description = "MASTERCARD",
                    code = CardNetworkCode.MASTERCARD,
                    name = "MASTERCARD",
                    prefixNumbers = 5))
        }
    }

    @Transactional(readOnly = true)
    override fun getCardById(cardId: Long): BankCardEntity {
        return cardRepository.getOne(cardId)
    }

    @Transactional(readOnly = true)
    override fun findCardsByCustomerId(customerId: Long): List<BankCardEntity> {
        return cardRepository.findCardsByCustomerId(customerId)
    }

    @Transactional
    override fun orderCard(request: OrderCardRequest) {
        val cardNetwork = cardNetworkRepository.findByCode(request.networkCode).get()

        val (number, binCode) = cardNumberGenerator.generate(cardNetwork)

        val newBankCard = BankCardParams(
                status = OPENED,
                type = DEBIT,
                pinCode = pinCodeGenerator.generate(Empty),
                securityCode = securityCodeGenerator.generate(Empty),
                expirationDate = LocalDate.now().plusYears(SERVICE_DURATION_IN_YEAR),
                customerId = request.customerId,
                network = cardNetwork,
                binCode = binCode,
                number = number,
                accountId = request.accountId
        )
        cardRepository.save(newBankCard)
    }

    @Transactional
    override fun closeCard(cardId: Long) {
        val bankCard = cardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        cardRepository.save(bankCard.copy(status = CLOSED))
    }

    @Transactional
    override fun blockCard(cardId: Long) {
        val bankCard = cardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        throwIfBlocked(bankCard)
        cardRepository.save(bankCard.copy(status = BLOCKED))
    }

    @Transactional
    override fun unblockCard(cardId: Long) {
        val bankCard = cardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        throwIfOpened(bankCard)
        cardRepository.save(bankCard.copy(status = BLOCKED))
    }

    @Transactional
    override fun changePinCode(cardId: Long, request: ChangePinCodeRequest) {
        val bankCard = cardRepository.getOne(cardId)
        throwIfBlocked(bankCard)
        throwIfClosed(bankCard)
        if (request.newPinCode.toString().length != PIN_CODE_DIGITS_LENGTH) {
            throw CardProcessingException("Wrong length of PIN code!")
        }
        cardRepository.save(bankCard.copy(pinCode = request.newPinCode))
    }

    @Transactional(readOnly = true)
    override fun getSecurityCode(cardId: Long): Short {
        val bankCard = cardRepository.getOne(cardId)
        throwIfBlocked(bankCard)
        throwIfClosed(bankCard)
        return bankCard.securityCode
    }

    private fun throwIfOpened(bankCard: BankCardEntity) {
        if (bankCard.status == OPENED) {
            throw CardProcessingException("This card is opened!")
        }
    }

    private fun throwIfBlocked(bankCard: BankCardEntity) {
        if (bankCard.status == BLOCKED) {
            throw CardProcessingException("This card has been blocked!")
        }
    }

    private fun throwIfClosed(bankCard: BankCardEntity) {
        if (bankCard.status == CLOSED) {
            throw CardProcessingException("This card has been closed!")
        }
    }
}