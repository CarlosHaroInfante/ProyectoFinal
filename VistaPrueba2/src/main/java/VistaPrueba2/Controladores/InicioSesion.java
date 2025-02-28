package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Servicios.InicioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet para gestionar el inicio de sesión.
 * <p>
 * Este servlet muestra el formulario de login (doGet) y procesa la autenticación del usuario (doPost).
 * Los datos se envían en formato JSON y se utiliza Base64 para la imagen del usuario.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/login")
@MultipartConfig
public class InicioSesion extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private InicioServicio servicio;
    private static final Logger log = LoggerFactory.getLogger(InicioSesion.class);

    /**
     * Inicializa el servlet e instancia el servicio de inicio de sesión.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        try {
            this.servicio = new InicioServicio();
            log.info("[INIT] InicioSesion servlet iniciado.");
        } catch (Exception e) {
            log.error("[INIT] Error al iniciar el servlet de InicioSesion.", e);
            throw new ServletException("Error en la inicialización de InicioSesion.", e);
        }
    }
    
    /**
     * Procesa las solicitudes GET.
     * <p>
     * Redirige al formulario de inicio de sesión.
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
            log.info("[GET] Acceso al formulario de login.");
            request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
        } catch (Exception e) {
            log.error("[GET] Error al cargar el formulario de login.", e);
            throw new ServletException("Error en doGet de InicioSesion.", e);
        }
    }

    /**
     * Procesa la autenticación del usuario y devuelve la respuesta en formato JSON.
     * <p>
     * Recoge los parámetros 'correoUsuario' y 'password' del formulario, autentica al usuario,
     * y si es exitoso, guarda la información en la sesión y retorna un JSON con éxito.
     * En caso de fallo, retorna un JSON con un mensaje de error y establece el estado HTTP correspondiente.
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
            log.info("[POST] Proceso de autenticación iniciado.");

            // Recoger parámetros del formulario
            String correo = request.getParameter("correoUsuario");
            String password = request.getParameter("password");
            log.info("[POST] Parámetros recibidos: correo = {}, password = {}", correo, password);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonResponse = new JSONObject();

            // Autenticar al usuario
            usuarioDTO usuario = servicio.autenticarUsuario(correo, password);
            if (usuario != null) {
                HttpSession session = request.getSession();
                // Guardar datos del usuario en la sesión
                session.setAttribute("usuarioLogueado", usuario.getCorreoUsuario());
                session.setAttribute("idUsuario", usuario.getIdUsuario());
                // Convertir la imagen del usuario a Base64, si existe
                if (usuario.getImagenUsuario() != null) {
                    String imagenBase64 = Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
                    session.setAttribute("imagenUsuario", imagenBase64);
                } else {
                    session.setAttribute("imagenUsuario", "");
                }
                // Guardar el rol y loguear la acción
                if ("Admin".equalsIgnoreCase(usuario.getRolUsuario())) {
                    log.info("[POST] Login exitoso como ADMINISTRADOR.");
                    session.setAttribute("rol", "Admin");
                } else {
                    log.info("[POST] Login exitoso como USUARIO.");
                    session.setAttribute("rol", "Usuario");
                }
                jsonResponse.put("success", true);
                jsonResponse.put("mensaje", "Login exitoso");
                PrintWriter out = response.getWriter();
                out.write(jsonResponse.toString());
                out.flush();
            } else {
                log.warn("[POST] Credenciales incorrectas.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("error", "Usuario o contraseña incorrectos.");
                PrintWriter out = response.getWriter();
                out.write(jsonResponse.toString());
                out.flush();
            }
        } catch (Exception e) {
            log.error("[POST] Error al autenticar el usuario.", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Error interno al procesar la autenticación: " + e.getMessage());
            PrintWriter out = response.getWriter();
            out.write(errorJson.toString());
            out.flush();
        }
    }
}
