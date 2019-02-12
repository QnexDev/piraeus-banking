package ua.piraeusbank.banking.internal.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.zalando.jackson.datatype.money.MoneyModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

object RetrofitServiceGenerator {

    inline fun <reified S> createService(baseUrl: String, authenticator: Interceptor =
            Interceptor { chain -> chain.proceed(chain.request()) }): S {
        val httpClient = OkHttpClient().newBuilder().addInterceptor(authenticator).build()

        val objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(MoneyModule())

        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))

        return builder.client(httpClient).build().create(S::class.java)
    }
}