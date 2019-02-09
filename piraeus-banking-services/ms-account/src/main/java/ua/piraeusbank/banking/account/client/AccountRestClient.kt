package ua.piraeusbank.banking.account.client

import org.javamoney.moneta.Money
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ua.piraeusbank.banking.account.service.AccountCreationRequest
import ua.piraeusbank.banking.account.service.MoneyTransferRequest
import ua.piraeusbank.banking.domain.entity.TransactionEntity


interface AccountRestClient {

    @GET("{accountId}/balance/check")
    fun checkCurrentBalance(@Path("accountId") accountId: Long): Money

    @POST("/money/transfer")
    fun transferMoney(@Body transferRequest: MoneyTransferRequest)

    @POST("/")
    fun createAccount(@Body request: AccountCreationRequest)

    @GET("/transaction/{transactionId}")
    fun getTransaction(@Path("transactionId") transactionId: Long): TransactionEntity

    @GET("/transactions/outgoing")
    fun getOutgoingTransactions(accountId: Long): List<TransactionEntity>


    @GET("/transactions/incoming")
    fun getIncomingTransactions(accountId: Long): List<TransactionEntity>
}