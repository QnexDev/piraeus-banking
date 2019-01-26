package ua.piraeusbank.banking.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.config.server.EnableConfigServer

@SpringBootApplication
@EnableConfigServer
open class ConfigServer

fun main(args: Array<String>) {
    SpringApplication.run(ConfigServer::class.java, *args)
}