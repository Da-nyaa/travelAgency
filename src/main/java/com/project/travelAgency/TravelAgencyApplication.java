package com.project.travelAgency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TravelAgencyApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context =
				SpringApplication.run(TravelAgencyApplication.class, args);
	PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
			System.out.println(encoder.encode("pass"));
	}

}
