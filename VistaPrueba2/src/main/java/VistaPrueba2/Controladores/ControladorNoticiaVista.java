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

@WebServlet("/crearNoticia")
@MultipartConfig
public class ControladorNoticiaVista extends HttpServlet {
    private static final long serialVersionUID = 1L;
    // Elimina la anotación @Autowired
    private ServicioNoticiaVista servicioNoticiaVista;

    @Override
    public void init() throws ServletException {
        // Instancia manualmente el servicio
        this.servicioNoticiaVista = new ServicioNoticiaVista();
        System.out.println("[INIT] ServicioNoticiaVista instanciado correctamente.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        // Construir el objeto noticiaDTO a partir de los parámetros del formulario
        noticiaDTO noticia = new noticiaDTO();
        noticia.setTitulo(request.getParameter("titulo"));
        noticia.setContenido(request.getParameter("contenido"));
        try {
            noticia.setFechaPublicacion(java.sql.Date.valueOf(request.getParameter("fechaPublicacion")));
        } catch(Exception e) {
            noticia.setFechaPublicacion(new java.sql.Date(System.currentTimeMillis()));
        }
        String imagenBase64 = request.getParameter("imagenNoticia");
        if (imagenBase64 != null && !imagenBase64.isEmpty()) {
            byte[] imagenBytes = java.util.Base64.getDecoder().decode(imagenBase64);
            noticia.setImagenNoticia(imagenBytes);
        }
        
        // Obtener el ID del usuario desde la sesión
        Long idUsuario = (Long) request.getSession().getAttribute("idUsuario");
        if (idUsuario == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Usuario no autenticado.");
            PrintWriter out = response.getWriter();
            out.write(errorJson.toString());
            out.flush();
            return;
        }
        // Crear un objeto usuarioDTO con el id del autor
        usuarioDTO autor = new usuarioDTO();
        autor.setIdUsuario(idUsuario);
        noticia.setAutor(autor);
        
        try {
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
            PrintWriter out = response.getWriter();
            out.write(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Error al crear noticia: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.write(errorJson.toString());
            out.flush();
        }
    }
}
