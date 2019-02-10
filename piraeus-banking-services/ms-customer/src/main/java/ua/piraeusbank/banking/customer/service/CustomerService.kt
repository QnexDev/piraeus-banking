package ua.piraeusbank.banking.customer.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.piraeusbank.banking.customer.repository.CustomerRepository
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import ua.piraeusbank.banking.domain.model.AuthUser
import ua.piraeusbank.banking.domain.model.CustomerRegistrationRequest
import ua.piraeusbank.banking.internal.api.AuthUserRestClient
import ua.piraeusbank.banking.internal.api.RetrofitServiceGenerator

@Service
class CustomerService(@Autowired val customerRepository: CustomerRepository) {

    private val accountRestClient: AuthUserRestClient = RetrofitServiceGenerator.createService("auth")


    @Transactional(readOnly = true)
    fun getCustomer(customerId: Long): CustomerEntity = customerRepository.getOne(customerId)

    @Transactional
    fun registerCustomer(request: CustomerRegistrationRequest) {
        accountRestClient.createUser(AuthUser(request.phoneNumber, request.password))

        customerRepository.saveAndFlush(
                CustomerEntity(
                        name = request.name,
                        dateOfBirthday = request.dateOfBirthday,
                        email = request.email,
                        lastName = request.lastName,
                        phoneNumber = request.phoneNumber))

    }

}