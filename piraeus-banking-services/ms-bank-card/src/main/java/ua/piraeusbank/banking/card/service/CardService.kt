package ua.piraeusbank.banking.card.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.card.CardProcessingException
import ua.piraeusbank.banking.card.conversion.CardModelConverterAlias
import ua.piraeusbank.banking.domain.entity.BankCard
import ua.piraeusbank.banking.domain.entity.BankCardData
import ua.piraeusbank.banking.domain.entity.BankCardStatus.*
import ua.piraeusbank.banking.domain.entity.BankCardType.DEBIT
import ua.piraeusbank.banking.domain.entity.CardNetworkCode
import ua.piraeusbank.banking.card.repository.CardRepository
import ua.piraeusbank.banking.card.repository.CardNetworkRepository
import ua.piraeusbank.banking.domain.entity.BankCardParams
import java.time.LocalDate

internal interface CardService {

    fun getCardInfoById(cardId: Long): BankCardData

    fun findAllCardInfos(): List<BankCardData>

    fun orderCard(request: IssueCardRequest)

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
        private val cardNumberGenerator: CardNumberGeneratorAlias,
        @Autowired private val cardModelConverter: CardModelConverterAlias) : CardService {

    companion object {
        const val SERVICE_DURATION_IN_YEAR = 3L
        const val PIN_CODE_DIGITS_LENGTH = 4
    }

    @Transactional(readOnly = true)
    override fun getCardInfoById(cardId: Long): BankCardData {
        return cardModelConverter(cardRepository.getOne(cardId))
    }

    @Transactional(readOnly = true)
    override fun findAllCardInfos(): List<BankCardData> {
        return cardRepository.findAll().map { cardModelConverter(it) }
    }

    @Transactional
    override fun orderCard(request: IssueCardRequest) {
        val cardNetwork = cardNetworkRepository.findByCode(request.networkCode)

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

    private fun throwIfOpened(bankCard: BankCard) {
        if (bankCard.status == OPENED) {
            throw CardProcessingException("This card is opened!")
        }
    }

    private fun throwIfBlocked(bankCard: BankCard) {
        if (bankCard.status == BLOCKED) {
            throw CardProcessingException("This card has been blocked!")
        }
    }

    private fun throwIfClosed(bankCard: BankCard) {
        if (bankCard.status == CLOSED) {
            throw CardProcessingException("This card has been closed!")
        }
    }
}

data class IssueCardRequest(val customerId: Long, val accountId: Long, val networkCode: CardNetworkCode)
data class ChangePinCodeRequest(val cardId: Long, val newPinCode: Short)