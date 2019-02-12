package ua.piraeusbank.banking.domain.model

import org.javamoney.moneta.Money
import ua.piraeusbank.banking.domain.entity.CardNetworkCode
import java.time.LocalDate

data class MoneyTransferRequest(
        val sourceAccountId: Long,
        val targetAccountId: Long,
        val amount: Money,
        val description: String?)

data class AccountCreationRequest(
        val customerId: Long,
        val currencyCode: String,
        val balance: Money = Money.of(0, "UAH"))



data class OrderCardRequest(val customerId: Long, val accountId: Long, val networkCode: CardNetworkCode)

data class CustomerRegistrationRequest(val phoneNumber: String,
                                       val email: String,
                                       val password: String,
                                       val name: String,
                                       val lastName: String,
                                       val dateOfBirthday: LocalDate)