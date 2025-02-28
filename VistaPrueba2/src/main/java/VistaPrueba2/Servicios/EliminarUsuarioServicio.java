package VistaPrueba2.Servicios;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import VistaPrueba2.Utils.FicheroLogVista;
import org.springframework.stereotype.Service;

/**
 * Servicio para eliminar un usuario.
 * <p>
 * Este servicio envía una solicitud DELETE a la API para eliminar el usuario con el ID proporcionado.
 * Retorna {@code true} si la eliminación fue exitosa (código HTTP 200), o {@code false} en caso de error.
 * </p>
 * 27/02/2025 - CHI
 */
@Service
public class EliminarUsuarioServicio {
    // URL base para el endpoint de eliminación de usuarios (debe coincidir con el mapeo @DeleteMapping en la API).
    private static final String API_URL = "http://localhost:9526/api/usuarios/baja/";

    /**
     * Elimina un usuario llamando al endpoint de la API.
     * <p>
     * Envía una solicitud HTTP DELETE a la URL construida concatenando el ID del usuario.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param idUsuario El ID del usuario a eliminar.
     * @return {@code true} si la API retorna un código HTTP 200 (OK); {@code false} en caso contrario.
     */
    public boolean eliminarUsuario(int idUsuario) {
        try {
            URL url = new URL(API_URL + idUsuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");

            int responseCode = conn.getResponseCode();
            FicheroLogVista.logInfo("EliminarUsuarioServicio: Código de respuesta para la eliminación del usuario con ID " 
                    + idUsuario + ": " + responseCode);
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            FicheroLogVista.logError("EliminarUsuarioServicio: Error al eliminar el usuario con ID: " + idUsuario, e);
            return false;
        }
    }
}
