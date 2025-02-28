package VistaPrueba2.Controladores;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import VistaPrueba2.Dtos.noticiaDTO;
import VistaPrueba2.Servicios.ServicioNoticiaVista;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servlet para mostrar noticias.
 * <p>
 * Este servlet maneja solicitudes GET para obtener todas las noticias.
 * Si la solicitud es AJAX (con el header "X-Requested-With"), retorna las noticias en formato JSON;
 * de lo contrario, realiza un forward a index.jsp.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/mostrarNoticias")
public class mostrarNoticias extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServicioNoticiaVista servicioNoticiaVista;
    
    /**
     * Inicializa el servlet e instancia el servicio de noticias.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        try {
            servicioNoticiaVista = new ServicioNoticiaVista();
            FicheroLogVista.logInfo("mostrarNoticias: ServicioNoticiaVista instanciado correctamente.");
        } catch (Exception e) {
            FicheroLogVista.logError("mostrarNoticias: Error al inicializar ServicioNoticiaVista.", e);
            throw new ServletException("Error en init de mostrarNoticias.", e);
        }
    }
    
    /**
     * Procesa las solicitudes GET para mostrar noticias.
     * <p>
     * Verifica si la solicitud es AJAX (por el header "X-Requested-With"). Si es así, convierte la lista de noticias
     * a formato JSON y la envía; de lo contrario, realiza un forward a index.jsp pasando la lista de noticias como atributo.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request  Objeto HttpServletRequest que contiene la solicitud.
     * @param response Objeto HttpServletResponse para enviar la respuesta.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            FicheroLogVista.logInfo("mostrarNoticias: Iniciando el doGet.");
            List<noticiaDTO> noticias = servicioNoticiaVista.obtenerNoticias();
            FicheroLogVista.logInfo("mostrarNoticias: Cantidad de noticias obtenidas: " 
                    + (noticias != null ? noticias.size() : "null"));
            
            String requestedWith = request.getHeader("X-Requested-With");
            if (requestedWith != null && requestedWith.equals("XMLHttpRequest")) {
                // Convertir la lista de noticias a JSON.
                JSONArray jsonArray = new JSONArray();
                for (noticiaDTO noticia : noticias) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("id_noticia", noticia.getId_noticia());
                    jsonObj.put("titulo", noticia.getTitulo());
                    jsonObj.put("contenido", noticia.getContenido());
                    jsonObj.put("fechaPublicacion", 
                        (noticia.getFechaPublicacion() != null) ? noticia.getFechaPublicacion().toString() : "");
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
                FicheroLogVista.logInfo("mostrarNoticias: JSON enviado correctamente.");
            } else {
                // No es AJAX: realizar forward a index.jsp.
                request.setAttribute("noticias", noticias);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);
                FicheroLogVista.logInfo("mostrarNoticias: Forward a index.jsp realizado correctamente.");
            }
        } catch(Exception e) {
            FicheroLogVista.logError("mostrarNoticias: Error al obtener o enviar las noticias.", e);
            // Si ocurre un error, devolver un error en JSON o realizar forward a index.jsp.
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
