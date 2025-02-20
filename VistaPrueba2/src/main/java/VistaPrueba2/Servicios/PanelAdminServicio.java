package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import VistaPrueba2.Dtos.usuarioDTO;

public class PanelAdminServicio {
    
    // Si 'limit' es 0 (o mayor que el total) se devuelve toda la lista; de lo contrario se limita
    public List<usuarioDTO> obtenerUsuarios(int limit) {
        try {
            // URL del endpoint de la API (en el proyecto API) que devuelve todos los usuarios
            String apiUrl = "http://localhost:9526/api/auth/todos";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            String json = sb.toString();
            
            Gson gson = new Gson();
            // Convertir el JSON a arreglo de usuarioDTO
            usuarioDTO[] usuariosArray = gson.fromJson(json, usuarioDTO[].class);
            List<usuarioDTO> usuarios = Arrays.asList(usuariosArray);
            
            // Ordenar la lista en forma descendente (suponiendo que el id mayor es el último registrado)
            Collections.sort(usuarios, (u1, u2) -> Long.compare(u2.getIdUsuario(), u1.getIdUsuario()));
            
            if (limit > 0 && limit < usuarios.size()) {
                usuarios = usuarios.subList(0, limit);
            }
            return usuarios;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
