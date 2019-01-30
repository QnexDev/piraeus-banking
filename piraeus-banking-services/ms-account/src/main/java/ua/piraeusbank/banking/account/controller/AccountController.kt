package ua.piraeusbank.banking.account.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ua.piraeusbank.banking.account.service.AccountService
import ua.piraeusbank.banking.account.service.MoneyTransferRequest

@RestController
class AccountController(@Autowired val accountService: AccountService) {

    @GetMapping("{accountId}/balance/check")
    fun checkCurrentBalance(@PathVariable("accountId") accountId: Long) =
            accountService.checkCurrentBalance(accountId)

    @PostMapping("/money/transfer")
    fun transferMoney(@RequestBody transferRequest: MoneyTransferRequest) =
            accountService.transferMoney(transferRequest)

    @GetMapping("/transaction/{transactionId}")
    fun getTransaction(@PathVariable("transactionId") transactionId: Long) =
            accountService.getTransaction(transactionId)

    @GetMapping("/transactions/outgoing")
    fun getOutgoingTransactions(accountId: Long) = accountService.getOutgoingTransactions(accountId)


    @GetMapping("/transactions/incoming")
    fun getIncomingTransactions(accountId: Long) = accountService.getIncomingTransactions(accountId)
}