package ua.piraeusbank.banking.account.controller

import org.javamoney.moneta.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ua.piraeusbank.banking.account.service.AccountCreationRequest
import ua.piraeusbank.banking.account.service.AccountService
import ua.piraeusbank.banking.account.service.MoneyTransferRequest
import ua.piraeusbank.banking.domain.entity.TransactionEntity

@RestController
class AccountController(@Autowired val accountService: AccountService) {

    @GetMapping("{accountId}/balance/check")
    fun checkCurrentBalance(@PathVariable("accountId") accountId: Long): Money =
            accountService.checkCurrentBalance(accountId)

    @PostMapping("/money/transfer")
    fun transferMoney(@RequestBody transferRequest: MoneyTransferRequest): Unit =
            accountService.transferMoney(transferRequest)

    @PostMapping("/")
    fun createAccount(@RequestBody request: AccountCreationRequest) {
        accountService.createAccount(request)
    }

    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable("transactionId") transactionId: Long): TransactionEntity =
            accountService.getTransaction(transactionId)

    @GetMapping("/transactions/outgoing")
    fun getOutgoingTransactions(accountId: Long): List<TransactionEntity> = accountService.getOutgoingTransactions(accountId)


    @GetMapping("/transactions/incoming")
    fun getIncomingTransactions(accountId: Long): List<TransactionEntity> = accountService.getIncomingTransactions(accountId)
}