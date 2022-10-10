package com.microel.speedtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpeedTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpeedTestApplication.class, args);
	}

}
