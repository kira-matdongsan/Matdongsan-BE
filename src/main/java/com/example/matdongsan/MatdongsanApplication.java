package com.example.matdongsan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.example.matdongsan.external"})
@SpringBootApplication
public class MatdongsanApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatdongsanApplication.class, args);
	}

}
