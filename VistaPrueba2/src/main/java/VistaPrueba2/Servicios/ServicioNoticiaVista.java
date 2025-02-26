package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import VistaPrueba2.Dtos.noticiaDTO;

public class ServicioNoticiaVista {
    private static final Logger log = LoggerFactory.getLogger(ServicioNoticiaVista.class);

    public noticiaDTO crearNoticia(noticiaDTO noticia) throws Exception {
        String apiUrl = "http://localhost:9526/api/noticias/crear";
        log.info("Conectando a: " + apiUrl);
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);

        // Construir el JSON a partir del DTO
        JSONObject jsonNoticia = new JSONObject();
        jsonNoticia.put("titulo", noticia.getTitulo()); // Se espera que sea String
        jsonNoticia.put("contenido", noticia.getContenido());
        if (noticia.getFechaPublicacion() != null) {
            jsonNoticia.put("fechaPublicacion", noticia.getFechaPublicacion().toString());
        }
        if (noticia.getImagenNoticia() != null) {
            String imagenBase64 = Base64.getEncoder().encodeToString(noticia.getImagenNoticia());
            jsonNoticia.put("imagenNoticia", imagenBase64);
        }
        // Enviar también el autor (sólo su id)
        if (noticia.getAutor() != null) {
            JSONObject jsonAutor = new JSONObject();
            jsonAutor.put("idUsuario", noticia.getAutor().getIdUsuario());
            jsonNoticia.put("autor", jsonAutor);
        }

        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonNoticia.toString().getBytes("UTF-8"));
            os.flush();
        }

        int responseCode = con.getResponseCode();
        BufferedReader in;
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        } else {
            if (con.getErrorStream() != null) {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            }
        }
        StringBuilder responseStr = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            responseStr.append(line);
        }
        in.close();

        JSONObject jsonResponse = new JSONObject(responseStr.toString());
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            noticiaDTO noticiaCreada = new noticiaDTO();
            noticiaCreada.setId_noticia(jsonResponse.getLong("id_noticia"));
            // Usar optString para evitar error si el valor es nulo
            noticiaCreada.setTitulo(jsonResponse.optString("titulo", ""));
            noticiaCreada.setContenido(jsonResponse.optString("contenido", ""));
            if (jsonResponse.has("fechaPublicacion") && !jsonResponse.optString("fechaPublicacion", "").isEmpty()) {
                noticiaCreada.setFechaPublicacion(java.sql.Date.valueOf(jsonResponse.getString("fechaPublicacion")));
            }
            if (jsonResponse.has("imagenNoticia")) {
                String imgBase64 = jsonResponse.optString("imagenNoticia", "");
                if (!imgBase64.isEmpty()) {
                    noticiaCreada.setImagenNoticia(Base64.getDecoder().decode(imgBase64));
                }
            }
            return noticiaCreada;
        } else {
            throw new Exception(jsonResponse.toString());
        }
    }
    
    public List<noticiaDTO> obtenerNoticias() throws Exception {
        String apiUrl = "http://localhost:9526/api/noticias/todos";
        log.info("Conectando a: " + apiUrl);
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        
        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new Exception("Error al obtener noticias. Código HTTP: " + responseCode);
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        StringBuilder responseStr = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            responseStr.append(line);
        }
        in.close();
        System.out.println("Respuesta de la API: " + responseStr.toString());

        
        JSONArray jsonArray = new JSONArray(responseStr.toString());
        List<noticiaDTO> noticias = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonNoticia = jsonArray.getJSONObject(i);
            noticiaDTO noticia = new noticiaDTO();
            noticia.setId_noticia(jsonNoticia.getLong("id_noticia"));
            // Usamos getString asumiendo que "titulo" y "contenido" son cadenas
            noticia.setTitulo(jsonNoticia.getString("titulo"));
            noticia.setContenido(jsonNoticia.getString("contenido"));
            if (jsonNoticia.has("fechaPublicacion") && !jsonNoticia.getString("fechaPublicacion").isEmpty()) {
                noticia.setFechaPublicacion(java.sql.Date.valueOf(jsonNoticia.getString("fechaPublicacion")));
            }
            if (jsonNoticia.has("imagenNoticia") && !jsonNoticia.getString("imagenNoticia").isEmpty()) {
                String imgBase64 = jsonNoticia.getString("imagenNoticia");
                noticia.setImagenNoticia(Base64.getDecoder().decode(imgBase64));
            }
            // Puedes agregar el campo autor si lo necesitas
            noticias.add(noticia);
        }
        return noticias;
    }

}
