package edu.Periodico.Prueba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import edu.Periodico.Prueba.Util.FicheroLog;

/**
 * Clase principal donde se encuentra el main de la aplicación.
 * 17/01/2025 - CHI
 */
@ServletComponentScan
@SpringBootApplication
public class PruebaApplication {
    /**
     * Clase main del programa que contiene el .run de la aplicación.
     * 17/01/2025 - CHI
     */
    public static void main(String[] args) {
        try {
            SpringApplication.run(PruebaApplication.class, args);
            FicheroLog.logInfo("La API se ha arrancado exitosamente.");
        } catch (Exception e) {
        	FicheroLog.logError("Error al arrancar la API", e);
        }
    }
}
