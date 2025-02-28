package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servicio para obtener usuarios para el panel de administración.
 * <p>
 * Este servicio se encarga de enviar una solicitud GET al endpoint de la API para obtener todos los usuarios en formato JSON,
 * deserializar la respuesta y construir una lista de objetos {@code usuarioDTO}. Además, se puede limitar el número de usuarios retornados.
 * </p>
 * 27/02/2025 - CHI
 */
public class PanelAdminServicio {

    /**
     * Obtiene la lista de usuarios desde la API.
     * <p>
     * Realiza una solicitud HTTP GET al endpoint que retorna todos los usuarios, deserializa la respuesta JSON y construye
     * una lista de objetos {@code usuarioDTO}. Si se especifica un límite mayor que 0, se retorna únicamente esa cantidad de usuarios.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param limit El número máximo de usuarios a retornar. Si es 0 o mayor que el tamaño total, se retornan todos los usuarios.
     * @return Un {@code ArrayList} de {@code usuarioDTO} con los usuarios obtenidos de la API.
     */
    public ArrayList<usuarioDTO> obtenerUsuarios(int limit) {
        ArrayList<usuarioDTO> lista = new ArrayList<>();
        try {
            // URL del endpoint de la API
            String urlApi = "http://localhost:9526/api/usuarios/todos";
            URL url = new URL(urlApi);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");

            int responseCode = conexion.getResponseCode();
            FicheroLogVista.logInfo("PanelAdminServicio: Código de respuesta al obtener usuarios: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder respuesta = new StringBuilder();
                String linea;
                while ((linea = in.readLine()) != null) {
                    respuesta.append(linea);
                }
                in.close();

                // Parsear el JSON recibido
                JSONArray jsonArray = new JSONArray(respuesta.toString());
                FicheroLogVista.logInfo("PanelAdminServicio: Usuarios obtenidos: " + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonUsuario = jsonArray.getJSONObject(i);
                    usuarioDTO usuario = new usuarioDTO();

                    usuario.setIdUsuario(jsonUsuario.getLong("idUsuario"));
                    usuario.setNombreCompleto(jsonUsuario.getString("nombreCompleto"));
                    usuario.setCorreoUsuario(jsonUsuario.getString("correoUsuario"));
                    usuario.setRolUsuario(jsonUsuario.getString("rolUsuario"));
                    
                    // Decodificar la imagen en Base64 a byte[]
                    String base64Imagen = jsonUsuario.getString("imagenUsuario");
                    if (base64Imagen != null && !base64Imagen.isEmpty()) {
                        byte[] imagenBytes = Base64.getDecoder().decode(base64Imagen);
                        usuario.setImagenUsuario(imagenBytes);
                    }
                    
                    lista.add(usuario);
                }
            } else {
                FicheroLogVista.logError("PanelAdminServicio: Error al obtener usuarios. Código de respuesta: " + responseCode, null);
            }

            // Aplicar límite si es necesario
            if (limit > 0 && limit < lista.size()) {
                lista = new ArrayList<>(lista.subList(0, limit));
            }
        } catch (Exception e) {
            FicheroLogVista.logError("PanelAdminServicio: ERROR en obtenerUsuarios", e);
        }
        FicheroLogVista.logInfo("PanelAdminServicio: Número total de usuarios: " + lista.size());
        return lista;
    }
}
