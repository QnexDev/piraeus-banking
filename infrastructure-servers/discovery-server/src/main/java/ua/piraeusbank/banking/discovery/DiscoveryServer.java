package ua.piraeusbank.banking.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEurekaServer
@Import(BrokerServerConfig.class)
public class DiscoveryServer {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryServer.class, args);
	}
}