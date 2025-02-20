	package VistaPrueba2.Controladores;
	
	
	import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import VistaPrueba2.Servicios.RegistroServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
	
	
	@WebServlet("/registro")
	@MultipartConfig
	public class RegistroUsuario extends HttpServlet {
	
	
		private RegistroServicio servicio;
	
	    @Override
	    public void init() throws ServletException {
	        this.servicio = new RegistroServicio();
	    }
	    
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        // Recuperar la acción enviada por el formulario
	        String accion = request.getParameter("accion");
	        if (accion == null) {
	            Part accionPart = request.getPart("accion");
	            if (accionPart != null) {
	                accion = new String(accionPart.getInputStream().readAllBytes());
	            }
	        }
	        System.out.println("Acción recibida: " + accion);

	        if (accion != null && accion.equals("registroSinPassword")) {
	            // Recoger parámetros del formulario
	            String nombreCompleto = request.getParameter("nombreCompleto");
	            String numeroUsuario = request.getParameter("numeroUsuario");
	            String correoUsuario = request.getParameter("correoUsuario");
	            String rolUsuario = request.getParameter("rolUsuario");
	            if (rolUsuario == null || rolUsuario.isEmpty()) {
	                rolUsuario = "Usuario";
	            }
	            Part filePart = request.getPart("imagenUsuario");
	            byte[] imagenUsuario = null;
	            if (filePart != null && filePart.getSize() > 0) {
	                imagenUsuario = filePart.getInputStream().readAllBytes();
	            }
	            
	            // Crear un objeto JSON con los datos del usuario
	            JSONObject json = new JSONObject();
	            json.put("nombreCompleto", nombreCompleto);
	            json.put("numeroUsuario", numeroUsuario);
	            json.put("correoUsuario", correoUsuario);
	            json.put("rolUsuario", rolUsuario);
	            // No se envía contraseña
	            json.put("password", "");
	            
	            // Aquí la API se encargará de generar el código y enviar el correo,
	            // por lo que no se incluyen los campos tokenConfirmacion y confirmado.
	            // (La API asignará esos valores)
	            if (imagenUsuario != null) {
	                String imagenBase64 = java.util.Base64.getEncoder().encodeToString(imagenUsuario);
	                json.put("imagenUsuario", imagenBase64);
	            }
	            
	            // Realizar la petición HTTP al endpoint de la API para registro sin contraseña
	            String apiUrl = "http://localhost:9526/api/auth/registroSinPassword";
	            URL url = new URL(apiUrl);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            con.setRequestMethod("POST");
	            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
	            con.setDoOutput(true);
	            
	            try (OutputStream os = con.getOutputStream()) {
	                byte[] input = json.toString().getBytes("utf-8");
	                os.write(input, 0, input.length);
	            }
	            
	            int responseCode = con.getResponseCode();
	            boolean usuarioRegistrado = (responseCode == HttpURLConnection.HTTP_OK ||
	                                          responseCode == HttpURLConnection.HTTP_CREATED);
	            if (usuarioRegistrado) {
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.getWriter().write("{\"success\": true, \"message\": \"Usuario registrado. Se ha enviado un correo electrónico de verificación.\"}");
	            } else {
	                response.setContentType("application/json");
	                response.setCharacterEncoding("UTF-8");
	                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                response.getWriter().write("{\"error\": \"Acción no válida, parámetros faltantes o El correo ya existe.\"}");
	            }
	        }else {
		        	System.out.println("Acción no válida o parámetros faltantes.");
		            response.setContentType("application/json");
		            response.setCharacterEncoding("UTF-8");
		            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            response.getWriter().write("{\"error\": \"El correo ya existe.\"}");
		       }
	        
	    }
	}