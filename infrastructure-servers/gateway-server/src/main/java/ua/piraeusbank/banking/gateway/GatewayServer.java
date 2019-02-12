package ua.piraeusbank.banking.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Import;
import ua.piraeusbank.banking.common.config.MessagingConfig;
import ua.piraeusbank.banking.common.config.RestAuthConfig;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
@Import({MessagingConfig.class, RestAuthConfig.class})
public class GatewayServer {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServer.class, args);
	}
}
