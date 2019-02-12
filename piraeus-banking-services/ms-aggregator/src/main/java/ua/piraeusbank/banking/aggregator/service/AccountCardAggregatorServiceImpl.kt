package ua.piraeusbank.banking.aggregator.service

import okhttp3.Interceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Service
import ua.piraeusbank.banking.aggregator.conversion.CardConvertParams
import ua.piraeusbank.banking.aggregator.conversion.CardConverter
import ua.piraeusbank.banking.common.domain.AccountAndCardCreationRequest
import ua.piraeusbank.banking.common.domain.CardMoneyTransferRequest
import ua.piraeusbank.banking.common.domain.PaymentCard
import ua.piraeusbank.banking.domain.entity.CardNetworkCode
import ua.piraeusbank.banking.domain.model.AccountCreationRequest
import ua.piraeusbank.banking.domain.model.CardAccountCreationMessage
import ua.piraeusbank.banking.domain.model.MoneyTransferRequest
import ua.piraeusbank.banking.domain.model.OrderCardRequest
import ua.piraeusbank.banking.internal.api.AccountRestClient
import ua.piraeusbank.banking.internal.api.CardRestClient
import ua.piraeusbank.banking.internal.api.RetrofitServiceGenerator

interface AccountCardAggregatorService {
    fun createCardAndAccount(request: AccountAndCardCreationRequest)
    fun findPaymentCard(cardId: Long): PaymentCard
    fun findPaymentCardsByCustomerId(customerId: Long): List<PaymentCard>
    fun transferBetweenCards(request: CardMoneyTransferRequest)
}

@Service
class AccountCardAggregatorServiceImpl(
        @Autowired private val discoveryClient: DiscoveryClient,
        @Autowired private val cardConverter: CardConverter,
        @Qualifier("tokenAuthenticator")
        @Autowired private val tokenAuthenticator: Interceptor) : AccountCardAggregatorService {

    private val cardRestClient: CardRestClient = RetrofitServiceGenerator.createService("http://localhost:8002/cards/", tokenAuthenticator)
    private val accountRestClient: AccountRestClient = RetrofitServiceGenerator.createService("http://localhost:8000/accounts/", tokenAuthenticator)


    override fun createCardAndAccount(request: AccountAndCardCreationRequest) {
        val accountId = accountRestClient.createAccount(
                AccountCreationRequest(request.customerId, request.currencyCode, request.balance)).execute().body()
        cardRestClient.orderCard(
                OrderCardRequest(
                        request.customerId,
                        accountId,
                        CardNetworkCode.valueOf(request.networkCode))).execute()
    }

    override fun findPaymentCard(cardId: Long): PaymentCard {
        val card = cardRestClient.getCardById(cardId).execute().body()
        val balance = accountRestClient.checkCurrentBalance(card.account.accountId!!).execute().body()
        return cardConverter.convert(card, CardConvertParams(balance))
    }

    override fun findPaymentCardsByCustomerId(customerId: Long): List<PaymentCard> {
        val cards = cardRestClient.findCardsByCustomerId(customerId).execute().body()
        return cards.map {
            val balance = accountRestClient.checkCurrentBalance(it.account.accountId!!).execute().body()
            cardConverter.convert(it, CardConvertParams(balance))
        }
    }

    override fun transferBetweenCards(request: CardMoneyTransferRequest) {
        val sourceCard = cardRestClient.getCardById(request.sourceCardId).execute().body()
        val targetCard = cardRestClient.getCardById(request.targetCardId).execute().body()

        accountRestClient.transferMoney(MoneyTransferRequest(
                sourceAccountId = sourceCard.account.accountId!!,
                targetAccountId = targetCard.account.accountId!!,
                description = request.description,
                amount = request.amount))
    }

    @JmsListener(destination = "accountCreation", containerFactory = "connectionFactory")
    fun handleDefaultCardAccountCreation(message: CardAccountCreationMessage) {
        createCardAndAccount(message.request)
    }
}

