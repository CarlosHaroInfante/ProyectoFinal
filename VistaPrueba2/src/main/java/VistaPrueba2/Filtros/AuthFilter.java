package VistaPrueba2.Filtros;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import VistaPrueba2.Utils.FicheroLogVista;

/**
 * Filtro de autenticación para controlar el acceso a páginas protegidas.
 * <p>
 * Este filtro verifica si el usuario está autenticado (es decir, si existe una sesión y tiene
 * el atributo "usuarioLogueado") antes de permitir el acceso a las rutas especificadas. Si el usuario
 * no está autenticado, es redirigido a la página "index.html".
 * </p>
 * 27/02/2025 - CHI
 */
@WebFilter(urlPatterns = {"/index.html", "/InicioSesionCompleto.html", "/InicioUsuario.html", "/otrasPaginasProtegidas/*"})
public class AuthFilter implements Filter {

    /**
     * Inicializa el filtro.
     * <p>
     * Este método se ejecuta una única vez al iniciar el filtro y se utiliza para realizar cualquier
     * inicialización necesaria.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param filterConfig La configuración del filtro.
     * @throws ServletException Si ocurre un error durante la inicialización.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            // Inicialización si es necesaria
            FicheroLogVista.logInfo("AuthFilter: Filtro de autenticación inicializado.");
        } catch (Exception e) {
            FicheroLogVista.logError("AuthFilter: Error durante la inicialización.", e);
            throw new ServletException("Error en init del filtro de autenticación.", e);
        }
    }

    /**
     * Filtra las solicitudes entrantes para controlar el acceso a páginas protegidas.
     * <p>
     * Verifica si el usuario tiene una sesión activa y si está autenticado (atributo "usuarioLogueado").
     * Si no lo está, redirige a "index.html"; de lo contrario, permite el paso de la solicitud.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request  El objeto ServletRequest que contiene la solicitud.
     * @param response El objeto ServletResponse para enviar la respuesta.
     * @param chain    La cadena de filtros.
     * @throws IOException      Si ocurre un error de entrada/salida.
     * @throws ServletException Si ocurre un error en el filtro.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            HttpServletResponse httpRes = (HttpServletResponse) response;
            HttpSession session = httpReq.getSession(false);

            if (session == null || session.getAttribute("usuarioLogueado") == null) {
                FicheroLogVista.logInfo("AuthFilter: Usuario no autenticado. Redirigiendo a index.html.");
                httpRes.sendRedirect(httpReq.getContextPath() + "/index.html");
            } else {
                FicheroLogVista.logInfo("AuthFilter: Usuario autenticado. Continuando con la cadena de filtros.");
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            FicheroLogVista.logError("AuthFilter: Error durante la filtración.", e);
            throw new ServletException("Error en el filtro de autenticación.", e);
        }
    }

    /**
     * Libera los recursos del filtro.
     * <p>
     * Este método se invoca cuando el filtro está a punto de ser destruido.
     * </p>
     * 27/02/2025 - CHI
     */
    @Override
    public void destroy() {
        try {
            // Limpieza si es necesaria
            FicheroLogVista.logInfo("AuthFilter: Filtro destruido.");
        } catch (Exception e) {
            FicheroLogVista.logError("AuthFilter: Error en destroy.", e);
        }
    }
}
