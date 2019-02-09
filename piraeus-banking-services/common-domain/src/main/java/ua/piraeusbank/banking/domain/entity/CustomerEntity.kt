package ua.piraeusbank.banking.domain.entity

import org.javamoney.moneta.Money
import ua.piraeusbank.banking.domain.conversion.MoneyConverter
import java.time.LocalDate
import javax.persistence.*

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
        @ManyToOne
        @JoinColumn(name = "customer_id")
        val customer: CustomerEntity,
        @Column(name = "date") val date: LocalDate,
        @Column(name = "type") val type: String,
        @Column(name = "description") val description: String,
        @Convert(converter = MoneyConverter::class)
        @Column(name = "paid_in") val paidIn: Money,
        @Convert(converter = MoneyConverter::class)
        @Column(name = "paid_out") val paidOut: Money,
        @Convert(converter = MoneyConverter::class)
        @Column(name = "balance") val balance: Money
)