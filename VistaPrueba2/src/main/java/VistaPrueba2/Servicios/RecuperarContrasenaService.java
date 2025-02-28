package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servicio para enviar el correo de recuperación de contraseña.
 * <p>
 * Este servicio realiza una llamada a la API para enviar un correo de recuperación utilizando el correo del usuario.
 * </p>
 * 27/02/2025 - CHI
 */
public class RecuperarContrasenaService {

    /**
     * Envía el correo de recuperación de contraseña.
     * <p>
     * Construye un objeto JSON con el correo del usuario y realiza una solicitud HTTP POST al endpoint de la API encargado de enviar
     * el correo de recuperación.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param correoUsuario El correo del usuario al que se enviará el correo de recuperación.
     * @return {@code true} si la API confirma el envío del correo exitosamente; {@code false} en caso contrario.
     */
    public boolean enviarCorreoRecuperacion(String correoUsuario) {
        try {
            // Construir la URL del endpoint de la API para la recuperación de contraseña.
            String apiUrl = "http://localhost:9526/api/auth/controladorRecuperarContrasena";
            URL url = new URL(apiUrl);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conexion.setDoOutput(true);

            // Crear el JSON con el correo del usuario.
            JSONObject json = new JSONObject();
            json.put("correoUsuario", correoUsuario);
            FicheroLogVista.logInfo("RecuperarContrasenaService: JSON enviado: " + json.toString());

            // Enviar la solicitud con el JSON.
            try (OutputStream os = conexion.getOutputStream()) {
                os.write(json.toString().getBytes("UTF-8"));
            }

            int responseCode = conexion.getResponseCode();
            FicheroLogVista.logInfo("RecuperarContrasenaService: Código de respuesta: " + responseCode);

            // Si la respuesta es HTTP OK o CREATED, procesar la respuesta.
            if (responseCode == HttpURLConnection.HTTP_OK ||
                responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                FicheroLogVista.logInfo("RecuperarContrasenaService: Respuesta de la API: " + response.toString());
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.optBoolean("success", false);
            } else {
                FicheroLogVista.logInfo("RecuperarContrasenaService: Respuesta de la API no OK.");
                return false;
            }
        } catch (Exception e) {
            FicheroLogVista.logError("RecuperarContrasenaService: Error al enviar el correo de recuperación.", e);
            return false;
        }
    }
}
