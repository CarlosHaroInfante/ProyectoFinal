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
import org.springframework.web.bind.annotation.RestController;

import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.dtos.usuarioDTO;
import edu.Periodico.Prueba.servicios.servicioRecuperarContrasena;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/auth")
public class controladorRecuperarContrasena {

    @Autowired
    private servicioRecuperarContrasena ServicioRecuperarContraseña;
    
    @Autowired
    private repositorioUsuario repositorioUsu;

    // Endpoint para enviar el correo de recuperación (este endpoint se invoca cuando se solicita restablecer la contraseña)
    @PutMapping("/actualizarTokenRecuperacion")
    public ResponseEntity<?> actualizarTokenRecuperacion(@RequestBody TokenUpdateRequest request) {
        usuarioDTO usuario = repositorioUsu.findByCorreoUsuario(request.getCorreoUsuario());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"error\":\"El usuario no existe.\"}");
        }
        usuario.setTokenConfirmacion(request.getTokenConfirmacion());
        usuario.setConfirmado(request.isConfirmado());
        usuario = repositorioUsu.save(usuario);
        return ResponseEntity.ok("{\"success\": true, \"message\":\"Token actualizado.\"}");
    }
    
    @PostMapping("/actualizarContrasenaSinToken")
    public ResponseEntity<?> actualizarContrasenaSinToken(@RequestBody ActualizarContrasenaRecuperacionRequest request) {
        String apiResponse = ServicioRecuperarContraseña.actualizarContrasenaSinToken(
            request.getCorreo(),
            request.getNuevaPassword(),
            request.getConfirmarPassword()
        );
        
        JSONObject jsonResp = new JSONObject(apiResponse);
        if (jsonResp.optBoolean("success", false)) {
            return ResponseEntity.ok(jsonResp.toString());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(jsonResp.toString());
        }
    }
    
    // DTOs para la solicitud
    public static class RecuperarContrasenaRequest {
        private String correo;
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
    }
    
    public static class ActualizarContrasenaRecuperacionRequest {
        private String correo;
        private String codigoVerificacion;
        private String nuevaPassword;
        private String confirmarPassword;
        
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public String getCodigoVerificacion() { return codigoVerificacion; }
        public void setCodigoVerificacion(String codigoVerificacion) { this.codigoVerificacion = codigoVerificacion; }
        public String getNuevaPassword() { return nuevaPassword; }
        public void setNuevaPassword(String nuevaPassword) { this.nuevaPassword = nuevaPassword; }
        public String getConfirmarPassword() { return confirmarPassword; }
        public void setConfirmarPassword(String confirmarPassword) { this.confirmarPassword = confirmarPassword; }
    }
    
    public static class TokenUpdateRequest {
        private String correoUsuario;
        private String tokenConfirmacion;
        private boolean confirmado;
        
        public String getCorreoUsuario() {
            return correoUsuario;
        }
        public void setCorreoUsuario(String correoUsuario) {
            this.correoUsuario = correoUsuario;
        }
        public String getTokenConfirmacion() {
            return tokenConfirmacion;
        }
        public void setTokenConfirmacion(String tokenConfirmacion) {
            this.tokenConfirmacion = tokenConfirmacion;
        }
        public boolean isConfirmado() {
            return confirmado;
        }
        public void setConfirmado(boolean confirmado) {
            this.confirmado = confirmado;
        }
    }
}
