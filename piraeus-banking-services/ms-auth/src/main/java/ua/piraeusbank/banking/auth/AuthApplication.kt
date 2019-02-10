package ua.piraeusbank.banking.auth

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import ua.piraeusbank.banking.common.config.MessagingConfig
import ua.piraeusbank.banking.common.config.PersistenceConfig

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(PersistenceConfig::class, MessagingConfig::class)
class AuthApplication

fun main(args: Array<String>) {
    SpringApplication.run(AuthApplication::class.java, *args)
}


