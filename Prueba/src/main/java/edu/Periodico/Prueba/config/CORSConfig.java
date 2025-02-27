package edu.Periodico.Prueba.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración de CORS para la API.
 * 
 * Esta clase configura la política de CORS para todas las rutas que comienzan con "/api/".
 * Permite que solicitudes desde "http://localhost:8080" accedan a los recursos de la API,
 * usando los métodos HTTP GET, POST, PUT y DELETE, y permitiendo cualquier encabezado.
 */
@Configuration
public class CORSConfig implements WebMvcConfigurer {

    /**
     * Configura los mapeos de CORS.
     * 
     * @param registry El objeto CorsRegistry al que se añaden las reglas.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Se aplica la configuración a todas las rutas que comienzan con "/api/"
        registry.addMapping("/api/**")
                // Permite solicitudes desde el origen especificado (por ejemplo, localhost:8080)
                .allowedOrigins("http://localhost:8080")
                // Permite los métodos HTTP indicados
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // Permite cualquier encabezado en la solicitud
                .allowedHeaders("*");
    }
}
