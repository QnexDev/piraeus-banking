package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.common.domain.AccountAndCardCreationRequest
import ua.piraeusbank.banking.customer.repository.CustomerRepository
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import ua.piraeusbank.banking.domain.model.AuthUser
import ua.piraeusbank.banking.domain.model.CustomerRegistrationRequest
import ua.piraeusbank.banking.domain.model.DefaultCardAccountCreationMessage
import ua.piraeusbank.banking.internal.api.AuthUserRestClient
import java.time.LocalDate
import javax.annotation.PostConstruct

@Service
class CustomerService(
        @Autowired val customerRepository: CustomerRepository,
        @Autowired val jmsTemplate: JmsTemplate,
        @Autowired val authUserRestClient: AuthUserRestClient) {

    @PostConstruct
    fun init() {
        customerRepository.findByPhoneNumber("+380636303637").orElseGet {
            registerCustomer(CustomerRegistrationRequest(
                    phoneNumber = "+380636303637",
                    lastName = "Denysenko",
                    dateOfBirthday = LocalDate.of(1993, 2, 18),
                    email = "qnexdev@mgail.com",
                    name = "Alex",
                    password = "stalker2"
            ))
        }
    }

    @Transactional(readOnly = true)
    fun getCustomer(customerId: Long): CustomerEntity = customerRepository.getOne(customerId)

    @Transactional
    fun registerCustomer(request: CustomerRegistrationRequest): CustomerEntity {
        authUserRestClient.createUser(AuthUser(request.phoneNumber, request.password))

        val newCustomer = customerRepository.saveAndFlush(
                CustomerEntity(
                        name = request.name,
                        dateOfBirthday = request.dateOfBirthday,
                        email = request.email,
                        lastName = request.lastName,
                        phoneNumber = request.phoneNumber))

        jmsTemplate.convertAndSend(
                "account",
                DefaultCardAccountCreationMessage(
                        AccountAndCardCreationRequest(
                                customerId = newCustomer.customerId!!,
                                currencyCode = "UAH",
                                networkCode = "VISA"
                        )))

        return newCustomer
    }
}