
package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

import org.json.JSONObject;

import VistaPrueba2.Dtos.usuarioDTO;
import edu.Periodico.Prueba.Util.EmailUtil;

public class RegistroServicio {

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

    // Método: Registrar usuario sin contraseña (vista)
    // La vista se encarga de generar el token y enviar el correo; luego, envía el usuario a la API para que lo inserte.
    public boolean registrarUsuarioSinPassword(String nombreCompleto, String numeroUsuario, String correoUsuario, String rolUsuario, byte[] imagenUsuario) {
        // Crear el objeto usuario
        usuarioDTO usuario = new usuarioDTO();
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setNumeroUsuario(numeroUsuario);
        usuario.setCorreoUsuario(correoUsuario);
        usuario.setRolUsuario(rolUsuario);
        usuario.setImagenUsuario(imagenUsuario);
        usuario.setPassword(""); // Registro sin contraseña

        // Generar el token de verificación
        String token = generateRandomCode(10);
        usuario.setTokenConfirmacion(token);
        usuario.setConfirmado(false);

        // Convertir el objeto usuario a JSON para enviarlo a la API
        JSONObject json = new JSONObject();
        json.put("nombreCompleto", usuario.getNombreCompleto());
        json.put("numeroUsuario", usuario.getNumeroUsuario());
        json.put("correoUsuario", usuario.getCorreoUsuario());
        json.put("rolUsuario", usuario.getRolUsuario());
        json.put("password", usuario.getPassword());
        json.put("tokenConfirmacion", usuario.getTokenConfirmacion());
        json.put("confirmado", usuario.isConfirmado());
        json.put("tokenFecha", LocalDateTime.now().toString());
        if (usuario.getImagenUsuario() != null) {
            String imagenBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
            json.put("imagenUsuario", imagenBase64);
        }

        // Cambiar la URL al endpoint de registro sin contraseña en la API
        String urlApi = "http://localhost:9526/api/auth/registroSinPassword";
        try {
            URL url = new URL(urlApi);
            HttpURLConnection conex = (HttpURLConnection) url.openConnection();
            conex.setRequestMethod("POST");
            conex.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conex.setDoOutput(true);
            try (OutputStream os = conex.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int responseCode = conex.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conex.getInputStream(), "utf-8"));
                StringBuilder responseStr = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    responseStr.append(line);
                }
                in.close();
                System.out.println("Respuesta de la API (registro sin password): " + responseStr.toString());
            } else {
                System.out.println("Error al registrar el usuario en la API, response code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Enviar correo de verificación desde la vista
        String verificationLink = "http://localhost:8080/VistaPrueba2/verificarCodigo.html?correo=" + correoUsuario;
        String body = "Hola " + nombreCompleto + ",\n\n" +
                      "Tu código de verificación es: " + token + "\n\n" +
                      "Haz clic en el siguiente enlace para verificar tu cuenta y establecer tu contraseña:\n" +
                      verificationLink + "\n\n" +
                      "Si no solicitaste este registro, ignora este mensaje.";
        EmailUtil.sendEmail(correoUsuario, "Código de Verificación", body);

        return true;
    }
    
    // Método para confirmar el usuario (llama a la API)
    public boolean confirmarUsuario(String tokenConfirmacion) {
        try {
            String urlApi = "http://localhost:9526/api/usuarios/confirmar?token=" + tokenConfirmacion;
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseStr.append(line);
            }
            in.close();
            System.out.println("Respuesta de confirmación: " + responseStr.toString());
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
 // Método para actualizar la contraseña en el flujo de recuperación.
    // Este método se invoca desde el servlet de la vista y envía los datos a la API.
    public String actualizarContrasenaRecuperacion(String correoUsuario, String tokenConfirmacion, String nuevaPassword, String confirmarPassword) {
        if (!nuevaPassword.equals(confirmarPassword)) {
            return "{\"error\":\"Las contraseñas no coinciden.\"}";
        }
        try {
            JSONObject json = new JSONObject();
            json.put("correo", correoUsuario);
            json.put("codigoVerificacion", tokenConfirmacion);
            json.put("nuevaPassword", nuevaPassword);
            json.put("confirmarPassword", confirmarPassword);
            
            
            String urlApi = "http://localhost:9526/api/auth/actualizarPassword";
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            
            StringBuilder params = new StringBuilder();
            params.append("correo=").append(URLEncoder.encode(correoUsuario, "UTF-8"));
            params.append("&codigoVerificacion=").append(URLEncoder.encode(tokenConfirmacion, "UTF-8"));
            params.append("&nuevaPassword=").append(URLEncoder.encode(nuevaPassword, "UTF-8"));
            params.append("&confirmarPassword=").append(URLEncoder.encode(confirmarPassword, "UTF-8"));
            
            try (OutputStream os = connection.getOutputStream()) {
                os.write(params.toString().getBytes("UTF-8"));
            }
            
            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseStr.append(line);
            }
            in.close();
            System.out.println("Respuesta de la API (actualización contraseña): " + responseStr.toString());
            return responseStr.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Error al actualizar la contraseña.\"}";
        }
    }
    
 // Método para enviar el correo de restablecimiento de contraseña desde la vista.
    // La vista genera el token, actualiza (si existe un endpoint en la API para ello) y envía el correo.
    public boolean enviarCorreoRestablecerContrasena(String nombreCompleto, String correoUsuario) {
        // Generar token de recuperación
    	
    	
        String token = generateRandomCode(10);
        
        // Actualizar el usuario en la API con el token (si tienes un endpoint para ello)
        try {
            JSONObject json = new JSONObject();
            json.put("correoUsuario", correoUsuario);
            json.put("tokenConfirmacion", token);   // Incluir el token generado
            json.put("confirmado", false); 
            
            String urlApi = "http://localhost:9526/api/auth/actualizarTokenRecuperacion";
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
                System.out.println("Error al actualizar el token en la API, response code: " + responseCode);
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder responseStr = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseStr.append(line);
            }
            in.close();
            System.out.println("Token actualizado en API: " + responseStr.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
        // Construir el enlace para restablecer la contraseña
        try {
        	String verificationLink = "http://localhost:8080/VistaPrueba2/actualizarContrasena.html?correo="
                    + URLEncoder.encode(correoUsuario, StandardCharsets.UTF_8.toString())
                    + "&codigoVerificacion=" + URLEncoder.encode(token, StandardCharsets.UTF_8.toString());

            String body = "Hola " + nombreCompleto + ",\n\n" +
                          "Tu código de verificación es: " + token + "\n\n" +
                          "Haz clic en el siguiente enlace para verificar tu cuenta y establecer tu contraseña:\n" +
                          verificationLink + "\n\n" +
                          "Si no solicitaste este registro, ignora este mensaje.";
            EmailUtil.sendEmail(correoUsuario, "Código de Verificación", body);
            System.out.println("Correo de recuperación enviado a " + correoUsuario + " con token " + token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
