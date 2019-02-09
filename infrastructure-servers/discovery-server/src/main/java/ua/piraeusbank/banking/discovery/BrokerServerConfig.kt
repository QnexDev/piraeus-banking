package ua.piraeusbank.banking.discovery

import org.apache.activemq.broker.BrokerService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.Assert
import javax.annotation.PostConstruct


@Configuration
class BrokerServerConfig {

    @PostConstruct
    fun init() {
        Assert.isTrue(brokerServer().isStarted, "ActiveMQ Broker service is not started!")
    }

    @Bean
    fun brokerServer(): BrokerService {
        val broker = BrokerService()
        broker.addConnector("tcp://localhost:61616")
        broker.start()
        return broker
    }

}