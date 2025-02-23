package VistaPrueba2.Servicios;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

@Service
public class EliminarUsuarioServicio {
    // Esta URL debe coincidir con la configuración de tu API (mapeo @DeleteMapping en la API)
    private static final String API_URL = "http://localhost:9526/api/usuarios/baja/";

    public boolean eliminarUsuario(int idUsuario) {
        try {
            URL url = new URL(API_URL + idUsuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
