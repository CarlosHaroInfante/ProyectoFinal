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
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Servlet para la recuperación de contraseña.
 * <p>
 * Este servlet recoge los parámetros del formulario ("correoUsuario" y "nombreCompleto")
 * y llama al servicio para enviar el correo de recuperación. La respuesta se devuelve en formato JSON.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/recuperarContrasenia")
@MultipartConfig
public class RecuperarContrasena extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RegistroServicio servicio;

    /**
     * Inicializa el servlet e instancia el servicio de registro.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        try {
            servicio = new RegistroServicio();
            FicheroLogVista.logInfo("RecuperarContrasena: Servicio de registro instanciado correctamente.");
        } catch (Exception e) {
            FicheroLogVista.logError("RecuperarContrasena: Error al instanciar el servicio de registro.", e);
            throw new ServletException("Error en init de RecuperarContrasena.", e);
        }
    }

    /**
     * Procesa la solicitud POST para enviar el correo de recuperación.
     * <p>
     * Recoge los parámetros "correoUsuario" y "nombreCompleto" del formulario. Si alguno de estos
     * parámetros está ausente o es vacío, devuelve un error (HTTP 400). De lo contrario, llama al servicio
     * para enviar el correo de recuperación y retorna la respuesta en formato JSON.
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
        try {
            // Recoger parámetros del formulario
            String correo = request.getParameter("correoUsuario");
            String nombreCompleto = request.getParameter("nombreCompleto");
            FicheroLogVista.logInfo("RecuperarContrasena: Parámetros recibidos - correo: " + correo + ", nombreCompleto: " + nombreCompleto);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            // Validar que se han recibido datos
            if (correo == null || correo.trim().isEmpty() || nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
                FicheroLogVista.logInfo("RecuperarContrasena: Faltan datos.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JSONObject json = new JSONObject();
                json.put("error", "Faltan datos.");
                response.getWriter().write(json.toString());
                return;
            }
            
            // Llamar al método del servicio para enviar el correo de recuperación
            boolean exito = servicio.enviarCorreoRestablecerContrasena(nombreCompleto, correo);
            FicheroLogVista.logInfo("RecuperarContrasena: Resultado del envío de correo: " + exito);
            
            JSONObject jsonResponse = new JSONObject();
            if (exito) {
                jsonResponse.put("success", true);
                jsonResponse.put("message", "Correo de recuperación enviado.");
            } else {
                jsonResponse.put("error", "El correo no existe o hubo un error.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            FicheroLogVista.logError("RecuperarContrasena: Error al procesar la solicitud POST.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Error interno al procesar la solicitud: " + e.getMessage());
            response.getWriter().write(errorJson.toString());
        }
    }
}
