package ua.piraeusbank.banking.internal.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ua.piraeusbank.banking.domain.model.AuthUser

interface AuthUserRestClient {

    @POST("/")
    fun createUser(@Body user: AuthUser): Call<Void>
}