package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import VistaPrueba2.Dtos.noticiaDTO;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servicio para gestionar las operaciones relacionadas con las noticias desde la vista.
 * <p>
 * Provee métodos para crear y obtener noticias mediante llamadas a la API.
 * </p>
 * 27/02/2025 - CHI
 */
public class ServicioNoticiaVista {
    
    private static final Logger log = LoggerFactory.getLogger(ServicioNoticiaVista.class);

    /**
     * Crea una noticia enviando una solicitud POST a la API.
     * <p>
     * Construye un objeto JSON a partir del objeto {@code noticiaDTO} y lo envía al endpoint de creación de noticias.
     * Si la operación es exitosa, se retorna el objeto {@code noticiaDTO} creado; de lo contrario, se lanza una excepción.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param noticia El objeto {@code noticiaDTO} que contiene los datos de la noticia a crear.
     * @return El objeto {@code noticiaDTO} creado con el ID asignado por la API.
     * @throws Exception Si la API retorna un error o ocurre algún problema durante la comunicación.
     */
    public noticiaDTO crearNoticia(noticiaDTO noticia) throws Exception {
        String apiUrl = "http://localhost:9526/api/noticias/crear";
        FicheroLogVista.logInfo("ServicioNoticiaVista: Conectando a: " + apiUrl);
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);

            // Construir el JSON a partir del DTO
            JSONObject jsonNoticia = new JSONObject();
            jsonNoticia.put("titulo", noticia.getTitulo());
            jsonNoticia.put("contenido", noticia.getContenido());
            if (noticia.getFechaPublicacion() != null) {
                jsonNoticia.put("fechaPublicacion", noticia.getFechaPublicacion().toString());
            }
            if (noticia.getImagenNoticia() != null) {
                String imagenBase64 = Base64.getEncoder().encodeToString(noticia.getImagenNoticia());
                jsonNoticia.put("imagenNoticia", imagenBase64);
            }
            // Incluir el autor (sólo su id)
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
            FicheroLogVista.logInfo("ServicioNoticiaVista: Código de respuesta POST: " + responseCode);
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
                FicheroLogVista.logInfo("ServicioNoticiaVista: Noticia creada exitosamente: " + noticiaCreada);
                return noticiaCreada;
            } else {
                FicheroLogVista.logError("ServicioNoticiaVista: Error en la respuesta de la API: " + jsonResponse.toString(), null);
                throw new Exception(jsonResponse.toString());
            }
        } catch (Exception e) {
            FicheroLogVista.logError("ServicioNoticiaVista: Excepción en crearNoticia.", e);
            throw e;
        }
    }

    /**
     * Obtiene todas las noticias enviando una solicitud GET a la API.
     * <p>
     * Realiza una solicitud HTTP GET al endpoint de la API para obtener todas las noticias y deserializa la respuesta
     * a una lista de objetos {@code noticiaDTO}.
     * </p>
     * 27/02/2025 - CHI
     *
     * @return Una lista de {@code noticiaDTO} con todas las noticias.
     * @throws Exception Si ocurre un error durante la obtención de las noticias.
     */
    public List<noticiaDTO> obtenerNoticias() throws Exception {
        String apiUrl = "http://localhost:9526/api/noticias/todos";
        FicheroLogVista.logInfo("ServicioNoticiaVista: Conectando a: " + apiUrl);
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            
            int responseCode = con.getResponseCode();
            FicheroLogVista.logInfo("ServicioNoticiaVista: Código de respuesta GET: " + responseCode);
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
            FicheroLogVista.logInfo("ServicioNoticiaVista: Muestra las noticias");
            
            JSONArray jsonArray = new JSONArray(responseStr.toString());
            List<noticiaDTO> noticias = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonNoticia = jsonArray.getJSONObject(i);
                noticiaDTO noticia = new noticiaDTO();
                noticia.setId_noticia(jsonNoticia.getLong("id_noticia"));
                noticia.setTitulo(jsonNoticia.getString("titulo"));
                noticia.setContenido(jsonNoticia.getString("contenido"));
                if (jsonNoticia.has("fechaPublicacion") && !jsonNoticia.getString("fechaPublicacion").isEmpty()) {
                    noticia.setFechaPublicacion(java.sql.Date.valueOf(jsonNoticia.getString("fechaPublicacion")));
                }
                if (jsonNoticia.has("imagenNoticia") && !jsonNoticia.getString("imagenNoticia").isEmpty()) {
                    String imgBase64 = jsonNoticia.getString("imagenNoticia");
                    noticia.setImagenNoticia(Base64.getDecoder().decode(imgBase64));
                }
                // Se puede agregar la lógica para el campo autor si es necesario
                noticias.add(noticia);
            }
            FicheroLogVista.logInfo("ServicioNoticiaVista: Número total de noticias obtenidas: " + noticias.size());
            return noticias;
        } catch (Exception e) {
            FicheroLogVista.logError("ServicioNoticiaVista: Excepción en obtenerNoticias.", e);
            throw e;
        }
    }
}
