package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CambioContraseña {

    public String actualizarPassword(String correo, String codigo, String nuevaPassword, String confirmarPassword) throws IOException {
        // Construir el JSON con los datos requeridos por la API
        JSONObject json = new JSONObject();
        json.put("correo", correo);
        json.put("codigoVerificacion", codigo);
        json.put("nuevaPassword", nuevaPassword);
        json.put("confirmarPassword", confirmarPassword);
        
        // URL del endpoint de la API (ajusta puerto y context path según corresponda)
        URL url = new URL("http://localhost:9526/api/auth/actualizarPassword");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        
        // Enviar la petición JSON
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // Leer la respuesta
        int responseCode = con.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                (responseCode == HttpURLConnection.HTTP_OK ? con.getInputStream() : con.getErrorStream()), "utf-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }
        }
        return response.toString();
    }
}
