package ua.piraeusbank.banking.customer.service

import org.javamoney.moneta.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.common.domain.AccountAndCardCreationRequest
import ua.piraeusbank.banking.common.domain.Customer
import ua.piraeusbank.banking.customer.repository.CustomerRepository
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import ua.piraeusbank.banking.domain.model.AuthUser
import ua.piraeusbank.banking.domain.model.CardAccountCreationMessage
import ua.piraeusbank.banking.domain.model.CustomerRegistrationRequest
import ua.piraeusbank.banking.domain.model.UserRegistrationMessage
import java.time.LocalDate
import javax.annotation.PostConstruct

@Service
class CustomerService(
        @Autowired val customerRepository: CustomerRepository,
        @Autowired val jmsTemplate: JmsTemplate) {

    @PostConstruct
    fun init() {
        customerRepository.findByPhoneNumber("+3806363036393438").orElseGet {
            registerCustomer(CustomerRegistrationRequest(
                    phoneNumber = "+3806363036393438",
                    lastName = "Denysenko",
                    dateOfBirthday = LocalDate.of(1993, 2, 18),
                    email = "qnexdev@gmail.com",
                    name = "Alex",
                    password = "stalker2"
            ))
        }
    }

    @Transactional(readOnly = true)
    fun findByPhoneNumber(phoneNumber: String): Customer = customerRepository.findByPhoneNumber(phoneNumber).map {
        Customer(
                customerId = it.customerId,
                name = it.name,
                email = it.email,
                dateOfBirthday = it.dateOfBirthday,
                lastName = it.lastName,
                phoneNumber = it.phoneNumber
        )
    }.get()

    @Transactional
    fun registerCustomer(request: CustomerRegistrationRequest): CustomerEntity {
        jmsTemplate.convertAndSend(
                "registration",
                UserRegistrationMessage(AuthUser(request.phoneNumber, request.password)))

        val newCustomer = customerRepository.saveAndFlush(
                CustomerEntity(
                        name = request.name,
                        dateOfBirthday = request.dateOfBirthday,
                        email = request.email,
                        lastName = request.lastName,
                        phoneNumber = request.phoneNumber))

        jmsTemplate.convertAndSend(
                "accountCreation",
                CardAccountCreationMessage(
                        AccountAndCardCreationRequest(
                                customerId = newCustomer.customerId!!,
                                currencyCode = "UAH",
                                networkCode = "VISA",
                                balance = Money.of(10000, "UAH")
                        )))

        return newCustomer
    }
}