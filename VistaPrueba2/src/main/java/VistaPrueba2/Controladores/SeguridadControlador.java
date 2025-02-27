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

@WebFilter("/*")
public class SeguridadControlador implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización si es necesaria
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
                
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
            chain.doFilter(request, response);
            return;
        }

        // Verificar que exista la sesión y que el usuario esté autenticado
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioLogueado") == null) {
            // Redirigir a index.jsp si no hay sesión
            response.sendRedirect(contextPath + "/index.jsp");
            return;
        }

        // Bloquear acceso a páginas de administración para usuarios que no sean Admin
        // Si se intenta acceder a "/Admin.html" o a cualquier URL que empiece con "/Admin/"
        if (uri.equals(contextPath + "/Admin.html") || uri.startsWith(contextPath + "/Admin/")) {
            String rol = (String) session.getAttribute("rol");
            if (rol == null || !rol.equalsIgnoreCase("Admin")) {
                // Redirigir a index.jsp si el usuario no es administrador
                response.sendRedirect(contextPath + "/index.jsp");
                return;
            }
        }

        // Si pasó todas las validaciones, continuar con la cadena
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Liberar recursos si es necesario
    }
}
