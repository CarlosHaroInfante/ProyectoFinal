package edu.Periodico.Prueba.servicios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.EmailUtil;
import edu.Periodico.Prueba.dtos.usuarioDTO;

/*
 * Servicio que contiene la lógica de los métodos relacionados con los usuarios.
 * 17/1/2025 - CHI 
 * */
@Service
public class servicioUsuario {

    @Autowired
    repositorioUsuario RepUsuario;

    // Método para encriptar contraseñas usando SHA-256
    public String encriptarContrasenya(String contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = String.format("%02x", b); // Formato hexadecimal simplificado
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Nuevo método: Registrar usuario sin contraseña
    public boolean registrarUsuarioSinPassword(usuarioDTO usuario) {
    	
    	long count = RepUsuario.countByCorreoUsuario(usuario.getCorreoUsuario());
        if (count > 0) {
            System.out.println("El correo " + usuario.getCorreoUsuario() + " ya está registrado (count = " + count + ").");
            return false;
        }
        	
        // Dejar la contraseña vacía
        usuario.setPassword("");

        // Generar un código alfanumérico de 10 caracteres
        String codigoVerificacion = generateRandomCode(10); // Método auxiliar
        usuario.setTokenConfirmacion(codigoVerificacion);
        usuario.setConfirmado(false);

        // Guardar el usuario en la base de datos usando el repositorio
        usuarioDTO usuarioGuardado = RepUsuario.save(usuario);
        if (usuarioGuardado == null || usuarioGuardado.getIdUsuario() == 0) {
            System.out.println("Error al guardar el usuario en la base de datos.");
            return false;
        }
        System.out.println("Usuario guardado correctamente: " + usuarioGuardado);

        // Construir el enlace de verificación (ajusta la URL de la página de verificación de Vista)
        String verificationLink = "http://localhost:8080/VistaPrueba2/verificarCodigo.html?correo=" + usuario.getCorreoUsuario();
        String body = "Hola " + usuario.getNombreCompleto() + ",\n\n" +
                      "Tu código de verificación es: " + codigoVerificacion + "\n\n" +
                      "Haz clic en el siguiente enlace para verificar tu cuenta y establecer tu contraseña:\n" +
                      verificationLink + "\n\n" +
                      "Si no solicitaste este registro, ignora este mensaje.";

        // Enviar correo usando EmailUtil
        EmailUtil.sendEmail(usuario.getCorreoUsuario(), "Código de Verificación", body);

        return true; // O el valor que corresponda según la lógica de guardado.
    }

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
    
    // Nuevo método: Actualizar la contraseña (después de verificar el código)
    public boolean actualizarPassword(String correo, String codigo, String nuevaPassword) {
        usuarioDTO usuario = RepUsuario.findByCorreoUsuario(correo); 
        if (usuario != null && usuario.getTokenConfirmacion() != null) {
            System.out.println("Código almacenado: " + usuario.getTokenConfirmacion());
            System.out.println("Código ingresado: " + codigo);
            if (usuario.getTokenConfirmacion().equalsIgnoreCase(codigo)) {
                String passwordEncriptada = encriptarContrasenya(nuevaPassword);
                usuario.setPassword(passwordEncriptada);
                usuario.setConfirmado(true);
                usuario.setTokenConfirmacion(null);
                RepUsuario.save(usuario);
                return true;
            } else {
                System.out.println("El código de verificación no coincide.");
            }
        }
        return false;
    }


    
    // Método para confirmar el usuario a partir del token
    public boolean confirmarUsuario(String token) {
        // Buscar usuario por token
        usuarioDTO usuario = RepUsuario.findByTokenConfirmacion(token);

        if (usuario == null) {
            System.out.println("No se encontró usuario para el token: " + token);
            return false;
        }

        if (usuario.isConfirmado()) {
            System.out.println("El usuario ya se encuentra confirmado.");
            return false;
        }

        // Actualizar el estado a confirmado e invalidar el token
        usuario.setConfirmado(true);
        usuario.setTokenConfirmacion(null);

        // Guardar los cambios en la base de datos
        RepUsuario.save(usuario);
        System.out.println("Usuario confirmado correctamente con token: " + token);
        return true;
    }

    public boolean eliminarUsuarioPorId(Long idUsuario) {
        try {
            if (RepUsuario.existsById(idUsuario)) {
                RepUsuario.deleteById(idUsuario);
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al intentar eliminar el usuario.");
        }
    }

    public usuarioDTO modificarUsuario(Long idUsuario, usuarioDTO usuarioActualizado) {
        return RepUsuario.findById(idUsuario).map(usuario -> {
            usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
            usuario.setNumeroUsuario(usuarioActualizado.getNumeroUsuario());
            usuario.setCorreoUsuario(usuarioActualizado.getCorreoUsuario());
            usuario.setRolUsuario(usuarioActualizado.getRolUsuario());
            // Si se proporciona una nueva contraseña, encriptarla
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                String contrasenaEncriptada = encriptarContrasenya(usuarioActualizado.getPassword());
                usuario.setPassword(contrasenaEncriptada);
            }
            usuario.setImagenUsuario(usuarioActualizado.getImagenUsuario());
            return RepUsuario.save(usuario);
        }).orElse(null);
    }

    public ArrayList<usuarioDTO> obtenerTodosLosUsuarios() {
        return RepUsuario.findAll();
    }
    
    public usuarioDTO autenticarUsuario(String correoUsuario, String password) {
        usuarioDTO usuario = RepUsuario.findByCorreoUsuario(correoUsuario);
        
        System.out.println(correoUsuario);
        System.out.println(password);

        
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        // Si la contraseña almacenada está encriptada, se debe encriptar el valor recibido y comparar.
        if (!password.equals(usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
    }
}

