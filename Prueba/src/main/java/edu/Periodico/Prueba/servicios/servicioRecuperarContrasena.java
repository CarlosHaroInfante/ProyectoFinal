/*package edu.Periodico.Prueba.servicios;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.EmailUtil;
import edu.Periodico.Prueba.dtos.usuarioDTO;

@Service
public class servicioRecuperarContrasena {
    
    @Autowired
    private repositorioUsuario repositorioUsu;
    
    public boolean recuperarContrasena(String correo) {
        // Buscar el usuario por correo
        usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(correo);
        if (usuario == null) {
            System.out.println("El correo " + correo + " no existe.");
            return false;
        }
        
        // Generar un token (código de recuperación)
        String token = generateRandomCode(10);
        usuario.setTokenConfirmacion(token);
        usuario.setConfirmado(false);
        usuarioDTO usuarioGuardado = repositorioUsu.save(usuario);
        if (usuarioGuardado == null) {
            System.out.println("Error al actualizar el usuario con el token.");
            return false;
        }
        
        try {
        	// Construir el enlace de recuperación incluyendo el correo y el token (codificado)
        	// En servicioRecuperarContrasena
        	String link = "http://localhost:8080/VistaPrueba2/cambiarContrasena.html?correo="
        	    + URLEncoder.encode(correo, StandardCharsets.UTF_8)
        	    + "&codigoVerificacion=" + token;


            String emailBody = "Hola " + usuario.getNombreCompleto() + ",\n\n" +
                               "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" +
                               link + "\n\n" +
                               "Si no solicitaste este cambio, ignora este mensaje.";
            EmailUtil.sendEmail(correo, "Recuperación de contraseña", emailBody);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Método auxiliar para generar un código aleatorio de 10 caracteres
    public static String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}*/

package edu.Periodico.Prueba.servicios;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.EmailUtil;
import edu.Periodico.Prueba.dtos.usuarioDTO;
import edu.Periodico.Prueba.servicios.servicioUsuario;

@Service
public class servicioRecuperarContrasena {
    
    @Autowired
    private repositorioUsuario repositorioUsu;
    
    @Autowired
    private servicioUsuario servicioUsuario;
    
    // Método para enviar el correo de recuperación
    public boolean recuperarContrasena(String correo) {
        usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(correo);
        if (usuario == null) {
            System.out.println("El correo " + correo + " no existe.");
            return false;
        }
        
        // Generar un token de recuperación
        String token = generateRandomCode(10);
        usuario.setTokenConfirmacion(token);
        usuario.setConfirmado(false);
        usuarioDTO usuarioGuardado = repositorioUsu.save(usuario);
        if (usuarioGuardado == null) {
            System.out.println("Error al actualizar el usuario con el token.");
            return false;
        }
        
        try {
            // Construir el enlace para la recuperación usando "codigoVerificacion"
            String link = "http://localhost:8080/VistaPrueba2/actualizarContrasena.html?correo="
                    + URLEncoder.encode(correo, StandardCharsets.UTF_8.toString())
                    + "&codigoVerificacion=" + token;
            String emailBody = "Hola " + usuario.getNombreCompleto() + ",\n\n" +
                               "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" +
                               link + "\n\n" +
                               "Si no solicitaste este cambio, ignora este mensaje.";
            EmailUtil.sendEmail(correo, "Recuperación de contraseña", emailBody);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Método exclusivo para actualizar la contraseña en el flujo de recuperación
    public String actualizarContrasenaRecuperacion(String correo, String token, String nuevaPassword, String confirmarPassword) {
        // Verificar que las contraseñas coinciden
        if (!nuevaPassword.equals(confirmarPassword)) {
            return "{\"error\":\"Las contraseñas no coinciden.\"}";
        }
        // Buscar el usuario por correo
        usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(correo);
        if (usuario == null) {
            return "{\"error\":\"El correo no coincide con ningún usuario.\"}";
        }
        // Verificar el token de recuperación
        if (usuario.getTokenConfirmacion() == null || !usuario.getTokenConfirmacion().equalsIgnoreCase(token)) {
            return "{\"error\":\"El código de verificación es incorrecto.\"}";
        }
        // Actualizar la contraseña (encriptada) y limpiar el token
        String passwordEncriptada = servicioUsuario.encriptarContrasenya(nuevaPassword);
        usuario.setPassword(passwordEncriptada);
        usuario.setConfirmado(true);
        usuario.setTokenConfirmacion(null);
        repositorioUsu.save(usuario);
        return "{\"success\": true, \"message\":\"Contraseña actualizada correctamente.\"}";
    }
    
    // Método auxiliar para generar un código aleatorio
    public static String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
