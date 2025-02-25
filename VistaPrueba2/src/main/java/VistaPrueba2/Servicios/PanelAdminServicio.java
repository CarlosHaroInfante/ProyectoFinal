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

public class PanelAdminServicio {

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
                System.out.println("Usuarios obtenidos: " + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonUsuario = jsonArray.getJSONObject(i);
                    usuarioDTO usuario = new usuarioDTO();

                    usuario.setIdUsuario(jsonUsuario.getLong("idUsuario"));
                    usuario.setNombreCompleto(jsonUsuario.getString("nombreCompleto"));
                    usuario.setCorreoUsuario(jsonUsuario.getString("correoUsuario"));
                    usuario.setRolUsuario(jsonUsuario.getString("rolUsuario"));
                    
                    // Decodificar la imagen en Base64 a byte[]
                    String base64Imagen = jsonUsuario.getString("imagenUsuario");
                    byte[] imagenBytes = Base64.getDecoder().decode(base64Imagen);
                    usuario.setImagenUsuario(imagenBytes);

                    lista.add(usuario);
                }
            } else {
                System.out.println("Error al obtener usuarios. Código de respuesta: " + responseCode);
            }

            // Aplicar límite si es necesario
            if (limit > 0 && limit < lista.size()) {
                lista = new ArrayList<>(lista.subList(0, limit));
            }
        } catch (Exception e) {
            System.out.println("ERROR - PanelAdminServicio - obtenerUsuarios: " + e);
            e.printStackTrace();
        }
        System.out.println("Número total de usuarios: " + lista.size());
        return lista;
    }
}
