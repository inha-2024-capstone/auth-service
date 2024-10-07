package com.mog.authserver;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.TimeZone;

@EnableWebSecurity(debug=true)
@SpringBootApplication
@EnableJpaAuditing
public class AuthserverApplication {

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

}
