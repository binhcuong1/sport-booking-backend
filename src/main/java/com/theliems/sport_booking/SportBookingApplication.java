package com.theliems.sport_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SportBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportBookingApplication.class, args);
	}

}
