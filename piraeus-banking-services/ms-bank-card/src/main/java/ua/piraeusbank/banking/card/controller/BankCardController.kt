package ua.piraeusbank.banking.card.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ua.piraeusbank.banking.card.service.BankCardService
import ua.piraeusbank.banking.card.service.ChangePinCodeRequest
import ua.piraeusbank.banking.card.service.IssueCardRequest

@RestController
internal class BankCardController(@Autowired val bankCardService: BankCardService) {

    @GetMapping
    fun findAllCards() = bankCardService.findAllCardInfos()

    @PostMapping
    fun issueCard(@RequestBody issueCardRequest: IssueCardRequest) {
        bankCardService.issueCard(issueCardRequest)
    }

    @GetMapping("/{cardId}")
    fun getCardById(@PathVariable cardId: Long) = bankCardService.getCardInfoById(cardId)

    @PutMapping("/{cardId}/close")
    fun closeCard(@PathVariable cardId: Long) = bankCardService.closeCard(cardId)

    @PutMapping("/{cardId}/block")
    fun blockCard(@PathVariable cardId: Long) = bankCardService.blockCard(cardId)

    @PutMapping("/{cardId}/change/pin")
    fun changePinCode(@PathVariable cardId: Long,
                      @RequestBody changePinCodeRequest: ChangePinCodeRequest) =
            bankCardService.changePinCode(cardId, changePinCodeRequest)

    @GetMapping("/{cardId}/securityCode")
    fun getSecurityCode(@PathVariable cardId: Long) = bankCardService.getSecurityCode(cardId)

}