package ua.piraeusbank.banking.client.service.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ua.piraeusbank.banking.common.domain.Customer
import ua.piraeusbank.banking.common.domain.StatementRecord

interface CustomerRestClient {

    @GET("/statements/{customerId}")
    fun getStatements(@Path("customerId") customerId: Long): Call<List<StatementRecord>>

    @GET("phone/{phoneNumber}")
    fun findByPhoneNumber(@Path("phoneNumber") phoneNumber: String): Call<Customer>

}