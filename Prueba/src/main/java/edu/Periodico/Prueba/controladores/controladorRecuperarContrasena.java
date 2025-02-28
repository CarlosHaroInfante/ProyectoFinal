package edu.Periodico.Prueba.controladores;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.usuarioDTO;
import edu.Periodico.Prueba.servicios.servicioRecuperarContrasena;

/**
 * Controlador para gestionar el CRUD para recuperar la contraseña.
 * <p>
 * Este controlador expone endpoints para actualizar el token de recuperación,
 * actualizar la contraseña sin validar el token y confirmar el usuario mediante token.
 * </p>
 * 27/02/2025 - CHI
 */
@CrossOrigin(origins = "http://localhost:8080") // Permite solicitudes CORS desde "http://localhost:8080"
@RestController // Indica que esta clase es un controlador REST; los métodos retornan JSON.
@RequestMapping("/api/auth") // Define la ruta base para todos los endpoints de este controlador.
public class controladorRecuperarContrasena {

    /**
     * Servicio encargado de la recuperación de contraseña.
     */
    @Autowired
    private servicioRecuperarContrasena ServicioRecuperarContraseña;
    
    /**
     * Repositorio de usuario para acceder a la base de datos.
     */
    @Autowired
    private repositorioUsuario repositorioUsu;

