package edu.Periodico.Prueba.controladores;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.Periodico.Prueba.servicios.servicioRecuperarContrasena;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/auth")
public class controladorRecuperarContrasena {

    
	@Autowired
    private servicioRecuperarContrasena ServicioRecuperarContraseña;

 // Endpoint para enviar el correo de recuperación
	@PostMapping("/recuperarContrasena")
    public ResponseEntity<?> recuperarContrasena(@RequestBody RecuperarContrasenaRequest request) {
        boolean exito = ServicioRecuperarContraseña.recuperarContrasena(request.getCorreo());
        if (exito) {
            return ResponseEntity.ok("{\"success\": true, \"message\":\"Correo de recuperación enviado.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("{\"error\":\"El correo no existe o hubo un error.\"}");
        }
    }
    
    // Endpoint exclusivo para actualizar la contraseña en el flujo de recuperación
    @PostMapping("/actualizarContrasenaRecuperacion")
    public ResponseEntity<?> actualizarContrasenaRecuperacion(@RequestBody ActualizarContrasenaRecuperacionRequest request) {
    	// Validar que se han enviado las contraseñas y que coinciden
        if (request.getNuevaPassword() == null || request.getConfirmarPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Faltan datos de la contraseña.\"}");
        }
        if (!request.getNuevaPassword().equals(request.getConfirmarPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Las contraseñas no coinciden.\"}");
        }
        
        // Llamar al servicio exclusivo para actualizar la contraseña de recuperación
        String apiResponse = ServicioRecuperarContraseña.actualizarContrasenaRecuperacion(
            request.getCorreo(),
            request.getCodigoVerificacion(),
            request.getNuevaPassword(),
            request.getConfirmarPassword()
        );
        
        // Parsear la respuesta JSON para verificar si "success" es true
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
}