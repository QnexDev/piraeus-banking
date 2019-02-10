package ua.piraeusbank.banking.discovery

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@SpringBootApplication
@EnableEurekaServer
class DiscoveryServer

fun main(args: Array<String>) {
    SpringApplication.run(DiscoveryServer::class.java, *args)
}