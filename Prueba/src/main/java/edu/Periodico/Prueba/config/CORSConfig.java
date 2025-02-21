package edu.Periodico.Prueba.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Permite CORS para todas las rutas que comienzan con /api/
                .allowedOrigins("http://localhost:8080")  // Permite el acceso desde localhost:8080
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Permite los métodos que vas a usar
                .allowedHeaders("*");  // Permite todos los encabezados
    }
}


