package com.learning.couchbase.covid19service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Covid19serviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Covid19serviceApplication.class, args);
	}

}
