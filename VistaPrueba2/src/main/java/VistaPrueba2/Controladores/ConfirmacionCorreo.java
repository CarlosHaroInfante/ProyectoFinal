package VistaPrueba2.Controladores;

import java.io.IOException;
import VistaPrueba2.Servicios.RegistroServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/confirmacionCorreo")
public class ConfirmacionCorreo extends HttpServlet {

    private RegistroServicio servicio;

    @Override
    public void init() throws ServletException {
        this.servicio = new RegistroServicio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Obtener el token desde el parámetro de la URL
        String token = request.getParameter("token");

        if (token != null && !token.isEmpty()) {
            boolean confirmado = servicio.confirmarUsuario(token);
            if (confirmado) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                response.getWriter().write("El token es inválido o ha expirado.");
            }
        } else {
            response.getWriter().write("No se proporcionó un token válido.");
        }
    }
}
