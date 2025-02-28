package edu.Periodico.Prueba.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.usuarioDTO;

/**
 * Servicio para la recuperación de contraseñas.
 * <p>
 * Este servicio ofrece métodos para actualizar la contraseña del usuario en el flujo de recuperación,
 * tanto validando el token como sin requerirlo.
 * </p>
 * 27/02/2025 - CHI
 */
@Service
public class servicioRecuperarContrasena {
    
    /**
     * Repositorio de usuario para acceder a la base de datos.
     */
    @Autowired
    private repositorioUsuario repositorioUsu;
    
    /**
     * Servicio de usuario para realizar operaciones relacionadas con los usuarios.
     */
    @Autowired
    private servicioUsuario servicioUsuario;
    
    /**
     * Actualiza la contraseña del usuario en el flujo de recuperación.
     * <p>
     * Valida que el token recibido coincida con el almacenado en la base de datos, encripta la nueva contraseña 
     * y actualiza el usuario.
     * </p>
     * 27/02/2025 - CHI
     * 
     * @param correoUsuario    El correo del usuario.
     * @param token            El código de verificación enviado por correo.
     * @param nuevaPassword    La nueva contraseña.
     * @param confirmarPassword La confirmación de la nueva contraseña.
     * @return Un String en formato JSON indicando éxito o error.
     */
    public String actualizarContrasenaRecuperacion(String correoUsuario, String token, String nuevaPassword, String confirmarPassword) {
        try {
            // Validar que ambas contraseñas coinciden.
            if (!nuevaPassword.equals(confirmarPassword)) {
                return "{\"error\":\"Las contraseñas no coinciden.\"}";
            }
            
            // Buscar el usuario por correo.
            usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(correoUsuario);
            if (usuario == null) {
                return "{\"error\":\"El correo no coincide con ningún usuario.\"}";
            }
            
            // Verificar el token de recuperación.
            if (usuario.getTokenConfirmacion() == null || !usuario.getTokenConfirmacion().equalsIgnoreCase(token)) {
                return "{\"error\":\"El código de verificación es incorrecto.\"}";
            }
            
            // Encriptar la nueva contraseña.
            String passwordEncriptada = servicioUsuario.encriptarContrasenya(nuevaPassword);
            usuario.setPassword(passwordEncriptada);
            usuario.setConfirmado(true);
            usuario.setTokenConfirmacion(null);
            
            // Guardar los cambios en la base de datos.
            repositorioUsu.save(usuario);
            
            return "{\"success\": true, \"message\":\"Contraseña actualizada correctamente.\"}";
        } catch (Exception e) {
            FicheroLog.logError("Error en actualizarContrasenaRecuperacion", e);
            return "{\"error\":\"Error interno al actualizar la contraseña.\"}";
        }
    }
    
    /**
     * Actualiza la contraseña del usuario sin requerir la verificación del token.
     * <p>
     * Este método omite la verificación del token (utilizado normalmente para construir la URL) 
     * y actualiza la contraseña del usuario directamente.
     * </p>
     * 27/02/2025 - CHI
     * 
     * @param correoUsuario    El correo del usuario.
     * @param nuevaPassword    La nueva contraseña.
     * @param confirmarPassword La confirmación de la nueva contraseña.
     * @return Un String en formato JSON indicando éxito o error.
     */
    public String actualizarContrasenaSinToken(String correoUsuario, String nuevaPassword, String confirmarPassword) {
        try {
            // Validar que ambas contraseñas coinciden.
            if (!nuevaPassword.equals(confirmarPassword)) {
                return "{\"error\":\"Las contraseñas no coinciden.\"}";
            }
            
            // Buscar el usuario por correo.
            usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(correoUsuario);
            if (usuario == null) {
                return "{\"error\":\"El correo no coincide con ningún usuario.\"}";
            }
            
            // Aquí se omite la verificación del token, ya que el token solo se usa para construir la URL.
            // Encriptar la nueva contraseña.
            String passwordEncriptada = servicioUsuario.encriptarContrasenya(nuevaPassword);
            usuario.setPassword(passwordEncriptada);
            usuario.setConfirmado(true);
            usuario.setTokenConfirmacion(null);  // Limpiamos el token, si lo hubiese.
            
            // Guardar los cambios en la base de datos.
            repositorioUsu.save(usuario);
            
            return "{\"success\": true, \"message\":\"Contraseña actualizada correctamente sin requerir token.\"}";
        } catch (Exception e) {
            FicheroLog.logError("Error en actualizarContrasenaSinToken", e);
            return "{\"error\":\"Error interno al actualizar la contraseña.\"}";
        }
    }
}
