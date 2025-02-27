package VistaPrueba2.Controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import VistaPrueba2.Servicios.RegistroServicio;

@WebServlet("/recuperarContrasenia")
public class RecuperarContrasena extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RegistroServicio servicio;

    @Override
    public void init() throws ServletException {
        servicio = new RegistroServicio();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger parámetros del formulario
        String correo = request.getParameter("correoUsuario");
        String nombreCompleto = request.getParameter("nombreCompleto");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Validar que se han recibido datos
        if (correo == null || correo.trim().isEmpty() || nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JSONObject json = new JSONObject();
            json.put("error", "Faltan datos.");
            response.getWriter().write(json.toString());
            return;
        }
        
        // Llamar al método del servicio para enviar el correo de recuperación
        boolean exito = servicio.enviarCorreoRestablecerContrasena(nombreCompleto, correo);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        if (exito) {
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Correo de recuperación enviado.");
        } else {
            jsonResponse.put("error", "El correo no existe o hubo un error.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.getWriter().write(jsonResponse.toString());

        
    }
}
