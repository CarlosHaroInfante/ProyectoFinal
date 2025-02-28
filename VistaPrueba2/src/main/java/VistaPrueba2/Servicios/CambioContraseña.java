package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servicio para actualizar la contraseña del usuario.
 * <p>
 * Este servicio envía una solicitud HTTP POST a un endpoint de la API para actualizar la contraseña
 * del usuario. Construye un objeto JSON con los datos necesarios y devuelve la respuesta del endpoint.
 * </p>
 * 27/02/2025 - CHI
 */
public class CambioContraseña {

    /**
     * Actualiza la contraseña del usuario enviando una solicitud HTTP POST al endpoint de la API.
     * <p>
     * Se construye un objeto JSON con el correo, el código de verificación, la nueva contraseña y su confirmación,
     * se envía la solicitud y se retorna la respuesta del servidor en formato String.
     * En caso de error, se registra el fallo y se retorna un mensaje de error en formato JSON.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param correo           El correo del usuario.
     * @param codigo           El código de verificación enviado por correo.
     * @param nuevaPassword    La nueva contraseña.
     * @param confirmarPassword La confirmación de la nueva contraseña.
     * @return La respuesta del endpoint en formato String, o un mensaje de error en formato JSON.
     */
    public String actualizarPassword(String correo, String codigo, String nuevaPassword, String confirmarPassword) {
        try {
            // Construir el JSON con los datos requeridos por la API.
            JSONObject json = new JSONObject();
            json.put("correo", correo);
            json.put("codigoVerificacion", codigo);
            json.put("nuevaPassword", nuevaPassword);
            json.put("confirmarPassword", confirmarPassword);
            
            // URL del endpoint de la API (ajusta el puerto y context path según corresponda).
            URL url = new URL("http://localhost:9526/api/auth/actualizarPassword");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            
            // Enviar la petición JSON.
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            // Leer la respuesta.
            int responseCode = con.getResponseCode();
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    (responseCode == HttpURLConnection.HTTP_OK ? con.getInputStream() : con.getErrorStream()), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }
            
            FicheroLogVista.logInfo("CambioContraseña: Respuesta obtenida: " + response.toString());
            return response.toString();
        } catch (IOException e) {
            FicheroLogVista.logError("CambioContraseña: Error al actualizar la contraseña.", e);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Error al actualizar la contraseña: " + e.getMessage());
            return errorJson.toString();
        }
    }
}
