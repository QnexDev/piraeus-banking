package ua.piraeusbank.banking.account

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import ua.piraeusbank.banking.common.config.MessagingConfig
import ua.piraeusbank.banking.common.config.PersistenceConfig
import ua.piraeusbank.banking.common.config.ResourceServerConfig

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(PersistenceConfig::class, MessagingConfig::class, ResourceServerConfig::class)
class AccountApplication

fun main(args: Array<String>) {
    SpringApplication.run(AccountApplication::class.java, *args)
}

