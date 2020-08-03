package proPets.searching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
public class ProPetsSearchingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProPetsSearchingServiceApplication.class, args);
	}

}
