package ua.piraeusbank.banking.gateway

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.stereotype.Service
import ua.piraeusbank.banking.common.domain.AccountAndCardCreationRequest
import ua.piraeusbank.banking.common.domain.CardMoneyTransferRequest
import ua.piraeusbank.banking.common.domain.PaymentCard
import ua.piraeusbank.banking.domain.entity.CardNetworkCode
import ua.piraeusbank.banking.domain.model.AccountCreationRequest
import ua.piraeusbank.banking.domain.model.MoneyTransferRequest
import ua.piraeusbank.banking.domain.model.OrderCardRequest
import ua.piraeusbank.banking.gateway.conversion.CardConvertParams
import ua.piraeusbank.banking.gateway.conversion.CardConverter
import ua.piraeusbank.banking.gateway.rest.client.internal.AccountRestClient
import ua.piraeusbank.banking.gateway.rest.client.internal.CardRestClient
import ua.piraeusbank.banking.gateway.rest.client.internal.RetrofitServiceGenerator

interface AccountCardAggregatorService {
    fun createCardAndAccount(request: AccountAndCardCreationRequest)
    fun findPaymentCard(cardId: Long): PaymentCard
    fun findPaymentCardsByCustomerId(customerId: Long): List<PaymentCard>
    fun transferMoneyBetweenCards(request: CardMoneyTransferRequest)
}

@Service
class AccountCardAggregatorServiceImpl(
        @Autowired private val  discoveryClient: DiscoveryClient,
        @Autowired private val cardConverter: CardConverter) : AccountCardAggregatorService {

    private val cardRestClient: CardRestClient = RetrofitServiceGenerator.createService("card")
    private val accountRestClient: AccountRestClient = RetrofitServiceGenerator.createService("account")


    override fun createCardAndAccount(request: AccountAndCardCreationRequest) {
        val accountId = accountRestClient.createAccount(AccountCreationRequest(request.customerId, request.currencyCode))
        cardRestClient.orderCard(OrderCardRequest(request.customerId, accountId, CardNetworkCode.valueOf(request.networkCode)))
    }

    override fun findPaymentCard(cardId: Long): PaymentCard {
        val card = cardRestClient.getCardById(cardId)
        val balance = accountRestClient.checkCurrentBalance(card.account.accountId!!)
        return cardConverter.convert(card, CardConvertParams(balance))
    }

    override fun findPaymentCardsByCustomerId(customerId: Long): List<PaymentCard> {
        val cards = cardRestClient.findCardsByCustomerId(customerId)
        return cards.map {
            val balance = accountRestClient.checkCurrentBalance(it.account.accountId!!)
            cardConverter.convert(it, CardConvertParams(balance))
        }
    }

    override fun transferMoneyBetweenCards(request: CardMoneyTransferRequest) {
        val sourceCard = cardRestClient.getCardById(request.sourceCardId)
        val targetCard = cardRestClient.getCardById(request.targetCardId)

        accountRestClient.transferMoney(MoneyTransferRequest(
                sourceAccountId = sourceCard.account.accountId!!,
                targetAccountId = targetCard.account.accountId!!,
                description = request.description,
                amount = request.amount))
    }
}

