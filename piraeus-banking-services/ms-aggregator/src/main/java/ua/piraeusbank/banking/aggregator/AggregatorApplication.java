package ua.piraeusbank.banking.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import ua.piraeusbank.banking.common.config.MessagingConfig;
import ua.piraeusbank.banking.common.config.ResourceServerConfig;
import ua.piraeusbank.banking.common.config.RestAuthConfig;

@SpringBootApplication
@EnableDiscoveryClient
@Import({MessagingConfig.class, RestAuthConfig.class, ResourceServerConfig.class})
public class AggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
	}
}
