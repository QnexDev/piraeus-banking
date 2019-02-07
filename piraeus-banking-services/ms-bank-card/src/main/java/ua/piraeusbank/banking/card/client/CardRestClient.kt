package ua.piraeusbank.banking.card.client

import retrofit2.http.*
import ua.piraeusbank.banking.card.service.ChangePinCodeRequest
import ua.piraeusbank.banking.card.service.IssueCardRequest
import ua.piraeusbank.banking.domain.entity.BankCardData

interface CardRestClient {
    
    @GET
    fun findAllCards()

    @POST
    fun issueCard(@Body issueCardRequest: IssueCardRequest)

    @GET("/{cardId}")
    fun getCardById(@Path("cardId") cardId: Long): BankCardData

    @PUT("/{cardId}/close")
    fun closeCard(@Path("cardId") cardId: Long)

    @PUT("/{cardId}/block")
    fun blockCard(@Path("cardId") cardId: Long)

    @PUT("/{cardId}/unblock")
    fun unblockCard(@Path("cardId") cardId: Long)

    @PUT("/{cardId}/change/pin")
    fun changePinCode(@Path("cardId") cardId: Long,
                      @Body changePinCodeRequest: ChangePinCodeRequest)

    @GET("/{cardId}/securityCode")
    fun getSecurityCode(@Path("cardId") cardId: Long): Short
}