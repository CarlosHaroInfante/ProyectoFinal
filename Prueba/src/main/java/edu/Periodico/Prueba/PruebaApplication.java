package edu.Periodico.Prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Clase principal donde se encuentra el main de la aplicación.
 * 17/01/2025 - CHI
 * **/
@ServletComponentScan
@SpringBootApplication
public class PruebaApplication {
	/**
	 * Clase main del programa que contiene el .run de la aplicación.
	 * 17/01/2025 - CHI
	 * **/
	public static void main(String[] args) {
		SpringApplication.run(PruebaApplication.class, args);
	}

}
