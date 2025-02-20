/*package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.JSONObject;

public class InicioServicio {

    // Método para encriptar contraseñas
    public String encriptarContrasenya(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            System.out.println("[SERVICIO] Contraseña encriptada: " + hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("[SERVICIO] Error encriptando la contraseña: " + e);
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Boolean> verificarUsuario(String correo, String password) {
        ArrayList<Boolean> arrayList = new ArrayList<>();
        try {
            // URL de la API de autenticación
            URL url = new URL("http://localhost:9526/api/auth/login");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            String contrasenyaEncript = encriptarContrasenya(password);

            // Crear el JSON con los datos necesarios
            String json = new JSONObject()
                    .put("correoUsuario", correo)
                    .put("password", contrasenyaEncript)
                    .toString();
            System.out.println("[SERVICIO] JSON enviado a la API: " + json);

            // Enviar la solicitud
            try (OutputStream ot = conexion.getOutputStream()) {
                ot.write(json.getBytes());
                ot.flush();
            }

            int responseCode = conexion.getResponseCode();
            System.out.println("[SERVICIO] Código de respuesta de la API: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("[SERVICIO] Respuesta de la API: " + response.toString());

                // Deserializar la respuesta JSON
                JSONObject jsonResponse = new JSONObject(response.toString());
                String mensaje = jsonResponse.getString("mensaje");
                int idUsuario = jsonResponse.getInt("idUsuario");
                String rolUsuario = jsonResponse.getString("rolUsuario");

                if (mensaje != null && mensaje.equals("Login exitoso") && idUsuario > 0) {
                    arrayList.add(0, true);  // Usuario válido
                    if (rolUsuario != null && rolUsuario.equalsIgnoreCase("Administrador")) {
                        System.out.println("[SERVICIO] Usuario autenticado como Administrador: " + rolUsuario);
                        arrayList.add(1, true);  // Es administrador
                    } else {
                        System.out.println("[SERVICIO] Usuario autenticado pero NO es Administrador: " + rolUsuario);
                        arrayList.add(1, false); // Usuario normal u otro rol
                    }
                } else {
                    System.out.println("[SERVICIO] Datos de autenticación no válidos: mensaje=" + mensaje + ", idUsuario=" + idUsuario);
                    arrayList.add(0, false);
                    arrayList.add(1, false);
                }
            } else {
                System.out.println("[SERVICIO] Error: Código de respuesta no OK. Código: " + responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getErrorStream()));
                StringBuilder errorResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                in.close();
                System.out.println("[SERVICIO] Respuesta de error del servidor: " + errorResponse.toString());
                arrayList.add(0, false);
                arrayList.add(1, false);
            }
        } catch (Exception e) {
            System.out.println("[SERVICIO] ERROR en verificarUsuario: " + e);
            arrayList.add(0, false);
            arrayList.add(1, false);
        }
        return arrayList;
    }
}*/

package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InicioServicio {
    
    private static final Logger log = LoggerFactory.getLogger(InicioServicio.class);

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

    public ArrayList<Boolean> verificarUsuario(String correo, String password) {
        ArrayList<Boolean> arrayList = new ArrayList<>();
        try {
            // Crear la conexión a la API
            URL url = new URL("http://localhost:9526/api/auth/login");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            // Encriptar la contraseña y construir el JSON
            String contrasenyaEncript = encriptarContrasenya(password);
            String json = new JSONObject()
                    .put("correoUsuario", correo)
                    .put("password", contrasenyaEncript)
                    .toString();
            log.info("[SERVICIO] JSON enviado a la API: {}", json);

            // Enviar la solicitud
            try (OutputStream ot = conexion.getOutputStream()) {
                ot.write(json.getBytes());
                ot.flush();
            }

            // Leer el código de respuesta
            int responseCode = conexion.getResponseCode();
            log.info("[SERVICIO] Código de respuesta de la API: {}", responseCode);

            // Leer la respuesta (ya sea OK o error)
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

            // Imprimir el JSON recibido completo
            String jsonResponseStr = response.toString();
            log.info("[SERVICIO] Respuesta completa de la API: {}", jsonResponseStr);
            System.out.println("[SERVICIO] Respuesta completa de la API: " + jsonResponseStr);

            // Intentar parsear el JSON recibido
            JSONObject jsonResponse = new JSONObject(jsonResponseStr);
            // Imprimir los campos individuales para ver qué se obtiene
            if(jsonResponse.has("mensaje")){
                log.info("[SERVICIO] Campo 'mensaje': {}", jsonResponse.getString("mensaje"));
            }
            if(jsonResponse.has("idUsuario")){
                log.info("[SERVICIO] Campo 'idUsuario': {}", jsonResponse.getInt("idUsuario"));
            }
            if(jsonResponse.has("rolUsuario")){
                log.info("[SERVICIO] Campo 'rolUsuario': {}", jsonResponse.getString("rolUsuario"));
            }

            // Validar la respuesta según lo esperado
            String mensaje = jsonResponse.optString("mensaje", "");
            int idUsuario = jsonResponse.optInt("idUsuario", -1);
            String rolUsuario = jsonResponse.optString("rolUsuario", "");
            
            if (mensaje.equals("Login exitoso") && idUsuario > 0) {
                arrayList.add(0, true);  // Usuario válido
                if (rolUsuario.equalsIgnoreCase("Administrador")) {
                    log.info("[SERVICIO] Usuario autenticado como Administrador: {}", rolUsuario);
                    arrayList.add(1, true);  // Es administrador
                } else {
                    log.info("[SERVICIO] Usuario autenticado pero NO es Administrador: {}", rolUsuario);
                    arrayList.add(1, false); // Usuario normal u otro rol
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
}
