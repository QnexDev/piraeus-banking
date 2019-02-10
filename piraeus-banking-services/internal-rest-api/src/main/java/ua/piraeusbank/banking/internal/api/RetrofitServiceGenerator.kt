package ua.piraeusbank.banking.internal.api

import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitServiceGenerator {

    inline fun <reified S> createService(baseUrl: String): S {
        val httpClient = OkHttpClient().newBuilder().authenticator(Authenticator.NONE).build()

        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())

        return builder.client(httpClient).build().create(S::class.java)
    }
}