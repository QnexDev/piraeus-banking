package ua.piraeusbank.banking.customer.client

import retrofit2.http.GET
import retrofit2.http.Path
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import ua.piraeusbank.banking.domain.entity.StatementEntity

interface CustomerRestClient {

    @GET("/statements/{customerId}")
    fun getStatements(@Path("customerId") customerId: Long): List<StatementEntity>

    @GET("/{customerId}")
    fun getCustomer(@Path("customerId") customerId: Long): CustomerEntity
}