package net.example.ospf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OspfApplication {

	public static void main(String[] args) {
		SpringApplication.run(OspfApplication.class, args);
	}

}
