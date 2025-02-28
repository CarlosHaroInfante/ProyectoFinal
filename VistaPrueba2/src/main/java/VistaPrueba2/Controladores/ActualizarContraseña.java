package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import VistaPrueba2.Servicios.CambioContraseña;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servlet para actualizar la contraseña del usuario.
 * <p>
 * Este servlet recoge los parámetros del formulario, valida que las contraseñas coincidan, y llama al servicio
 * de cambio de contraseña. En caso de éxito, retorna la respuesta de la API; en caso de error, retorna un mensaje
 * de error.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/actualizarPassword")
public class ActualizarContraseña extends HttpServlet {

    private CambioContraseña cambioContraseña;

    /**
     * Inicializa el servlet y crea una instancia del servicio CambioContraseña.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        this.cambioContraseña = new CambioContraseña();
        FicheroLogVista.logInfo("Inicializando ActualizarContraseña servlet.");
    }

    /**
     * Procesa las solicitudes POST para actualizar la contraseña.
     * <p>
     * Recoge los parámetros del formulario (correo, código de verificación, nueva contraseña y confirmación),
     * valida que las contraseñas coincidan y llama al servicio para actualizar la contraseña. Devuelve la respuesta
     * de la API en formato JSON.
     * </p>
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud.
     * @param response Objeto HttpServletResponse para enviar la respuesta.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     * 27/02/2025 - CHI
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger parámetros del formulario.
        String correo = request.getParameter("correo");
        String codigo = request.getParameter("codigoVerificacion"); // Asegúrate de que aquí se llame "codigoVerificacion"
        String nuevaPassword = request.getParameter("nuevaPassword");
        String confirmarPassword = request.getParameter("confirmarPassword");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        FicheroLogVista.logInfo("ActualizarContraseña: Correo recibido: " + correo);
        FicheroLogVista.logInfo("ActualizarContraseña: Código recibido: " + codigo);
        
        // Validar que se han enviado ambas contraseñas y que coinciden.
        if (nuevaPassword == null || confirmarPassword == null || !nuevaPassword.equals(confirmarPassword)) {
            FicheroLogVista.logInfo("ActualizarContraseña: Validación fallida, contraseñas no coinciden o faltan datos.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Las contraseñas no coinciden o faltan datos.\"}");
            return;
        }

        try {
            // Llamar al servicio que actualiza la contraseña.
            String apiResponse = cambioContraseña.actualizarPassword(correo, codigo, nuevaPassword, confirmarPassword);
            FicheroLogVista.logInfo("ActualizarContraseña: API response: " + apiResponse);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(apiResponse);
        } catch (Exception e) {
            FicheroLogVista.logError("ActualizarContraseña: Error al actualizar la contraseña", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Error al actualizar la contraseña: " + e.getMessage() + "\"}");
        }
    }
}
