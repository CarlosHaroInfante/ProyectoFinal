package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Servicios.ModificarUsuarioServicio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/ModificarUsuario")
@MultipartConfig
public class ModificarUsuarioControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ModificarUsuarioServicio modificarUsuarioServicio;

    @Override
    public void init() throws ServletException {
        modificarUsuarioServicio = new ModificarUsuarioServicio();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si no se envían campos de modificación, se interpreta como solicitud de carga del formulario
        if (request.getParameter("nombreCompleto") == null) {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            usuarioDTO usuario = modificarUsuarioServicio.obtenerUsuarioPorId(idUsuario);
            if (usuario != null) {
                request.setAttribute("usuario", usuario);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/modificarUsuario.jsp");
                dispatcher.forward(request, response);
                return;
            } else {
                response.sendRedirect("panelAdmin.jsp?error=Usuario+no+encontrado");
            }
        } else {
            // Procesamiento de la actualización
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            String nombreCompleto = request.getParameter("nombreCompleto");
            String numeroUsuario = request.getParameter("numeroUsuario");
            String rolUsuario = request.getParameter("rolUsuario");
            String correoUsuario = request.getParameter("correoUsuario"); // Se lee el correo

            byte[] imagenUsuario = null;
            Part imagenPart = request.getPart("imagenUsuario");
            if (imagenPart != null && imagenPart.getSize() > 0) {
                // Se ha subido una nueva imagen
                try (InputStream is = imagenPart.getInputStream()) {
                    imagenUsuario = is.readAllBytes();
                }
            } else {
                // No se subió nueva imagen, usar la imagen actual del input hidden
                String imagenActualBase64 = request.getParameter("imagenUsuarioActual");
                if (imagenActualBase64 != null && !imagenActualBase64.isEmpty()) {
                    imagenUsuario = Base64.getDecoder().decode(imagenActualBase64);
                }
            }
            
            usuarioDTO usuarioActualizado = new usuarioDTO();
            usuarioActualizado.setIdUsuario(idUsuario);
            usuarioActualizado.setNombreCompleto(nombreCompleto);
            usuarioActualizado.setNumeroUsuario(numeroUsuario);
            usuarioActualizado.setRolUsuario(rolUsuario);
            usuarioActualizado.setCorreoUsuario(correoUsuario); // Se asigna el correo
            usuarioActualizado.setImagenUsuario(imagenUsuario);
            
            usuarioDTO modificado = modificarUsuarioServicio.modificarUsuario(idUsuario, usuarioActualizado);
            if (modificado != null) {
                response.sendRedirect("panelAdmin.jsp?mensaje=Usuario+modificado+correctamente");
            } else {
                response.sendRedirect("panelAdmin.jsp?error=Error+al+modificar+usuario");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("Método GET no soportado para modificación, use POST.");
    }
}
