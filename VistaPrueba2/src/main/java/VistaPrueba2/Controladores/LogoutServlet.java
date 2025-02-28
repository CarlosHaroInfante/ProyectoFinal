package VistaPrueba2.Controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servlet para cerrar la sesión del usuario.
 * <p>
 * Este servlet invalida la sesión actual del usuario y redirige a la página de inicio (index.jsp).
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * Procesa las solicitudes GET para cerrar la sesión del usuario.
     * <p>
     * Invalida la sesión actual si existe y redirige al formulario de login (index.jsp).
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
            HttpSession session = request.getSession(false); // Obtener la sesión existente, si hay
            if (session != null) {
                session.invalidate(); // Invalida la sesión
                FicheroLogVista.logInfo("LogoutServlet: Sesión invalidada correctamente.");
            } else {
                FicheroLogVista.logInfo("LogoutServlet: No existe sesión activa para invalidar.");
            }
            // Redirige al formulario de login o a otra página, según el flujo de la aplicación
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (Exception e) {
            FicheroLogVista.logError("LogoutServlet: Error al cerrar la sesión.", e);
            throw new ServletException("Error al cerrar la sesión.", e);
        }
    }
}


