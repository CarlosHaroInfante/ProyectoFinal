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
import edu.Periodico.Prueba.dtos.usuarioDTO;
import edu.Periodico.Prueba.servicios.servicioUsuario;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/auth")
public class controladorAuth {

    @Autowired
    private servicioUsuario servicioUsuario;
    
    @Autowired
    private repositorioUsuario repositorioUsu;

    // Endpoint de login (se mantiene igual)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("Correo recibido: " + loginRequest.getCorreoUsuario());
        System.out.println("Contraseña recibida: " + loginRequest.getPassword());
        
        try {
            usuarioDTO usuario = servicioUsuario.autenticarUsuario(loginRequest.getCorreoUsuario(), loginRequest.getPassword());
            System.out.println("Usuario autenticado: " + usuario.getIdUsuario() + " " + usuario.getRolUsuario());
            String imagenUsuarioBase64 = "";
            if (usuario.getImagenUsuario() != null) {
                // Convertir el arreglo de bytes a Base64
                imagenUsuarioBase64 = Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
            }
            return ResponseEntity.ok(new LoginResponse(usuario.getIdUsuario(), "Login exitoso", usuario.getRolUsuario(), imagenUsuarioBase64));
        } catch (RuntimeException e) {
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

    // Endpoint para registrar usuario SIN contraseña (envía código de verificación por correo)
    @PostMapping("/registroSinPassword")
    public ResponseEntity<?> registroSinPassword(@RequestBody usuarioDTO usuario) {
        System.out.println("Registrando usuario sin contraseña: " + usuario);
        boolean registrado = servicioUsuario.registrarUsuarioSinPassword(usuario);
        if (registrado) {
            return ResponseEntity.ok("Usuario registrado. Revisa tu correo para el código de verificación.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el registro.");
        }
    }


    //Endpoint para actualizar la contraseña después de verificar el código recibido por correo
        @PostMapping("/actualizarPassword")
        public ResponseEntity<?> actualizarPassword(@RequestBody ActualizarPasswordRequest request) {
        System.out.println("Correo recibido: " + request.getCorreo());
        System.out.println("Código recibido: " + request.getCodigoVerificacion());
        System.out.println("Nueva contraseña recibida: " + request.getNuevaPassword());
        System.out.println("Confirmar contraseña recibida: " + request.getConfirmarPassword());
        
        usuarioDTO usuario1 = repositorioUsu.findByCorreoUsuario(request.getCorreo());
        if (usuario1 != null) {
            System.out.println("Token almacenado en DB: " + usuario1.getTokenConfirmacion());
        } else {
            System.out.println("No se encontró usuario para el correo: " + request.getCorreo());
        }
        
        // Validar que se han enviado las contraseñas y que coinciden
        if (request.getNuevaPassword() == null || request.getConfirmarPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Faltan datos de la contraseña.\"}");
        }
        if (!request.getNuevaPassword().equals(request.getConfirmarPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Las contraseñas no coinciden.\"}");
        }
        
        // Buscar el usuario por correo utilizando el repositorio inyectado
        usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(request.getCorreo());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"El correo no coincide con ningún usuario.\"}");
        }
        
        // Comparar el código de verificación
        if (usuario.getTokenConfirmacion() == null ||
                !usuario.getTokenConfirmacion().equalsIgnoreCase(request.getCodigoVerificacion())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\":\"El código de verificación es incorrecto.\"}");
            }	
        
        // Actualizar la contraseña: encriptarla y actualizar el usuario
        String passwordEncriptada = servicioUsuario.encriptarContrasenya(request.getNuevaPassword());
        usuario.setPassword(passwordEncriptada);
        usuario.setConfirmado(true);
        usuario.setTokenConfirmacion(null);
        repositorioUsu.save(usuario);
        
        return ResponseEntity.ok("{\"success\": true, \"message\":\"Contraseña actualizada correctamente.\"}");
    }


    // (Opcional) Endpoint GET de confirmación, si lo deseas conservar.
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmarUsuario(@RequestParam("token") String token) {
        boolean confirmado = servicioUsuario.confirmarUsuario(token);
        if (confirmado) {
            return ResponseEntity.ok("Usuario confirmado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Token inválido o expirado.");
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


