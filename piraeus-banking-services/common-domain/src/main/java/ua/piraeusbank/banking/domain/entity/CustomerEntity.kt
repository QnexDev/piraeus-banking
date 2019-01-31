package ua.piraeusbank.banking.domain.entity

import org.javamoney.moneta.Money
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "CUSTOMER")
data class CustomerEntity(
        @Id @Column(name = "customer_id") val customerId: Long,
        @Column(name = "name") val name: String,
        @Column(name = "last_name") val lastName: String,
        @Column(name = "phone_number") val phoneNumber: String,
        @Column(name = "email") val email: String,
        @Column(name = "date_of_birthday") val dateOfBirthday: LocalDate
)

@Entity
@Table(name = "STATEMENT")
data class StatementEntity(
        @Id @Column(name = "statement_id") val statement_id: Long,
        @Column(name = "customer_id") val customerId: Long,
        @Column(name = "date") val date: LocalDate,
        @Column(name = "type") val type: String,
        @Column(name = "description") val description: String,
        @Column(name = "paid_in") val paidIn: Money,
        @Column(name = "paid_out") val paidOut: Money,
        @Column(name = "balance") val balance: Money
)