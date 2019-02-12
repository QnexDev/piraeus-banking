package ua.piraeusbank.banking.aggregator.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ua.piraeusbank.banking.aggregator.service.AccountCardAggregatorService
import ua.piraeusbank.banking.common.domain.AccountAndCardCreationRequest
import ua.piraeusbank.banking.common.domain.CardMoneyTransferRequest
import ua.piraeusbank.banking.common.domain.PaymentCard

@RestController
class AggregatorController(
        @Autowired private val accountCardAggregatorService: AccountCardAggregatorService) {

    @PostMapping("/card/order")
    fun createCardAndAccount(@RequestBody request: AccountAndCardCreationRequest) =
            accountCardAggregatorService.createCardAndAccount(request)

    @GetMapping("/card/{cardId}")
    fun findPaymentCard(@PathVariable cardId: Long): PaymentCard =
            accountCardAggregatorService.findPaymentCard(cardId)

    @GetMapping("/card/customer/{customerId}")
    fun findPaymentCardsByCustomerId(@PathVariable customerId: Long): List<PaymentCard> =
            accountCardAggregatorService.findPaymentCardsByCustomerId(customerId)

    @PutMapping("/card/money/transfer")
    fun transferMoneyBetweenCards(@RequestBody request: CardMoneyTransferRequest) =
            accountCardAggregatorService.transferBetweenCards(request)
}
