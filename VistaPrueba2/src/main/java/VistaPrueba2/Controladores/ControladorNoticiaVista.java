package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import org.json.JSONObject;
import VistaPrueba2.Dtos.noticiaDTO;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Servicios.ServicioNoticiaVista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Controlador para la creación de noticias desde la vista.
 * <p>
 * Este servlet procesa solicitudes POST para crear noticias. Obtiene los parámetros del formulario,
 * construye un objeto noticiaDTO y lo envía al servicio de noticias. Si el usuario no está autenticado,
 * se devuelve un error de autorización.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/crearNoticia")
@MultipartConfig
public class ControladorNoticiaVista extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Servicio para gestionar las noticias desde la vista.
     */
    private ServicioNoticiaVista servicioNoticiaVista;

    /**
     * Inicializa el servlet e instancia manualmente el servicio de noticias.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        this.servicioNoticiaVista = new ServicioNoticiaVista();
        FicheroLogVista.logInfo("[INIT] ServicioNoticiaVista instanciado correctamente.");
    }

    /**
     * Procesa la solicitud POST para crear una noticia.
     * <p>
     * Obtiene los parámetros del formulario (título, contenido, fecha de publicación, imagen) y el ID del usuario
     * desde la sesión. Construye un objeto noticiaDTO, lo envía al servicio para crear la noticia y retorna la respuesta
     * en JSON.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request  Objeto HttpServletRequest que contiene la solicitud.
     * @param response Objeto HttpServletResponse para enviar la respuesta.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Establecer codificación de caracteres y tipo de contenido.
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        FicheroLogVista.logInfo("Procesando solicitud para crear noticia.");

        // Construir el objeto noticiaDTO a partir de los parámetros del formulario.
        noticiaDTO noticia = new noticiaDTO();
        noticia.setTitulo(request.getParameter("titulo"));
        noticia.setContenido(request.getParameter("contenido"));
        try {
            // Intentar establecer la fecha de publicación a partir del parámetro recibido.
            noticia.setFechaPublicacion(java.sql.Date.valueOf(request.getParameter("fechaPublicacion")));
        } catch(Exception e) {
            FicheroLogVista.logError("Error al parsear la fecha de publicación, se asigna la fecha actual.", e);
            noticia.setFechaPublicacion(new java.sql.Date(System.currentTimeMillis()));
        }
        String imagenBase64 = request.getParameter("imagenNoticia");
        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            try {
                byte[] imagenBytes = Base64.getDecoder().decode(imagenBase64);
                noticia.setImagenNoticia(imagenBytes);
            } catch (Exception e) {
                FicheroLogVista.logError("Error al decodificar la imagen de la noticia.", e);
            }
        }
        
        // Obtener el ID del usuario desde la sesión.
        Long idUsuario = (Long) request.getSession().getAttribute("idUsuario");
        if (idUsuario == null) {
            FicheroLogVista.logInfo("Usuario no autenticado. No se encontró idUsuario en la sesión.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Usuario no autenticado.");
            PrintWriter out = response.getWriter();
            out.write(errorJson.toString());
            out.flush();
            return;
        }
        // Crear un objeto usuarioDTO con el ID del autor y asignarlo a la noticia.
        usuarioDTO autor = new usuarioDTO();
        autor.setIdUsuario(idUsuario);
        noticia.setAutor(autor);
        
        try {
            // Crear la noticia usando el servicio.
            noticiaDTO noticiaCreada = servicioNoticiaVista.crearNoticia(noticia);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("id_noticia", noticiaCreada.getId_noticia());
            jsonResponse.put("titulo", noticiaCreada.getTitulo());
            jsonResponse.put("contenido", noticiaCreada.getContenido());
            if(noticiaCreada.getFechaPublicacion() != null) {
                jsonResponse.put("fechaPublicacion", noticiaCreada.getFechaPublicacion().toString());
            }
            if (noticiaCreada.getImagenNoticia() != null) {
                String imgBase64 = Base64.getEncoder().encodeToString(noticiaCreada.getImagenNoticia());
                jsonResponse.put("imagenNoticia", imgBase64);
            }
            FicheroLogVista.logInfo("Noticia creada exitosamente con ID: " + noticiaCreada.getId_noticia());
            PrintWriter out = response.getWriter();
            out.write(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            FicheroLogVista.logError("Error al crear noticia", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Error al crear noticia: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.write(errorJson.toString());
            out.flush();
        }
    }
}
