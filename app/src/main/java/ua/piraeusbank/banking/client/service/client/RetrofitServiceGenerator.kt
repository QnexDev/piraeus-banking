package ua.piraeusbank.banking.client.service.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ua.piraeusbank.banking.client.config.RestAuthConfig

object RetrofitServiceGenerator {

    inline fun <reified S> createService(baseUrl: String): S {
        val httpClient = OkHttpClient().newBuilder().addInterceptor(RestAuthConfig.tokenAuthenticator()).build()

        val objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())

        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))

        return builder.client(httpClient).build().create(S::class.java)
    }
}