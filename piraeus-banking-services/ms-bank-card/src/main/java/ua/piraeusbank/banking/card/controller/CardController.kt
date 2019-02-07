package ua.piraeusbank.banking.card.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ua.piraeusbank.banking.card.service.CardService
import ua.piraeusbank.banking.card.service.ChangePinCodeRequest
import ua.piraeusbank.banking.card.service.IssueCardRequest
import ua.piraeusbank.banking.domain.entity.BankCardData

@RestController
internal class CardController(@Autowired val cardService: CardService) {

    @GetMapping
    fun findAllCards() = cardService.findAllCardInfos()

    @PostMapping
    fun issueCard(@RequestBody issueCardRequest: IssueCardRequest) = cardService.orderCard(issueCardRequest)

    @GetMapping("/{cardId}")
    fun getCardById(@PathVariable cardId: Long): BankCardData = cardService.getCardInfoById(cardId)

    @PutMapping("/{cardId}/close")
    fun closeCard(@PathVariable cardId: Long) = cardService.closeCard(cardId)

    @PutMapping("/{cardId}/block")
    fun blockCard(@PathVariable cardId: Long) = cardService.blockCard(cardId)

    @PutMapping("/{cardId}/unblock")
    fun unblockCard(@PathVariable cardId: Long) = cardService.unblockCard(cardId)

    @PutMapping("/{cardId}/change/pin")
    fun changePinCode(@PathVariable cardId: Long,
                      @RequestBody changePinCodeRequest: ChangePinCodeRequest) =
            cardService.changePinCode(cardId, changePinCodeRequest)

    @GetMapping("/{cardId}/securityCode")
    fun getSecurityCode(@PathVariable cardId: Long): Short = cardService.getSecurityCode(cardId)

}