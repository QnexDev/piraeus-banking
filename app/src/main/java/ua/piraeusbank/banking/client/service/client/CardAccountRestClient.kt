package ua.piraeusbank.banking.client.service.client

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ua.piraeusbank.banking.client.ui.model.request.AccountAndCardCreationRequest
import ua.piraeusbank.banking.client.ui.model.request.CardMoneyTransferRequest
import ua.piraeusbank.banking.common.domain.PaymentCard

interface CardAccountRestClient {

    @POST("card/order")
    fun createCardAndAccount(@Body request: AccountAndCardCreationRequest): Call<Void>

    @GET("card/{cardId}")
    fun findPaymentCard(@Path("cardId") cardId: Long): Call<PaymentCard>

    @GET("card/customer/{customerId}")
    fun findPaymentCardsByCustomerId(@Path("customerId") customerId: Long): Call<ResponseBody>

    @POST("card/money/transfer")
    fun transferMoneyBetweenCards(@Body request: CardMoneyTransferRequest): Call<Void>

}

fun CardAccountRestClient.rxTransferMoneyBetweenCards(request: CardMoneyTransferRequest): Observable<String> {

    val subject = PublishSubject.create<String>()

    transferMoneyBetweenCards(request).enqueue(object: Callback<Void?> {
        override fun onFailure(call: Call<Void?>, t: Throwable) {
            subject.onError(t)
        }

        override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
            if (response.isSuccessful) {
                subject.onNext("")
            } else {
                subject.onNext(response.errorBody().string())
            }
        }
    })
    return subject
}

fun CardAccountRestClient.rxCreateCardAndAccount(request: AccountAndCardCreationRequest): Observable<String> {

    val subject = PublishSubject.create<String>()

    createCardAndAccount(request).enqueue(object: Callback<Void?> {
        override fun onFailure(call: Call<Void?>, t: Throwable) {
            subject.onError(t)
        }

        override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
            if (response.isSuccessful) {
                subject.onNext("")
            } else {
                subject.onNext(response.errorBody().string())
            }
        }
    })
    return subject
}

fun CardAccountRestClient.getPaymentCardsByCustomerId(customerId: Long): Observable<ResponseBody> {
    val subject = PublishSubject.create<ResponseBody>()

    findPaymentCardsByCustomerId(customerId).enqueue(object : Callback<ResponseBody?> {
        override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
            subject.onError(t)
        }

        override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
            subject.onNext(response.body()!!)
        }
    })
    return subject
}