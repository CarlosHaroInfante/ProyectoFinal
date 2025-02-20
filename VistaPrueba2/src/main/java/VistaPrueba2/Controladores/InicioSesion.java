/*package VistaPrueba2.Controladores;

import java.io.IOException;
import java.util.ArrayList;
import VistaPrueba2.Servicios.InicioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class InicioSesion extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private InicioServicio servicio;

    @Override
    public void init() throws ServletException {
        this.servicio = new InicioServicio();
        System.out.println("[INIT] InicioSesion servlet iniciado.");
    }
    
    // Muestra el formulario de login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[GET] Acceso al formulario de login.");
        // Usar ruta absoluta para mayor claridad
        request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
    }

    // Procesa la autenticación
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[POST] Proceso de autenticación iniciado.");

        // Recoger los parámetros del formulario
        String correo = request.getParameter("correoUsuario");
        String password = request.getParameter("password");

        System.out.println("[POST] Parámetros recibidos: correo = " + correo + ", password = " + password);

        // Llamar al servicio para verificar el usuario
        ArrayList<Boolean> resultados = servicio.verificarUsuario(correo, password);
        System.out.println("[POST] Resultados de verificación: " + resultados);

        if (resultados != null && resultados.size() >= 2) {
            HttpSession session = request.getSession();
            if (resultados.get(0) && resultados.get(1)) {
                // Usuario válido y es administrador
                System.out.println("[POST] Login exitoso como ADMINISTRADOR.");
                session.setAttribute("usuarioLogueado", correo);
                session.setAttribute("rol", "Admin");
                response.sendRedirect(request.getContextPath() + "/index2.html");
                System.out.println("[POST] Redirigiendo a: " + request.getContextPath() + "/index2.html");
            } else if (resultados.get(0) && !resultados.get(1)) {
                // Usuario válido pero no es administrador
                System.out.println("[POST] Login exitoso como USUARIO.");
                session.setAttribute("usuarioLogueado", correo);
                session.setAttribute("rol", "Usuario");
                response.sendRedirect(request.getContextPath() + "/index2.html");
                System.out.println("[POST] Redirigiendo a: " + request.getContextPath() + "/index2.html");
            } else {
                // Credenciales incorrectas
                System.out.println("[POST] Credenciales incorrectas.");
                request.setAttribute("errorMessage", "Usuario o contraseña incorrectos.");
                request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
                System.out.println("[POST] Forward al formulario de login.");
            }
        } else {
            // Resultados inesperados
            System.out.println("[POST] Error en la autenticación: resultados inesperados.");
            request.setAttribute("errorMessage", "Error en la autenticación.");
            request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
            System.out.println("[POST] Forward al formulario de login.");
        }
    }
}*/


/*package VistaPrueba2.Controladores;

import java.io.IOException;
import java.util.ArrayList;
import VistaPrueba2.Servicios.InicioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/login")
public class InicioSesion extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private InicioServicio servicio;
    private static final Logger log = LoggerFactory.getLogger(InicioSesion.class);

    @Override
    public void init() throws ServletException {
        this.servicio = new InicioServicio();
        log.info("[INIT] InicioSesion servlet iniciado.");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[GET] Acceso al formulario de login.");
        request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[POST] Proceso de autenticación iniciado.");
        String correo = request.getParameter("correoUsuario");
        String password = request.getParameter("password");
        log.info("[POST] Parámetros recibidos: correo = {}, password = {}", correo, password);

        ArrayList<Boolean> resultados = servicio.verificarUsuario(correo, password);
        log.info("[POST] Resultados de verificación: {}", resultados);
        HttpSession session = request.getSession();
        if (resultados != null && resultados.size() >= 2) {
            if (resultados.get(0) && resultados.get(1)) {
                log.info("[POST] Login exitoso como ADMINISTRADOR.");
                session.setAttribute("usuarioLogueado", correo);
                session.setAttribute("rol", "Admin");
                String redireccion = request.getContextPath() + "/index.jsp";
                log.info("[POST] Redirigiendo a: {}", redireccion);
                response.sendRedirect(redireccion);
            } else if (resultados.get(0) && !resultados.get(1)) {
                log.info("[POST] Login exitoso como USUARIO.");
                session.setAttribute("usuarioLogueado", correo);
                session.setAttribute("rol", "Usuario");
                String redireccion = request.getContextPath() + "/index.jsp";
                log.info("[POST] Redirigiendo a: {}", redireccion);
                response.sendRedirect(redireccion);
            } else {
                log.warn("[POST] Credenciales incorrectas.");
                request.setAttribute("errorMessage", "Usuario o contraseña incorrectos.");
                request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
                log.info("[POST] Forward al formulario de login.");
            }
        } else {
            log.error("[POST] Error en la autenticación: resultados inesperados.");
            request.setAttribute("errorMessage", "Error en la autenticación.");
            request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
            log.info("[POST] Forward al formulario de login.");
        }
    }
}*/


package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import VistaPrueba2.Servicios.InicioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
@MultipartConfig
public class InicioSesion extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private InicioServicio servicio;
    private static final Logger log = LoggerFactory.getLogger(InicioSesion.class);

    @Override
    public void init() throws ServletException {
        this.servicio = new InicioServicio();
        log.info("[INIT] InicioSesion servlet iniciado.");
    }
    
    // Si usas fetch, el GET puede seguir haciendo forward al formulario.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[GET] Acceso al formulario de login.");
        request.getRequestDispatcher("/InicioSesion.html").forward(request, response);
    }

    // Procesa la autenticación y devuelve JSON
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[POST] Proceso de autenticación iniciado.");

        // Asumimos que usas FormData (por lo que los parámetros llegan vía request.getParameter)
        String correo = request.getParameter("correoUsuario");
        String password = request.getParameter("password");
        log.info("[POST] Parámetros recibidos: correo = {}, password = {}", correo, password);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        
        ArrayList<Boolean> resultados = servicio.verificarUsuario(correo, password);
        log.info("[POST] Resultados de verificación: {}", resultados);
        HttpSession session = request.getSession();
        if (resultados != null && resultados.size() >= 2) {
            if (resultados.get(0)) {
                // Usuario válido
                session.setAttribute("usuarioLogueado", correo);
                if (resultados.get(1)) {
                    log.info("[POST] Login exitoso como ADMINISTRADOR.");
                    session.setAttribute("rol", "Admin");
                } else {
                    log.info("[POST] Login exitoso como USUARIO.");
                    session.setAttribute("rol", "Usuario");
                }
                jsonResponse.put("success", true);
                jsonResponse.put("mensaje", "Login exitoso");
                // En caso de éxito, puedes incluir también otros datos si lo deseas.
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
        } else {
            log.error("[POST] Error en la autenticación: resultados inesperados.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("error", "Error en la autenticación.");
            PrintWriter out = response.getWriter();
            out.write(jsonResponse.toString());
            out.flush();
        }
    }
}

