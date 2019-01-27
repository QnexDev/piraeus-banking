package ua.piraeusbank.banking.card.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.card.CardProcessingException
import ua.piraeusbank.banking.card.domain.BankCard
import ua.piraeusbank.banking.card.domain.BankCardState.*
import ua.piraeusbank.banking.card.domain.BankCardType.DEBIT
import ua.piraeusbank.banking.card.repository.BankCardRepository
import ua.piraeusbank.banking.card.repository.BankNetworkRepository
import java.time.LocalDate

interface BankCardService {

    fun getCardById(cardId: Long): BankCard

    fun findAllCards(): List<BankCard>

    fun issueCard(issueCardRequest: IssueCardRequest)

    fun closeCard(cardId: Long)

    fun blockCard(cardId: Long)

    fun unblockCard(cardId: Long)

    fun changePinCode(cardId: Long, request: ChangePinCodeRequest)

    fun getSecurityCode(cardId: Long): Short
}


@Service
class BankCardServiceImpl(
        @Autowired private val bankCardRepository: BankCardRepository,
        @Autowired private val bankNetworkRepository: BankNetworkRepository,
        @Qualifier("pinCodeGenerator")
        private val pinCodeGenerator: PinCodeGeneratorAlias,
        @Qualifier("securityCodeGenerator")
        @Autowired
        private val securityCodeGenerator: SecurityCodeGeneratorAlias,
        @Qualifier("cardNumberGenerator")
        @Autowired
        private val cardNumberGenerator: CardNumberGeneratorAlias) : BankCardService {

    companion object {
        const val SERVICE_DURATION_IN_YEAR = 3L
        const val PIN_CODE_DIGITS_LENGTH = 4
    }

    @Transactional(readOnly = true)
    override fun getCardById(cardId: Long): BankCard {
        return bankCardRepository.getOne(cardId)
    }

    @Transactional(readOnly = true)
    override fun findAllCards(): List<BankCard> {
        return bankCardRepository.findAll()
    }

    @Transactional
    override fun issueCard(issueCardRequest: IssueCardRequest) {
        val cardNetwork = bankNetworkRepository.findByCode(issueCardRequest.networkCode)

        val (number, binCode) = cardNumberGenerator.generate(cardNetwork.code)

        val newBankCard = BankCard(
                state = OPENED,
                type = DEBIT,
                pinCode = pinCodeGenerator.generate(Empty),
                securityCode = securityCodeGenerator.generate(Empty),
                expirationDate = LocalDate.now().plusYears(SERVICE_DURATION_IN_YEAR),
                cardholderId = issueCardRequest.customerId,
                network = cardNetwork,
                binCode = binCode,
                number = number
        )
        bankCardRepository.saveAndFlush(newBankCard)
    }

    @Transactional
    override fun closeCard(cardId: Long) {
        val bankCard = bankCardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        bankCardRepository.saveAndFlush(bankCard.copy(state = CLOSED))
    }

    @Transactional
    override fun blockCard(cardId: Long) {
        val bankCard = bankCardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        throwIfBlocked(bankCard)
        bankCardRepository.saveAndFlush(bankCard.copy(state = BLOCKED))
    }

    @Transactional
    override fun unblockCard(cardId: Long) {
        val bankCard = bankCardRepository.getOne(cardId)
        throwIfClosed(bankCard)
        throwIfOpened(bankCard)
        bankCardRepository.saveAndFlush(bankCard.copy(state = BLOCKED))
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
        if (bankCard.state == OPENED) {
            throw CardProcessingException("This card is opened!")
        }
    }

    private fun throwIfBlocked(bankCard: BankCard) {
        if (bankCard.state == BLOCKED) {
            throw CardProcessingException("This card already has been blocked!")
        }
    }

    private fun throwIfClosed(bankCard: BankCard) {
        if (bankCard.state == CLOSED) {
            throw CardProcessingException("This card already has been closed!")
        }
    }
}

data class IssueCardRequest(val customerId: Long, val networkCode: String)
data class ChangePinCodeRequest(val cardId: Long, val newPinCode: Short)