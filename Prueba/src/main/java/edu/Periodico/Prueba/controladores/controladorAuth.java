package edu.Periodico.Prueba.controladores;

import java.util.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.usuarioDTO;
import edu.Periodico.Prueba.servicios.servicioUsuario;

/**
 * Controlador de autenticación.
 * <p>
 * Este controlador gestiona los endpoints relacionados con la autenticación de usuarios, 
 * registro sin contraseña propia, actualización de la contraseña y confirmación del token.
 * </p>
 * 
 * 27/02/2025 - CHI
 */
@CrossOrigin(origins = "http://localhost:8080") // Permite solicitudes CORS desde "http://localhost:8080"
@RestController // Define la clase como un controlador REST que retorna respuestas en formato JSON
@RequestMapping("/api/auth") // Ruta base para todos los endpoints de autenticación
public class controladorAuth {

    @Autowired // Inyecta automáticamente el bean del servicio de usuario
    private servicioUsuario servicioUsuario;
    
    @Autowired // Inyecta automáticamente el bean del repositorio de usuario para acceder a la BBDD
    private repositorioUsuario repositorioUsu;
    
    /**
     * Endpoint del login.
     * <p>
     * Autentica al usuario verificando el correo y la contraseña.
     * Devuelve un objeto LoginResponse en caso de éxito o un mensaje de error.
     * </p>
     * 
     * @param loginRequest Objeto con el correo y la contraseña.
     * @return ResponseEntity con LoginResponse o error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            FicheroLog.logInfo("Login: Correo recibido: " + loginRequest.getCorreoUsuario());
            
            // Autentica al usuario
            usuarioDTO usuario = servicioUsuario.autenticarUsuario(loginRequest.getCorreoUsuario(), loginRequest.getPassword());
            FicheroLog.logInfo("Login: Usuario autenticado: " + usuario.getIdUsuario() + " - Rol: " + usuario.getRolUsuario());
            
            // Convertir la imagen del usuario a Base64, si existe
            String imagenUsuarioBase64 = "";
            if (usuario.getImagenUsuario() != null) {
                imagenUsuarioBase64 = Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
                FicheroLog.logInfo("Login: Imagen convertida a Base64.");
            } else {
                FicheroLog.logInfo("Login: El usuario no tiene imagen.");
            }
            return ResponseEntity.ok(new LoginResponse(usuario.getIdUsuario(), "Login exitoso", usuario.getRolUsuario(), imagenUsuarioBase64));
        } catch (RuntimeException e) {
            FicheroLog.logError("Login: Error al autenticar usuario", e);
            String errorMsg = e.getMessage();
            if (errorMsg.contains("Usuario no encontrado")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("{\"error\":\"El correo no existe.\"}");
            } else if (errorMsg.contains("Contraseña incorrecta")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("{\"error\":\"La contraseña es incorrecta.\"}");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("{\"error\":\"Correo o contraseña incorrectos.\"}");
            }
        }
    }

    /**
     * Endpoint del registro sin contraseña propia.
     * <p>
     * Registra al usuario y envía un correo con un token de verificación para que pueda cambiar su contraseña.
     * </p>
     * 
     * @param usuario Objeto usuarioDTO con los datos del usuario.
     * @return ResponseEntity con mensaje de éxito o error.
     */
    @PostMapping("/registroSinPassword")
    public ResponseEntity<?> registroSinPassword(@RequestBody usuarioDTO usuario) {
        try {
            FicheroLog.logInfo("Registro: Registrando usuario sin contraseña: " + usuario);
            boolean registrado = servicioUsuario.registrarUsuarioSinPassword(usuario); 
            if (registrado) { 
                FicheroLog.logInfo("Registro: Usuario registrado sin password propia.");
                return ResponseEntity.ok("Usuario registrado. Revisa tu correo para el código de verificación.");
            } else {
                FicheroLog.logInfo("Registro: Error en el registro.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el registro.");
            }
        } catch (Exception e) {
            FicheroLog.logError("Registro: Error al registrar el usuario sin contraseña", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno en el servidor.");
        }
    }

    /**
     * Endpoint para actualizar la contraseña.
     * <p>
     * Actualiza la contraseña del usuario, validando que el token recibido coincida con el almacenado.
     * </p>
     * 
     * @param request Objeto ActualizarPasswordRequest que contiene el correo, token, nueva contraseña y confirmación.
     * @return ResponseEntity con mensaje de éxito o error.
     */
    @PostMapping("/actualizarPassword")
    public ResponseEntity<?> actualizarPassword(@RequestBody ActualizarPasswordRequest request) {
        try {
            FicheroLog.logInfo("ActualizarPassword: Correo recibido: " + request.getCorreo());
            FicheroLog.logInfo("ActualizarPassword: Código recibido: " + request.getCodigoVerificacion());
            
            usuarioDTO usuario1 = repositorioUsu.findByCorreoUsuario(request.getCorreo());
            if (usuario1 != null) {
                FicheroLog.logInfo("ActualizarPassword: Token almacenado en DB: " + usuario1.getTokenConfirmacion());
            } else {
                FicheroLog.logInfo("ActualizarPassword: No se encontró usuario para el correo: " + request.getCorreo());
            }
            
            if (request.getNuevaPassword() == null || request.getConfirmarPassword() == null) {
                FicheroLog.logError("ActualizarPassword: Faltan datos de la contraseña.", new Exception("Datos de contraseña nulos"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"Faltan datos de la contraseña.\"}");
            }
            if (!request.getNuevaPassword().equals(request.getConfirmarPassword())) {
                FicheroLog.logError("ActualizarPassword: Las contraseñas no coinciden.", new Exception("Contraseñas no coinciden"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"Las contraseñas no coinciden.\"}");
            }
            
            usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(request.getCorreo());
            if (usuario == null) {
                FicheroLog.logError("ActualizarPassword: El correo no coincide con ningún usuario.", new Exception("Usuario no encontrado"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"El correo no coincide con ningún usuario.\"}");
            }
            
            if (usuario.getTokenConfirmacion() == null ||
                !usuario.getTokenConfirmacion().equalsIgnoreCase(request.getCodigoVerificacion())) {
                FicheroLog.logError("ActualizarPassword: El código de verificación es incorrecto.", new Exception("Código de verificación incorrecto"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"El código de verificación es incorrecto.\"}");
            }
            
            String passwordEncriptada = servicioUsuario.encriptarContrasenya(request.getNuevaPassword());
            usuario.setPassword(passwordEncriptada);
            usuario.setConfirmado(true);
            usuario.setTokenConfirmacion(null);
            repositorioUsu.save(usuario);
            
            FicheroLog.logInfo("ActualizarPassword: Contraseña actualizada correctamente para el usuario: " + request.getCorreo());
            return ResponseEntity.ok("{\"success\": true, \"message\":\"Contraseña actualizada correctamente.\"}");
        } catch (Exception e) {
            FicheroLog.logError("ActualizarPassword: Excepción en actualizarPassword", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al actualizar la contraseña.\"}");
        }
    }

    /**
     * Endpoint para confirmar el usuario mediante token.
     * <p>
     * Si el token es válido, se confirma el usuario; de lo contrario, se retorna un error.
     * </p>
     * 
     * @param token El token de verificación recibido.
     * @return ResponseEntity con mensaje de confirmación o error.
     */
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmarUsuario(@RequestParam("token") String token) {
        try {
            FicheroLog.logInfo("Confirm: Iniciando confirmación de usuario con token: " + token);
            boolean confirmado = servicioUsuario.confirmarUsuario(token);
            if (confirmado) {
                FicheroLog.logInfo("Confirm: Usuario confirmado correctamente con token: " + token);
                return ResponseEntity.ok("Usuario confirmado correctamente.");
            } else {
                FicheroLog.logError("Confirm: Token inválido o expirado: " + token, new Exception("Token inválido o expirado"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Token inválido o expirado.");
            }
        } catch (Exception e) {
            FicheroLog.logError("Confirm: Excepción al confirmar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno al confirmar usuario.");
        }
    }

    // DTOs para las solicitudes y respuestas

    /**
     * DTO para la solicitud de actualización de contraseña.
     */
    public static class ActualizarPasswordRequest {
        private String correo;
        private String codigoVerificacion;
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
         * @param correo El correo del usuario.
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
         * @param codigoVerificacion El código de verificación.
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
         * @param confirmarPassword La confirmación de la nueva contraseña.
         */
        public void setConfirmarPassword(String confirmarPassword) {
            this.confirmarPassword = confirmarPassword;
        }
    }
    
    /**
     * DTO para la solicitud de login.
     */
    public static class LoginRequest {
        private String correoUsuario;
        private String password;
        
        /**
         * @return El correo del usuario.
         */
        public String getCorreoUsuario() {
            return correoUsuario;
        }
        /**
         * Establece el correo del usuario.
         * @param correoUsuario El correo del usuario.
         */
        public void setCorreoUsuario(String correoUsuario) {
            this.correoUsuario = correoUsuario;
        }
        /**
         * @return La contraseña del usuario.
         */
        public String getPassword() {
            return password;
        }
        /**
         * Establece la contraseña del usuario.
         * @param password La contraseña del usuario.
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    /**
     * DTO para la respuesta del login.
     */
    public static class LoginResponse {
        private long idUsuario;
        private String mensaje;
        private String rolUsuario;
        private String imagenUsuario;
        
        /**
         * Constructor para LoginResponse.
         * @param idUsuario El ID del usuario.
         * @param mensaje Mensaje de respuesta.
         * @param rolUsuario Rol del usuario.
         * @param imagenUsuario Imagen del usuario en Base64.
         */
        public LoginResponse(long idUsuario, String mensaje, String rolUsuario, String imagenUsuario) {
            this.idUsuario = idUsuario;
            this.mensaje = mensaje;
            this.rolUsuario = rolUsuario;
            this.imagenUsuario = imagenUsuario;
        }
        
        /**
         * @return El ID del usuario.
         */
        public long getIdUsuario() {
            return idUsuario;
        }
        /**
         * Establece el ID del usuario.
         * @param idUsuario El ID del usuario.
         */
        public void setIdUsuario(long idUsuario) {
            this.idUsuario = idUsuario;
        }
        /**
         * @return El mensaje de respuesta.
         */
        public String getMensaje() {
            return mensaje;
        }
        /**
         * Establece el mensaje de respuesta.
         * @param mensaje El mensaje de respuesta.
         */
        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
        /**
         * @return El rol del usuario.
         */
        public String getRolUsuario() {
            return rolUsuario;
        }
        /**
         * Establece el rol del usuario.
         * @param rolUsuario El rol del usuario.
         */
        public void setRolUsuario(String rolUsuario) {
            this.rolUsuario = rolUsuario;
        }
        /**
         * @return La imagen del usuario en Base64.
         */
        public String getImagenUsuario() {
            return imagenUsuario;
        }
        /**
         * Establece la imagen del usuario en Base64.
         * @param imagenUsuario La imagen del usuario en Base64.
         */
        public void setImagenUsuario(String imagenUsuario) {
            this.imagenUsuario = imagenUsuario;
        }
    }
}
