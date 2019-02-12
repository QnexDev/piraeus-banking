package ua.piraeusbank.banking.client.service.client

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import ua.piraeusbank.banking.common.domain.ChangePinCodeRequest

interface CardRestClient {

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