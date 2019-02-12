package ua.piraeusbank.banking.internal.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ua.piraeusbank.banking.common.domain.StatementRecord
import ua.piraeusbank.banking.domain.entity.CustomerEntity
import ua.piraeusbank.banking.domain.entity.StatementRecordEntity

interface CustomerRestClient {

    @GET("/statements/{customerId}")
    fun getStatements(@Path("customerId") customerId: Long): Call<List<StatementRecord>>

}