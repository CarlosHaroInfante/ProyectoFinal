package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Servicios.PanelAdminServicio;
import VistaPrueba2.Utils.ByteArrayToBase64TypeAdapter;
import VistaPrueba2.Utils.FicheroLogVista;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para mostrar los usuarios en el panel de administración.
 * <p>
 * Este servlet obtiene la lista de usuarios a partir del parámetro "limit" y devuelve la información
 * en formato JSON utilizando Gson con un adaptador para convertir arrays de bytes a Base64.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/mostrarUsuarios")
public class PanelAdminControlador extends HttpServlet {

    private PanelAdminServicio panelAdminServicio = new PanelAdminServicio();

    /**
     * Procesa la solicitud GET para obtener los usuarios.
     * <p>
     * Se obtiene el parámetro "limit" para limitar la cantidad de usuarios, se obtiene la lista de usuarios
     * mediante el servicio, se convierte la lista a JSON usando Gson y se escribe la respuesta.
     * En caso de error, se registra y se envía un mensaje de error en formato JSON.
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
            FicheroLogVista.logInfo("PanelAdminControlador: Iniciando doGet para mostrar usuarios.");
            
            // Obtener el parámetro "limit"
            String limitParam = request.getParameter("limit");
            int limit = 0;
            try {
                limit = (limitParam != null) ? Integer.parseInt(limitParam) : 0;
            } catch (NumberFormatException e) {
                FicheroLogVista.logError("PanelAdminControlador: Error al parsear el parámetro 'limit'. Se usará el valor por defecto 0.", e);
                limit = 0;
            }
            
            // Obtener la lista de usuarios a través del servicio.
            List<usuarioDTO> usuarios = panelAdminServicio.obtenerUsuarios(limit);
            FicheroLogVista.logInfo("PanelAdminControlador: Número de usuarios obtenidos: " + (usuarios != null ? usuarios.size() : 0));
            
            // Configurar Gson con el adaptador para byte[]
            Gson gson = new GsonBuilder()
                            .registerTypeAdapter(byte[].class, new ByteArrayToBase64TypeAdapter())
                            .create();
            String json = gson.toJson(usuarios);
            
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.write(json);
            out.flush();
            
            FicheroLogVista.logInfo("PanelAdminControlador: Respuesta JSON enviada correctamente.");
        } catch (Exception e) {
            FicheroLogVista.logError("PanelAdminControlador: Error al procesar la solicitud GET para mostrar usuarios.", e);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.write("{\"error\":\"Error al obtener los usuarios.\"}");
            out.flush();
        }
    }
}
