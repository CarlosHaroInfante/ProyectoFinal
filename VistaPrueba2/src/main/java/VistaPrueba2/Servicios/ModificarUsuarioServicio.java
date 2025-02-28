package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Utils.ByteArrayToBase64TypeAdapter;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servicio para modificar usuarios.
 * <p>
 * Este servicio se encarga de obtener un usuario filtrando la lista de todos los usuarios y de enviar
 * una solicitud para modificar un usuario mediante la API.
 * </p>
 * 27/02/2025 - CHI
 */
public class ModificarUsuarioServicio {

    /**
     * URL base para obtener todos los usuarios a través de la API.
     */
    private static final String API_URL_TODOS = "http://localhost:9526/api/usuarios/todos";

    /**
     * URL base para modificar un usuario mediante la API.
     */
    private static final String API_URL_MODIFICAR = "http://localhost:9526/api/usuarios/modificar/";

    /**
     * Instancia de Gson configurada con un adaptador para convertir arrays de bytes a Base64.
     */
    private Gson gson;

    /**
     * Constructor que inicializa el objeto Gson con el adaptador para byte[].
     * 27/02/2025 - CHI
     */
    public ModificarUsuarioServicio() {
        try {
            gson = new GsonBuilder()
                        .registerTypeAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                        .create();
            FicheroLogVista.logInfo("ModificarUsuarioServicio: Gson inicializado correctamente.");
        } catch (Exception e) {
            FicheroLogVista.logError("ModificarUsuarioServicio: Error al inicializar Gson.", e);
            throw new RuntimeException("Error al inicializar ModificarUsuarioServicio.", e);
        }
    }

    /**
     * Obtiene un usuario por su ID filtrando la lista de todos los usuarios obtenidos de la API.
     * <p>
     * Realiza una solicitud GET al endpoint que retorna todos los usuarios, los deserializa y
     * busca el usuario cuyo ID coincida con el proporcionado.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param idUsuario El ID del usuario a buscar.
     * @return El objeto usuarioDTO correspondiente al ID, o null si no se encuentra o ocurre un error.
     */
    public usuarioDTO obtenerUsuarioPorId(int idUsuario) {
        try {
            URL url = new URL(API_URL_TODOS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            FicheroLogVista.logInfo("ModificarUsuarioServicio: Código de respuesta GET usuarios: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }
                in.close();
                // Deserializar el JSON a un arreglo de usuarioDTO.
                usuarioDTO[] usuarios = gson.fromJson(jsonResponse.toString(), usuarioDTO[].class);
                for (usuarioDTO u : usuarios) {
                    if (u.getIdUsuario() == idUsuario) {
                        FicheroLogVista.logInfo("ModificarUsuarioServicio: Usuario encontrado con ID: " + idUsuario);
                        return u;
                    }
                }
                FicheroLogVista.logInfo("ModificarUsuarioServicio: Usuario con ID " + idUsuario + " no encontrado.");
            } else {
                FicheroLogVista.logError("ModificarUsuarioServicio: Error al obtener usuarios, código de respuesta: " + responseCode, null);
            }
        } catch (IOException e) {
            FicheroLogVista.logError("ModificarUsuarioServicio: Error en obtenerUsuarioPorId para ID: " + idUsuario, e);
        }
        return null;
    }

    /**
     * Modifica un usuario enviando una solicitud PUT al endpoint de la API.
     * <p>
     * Serializa el objeto usuarioDTO con los datos actualizados a JSON y lo envía al endpoint para modificar
     * el usuario identificado por el ID. Retorna el usuario modificado deserializado o null si ocurre un error.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param idUsuario         El ID del usuario a modificar.
     * @param usuarioActualizado El objeto usuarioDTO con los datos actualizados.
     * @return El objeto usuarioDTO modificado, o null si ocurre un error.
     */
    public usuarioDTO modificarUsuario(int idUsuario, usuarioDTO usuarioActualizado) {
        try {
            URL url = new URL(API_URL_MODIFICAR + idUsuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = gson.toJson(usuarioActualizado);
            FicheroLogVista.logInfo("ModificarUsuarioServicio: JSON enviado para modificar usuario: " + jsonInput);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes("UTF-8"));
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            FicheroLogVista.logInfo("ModificarUsuarioServicio: Código de respuesta PUT: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }
                in.close();
                usuarioDTO modificado = gson.fromJson(jsonResponse.toString(), usuarioDTO.class);
                FicheroLogVista.logInfo("ModificarUsuarioServicio: Usuario modificado exitosamente: " + modificado);
                return modificado;
            } else {
                FicheroLogVista.logError("ModificarUsuarioServicio: Error al modificar usuario, código de respuesta: " + responseCode, null);
            }
        } catch (IOException e) {
            FicheroLogVista.logError("ModificarUsuarioServicio: Error en modificarUsuario para ID: " + idUsuario, e);
        }
        return null;
    }
}
