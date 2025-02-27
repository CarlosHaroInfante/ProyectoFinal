package edu.Periodico.Prueba.servicios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.usuarioDTO;

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

    
    // Método: Registrar usuario sin contraseña (versión API)
    // La vista ya generó el token y configuró password = "" y confirmado = false.
    public boolean registrarUsuarioSinPassword(usuarioDTO usuario) {
        // Verificar si ya existe un usuario con ese correo
        long count = RepUsuario.countByCorreoUsuario(usuario.getCorreoUsuario());
        if (count > 0) {
            System.out.println("El correo " + usuario.getCorreoUsuario() + " ya está registrado (count = " + count + ").");
            return false;
        }
        
        // Guardar el usuario en la base de datos usando el repositorio
        usuarioDTO usuarioGuardado = RepUsuario.save(usuario);
        if (usuarioGuardado == null || usuarioGuardado.getIdUsuario() == 0) {
            System.out.println("Error al guardar el usuario en la base de datos.");
            return false;
        }
        System.out.println("Usuario guardado correctamente: " + usuarioGuardado);
        return true;
    }
            
    
    // Método para actualizar la contraseña (después de verificar el código)
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
        System.out.println("Confirmando token: " + token);
        usuarioDTO usuario = RepUsuario.findByTokenConfirmacion(token);
        if (usuario == null) {
            System.out.println("No se encontró usuario para el token: " + token);
            return false;
        }
        if (usuario.isConfirmado()) {
            System.out.println("El usuario ya se encuentra confirmado.");
            return false;
        }
        System.out.println("Usuario encontrado: " + usuario.getNombreCompleto() + ", token: " + usuario.getTokenConfirmacion());
        
        usuario.setConfirmado(true);
        usuario.setTokenConfirmacion(null);
        usuarioDTO actualizado = RepUsuario.save(usuario);
        if (actualizado != null && actualizado.isConfirmado() && actualizado.getTokenConfirmacion() == null) {
            System.out.println("Usuario confirmado correctamente con token: " + token);
            return true;
        } else {
            System.out.println("Error al confirmar el usuario.");
            return false;
        }
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
        if (!password.equals(usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        return usuario;
    }
}
