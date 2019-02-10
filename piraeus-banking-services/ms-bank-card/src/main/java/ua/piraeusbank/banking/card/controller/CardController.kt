package ua.piraeusbank.banking.card.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ua.piraeusbank.banking.card.service.CardService
import ua.piraeusbank.banking.common.domain.ChangePinCodeRequest
import ua.piraeusbank.banking.domain.entity.BankCardEntity
import ua.piraeusbank.banking.domain.model.OrderCardRequest

@RestController
internal class CardController(@Autowired val cardService: CardService) {

    @GetMapping("/customer/{customerId}")
    fun findCardsByCustomerId(@PathVariable customerId: Long) = cardService.findCardsByCustomerId(customerId)

    @PostMapping
    fun orderCard(@RequestBody orderCardRequest: OrderCardRequest) = cardService.orderCard(orderCardRequest)

    @GetMapping("/{cardId}")
    fun getCardById(@PathVariable cardId: Long): BankCardEntity = cardService.getCardById(cardId)

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