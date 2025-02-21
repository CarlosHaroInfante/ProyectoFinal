/*package VistaPrueba2.Controladores;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import VistaPrueba2.Servicios.CambioContraseña;

@WebServlet("/actualizarPassword")
public class ActualizarContraseña extends HttpServlet {

    private CambioContraseña cambioContraseña;

    @Override
    public void init() throws ServletException {
        this.cambioContraseña = new CambioContraseña();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger parámetros del formulario
        String correo = request.getParameter("correo");
        String codigo = request.getParameter("codigoVerificacion");
        String nuevaPassword = request.getParameter("nuevaPassword");
        String confirmarPassword = request.getParameter("confirmarPassword");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        // Validar que las contraseñas coinciden
        if (nuevaPassword == null || confirmarPassword == null || !nuevaPassword.equals(confirmarPassword)) {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Las contraseñas no coinciden o faltan datos.\"}");
            return;
        }
        
        // Llamar al servicio que a su vez invoca al endpoint de la API
        try {
            String apiResponse = cambioContraseña.actualizarPassword(correo, codigo, nuevaPassword, confirmarPassword);
            // Puedes analizar apiResponse si contiene un mensaje de éxito o error.
            // Si es exitoso, redirige a la página InicioSesion.html
            /*if (apiResponse.toLowerCase().contains("Código Incorrecto")) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"" + apiResponse + "\"}");
            } else {
                // Si el código es correcto y la contraseña se actualizó, redirigir a la página InicioSesion.html
                response.sendRedirect(request.getContextPath() + "/InicioSesion.html");
            }
            
            // Supongamos que la API devuelve un JSON con "success":true en caso de éxito,
            // o un JSON con "error": "El código de verificación es incorrecto." en caso de fallo.
            if (apiResponse.toLowerCase().contains("\"success\":true")) {
                // En caso de éxito, enviamos el mensaje para que el front-end lo procese
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(apiResponse);
                // (El front-end se encargará de redirigir a la página de inicio de sesión)
            } else {
                // Si el código no coincide o hubo otro error, no se redirige
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(apiResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error al actualizar la contraseña: " + e.getMessage());
        }
    }
}*/
package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import VistaPrueba2.Servicios.CambioContraseña;

@WebServlet("/actualizarPassword")
public class ActualizarContraseña extends HttpServlet {

    private CambioContraseña cambioContraseña;

    @Override
    public void init() throws ServletException {
        this.cambioContraseña = new CambioContraseña();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recoger parámetros del formulario
        String correo = request.getParameter("correo");
        String codigo = request.getParameter("codigoVerificacion"); // Asegúrate de que aquí se llame "codigoVerificacion"
        String nuevaPassword = request.getParameter("nuevaPassword");
        String confirmarPassword = request.getParameter("confirmarPassword");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        System.out.println("Correo recibido: " + correo);
        System.out.println("Código recibido: " + codigo);
        System.out.println("Nueva contraseña recibida: " + nuevaPassword);
        System.out.println("Confirmar contraseña recibida: " + confirmarPassword);

        // Validar que se han enviado ambas contraseñas y que coinciden
        if (nuevaPassword == null || confirmarPassword == null || !nuevaPassword.equals(confirmarPassword)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Las contraseñas no coinciden o faltan datos.\"}");
            return;
        }

        try {
            // Llamar al servicio que actualiza la contraseña
            String apiResponse = cambioContraseña.actualizarPassword(correo, codigo, nuevaPassword, confirmarPassword);
            // Imprimir la respuesta para depurar
            System.out.println("API response: " + apiResponse);
            
            // Aquí, en lugar de condicionar según el contenido exacto, se asume que
            // si el método no lanza excepción, la operación fue exitosa.
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Error al actualizar la contraseña: " + e.getMessage() + "\"}");
        }
    }
}

