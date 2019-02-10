package ua.piraeusbank.banking.internal.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.Authenticator
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.zalando.jackson.datatype.money.MoneyModule
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.net.URL

object RetrofitServiceGenerator {

    inline fun <reified S> createService(baseUrl: String): S {
        val httpClient = OkHttpClient().newBuilder().authenticator(Authenticator.NONE).build()

        val objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(MoneyModule())

        val builder = Retrofit.Builder()
                .baseUrl(HttpUrl.get(URL(baseUrl))!!)
                .addConverterFactory(JacksonConverterFactory.create())

        return builder.client(httpClient).build().create(S::class.java)
    }
}