package ua.piraeusbank.banking.internal.api

import org.javamoney.moneta.Money
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ua.piraeusbank.banking.domain.entity.TransactionEntity
import ua.piraeusbank.banking.domain.model.AccountCreationRequest
import ua.piraeusbank.banking.domain.model.MoneyTransferRequest


interface AccountRestClient {

    @GET("{accountId}/balance/check")
    fun checkCurrentBalance(@Path("accountId") accountId: Long): Call<Money?>

    @POST("money/transfer")
    fun transferMoney(@Body transferRequest: MoneyTransferRequest): Call<Void>

    @POST(".")
    fun createAccount(@Body request: AccountCreationRequest): Call<Long>

    @GET("transaction/{transactionId}")
    fun getTransaction(@Path("transactionId") transactionId: Long): Call<TransactionEntity>

    @GET("transactions/outgoing")
    fun getOutgoingTransactions(accountId: Long): Call<List<TransactionEntity>>


    @GET("transactions/incoming")
    fun getIncomingTransactions(accountId: Long): Call<List<TransactionEntity>>
}