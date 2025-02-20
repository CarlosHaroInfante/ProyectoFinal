/*package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONObject;

import VistaPrueba2.Servicios.CambioContraseña;
import VistaPrueba2.Servicios.RecuperarContrasenaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/recuperarContrasena")
@MultipartConfig
public class RecuperarContrasena extends HttpServlet {

    private RecuperarContrasenaService servicio;
    private CambioContraseña contraseña;
    @Override
    public void init() throws ServletException {
        this.servicio = new RecuperarContrasenaService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Recuperar parámetros (usando getParameter, ya que se envían como multipart)
        String correo = request.getParameter("correo");
        String codigo = request.getParameter("codigoVerificacion");
        String nuevaPassword = request.getParameter("nuevaPassword");
        String confirmarPassword = request.getParameter("confirmarPassword");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (nuevaPassword == null || confirmarPassword == null || !nuevaPassword.equals(confirmarPassword)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Las contraseñas no coinciden o faltan datos.\"}");
            return;
        }
        
        try {
            String apiResponse = contraseña.actualizarPassword(correo, codigo, nuevaPassword, confirmarPassword);
            if (apiResponse.toLowerCase().contains("\"success\":true")) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(apiResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(apiResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Error al actualizar la contraseña: " + e.getMessage() + "\"}");
        }
    }
}*/
package VistaPrueba2.Controladores;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/recuperarContrasena")
public class RecuperarContrasena extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String correo = request.getParameter("correo");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        
        if (correo == null || correo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("error", "El correo es obligatorio.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }
        
        // Llamar a la API de recuperación de contraseña
        String apiUrl = "http://localhost:9526/api/auth/recuperarContrasena";
        URL url = new URL(apiUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setDoOutput(true);
        
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("correo", correo);
        
        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonRequest.toString().getBytes("UTF-8"));
        }
        
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK ||
            responseCode == HttpURLConnection.HTTP_CREATED) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            jsonResponse = new JSONObject(sb.toString());
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse = new JSONObject(sb.toString());
        }
        PrintWriter out = response.getWriter();
        out.write(jsonResponse.toString());
        out.flush();
    }
}

