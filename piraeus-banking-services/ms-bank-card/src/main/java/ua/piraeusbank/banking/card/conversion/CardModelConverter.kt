package ua.piraeusbank.banking.card.conversion

import org.springframework.stereotype.Service
import ua.piraeusbank.banking.card.domain.BankCard
import ua.piraeusbank.banking.card.domain.BankCardData


internal typealias CardModelConverterAlias = (BankCard) -> BankCardData

@Service
internal class CardModelConverter : CardModelConverterAlias {

    override fun invoke(source: BankCard) =
            BankCardData(
                    id = source.id,
                    cardholderId = source.cardholderId,
                    expirationDate = source.expirationDate,
                    type = source.type,
                    binCode = source.binCode,
                    networkCode = source.network.code,
                    accountId = source.accountId
            )
}