package VistaPrueba2.Controladores;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import VistaPrueba2.Dtos.usuarioDTO;
import VistaPrueba2.Servicios.ModificarUsuarioServicio;
import VistaPrueba2.Utils.FicheroLogVista;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

/**
 * Controlador para la modificación de usuarios.
 * <p>
 * Este servlet gestiona la solicitud de modificación de un usuario. Si no se envían
 * campos de modificación, carga el formulario con los datos del usuario; de lo contrario,
 * procesa la actualización del usuario, incluyendo la gestión de la imagen.
 * </p>
 * 27/02/2025 - CHI
 */
@WebServlet("/ModificarUsuario")
@MultipartConfig
public class ModificarUsuarioControlador extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Servicio para la modificación de usuarios.
     */
    private ModificarUsuarioServicio modificarUsuarioServicio;

    /**
     * Inicializa el servlet e instancia el servicio de modificación de usuario.
     * 27/02/2025 - CHI
     */
    @Override
    public void init() throws ServletException {
        try {
            modificarUsuarioServicio = new ModificarUsuarioServicio();
            FicheroLogVista.logInfo("ModificarUsuarioControlador: Servicio de modificación de usuario inicializado correctamente.");
        } catch (Exception e) {
            FicheroLogVista.logError("ModificarUsuarioControlador: Error en init.", e);
            throw new ServletException("Error al inicializar el controlador de modificación de usuario.", e);
        }
    }

    /**
     * Procesa la solicitud POST para la modificación de un usuario.
     * <p>
     * Si no se envían campos de modificación, se interpreta como una solicitud de carga del formulario con los datos del usuario;
     * de lo contrario, se procesa la actualización del usuario, incluyendo la gestión de la imagen (nueva o existente).
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
            // Verificar si se envían campos de modificación
            if (request.getParameter("nombreCompleto") == null) {
                try {
                    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                    FicheroLogVista.logInfo("ModificarUsuarioControlador: Solicitud de carga de formulario para idUsuario: " + idUsuario);
                    usuarioDTO usuario = modificarUsuarioServicio.obtenerUsuarioPorId(idUsuario);
                    if (usuario != null) {
                        request.setAttribute("usuario", usuario);
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/modificarUsuario.jsp");
                        dispatcher.forward(request, response);
                        return;
                    } else {
                        FicheroLogVista.logInfo("ModificarUsuarioControlador: Usuario no encontrado con id: " + idUsuario);
                        response.sendRedirect("panelAdmin.jsp?error=Usuario+no+encontrado");
                        return;
                    }
                } catch (Exception e) {
                    FicheroLogVista.logError("ModificarUsuarioControlador: Error al cargar formulario de modificación.", e);
                    response.sendRedirect("panelAdmin.jsp?error=Error+al+cargar+el+formulario");
                    return;
                }
            } else {
                // Procesamiento de la actualización
                try {
                    int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
                    String nombreCompleto = request.getParameter("nombreCompleto");
                    String numeroUsuario = request.getParameter("numeroUsuario");
                    String rolUsuario = request.getParameter("rolUsuario");
                    String correoUsuario = request.getParameter("correoUsuario"); // Se lee el correo

                    byte[] imagenUsuario = null;
                    Part imagenPart = request.getPart("imagenUsuario");
                    if (imagenPart != null && imagenPart.getSize() > 0) {
                        // Se ha subido una nueva imagen
                        try (InputStream is = imagenPart.getInputStream()) {
                            imagenUsuario = is.readAllBytes();
                        }
                    } else {
                        // No se subió nueva imagen, usar la imagen actual del input hidden
                        String imagenActualBase64 = request.getParameter("imagenUsuarioActual");
                        if (imagenActualBase64 != null && !imagenActualBase64.isEmpty()) {
                            imagenUsuario = Base64.getDecoder().decode(imagenActualBase64);
                        }
                    }
                    
                    usuarioDTO usuarioActualizado = new usuarioDTO();
                    usuarioActualizado.setIdUsuario(idUsuario);
                    usuarioActualizado.setNombreCompleto(nombreCompleto);
                    usuarioActualizado.setNumeroUsuario(numeroUsuario);
                    usuarioActualizado.setRolUsuario(rolUsuario);
                    usuarioActualizado.setCorreoUsuario(correoUsuario); // Se asigna el correo
                    usuarioActualizado.setImagenUsuario(imagenUsuario);
                    
                    FicheroLogVista.logInfo("ModificarUsuarioControlador: Procesando actualización para usuario con id: " + idUsuario);
                    usuarioDTO modificado = modificarUsuarioServicio.modificarUsuario(idUsuario, usuarioActualizado);
                    if (modificado != null) {
                        FicheroLogVista.logInfo("ModificarUsuarioControlador: Usuario modificado correctamente: " + modificado);
                        response.sendRedirect("Admin.html?mensaje=Usuario+modificado+correctamente");
                    } else {
                        FicheroLogVista.logInfo("ModificarUsuarioControlador: Error al modificar usuario con id: " + idUsuario);
                        response.sendRedirect("Admin.html?error=Error+al+modificar+usuario");
                    }
                } catch (Exception e) {
                    FicheroLogVista.logError("ModificarUsuarioControlador: Error durante la actualización del usuario.", e);
                    response.sendRedirect("Admin.html?error=Error+al+modificar+usuario");
                }
            }
        } catch (Exception e) {
            FicheroLogVista.logError("ModificarUsuarioControlador: Error en doPost.", e);
            throw new ServletException("Error en doPost de ModificarUsuarioControlador.", e);
        }
    }

    /**
     * Método GET no soportado para la modificación de usuario.
     * <p>
     * Informa que se debe utilizar el método POST para la modificación.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param request  Objeto HttpServletRequest.
     * @param response Objeto HttpServletResponse.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("Método GET no soportado para modificación, use POST.");
    }
}
