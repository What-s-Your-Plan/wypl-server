package com.butter.wypl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class WyplApplication {

	public static void main(String[] args) {
		SpringApplication.run(WyplApplication.class, args);
	}

}
