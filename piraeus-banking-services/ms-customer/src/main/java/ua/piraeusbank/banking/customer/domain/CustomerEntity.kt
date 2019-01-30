package ua.piraeusbank.banking.customer.domain

import org.javamoney.moneta.Money
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity

@Entity
data class CustomerEntity(
        @Column(name = "customer_id") val customerId: Long,
        @Column(name = "name") val name: String,
        @Column(name = "last_name") val lastName: String,
        @Column(name = "phone_number") val phoneNumber: String,
        @Column(name = "email") val email: String,
        @Column(name = "date_of_birthday") val dateOfBirthday: LocalDate
)

@Entity
data class StatementEntity(
        @Column(name = "statement_id") val statement_id: Long,
        @Column(name = "customer_id") val customerId: Long,
        @Column(name = "date") val date: LocalDate,
        @Column(name = "type") val type: String,
        @Column(name = "description") val description: String,
        @Column(name = "paid_in") val paidIn: Money,
        @Column(name = "paid_out") val paidOut: Money,
        @Column(name = "balance") val balance: Money
)