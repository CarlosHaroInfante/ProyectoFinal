
package VistaPrueba2.Controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.json.JSONObject;
import VistaPrueba2.Servicios.RegistroServicio;

@WebServlet("/registro")
@MultipartConfig
public class RegistroUsuario extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RegistroServicio servicio;

    @Override
    public void init() throws ServletException {
        this.servicio = new RegistroServicio();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger parámetros del formulario
        String nombreCompleto = request.getParameter("nombreCompleto");
        String numeroUsuario = request.getParameter("numeroUsuario");
        String correoUsuario = request.getParameter("correoUsuario");
        String rolUsuario = request.getParameter("rolUsuario");
        if (rolUsuario == null || rolUsuario.isEmpty()) {
            rolUsuario = "Usuario";
        }
        Part filePart = request.getPart("imagenUsuario");
        byte[] imagenUsuario = null;
        if (filePart != null && filePart.getSize() > 0) {
            imagenUsuario = filePart.getInputStream().readAllBytes();
        }
        
        // Llamar al servicio para registrar el usuario sin contraseña
        boolean registrado = servicio.registrarUsuarioSinPassword(nombreCompleto, numeroUsuario, correoUsuario, rolUsuario, imagenUsuario);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        if (registrado) {
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Usuario registrado. Se ha enviado un correo de verificación.");
        } else {
            jsonResponse.put("error", "El correo ya existe o ocurrió un error en el registro.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.getWriter().write(jsonResponse.toString());
    }
}
