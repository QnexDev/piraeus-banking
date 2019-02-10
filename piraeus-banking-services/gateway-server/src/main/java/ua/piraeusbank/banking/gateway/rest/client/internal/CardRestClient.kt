package ua.piraeusbank.banking.gateway.rest.client.internal

import retrofit2.http.*
import ua.piraeusbank.banking.common.domain.ChangePinCodeRequest
import ua.piraeusbank.banking.domain.entity.BankCardEntity
import ua.piraeusbank.banking.domain.model.OrderCardRequest

interface CardRestClient {

    @GET("/customer/{customerId}")
    fun findCardsByCustomerId(@Path("customerId") customerId: Long): List<BankCardEntity>

    @POST
    fun orderCard(@Body request: OrderCardRequest)

    @GET("/{cardId}")
    fun getCardById(@Path("cardId") cardId: Long): BankCardEntity

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