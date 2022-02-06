package com.accel.mymicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MymicroserviceApplication {

	public static void main(String[] args) {
		SpringAppdflication.run(MymicroserviceApplication.class, args);
	}

}
