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
import ua.piraeusbank.banking.card.repository.BankCardRepository
import ua.piraeusbank.banking.card.repository.BankNetworkRepository
import java.time.LocalDate

internal interface BankCardService {

    fun getCardInfoById(cardId: Long): BankCardData

    fun findAllCardInfos(): List<BankCardData>

    fun issueCard(issueCardRequest: IssueCardRequest)

    fun closeCard(cardId: Long)

    fun blockCard(cardId: Long)

    fun unblockCard(cardId: Long)

    fun changePinCode(cardId: Long, request: ChangePinCodeRequest)

    fun getSecurityCode(cardId: Long): Short
}


@Service
internal class BankCardServiceImpl(
        @Autowired private val bankCardRepository: BankCardRepository,
        @Autowired private val bankNetworkRepository: BankNetworkRepository,
        @Qualifier("pinCodeGenerator")
        private val pinCodeGenerator: PinCodeGeneratorAlias,
        @Qualifier("securityCodeGenerator")
        @Autowired
        private val securityCodeGenerator: SecurityCodeGeneratorAlias,
        @Qualifier("cardNumberGenerator")
        @Autowired
        private val cardNumberGenerator: CardNumberGeneratorAlias,
        @Autowired private val cardModelConverter: CardModelConverterAlias) : BankCardService {

    companion object {
        const val SERVICE_DURATION_IN_YEAR = 3L
        const val PIN_CODE_DIGITS_LENGTH = 4
    }

    @Transactional(readOnly = true)
    override fun getCardInfoById(cardId: Long): BankCardData {
        return cardModelConverter(bankCardRepository.getOne(cardId))
    }

    @Transactional(readOnly = true)
    override fun findAllCardInfos(): List<BankCardData> {
        return bankCardRepository.findAll().map { cardModelConverter(it) }
    }

    @Transactional
    override fun issueCard(issueCardRequest: IssueCardRequest) {
        val cardNetwork = bankNetworkRepository.findByCode(issueCardRequest.networkCode)

        val (number, binCode) = cardNumberGenerator.generate(cardNetwork)

        val newBankCard = BankCard(
                status = OPENED,
                type = DEBIT,
                pinCode = pinCodeGenerator.generate(Empty),
                securityCode = securityCodeGenerator.generate(Empty),
                expirationDate = LocalDate.now().plusYears(SERVICE_DURATION_IN_YEAR),
                cardholderId = issueCardRequest.customerId,
                network = cardNetwork,
                binCode = binCode,
                number = number,
                //FIXME
                accountId = 0L
        )
        bankCardRepository.saveAndFlush(newBankCard)
    }

    @Transactional
    override fun closeCard(cardId: Long) {
        val bankCard = bankCardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        bankCardRepository.saveAndFlush(bankCard.copy(status = CLOSED))
    }

    @Transactional
    override fun blockCard(cardId: Long) {
        val bankCard = bankCardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        throwIfBlocked(bankCard)
        bankCardRepository.saveAndFlush(bankCard.copy(status = BLOCKED))
    }

    @Transactional
    override fun unblockCard(cardId: Long) {
        val bankCard = bankCardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        throwIfOpened(bankCard)
        bankCardRepository.saveAndFlush(bankCard.copy(status = BLOCKED))
    }

    @Transactional
    override fun changePinCode(cardId: Long, request: ChangePinCodeRequest) {
        val bankCard = bankCardRepository.getOne(cardId)
        throwIfBlocked(bankCard)
        throwIfClosed(bankCard)
        if (request.newPinCode.toString().length != PIN_CODE_DIGITS_LENGTH) {
            throw CardProcessingException("Wrong length of PIN code!")
        }
        bankCardRepository.saveAndFlush(bankCard.copy(pinCode = request.newPinCode))
    }

    @Transactional(readOnly = true)
    override fun getSecurityCode(cardId: Long): Short {
        val bankCard = bankCardRepository.getOne(cardId)
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

data class IssueCardRequest(val customerId: Long, val networkCode: CardNetworkCode)
data class ChangePinCodeRequest(val cardId: Long, val newPinCode: Short)