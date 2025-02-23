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

public class ModificarUsuarioServicio {
    // Para obtener un usuario, usaremos el endpoint de todos y filtraremos.
    private static final String API_URL_TODOS = "http://localhost:9526/api/usuarios/todos";
    private static final String API_URL_MODIFICAR = "http://localhost:9526/api/usuarios/modificar/";
    
    private Gson gson;
    
    public ModificarUsuarioServicio() {
        // Registrar el adaptador para byte[] (imagen)
        gson = new GsonBuilder()
                    .registerTypeAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                    .create();
    }
    
    // Método para obtener un usuario por su id filtrando la lista de todos
    public usuarioDTO obtenerUsuarioPorId(int idUsuario) {
        try {
            URL url = new URL(API_URL_TODOS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }
                in.close();
                // Convertir el JSON a un arreglo de usuarioDTO
                usuarioDTO[] usuarios = gson.fromJson(jsonResponse.toString(), usuarioDTO[].class);
                for (usuarioDTO u : usuarios) {
                    if (u.getIdUsuario() == idUsuario) {
                        return u;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Método para modificar el usuario mediante la API
    public usuarioDTO modificarUsuario(int idUsuario, usuarioDTO usuarioActualizado) {
        try {
            URL url = new URL(API_URL_MODIFICAR + idUsuario);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            String jsonInput = gson.toJson(usuarioActualizado);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonResponse.append(line);
                }
                in.close();
                usuarioDTO modificado = gson.fromJson(jsonResponse.toString(), usuarioDTO.class);
                return modificado;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
