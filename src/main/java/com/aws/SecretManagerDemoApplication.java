package com.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SecretManagerDemoApplication {
	public static void main(String[] args) throws JsonProcessingException {
		SpringApplication.run(SecretManagerDemoApplication.class, args);
    }

}
