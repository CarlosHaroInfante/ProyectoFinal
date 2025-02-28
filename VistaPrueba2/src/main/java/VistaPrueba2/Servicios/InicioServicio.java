package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import VistaPrueba2.Dtos.usuarioDTO;

/**
 * Servicio para la autenticación de usuarios.
 * <p>
 * Este servicio proporciona métodos para encriptar contraseñas, verificar usuarios y autenticar usuarios,
 * comunicándose con un endpoint de la API para el login.
 * </p>
 * 27/02/2025 - CHI
 */
public class InicioServicio {
    
    private static final Logger log = LoggerFactory.getLogger(InicioServicio.class);

    /**
     * Encripta la contraseña utilizando el algoritmo SHA-256.
     * 
     * @param password La contraseña a encriptar.
     * @return La contraseña encriptada en formato hexadecimal.
     * @throws RuntimeException Si el algoritmo SHA-256 no está disponible.
     */
    public String encriptarContrasenya(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            String encriptado = hexString.toString();
            log.info("[SERVICIO] Contraseña encriptada: {}", encriptado);
            return encriptado;
        } catch (NoSuchAlgorithmException e) {
            log.error("[SERVICIO] Error encriptando la contraseña: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifica la autenticación del usuario enviando una solicitud POST a la API.
     * <p>
     * Envía un JSON con el correo y la contraseña encriptada al endpoint de login, y analiza la respuesta
     * para determinar si el usuario es válido y si es administrador.
     * </p>
     * 
     * @param correo   El correo del usuario.
     * @param password La contraseña del usuario.
     * @return Un ArrayList de Boolean donde el primer elemento indica si el usuario es válido y el segundo
     *         indica si el usuario es administrador.
     */
    public ArrayList<Boolean> verificarUsuario(String correo, String password) {
        ArrayList<Boolean> arrayList = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:9526/api/auth/login");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            String contrasenyaEncript = encriptarContrasenya(password);
            String json = new JSONObject()
                    .put("correoUsuario", correo)
                    .put("password", contrasenyaEncript)
                    .toString();
            log.info("[SERVICIO] JSON enviado a la API: {}", json);

            try (OutputStream ot = conexion.getOutputStream()) {
                ot.write(json.getBytes());
                ot.flush();
            }

            int responseCode = conexion.getResponseCode();
            log.info("[SERVICIO] Código de respuesta de la API: {}", responseCode);

            BufferedReader in;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
            }
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonResponseStr = response.toString();
            log.info("[SERVICIO] Respuesta completa de la API: {}", jsonResponseStr);

            JSONObject jsonResponse = new JSONObject(jsonResponseStr);
            String mensaje = jsonResponse.optString("mensaje", "");
            int idUsuario = jsonResponse.optInt("idUsuario", -1);
            String rolUsuario = jsonResponse.optString("rolUsuario", "");
            
            if (mensaje.equals("Login exitoso") && idUsuario > 0) {
                arrayList.add(0, true);  // Usuario válido
                if (rolUsuario.equalsIgnoreCase("Admin")) {
                    log.info("[SERVICIO] Usuario autenticado como Administrador: {}", rolUsuario);
                    arrayList.add(1, true);  // Es administrador
                } else {
                    log.info("[SERVICIO] Usuario autenticado pero NO es Administrador: {}", rolUsuario);
                    arrayList.add(1, false); // Usuario normal
                }
            } else {
                log.warn("[SERVICIO] Datos de autenticación no válidos: mensaje={}, idUsuario={}", mensaje, idUsuario);
                arrayList.add(0, false);
                arrayList.add(1, false);
            }
        } catch (Exception e) {
            log.error("[SERVICIO] ERROR en verificarUsuario: {}", e.getMessage());
            arrayList.add(0, false);
            arrayList.add(1, false);
        }
        return arrayList;
    }

    /**
     * Autentica al usuario y retorna el objeto usuarioDTO.
     * <p>
     * Envía una solicitud POST a la API con el correo y la contraseña encriptada. Si la respuesta es exitosa,
     * crea y retorna un objeto usuarioDTO con la información del usuario. En caso de error, retorna null.
     * </p>
     * 
     * @param correo   El correo del usuario.
     * @param password La contraseña del usuario.
     * @return El objeto usuarioDTO autenticado, o null si la autenticación falla.
     */
    public usuarioDTO autenticarUsuario(String correo, String password) {
        try {
            URL url = new URL("http://localhost:9526/api/auth/login");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            String contrasenyaEncript = encriptarContrasenya(password);
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("correoUsuario", correo);
            jsonRequest.put("password", contrasenyaEncript);
            String jsonString = jsonRequest.toString();
            log.info("[SERVICIO] JSON enviado a la API: {}", jsonString);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(jsonString.getBytes());
                os.flush();
            }
            
            int responseCode = conexion.getResponseCode();
            log.info("[SERVICIO] Código de respuesta de la API: {}", responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
                String jsonResponseStr = sb.toString();
                log.info("[SERVICIO] Respuesta completa de la API: {}", jsonResponseStr);
                JSONObject jsonResponse = new JSONObject(jsonResponseStr);
                // Crear y llenar el objeto usuarioDTO
                usuarioDTO usuario = new usuarioDTO();
                usuario.setIdUsuario(jsonResponse.optInt("idUsuario", -1));
                usuario.setCorreoUsuario(jsonResponse.optString("correoUsuario", ""));
                usuario.setRolUsuario(jsonResponse.optString("rolUsuario", ""));
                // Supongamos que la API retorna la imagen en Base64 en la clave "imagenUsuario"
                String imagenBase64 = jsonResponse.optString("imagenUsuario", "");
                if (!imagenBase64.isEmpty()) {
                    byte[] imagenBytes = Base64.getDecoder().decode(imagenBase64);
                    usuario.setImagenUsuario(imagenBytes);
                }
                return usuario;
            } else {
                log.warn("[SERVICIO] Autenticación fallida, código de respuesta: {}", responseCode);
                return null;
            }
        } catch (Exception e) {
            log.error("[SERVICIO] ERROR en autenticarUsuario: {}", e.getMessage());
            return null;
        }
    }
}
