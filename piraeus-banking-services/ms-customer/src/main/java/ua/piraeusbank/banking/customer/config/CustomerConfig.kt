package ua.piraeusbank.banking.customer.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ua.piraeusbank.banking.internal.api.AuthUserRestClient
import ua.piraeusbank.banking.internal.api.RetrofitServiceGenerator

@Configuration
class CustomerConfig {

    @Bean
    fun authUserRestClient(): AuthUserRestClient = RetrofitServiceGenerator.createService("http://localhost:4000/auth/")
}