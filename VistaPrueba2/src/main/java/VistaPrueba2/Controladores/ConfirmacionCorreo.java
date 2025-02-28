package VistaPrueba2.Controladores;

import java.io.IOException;
import VistaPrueba2.Servicios.RegistroServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import edu.Periodico.Prueba.Util.FicheroLog;

/**
 * Servlet para la confirmación de correo.
 * <p>
 * Este servlet obtiene el token de la URL y lo utiliza para confirmar al usuario
 * mediante el servicio de registro. Si la confirmación es exitosa, redirige a index.jsp;
 * de lo contrario, muestra un mensaje de error.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/confirmacionCorreo")
public class ConfirmacionCorreo extends HttpServlet {

    private RegistroServicio servicio;

    /**
     * Inicializa el servlet y crea una instancia del servicio de registro.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        this.servicio = new RegistroServicio();
        FicheroLog.logInfo("ConfirmacionCorreo servlet inicializado.");
    }

    /**
     * Procesa las solicitudes GET para confirmar el correo del usuario.
     * <p>
     * Obtiene el token desde el parámetro de la URL y utiliza el servicio para confirmar al usuario.
     * Si el token es válido, redirige a index.jsp; de lo contrario, devuelve un mensaje de error.
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
            // Obtener el token desde el parámetro de la URL
            String token = request.getParameter("token");
            FicheroLog.logInfo("ConfirmacionCorreo: Token recibido: " + token);
            
            if (token != null && !token.isEmpty()) {
                boolean confirmado = servicio.confirmarUsuario(token);
                if (confirmado) {
                    FicheroLog.logInfo("ConfirmacionCorreo: Token confirmado correctamente.");
                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                } else {
                    FicheroLog.logInfo("ConfirmacionCorreo: Token inválido o expirado.");
                    response.getWriter().write("El token es inválido o ha expirado.");
                }
            } else {
                FicheroLog.logInfo("ConfirmacionCorreo: No se proporcionó un token válido.");
                response.getWriter().write("No se proporcionó un token válido.");
            }
        } catch (Exception e) {
            FicheroLog.logError("ConfirmacionCorreo: Error al procesar la confirmación del correo.", e);
            response.getWriter().write("Error interno al procesar la confirmación del correo.");
        }
    }
}
