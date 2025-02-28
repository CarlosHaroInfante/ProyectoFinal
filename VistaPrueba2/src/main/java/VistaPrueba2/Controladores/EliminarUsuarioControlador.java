package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import VistaPrueba2.Servicios.EliminarUsuarioServicio;
import VistaPrueba2.Utils.FicheroLogVista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para eliminar un usuario.
 * <p>
 * Este servlet recibe el parámetro "idUsuario" y elimina el usuario correspondiente.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/EliminarUsuario")
public class EliminarUsuarioControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Servicio encargado de eliminar un usuario.
     */
    private EliminarUsuarioServicio eliminarUsuarioServicio;

    /**
     * Inicializa el servlet e instancia el servicio de eliminación de usuario.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        try {
            eliminarUsuarioServicio = new EliminarUsuarioServicio();
            FicheroLogVista.logInfo("EliminarUsuarioControlador: Servicio de eliminación de usuario inicializado correctamente.");
        } catch (Exception e) {
            FicheroLogVista.logError("EliminarUsuarioControlador: Error durante la inicialización.", e);
            throw new ServletException("Error al inicializar EliminarUsuarioControlador.", e);
        }
    }

    /**
     * Método doGet para pruebas.
     * <p>
     * Confirma que el servlet está desplegado y funcionando.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request  El objeto HttpServletRequest.
     * @param response El objeto HttpServletResponse.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            response.setContentType("text/plain");
            response.getWriter().write("Servlet EliminarUsuario OK");
            FicheroLogVista.logInfo("EliminarUsuarioControlador: doGet ejecutado correctamente.");
        } catch (Exception e) {
            FicheroLogVista.logError("EliminarUsuarioControlador: Error en doGet.", e);
            throw new ServletException("Error en doGet de EliminarUsuarioControlador.", e);
        }
    }

    /**
     * Método doPost para eliminar un usuario.
     * <p>
     * Recibe el parámetro "idUsuario" desde la solicitud y elimina el usuario correspondiente.
     * Retorna un JSON indicando el resultado de la operación.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request  El objeto HttpServletRequest que contiene el parámetro "idUsuario".
     * @param response El objeto HttpServletResponse para enviar la respuesta.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
            FicheroLogVista.logInfo("EliminarUsuarioControlador: idUsuario recibido: " + idUsuario);
            
            boolean eliminado = eliminarUsuarioServicio.eliminarUsuario(idUsuario);
            FicheroLogVista.logInfo("EliminarUsuarioControlador: Resultado de eliminación: " + eliminado);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"eliminado\": " + eliminado + "}");
            out.flush();
        } catch (NumberFormatException e) {
            FicheroLogVista.logError("EliminarUsuarioControlador: ID de usuario inválido.", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"ID de usuario inválido.\"}");
        } catch (Exception e) {
            FicheroLogVista.logError("EliminarUsuarioControlador: Error al eliminar el usuario.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error interno al eliminar el usuario.\"}");
        }
    }
}
