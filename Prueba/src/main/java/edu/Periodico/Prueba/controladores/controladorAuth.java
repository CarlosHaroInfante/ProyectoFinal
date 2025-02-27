package edu.Periodico.Prueba.controladores;

import java.util.Base64;

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

@CrossOrigin(origins = "http://localhost:8080") // Permite solicitudes CORS desde "http://localhost:8080"
@RestController // Indica que esta clase es un controlador REST, devolviendo respuestas en formato JSON
@RequestMapping("/api/auth") // Define la ruta base "/api/auth" para todos los endpoints de este controlador
public class controladorAuth {

    @Autowired // Inyecta automáticamente el bean de servicioUsuario (gestión de usuarios)
    private servicioUsuario servicioUsuario;
    
    @Autowired // Inyecta automáticamente el bean de repositorioUsuario (acceso a la BBDD para usuarios)
    private repositorioUsuario repositorioUsu;
    
    /**
     * Endpoint del login donde se autentifica si llamará al servicio para autenticar si el correo y la contraseña existen y entrar con el correo y la sesión.
     * 27/02/2025 - CHI
     */
    // Endpoint de login (se mantiene igual)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Registrar los datos recibidos (nota: no se recomienda registrar contraseñas en texto claro en producción)
        //FicheroLog.logInfo("Correo recibido: " + loginRequest.getCorreoUsuario());
        //FicheroLog.logInfo("Contraseña recibida: " + loginRequest.getPassword());
        
