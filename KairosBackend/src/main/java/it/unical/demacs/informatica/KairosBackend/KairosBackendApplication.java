package it.unical.demacs.informatica.KairosBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KairosBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KairosBackendApplication.class, args);
	}

}
