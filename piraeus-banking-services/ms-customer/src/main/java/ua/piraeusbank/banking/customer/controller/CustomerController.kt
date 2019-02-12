package ua.piraeusbank.banking.customer.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ua.piraeusbank.banking.common.domain.Customer
import ua.piraeusbank.banking.common.domain.StatementRecord
import ua.piraeusbank.banking.customer.service.CustomerService
import ua.piraeusbank.banking.customer.service.StatementService
import ua.piraeusbank.banking.domain.model.CustomerRegistrationRequest

@RestController
class CustomerController(
        @Autowired private val statementService: StatementService,
        @Autowired private val customerService: CustomerService) {

    @GetMapping("/statements/{customerId}")
    fun getStatements(@PathVariable("customerId") customerId: Long): List<StatementRecord> =
            statementService.getStatements(customerId)

    @GetMapping("/phone/{phoneNumber}")
    fun findByPhoneNumber(@PathVariable("phoneNumber") phoneNumber: String): Customer =
            customerService.findByPhoneNumber(phoneNumber)

    @PostMapping
    fun registerCustomer(request: CustomerRegistrationRequest) =
            customerService.registerCustomer(request)

}