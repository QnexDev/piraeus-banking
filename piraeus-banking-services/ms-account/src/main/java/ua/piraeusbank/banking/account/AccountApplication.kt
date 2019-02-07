package ua.piraeusbank.banking.account

import org.h2.tools.Server
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import ua.piraeusbank.banking.common.config.PersistenceConfig

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(PersistenceConfig::class)
class AccountApplication

fun main(args: Array<String>) {
    var server: Server? = null
    try {
        server = Server.createTcpServer().start()
        SpringApplication.run(AccountApplication::class.java, *args)
    } finally {
        Thread.sleep(1000_000)
        server?.stop()
    }
}

