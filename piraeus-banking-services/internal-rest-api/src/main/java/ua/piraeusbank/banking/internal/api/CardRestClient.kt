package ua.piraeusbank.banking.internal.api

import retrofit2.Call
import retrofit2.http.*
import ua.piraeusbank.banking.common.domain.ChangePinCodeRequest
import ua.piraeusbank.banking.domain.entity.BankCardEntity
import ua.piraeusbank.banking.domain.model.OrderCardRequest

interface CardRestClient {

    @GET("customer/{customerId}")
    fun findCardsByCustomerId(@Path("customerId") customerId: Long): Call<List<BankCardEntity>>

    @POST
    fun orderCard(@Body request: OrderCardRequest): Call<Void>

    @GET("{cardId}")
    fun getCardById(@Path("cardId") cardId: Long): Call<BankCardEntity>

    @PUT("{cardId}/close")
    fun closeCard(@Path("cardId") cardId: Long): Call<Void>

    @PUT("{cardId}/block")
    fun blockCard(@Path("cardId") cardId: Long): Call<Void>

    @PUT("{cardId}/unblock")
    fun unblockCard(@Path("cardId") cardId: Long): Call<Void>

    @PUT("{cardId}/change/pin")
    fun changePinCode(@Path("cardId") cardId: Long,
                      @Body changePinCodeRequest: ChangePinCodeRequest): Call<Void>

    @GET("{cardId}/securityCode")
    fun getSecurityCode(@Path("cardId") cardId: Long): Call<Short>
}