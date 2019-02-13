package ua.piraeusbank.banking.client.service.client

import com.fasterxml.jackson.core.type.TypeReference
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ua.piraeusbank.banking.client.App
import ua.piraeusbank.banking.client.ui.model.StatementRecord
import ua.piraeusbank.banking.common.domain.Customer

interface CustomerRestClient {

    @GET("statements/{customerId}")
    fun getStatements(@Path("customerId") customerId: Long): Call<ResponseBody>

    @GET("phone/{phoneNumber}")
    fun findByPhoneNumber(@Path("phoneNumber") phoneNumber: String): Call<Customer>

}

fun CustomerRestClient.getRxStatements(customerId: Long): Observable<List<StatementRecord>> {
    val subject = PublishSubject.create<List<StatementRecord>>()
    getStatements(customerId).enqueue(object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            subject.onError(t)
        }

        override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody>) {
            val records: List<StatementRecord> =
                App.component.objectMapper.readValue<List<StatementRecord>>(
                    response.body()!!.string(),
                    object : TypeReference<List<StatementRecord>>() {})
            subject.onNext(records)
        }
    })

    return subject

}