    /**
     * Endpoint para actualizar el token de recuperación del usuario.
     * <p>
     * Se invoca cuando se solicita restablecer la contraseña. Actualiza el token y el estado
     * de confirmación para el usuario correspondiente al correo recibido.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request Objeto TokenUpdateRequest que contiene el correo, el token y el estado de confirmación.
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @PutMapping("/actualizarTokenRecuperacion")
    public ResponseEntity<?> actualizarTokenRecuperacion(@RequestBody TokenUpdateRequest request) {
        try {
            FicheroLog.logInfo("ActualizarToken: Recibido token para correo: " + request.getCorreoUsuario());
            
            usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(request.getCorreoUsuario());
            if (usuario == null) {
                FicheroLog.logInfo("ActualizarToken: No se encontró usuario para el correo: " + request.getCorreoUsuario());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("{\"error\":\"El usuario no existe.\"}");
            }
            
            // Asigna el token y el estado de confirmación.
            usuario.setTokenConfirmacion(request.getTokenConfirmacion());
            usuario.setConfirmado(request.isConfirmado());
            // Si deseas, podrías también asignar tokenFecha aquí, si no lo envías desde el cliente.
            usuario = repositorioUsu.save(usuario);
            
            FicheroLog.logInfo("ActualizarToken: Token actualizado para el usuario: " + usuario.getCorreoUsuario());
            return ResponseEntity.ok("{\"success\": true, \"message\":\"Token actualizado.\"}");
        } catch (Exception e) {
            FicheroLog.logError("ActualizarToken: Excepción al actualizar token para el correo: " + request.getCorreoUsuario(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al actualizar el token.\"}");
        }
    }
    
    /**
     * Endpoint para actualizar la contraseña sin requerir la verificación del token.
     * <p>
     * Se invoca desde la vista cuando se desea cambiar la contraseña sin validar el token.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request Objeto ActualizarContrasenaRecuperacionRequest que contiene el correo, 
     *                el código de verificación (no utilizado en este flujo), la nueva contraseña y su confirmación.
     * @return ResponseEntity con el resultado de la actualización o un error.
     */
    @PostMapping("/actualizarContrasenaSinToken")
    public ResponseEntity<?> actualizarContrasenaSinToken(@RequestBody ActualizarContrasenaRecuperacionRequest request) {
        try {
            FicheroLog.logInfo("ActualizarContrasenaSinToken: Iniciando proceso para correo: " + request.getCorreo());
            String apiResponse = ServicioRecuperarContraseña.actualizarContrasenaSinToken(
                request.getCorreo(),
                request.getNuevaPassword(),
                request.getConfirmarPassword()
            );
            
            JSONObject jsonResp = new JSONObject(apiResponse);
            if (jsonResp.optBoolean("success", false)) {
                FicheroLog.logInfo("ActualizarContrasenaSinToken: Contraseña actualizada correctamente para: " + request.getCorreo());
                return ResponseEntity.ok(jsonResp.toString());
            } else {
                FicheroLog.logInfo("ActualizarContrasenaSinToken: Error en la actualización para: " + request.getCorreo());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body(jsonResp.toString());
            }
        } catch (Exception e) {
            FicheroLog.logError("ActualizarContrasenaSinToken: Excepción al actualizar la contraseña para: " + request.getCorreo(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al actualizar la contraseña.\"}");
        }
    }
    
    /**
     * DTO para la solicitud de recuperación de contraseña.
     * 27/02/2025 - CHI
     */
    public static class RecuperarContrasenaRequest {
        private String correo;
        
        /**
         * @return El correo del usuario.
         */
        public String getCorreo() { 
            return correo; 
        }
        
        /**
         * Establece el correo del usuario.
         * @param correo El correo.
         */
        public void setCorreo(String correo) { 
            this.correo = correo; 
        }
    }
    
    /**
     * DTO para la solicitud de actualización de contraseña sin validar el token.
     * 27/02/2025 - CHI
     */
    public static class ActualizarContrasenaRecuperacionRequest {
        private String correo;
        private String codigoVerificacion; // No se utiliza en este método, pero se puede dejar para otros flujos.
        private String nuevaPassword;
        private String confirmarPassword;
        
        /**
         * @return El correo del usuario.
         */
        public String getCorreo() { 
            return correo; 
        }
        
        /**
         * Establece el correo del usuario.
         * @param correo El correo.
         */
        public void setCorreo(String correo) { 
            this.correo = correo; 
        }
        
        /**
         * @return El código de verificación.
         */
        public String getCodigoVerificacion() { 
            return codigoVerificacion; 
        }
        
        /**
         * Establece el código de verificación.
         * @param codigoVerificacion El código.
         */
        public void setCodigoVerificacion(String codigoVerificacion) { 
            this.codigoVerificacion = codigoVerificacion; 
        }
        
        /**
         * @return La nueva contraseña.
         */
        public String getNuevaPassword() { 
            return nuevaPassword; 
        }
        
        /**
         * Establece la nueva contraseña.
         * @param nuevaPassword La nueva contraseña.
         */
        public void setNuevaPassword(String nuevaPassword) { 
            this.nuevaPassword = nuevaPassword; 
        }
        
        /**
         * @return La confirmación de la nueva contraseña.
         */
        public String getConfirmarPassword() { 
            return confirmarPassword; 
        }
        
        /**
         * Establece la confirmación de la nueva contraseña.
         * @param confirmarPassword La confirmación.
         */
        public void setConfirmarPassword(String confirmarPassword) { 
            this.confirmarPassword = confirmarPassword; 
        }
    }
    
    /**
     * DTO para la solicitud de actualización del token.
     * 27/02/2025 - CHI
     */
    public static class TokenUpdateRequest {
        private String correoUsuario;
        private String tokenConfirmacion;
        private boolean confirmado;
        
        /**
         * @return El correo del usuario.
         */
        public String getCorreoUsuario() {
            return correoUsuario;
        }
        
        /**
         * Establece el correo del usuario.
         * @param correoUsuario El correo.
         */
        public void setCorreoUsuario(String correoUsuario) {
            this.correoUsuario = correoUsuario;
        }
        
        /**
         * @return El token de verificación.
         */
        public String getTokenConfirmacion() {
            return tokenConfirmacion;
        }
        
        /**
         * Establece el token de verificación.
         * @param tokenConfirmacion El token.
         */
        public void setTokenConfirmacion(String tokenConfirmacion) {
            this.tokenConfirmacion = tokenConfirmacion;
        }
        
        /**
         * @return El estado de confirmación.
         */
        public boolean isConfirmado() {
            return confirmado;
        }
        
        /**
         * Establece el estado de confirmación.
         * @param confirmado El estado (true o false).
         */
        public void setConfirmado(boolean confirmado) {
            this.confirmado = confirmado;
        }
    }
}
