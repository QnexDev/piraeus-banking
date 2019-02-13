package ua.piraeusbank.banking.client

import android.app.Application
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ua.piraeusbank.banking.client.service.client.CardAccountRestClient
import ua.piraeusbank.banking.client.service.client.CustomerRestClient
import ua.piraeusbank.banking.client.service.client.RetrofitServiceGenerator
import ua.piraeusbank.banking.client.util.PhoneUtils
import ua.piraeusbank.banking.client.util.ResourceProvider

class MainComponent(private val context: Application) {

    val phoneUtils by lazy { PhoneUtils }

    val resourceProvider by lazy { ResourceProvider(context) }

    val cardAccountRestClient: CardAccountRestClient by lazy {
        RetrofitServiceGenerator.createService<CardAccountRestClient>("http://192.168.0.102:8004/aggregator/")
    }

    val customerRestClient: CustomerRestClient by lazy {
        RetrofitServiceGenerator.createService<CustomerRestClient>("http://192.168.0.102:8003/customer/")
    }

    val objectMapper = ObjectMapper()
        .registerKotlinModule()
        .registerModule(Jdk8Module())
        .registerModule(JavaTimeModule())

}