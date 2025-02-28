package VistaPrueba2.Controladores;

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
import edu.Periodico.Prueba.Util.FicheroLog;

/**
 * Filtro de seguridad que controla el acceso a páginas protegidas.
 * 27/02/2025 - CHI
 */
@WebFilter("/*")
public class SeguridadControlador implements Filter {

    /**
     * Inicializa el filtro.
     * 27/02/2025 - CHI
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            // Inicialización si es necesaria
            FicheroLog.logInfo("SeguridadControlador: Filtro inicializado.");
        } catch (Exception e) {
            FicheroLog.logError("SeguridadControlador: Error en init.", e);
            throw new ServletException("Error en la inicialización del filtro de seguridad.", e);
        }
    }

    /**
     * Filtra las solicitudes entrantes.
     * 27/02/2025 - CHI
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest request  = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            String contextPath = request.getContextPath();
            String uri = request.getRequestURI();

            // Quitar la barra final (si no es la raíz) para evitar discrepancias
            if (!uri.equals(contextPath + "/") && uri.endsWith("/")) {
                uri = uri.substring(0, uri.length() - 1);
            }

            // Definir páginas públicas (estas se pueden acceder sin sesión)
            boolean esPublica = uri.equals(contextPath + "/") ||
                                uri.endsWith("InicioSesion.html") ||
                                uri.endsWith("actualizarContrasena.html") ||
                                uri.endsWith("recuperacionContrasenia.html") ||
                                uri.endsWith("verificarCodigo.html") ||
                                uri.endsWith("nuevoUsuario.html") ||
                                uri.endsWith("registro") ||
                                uri.endsWith("recuperarContrasenia") ||
                                uri.endsWith("recuperarContrasena") ||
                                uri.endsWith("actualizarPassword") ||
                                uri.contains("/login") ||
                                uri.contains("/mostrarNoticias") ||
                                uri.endsWith("index.jsp") ||
                                uri.matches(".*\\.(css|js|png|jpg|jpeg)$");

            if (esPublica) {
                FicheroLog.logInfo("SeguridadControlador: Acceso a página pública: " + uri);
                chain.doFilter(request, response);
                return;
            }

            // Verificar que exista la sesión y que el usuario esté autenticado
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("usuarioLogueado") == null) {
                FicheroLog.logInfo("SeguridadControlador: Usuario no autenticado, redirigiendo a index.jsp");
                response.sendRedirect(contextPath + "/index.jsp");
                return;
            }

            // Bloquear acceso a páginas de administración para usuarios que no sean Admin
            if (uri.equals(contextPath + "/Admin.html") || uri.startsWith(contextPath + "/Admin/")) {
                String rol = (String) session.getAttribute("rol");
                if (rol == null || !rol.equalsIgnoreCase("Admin")) {
                    FicheroLog.logInfo("SeguridadControlador: Usuario sin permisos administrativos, redirigiendo a index.jsp");
                    response.sendRedirect(contextPath + "/index.jsp");
                    return;
                }
            }

            // Si pasó todas las validaciones, continuar con la cadena de filtros
            chain.doFilter(request, response);
        } catch (Exception e) {
            FicheroLog.logError("SeguridadControlador: Error durante la filtración.", e);
            throw new ServletException("Error en el filtro de seguridad.", e);
        }
    }

    /**
     * Libera los recursos del filtro.
     * 27/02/2025 - CHI
     */
    @Override
    public void destroy() {
        try {
            // Liberar recursos si es necesario
            FicheroLog.logInfo("SeguridadControlador: Filtro destruido.");
        } catch (Exception e) {
            FicheroLog.logError("SeguridadControlador: Error en destroy.", e);
        }
    }
}
