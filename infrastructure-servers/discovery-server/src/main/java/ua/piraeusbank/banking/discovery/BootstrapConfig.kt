package ua.piraeusbank.banking.discovery

import org.apache.activemq.broker.BrokerService
import org.h2.tools.Server
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.Assert
import javax.annotation.PostConstruct


@Configuration
class BootstrapConfig {

    @Autowired
    private lateinit var server: Server

    @PostConstruct
    fun init() {
        Assert.isTrue(brokerServer().isStarted, "ActiveMQ Broker service is not started!")
        Assert.isTrue(server.isRunning(false), "Database is not running!")
    }

    @Bean(destroyMethod = "stop")
    fun dataBaseServer(@Value("\${db.connection.port}") port: Int): Server {
        return Server.createTcpServer("-tcpPort", port.toString()).start()
    }

    @Bean
    fun brokerServer(): BrokerService {
        val broker = BrokerService()
        broker.addConnector("tcp://localhost:61616")
        broker.start()
        return broker
    }

}