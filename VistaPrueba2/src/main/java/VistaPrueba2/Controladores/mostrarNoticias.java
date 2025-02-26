package VistaPrueba2.Controladores;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import VistaPrueba2.Dtos.noticiaDTO;
import VistaPrueba2.Servicios.ServicioNoticiaVista;

@WebServlet("/mostrarNoticias")
public class mostrarNoticias extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServicioNoticiaVista servicioNoticiaVista;
    
    @Override
    public void init() throws ServletException {
        servicioNoticiaVista = new ServicioNoticiaVista();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("mostrarNoticias: Iniciando el doGet");
        try {
            List<noticiaDTO> noticias = servicioNoticiaVista.obtenerNoticias();
            System.out.println("Cantidad de noticias obtenidas: " + (noticias != null ? noticias.size() : "null"));
            
            // Verifica si es una petición AJAX (se enviará este header desde el fetch)
            String requestedWith = request.getHeader("X-Requested-With");
            if (requestedWith != null && requestedWith.equals("XMLHttpRequest")) {
                // Convertir la lista de noticias a JSON
                JSONArray jsonArray = new JSONArray();
                for (noticiaDTO noticia : noticias) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id_noticia", noticia.getId_noticia());
                    jsonObj.put("titulo", noticia.getTitulo());
                    jsonObj.put("contenido", noticia.getContenido());
                    jsonObj.put("fechaPublicacion", 
                        noticia.getFechaPublicacion() != null ? noticia.getFechaPublicacion().toString() : "");
                    if (noticia.getImagenNoticia() != null && noticia.getImagenNoticia().length > 0) {
                        String imgBase64 = Base64.getEncoder().encodeToString(noticia.getImagenNoticia());
                        jsonObj.put("imagenNoticia", imgBase64);
                    } else {
                        jsonObj.put("imagenNoticia", "");
                    }
                    jsonArray.put(jsonObj);
                }
                response.setContentType("application/json");
                response.getWriter().write(jsonArray.toString());
            } else {
                // No es AJAX: se realiza el forward a index.jsp
                request.setAttribute("noticias", noticias);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch(Exception e) {
            e.printStackTrace();
            // Si ocurre un error, dependiendo del tipo de petición devolvemos error en JSON o forward
            if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                response.setStatus(500);
                response.getWriter().write("{\"error\":\"No se pudieron cargar las noticias.\"}");
            } else {
                request.setAttribute("error", "No se pudieron cargar las noticias.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        }
    }
}