        try {
            // Autenticar al usuario
            usuarioDTO usuario = servicioUsuario.autenticarUsuario(loginRequest.getCorreoUsuario(), loginRequest.getPassword()); //Auntetica buscando el correo en BBDD
            FicheroLog.logInfo("Usuario autenticado: " + usuario.getIdUsuario() + " - Rol: " + usuario.getRolUsuario());
            
            // Convertir la imagen del usuario a Base64, si existe
            String imagenUsuarioBase64 = "";
            if (usuario.getImagenUsuario() != null) {
                imagenUsuarioBase64 = Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
                FicheroLog.logInfo("Imagen del usuario convertida a Base64.");
            } else {
                FicheroLog.logInfo("El usuario no tiene imagen.");
            }
            
            // Retornar la respuesta de login exitoso
            return ResponseEntity.ok(new LoginResponse(usuario.getIdUsuario(), "Login exitoso", usuario.getRolUsuario(), imagenUsuarioBase64)); //Debuelve un login exitoso con el Id, el Rol y la imagen.
        } catch (RuntimeException e) {
            // Registrar el error ocurrido durante la autenticación
            FicheroLog.logError("Error al autenticar usuario", e);
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
     * Endpoint del registro que hára que se inserten los datos del formulario de la vista y se le genere un correo con un token y pueda cambiar la contraseña.
     * 27/02/2025 - CHI
     */
    @PostMapping("/registroSinPassword")
    public ResponseEntity<?> registroSinPassword(@RequestBody usuarioDTO usuario) {
        try {
            System.out.println("Registrando usuario sin contraseña: " + usuario);
            FicheroLog.logInfo("Registrando usuario sin contraseña: " + usuario);
            boolean registrado = servicioUsuario.registrarUsuarioSinPassword(usuario); 
            if (registrado) { 
                FicheroLog.logInfo("Usuario registrado sin password propia.");
                return ResponseEntity.ok("Usuario registrado. Revisa tu correo para el código de verificación.");
            } else {
                FicheroLog.logInfo("Error en el registro.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el registro.");
            }
        } catch (Exception e) {
            // Registra el error y devuelve una respuesta de error
            FicheroLog.logError("Error al registrar el usuario sin contraseña", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno en el servidor.");
        }
    }


    /**
     * Endpoint del cambiar a una contraseña personalizada del usuario contraseña cuando te llegua el correo al email.
     * 27/02/2025 - CHI
     */
    @PostMapping("/actualizarPassword")
    public ResponseEntity<?> actualizarPassword(@RequestBody ActualizarPasswordRequest request) {
        try {
            // Registrar datos recibidos (cuidado: en producción evita registrar contraseñas en texto claro)
            FicheroLog.logInfo("Correo recibido: " + request.getCorreo());
            FicheroLog.logInfo("Código recibido: " + request.getCodigoVerificacion());
            FicheroLog.logInfo("Nueva contraseña recibida: " + request.getNuevaPassword());
            FicheroLog.logInfo("Confirmar contraseña recibida: " + request.getConfirmarPassword());
            
            // Buscar el usuario por correo para ver el token almacenado (solo para log)
            usuarioDTO usuario1 = repositorioUsu.findByCorreoUsuario(request.getCorreo());
            if (usuario1 != null) {
                FicheroLog.logInfo("Token almacenado en DB: " + usuario1.getTokenConfirmacion());
            } else {
                FicheroLog.logInfo("No se encontró usuario para el correo: " + request.getCorreo());
            }
            
            // Validar que se han enviado las contraseñas y que coinciden
            if (request.getNuevaPassword() == null || request.getConfirmarPassword() == null) {
                FicheroLog.logError("Faltan datos de la contraseña.", new Exception("Datos de contraseña nulos"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"Faltan datos de la contraseña.\"}");
            }
            if (!request.getNuevaPassword().equals(request.getConfirmarPassword())) {
                FicheroLog.logError("Las contraseñas no coinciden.", new Exception("Contraseñas no coinciden"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"Las contraseñas no coinciden.\"}");
            }
            
            // Buscar el usuario por correo utilizando el repositorio inyectado
            usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(request.getCorreo());
            if (usuario == null) {
                FicheroLog.logError("El correo no coincide con ningún usuario.", new Exception("Usuario no encontrado"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"El correo no coincide con ningún usuario.\"}");
            }
            
            // Comparar el código de verificación
            if (usuario.getTokenConfirmacion() == null ||
                !usuario.getTokenConfirmacion().equalsIgnoreCase(request.getCodigoVerificacion())) {
                FicheroLog.logError("El código de verificación es incorrecto.", new Exception("Código de verificación incorrecto"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"El código de verificación es incorrecto.\"}");
            }
            
            // Actualizar la contraseña: encriptarla y actualizar el usuario
            String passwordEncriptada = servicioUsuario.encriptarContrasenya(request.getNuevaPassword());
            usuario.setPassword(passwordEncriptada);
            usuario.setConfirmado(true);
            usuario.setTokenConfirmacion(null);
            repositorioUsu.save(usuario);
            
            FicheroLog.logInfo("Contraseña actualizada correctamente para el usuario: " + request.getCorreo());
            return ResponseEntity.ok("{\"success\": true, \"message\":\"Contraseña actualizada correctamente.\"}");
        } catch (Exception e) {
            FicheroLog.logError("Excepción en actualizarPassword", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al actualizar la contraseña.\"}");
        }
    }



    /**
     * Endpoint para confirmar si el token es correcto que pase a confirmado, y si no es confirmado pasado pasado 10 minutos el usuario se elimina d .
     * 27/02/2025 - CHI
     */
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmarUsuario(@RequestParam("token") String token) {
        try {
            FicheroLog.logInfo("Iniciando confirmación de usuario con token: " + token);
            boolean confirmado = servicioUsuario.confirmarUsuario(token);
            if (confirmado) {
                FicheroLog.logInfo("Usuario confirmado correctamente con token: " + token);
                return ResponseEntity.ok("Usuario confirmado correctamente.");
            } else {
                FicheroLog.logError("Token inválido o expirado: " + token, new Exception("Token inválido o expirado"));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Token inválido o expirado.");
            }
        } catch (Exception e) {
            FicheroLog.logError("Excepción al confirmar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno al confirmar usuario.");
        }
    }


    // Clases internas para recibir y enviar datos en las peticiones/respuestas

    // Clase para la solicitud de actualización de contraseña
    public static class ActualizarPasswordRequest {
        private String correo;
        private String codigoVerificacion;
        private String nuevaPassword;
        private String confirmarPassword;
        
        public String getCorreo() {
            return correo;
        }
        public void setCorreo(String correo) {
            this.correo = correo;
        }
        public String getCodigoVerificacion() {
            return codigoVerificacion;
        }
        public void setCodigoVerificacion(String codigoVerificacion) {
            this.codigoVerificacion = codigoVerificacion;
        }
        public String getNuevaPassword() {
            return nuevaPassword;
        }
        public void setNuevaPassword(String nuevaPassword) {
            this.nuevaPassword = nuevaPassword;
        }
        public String getConfirmarPassword() {
            return confirmarPassword;
        }
        public void setConfirmarPassword(String confirmarPassword) {
            this.confirmarPassword = confirmarPassword;
        }
    }
 // Clases internas para request y response
    public static class LoginRequest {
        private String correoUsuario;
        private String password;
        
        public String getCorreoUsuario() {
            return correoUsuario;
        }
        public void setCorreoUsuario(String correoUsuario) {
            this.correoUsuario = correoUsuario;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    // Se ha añadido el campo imagenUsuario en la respuesta
    public static class LoginResponse {
        private long idUsuario;
        private String mensaje;
        private String rolUsuario;
        private String imagenUsuario;
        
        public LoginResponse(long idUsuario, String mensaje, String rolUsuario, String imagenUsuario) {
            this.idUsuario = idUsuario;
            this.mensaje = mensaje;
            this.rolUsuario = rolUsuario;
            this.imagenUsuario = imagenUsuario;
        }
        
        public long getIdUsuario() {
            return idUsuario;
        }
        public void setIdUsuario(long idUsuario) {
            this.idUsuario = idUsuario;
        }
        public String getMensaje() {
            return mensaje;
        }
        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
        public String getRolUsuario() {
            return rolUsuario;
        }
        public void setRolUsuario(String rolUsuario) {
            this.rolUsuario = rolUsuario;
        }
        public String getImagenUsuario() {
            return imagenUsuario;
        }
        public void setImagenUsuario(String imagenUsuario) {
            this.imagenUsuario = imagenUsuario;
        }
    }
}


