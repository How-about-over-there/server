package com.haot.lodge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication(scanBasePackages = {
		"com.haot.lodge",
		"com.haot.submodule"
})
public class LodgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LodgeApplication.class, args);
	}

}
