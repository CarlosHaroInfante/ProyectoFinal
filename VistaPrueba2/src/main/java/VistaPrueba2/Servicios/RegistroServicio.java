/*package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import VistaPrueba2.Dtos.usuarioDTO;

public class RegistroServicio {

    public boolean guardarUsuario(usuarioDTO usuario) {
        try {
            // Crear un objeto JSON con los datos del usuario
            JSONObject json = new JSONObject();
            json.put("nombreCompleto", usuario.getNombreCompleto());
            json.put("numeroUsuario", usuario.getNumeroUsuario());
            json.put("correoUsuario", usuario.getCorreoUsuario());
            json.put("rolUsuario", usuario.getRolUsuario());
            json.put("password", usuario.getPassword());

            // Si la imagen existe, la convertimos a Base64
            if (usuario.getImagenUsuario() != null) {
                String imagenBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
                json.put("imagenUsuario", imagenBase64);
            }

            // URL de la API para guardar el usuario
            String urlApi = "http://localhost:9526/api/usuarios/alta";
            URL url = new URL(urlApi);

            // Abrir la conexión HTTP
            HttpURLConnection conex = (HttpURLConnection) url.openConnection();
            conex.setRequestMethod("POST");
            conex.setRequestProperty("Content-Type", "application/json");
            conex.setDoOutput(true);

            // Enviar el JSON en el cuerpo de la solicitud
            try (OutputStream os = conex.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conex.getResponseCode();

            // Procesar la respuesta
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                // Leer la respuesta del servidor (si es necesario)
                BufferedReader in = new BufferedReader(new InputStreamReader(conex.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Respuesta de la API: " + response.toString());
                return true;
            } else {
                System.out.println("Error al crear el usuario: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Si ocurre un error, se captura y se retorna false
        }
    }

    // Agrega el método confirmarUsuario para validar la confirmación a través del token
    public boolean confirmarUsuario(String token) {
        try {
            // Construir la URL de la API de confirmación (ajusta esta URL según tu implementación)
            String urlApi = "http://localhost:9526/api/usuarios/confirmar?token=" + token;
            URL url = new URL(urlApi);

            // Abrir la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            
            // Leer la respuesta de la API, si es necesario
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Respuesta de la API de confirmación: " + response.toString());
            // Suponemos que HTTP_OK (200) indica que la confirmación fue exitosa
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}*/

package VistaPrueba2.Servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import org.json.JSONObject;

import VistaPrueba2.Dtos.usuarioDTO;
import edu.Periodico.Prueba.Util.EmailUtil;

public class RegistroServicio {

    // Método existente: guardarUsuario (para registro con contraseña)
    public boolean guardarUsuario(usuarioDTO usuario) {
        try {
            // Crear un objeto JSON con los datos del usuario (incluye contraseña)
            JSONObject json = new JSONObject();
            json.put("nombreCompleto", usuario.getNombreCompleto());
            json.put("numeroUsuario", usuario.getNumeroUsuario());
            json.put("correoUsuario", usuario.getCorreoUsuario());
            json.put("rolUsuario", usuario.getRolUsuario());
            json.put("password", usuario.getPassword());

            // Si la imagen existe, la convertimos a Base64
            if (usuario.getImagenUsuario() != null) {
                String imagenBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
                json.put("imagenUsuario", imagenBase64);
            }

            // URL de la API para guardar el usuario
            String urlApi = "http://localhost:9526/api/usuarios/alta";
            URL url = new URL(urlApi);

            HttpURLConnection conex = (HttpURLConnection) url.openConnection();
            conex.setRequestMethod("POST");
            conex.setRequestProperty("Content-Type", "application/json");
            conex.setDoOutput(true);

            try (OutputStream os = conex.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conex.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conex.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Respuesta de la API: " + response.toString());
                return true;
            } else {
                System.out.println("Error al crear el usuario: " + responseCode);
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Nuevo método: registrarUsuarioSinPassword
    public boolean registrarUsuarioSinPassword(usuarioDTO usuario) {
        try {
            // Para este flujo, dejamos la contraseña vacía o nula
            usuario.setPassword("");

            // Generar código de verificación aleatorio (número de 6 dígitos)
            int codigo = 100000 + new Random().nextInt(900000);  // Entre 100000 y 999999
            String codigoVerificacion = String.valueOf(codigo);
            usuario.setTokenConfirmacion(codigoVerificacion);
            usuario.setConfirmado(false);

            // Crear un objeto JSON con los datos del usuario, incluyendo el código
            JSONObject json = new JSONObject();
            json.put("nombreCompleto", usuario.getNombreCompleto());
            json.put("numeroUsuario", usuario.getNumeroUsuario());
            json.put("correoUsuario", usuario.getCorreoUsuario());
            json.put("rolUsuario", usuario.getRolUsuario());
            json.put("password", "");  // Sin contraseña
            json.put("tokenConfirmacion", codigoVerificacion);
            json.put("confirmado", false);

            // Si la imagen existe, convertirla a Base64
            if (usuario.getImagenUsuario() != null) {
                String imagenBase64 = java.util.Base64.getEncoder().encodeToString(usuario.getImagenUsuario());
                json.put("imagenUsuario", imagenBase64);
            }

            // URL de la API para registrar el usuario
            String urlApi = "http://localhost:9526/api/usuarios/alta";
            URL url = new URL(urlApi);

            HttpURLConnection conex = (HttpURLConnection) url.openConnection();
            conex.setRequestMethod("POST");
            conex.setRequestProperty("Content-Type", "application/json");
            conex.setDoOutput(true);

            try (OutputStream os = conex.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conex.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conex.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Respuesta de la API (registro sin password): " + response.toString());

                // Enviar correo de verificación usando el código generado
                String body = "Hola " + usuario.getNombreCompleto() + ",\n\n" +
                              "Tu código de verificación es: " + codigoVerificacion + "\n\n" +
                              "Ingresa este código en la página de verificación para establecer tu contraseña.";
                EmailUtil.sendEmail(usuario.getCorreoUsuario(), "Código de Verificación", body);

                return true;
            } else {
                System.out.println("Error al registrar el usuario (sin password): " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para confirmar usuario mediante token (puede mantenerse igual o adaptarse)
    public boolean confirmarUsuario(String token) {
        try {
            String urlApi = "http://localhost:9526/api/usuarios/confirmar?token=" + token;
            URL url = new URL(urlApi);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Respuesta de la API de confirmación: " + response.toString());
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

