package com.proyectoAPIREST.APIREST_Foto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableRetry
public class ApirestFotoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApirestFotoApplication.class, args);
                
	}

}
