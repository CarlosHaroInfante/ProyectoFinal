package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class RecuperarContrasenaService {

    // Este método hace una llamada a la API para enviar el correo de recuperación.
    public boolean enviarCorreoRecuperacion(String correo) {
        try {
            // Construir la URL de la API de recuperación (ajusta puerto y context path)
            String apiUrl = "http://localhost:9526/api/auth/recuperarContrasena";
            URL url = new URL(apiUrl);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conexion.setDoOutput(true);

            // Crear el JSON con el correo
            JSONObject json = new JSONObject();
            json.put("correo", correo);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(json.toString().getBytes("UTF-8"));
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK ||
                responseCode == HttpURLConnection.HTTP_CREATED) {
                // Leer la respuesta (si es necesario)
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.optBoolean("success", false);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
