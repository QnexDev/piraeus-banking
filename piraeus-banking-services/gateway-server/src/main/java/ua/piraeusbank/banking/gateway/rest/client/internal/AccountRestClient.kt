package ua.piraeusbank.banking.gateway.rest.client.internal

import org.javamoney.moneta.Money
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ua.piraeusbank.banking.domain.entity.TransactionEntity
import ua.piraeusbank.banking.domain.model.AccountCreationRequest
import ua.piraeusbank.banking.domain.model.MoneyTransferRequest


interface AccountRestClient {

    @GET("{accountId}/balance/check")
    fun checkCurrentBalance(@Path("accountId") accountId: Long): Money

    @POST("/money/transfer")
    fun transferMoney(@Body transferRequest: MoneyTransferRequest)

    @POST("/")
    fun createAccount(@Body request: AccountCreationRequest): Long

    @GET("/transaction/{transactionId}")
    fun getTransaction(@Path("transactionId") transactionId: Long): TransactionEntity

    @GET("/transactions/outgoing")
    fun getOutgoingTransactions(accountId: Long): List<TransactionEntity>


    @GET("/transactions/incoming")
    fun getIncomingTransactions(accountId: Long): List<TransactionEntity>
}