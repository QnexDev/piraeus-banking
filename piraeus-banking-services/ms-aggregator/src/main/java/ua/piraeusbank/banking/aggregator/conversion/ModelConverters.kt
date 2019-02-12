package ua.piraeusbank.banking.aggregator.conversion

import org.javamoney.moneta.Money
import org.springframework.stereotype.Service
import ua.piraeusbank.banking.common.domain.CardTransaction
import ua.piraeusbank.banking.common.domain.PaymentCard
import ua.piraeusbank.banking.domain.entity.BankCardEntity
import ua.piraeusbank.banking.domain.entity.TransactionEntity


interface Converter<T, P, V> {
    fun convert(source: T, params: P): V
}

data class TransactionConvertParams(val sourceCard: PaymentCard, val targetCard: PaymentCard)
data class CardConvertParams(val balance: Money)

typealias TransactionConverter = Converter<TransactionEntity, TransactionConvertParams, CardTransaction>
typealias CardConverter = Converter<BankCardEntity, CardConvertParams, PaymentCard>

@Service
class TransactionConverterImpl : TransactionConverter {

    override fun convert(source: TransactionEntity, params: TransactionConvertParams) =
            CardTransaction(
                    id = source.id,
                    amount = source.amount,
                    description = source.description,
                    errorMessage = source.errorMessage,
                    timestamp = source.timestamp,
                    sourceCard = params.sourceCard,
                    targetCard = params.targetCard
            )
}

@Service
class CardConverterImpl : CardConverter {

    override fun convert(source: BankCardEntity, params: CardConvertParams) =
            PaymentCard(
                    id = source.id!!,
                    accountId = source.account.accountId!!,
                    name = source.name,
                    type = source.type.name,
                    networkCode = source.network.name,
                    binCode = source.binCode,
                    cardholderId = source.cardholder.customerId!!,
                    expirationDate = source.expirationDate,
                    balance = params.balance
            )
}