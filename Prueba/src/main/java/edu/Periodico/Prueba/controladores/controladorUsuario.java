package edu.Periodico.Prueba.controladores;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.usuarioDTO;
import edu.Periodico.Prueba.servicios.servicioUsuario;

/**
 * Clase donde se encuentran los endpoints (GET, PUT, DELETE) relacionados con los usuarios.
 * <p>
 * Este controlador gestiona la eliminación, modificación y obtención de usuarios.
 * </p>
 * 27/02/2025 - CHI
 */
@RestController // Define la clase como un controlador REST que retorna respuestas en formato JSON.
@RequestMapping("/api/usuarios") // Ruta base para todos los endpoints de usuarios.
public class controladorUsuario {

    /**
     * Servicio de usuario que contiene la lógica de negocio.
     */
    @Autowired
    servicioUsuario servicio;
    
    /**
     * Endpoint para eliminar un usuario por su ID.
     * <p>
     * Elimina el usuario identificado por el ID recibido y retorna un mensaje de éxito o error.
     * </p>
     * 27/02/2025 - CHI
     * 
     * @param idUsuario El ID del usuario a eliminar.
     * @return ResponseEntity con un mensaje indicando si se eliminó el usuario o no.
     */
    @DeleteMapping("/baja/{idUsuario}")
    public ResponseEntity<String> bajaUsuario(@PathVariable Long idUsuario) {
        try {
            FicheroLog.logInfo("Iniciando eliminación del usuario con ID: " + idUsuario);
            boolean eliminado = servicio.eliminarUsuarioPorId(idUsuario);
            if (eliminado) {
                FicheroLog.logInfo("Usuario eliminado correctamente con ID: " + idUsuario);
                return ResponseEntity.ok("Usuario eliminado correctamente.");
            } else {
                FicheroLog.logInfo("Usuario no encontrado con ID: " + idUsuario);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al eliminar el usuario con ID: " + idUsuario, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Ocurrió un error al eliminar el usuario.");
        }
    }
    
    /**
     * Endpoint para modificar un usuario.
     * <p>
     * Modifica los datos del usuario identificado por el ID recibido con la información proporcionada.
     * </p>
     * 27/02/2025 - CHI
     * 
     * @param idUsuario El ID del usuario a modificar.
     * @param usuarioActualizado Objeto usuarioDTO con los datos actualizados.
     * @return ResponseEntity con el usuario modificado y estado HTTP 200, o null si el usuario no se encuentra.
     */
    @PutMapping("/modificar/{idUsuario}")
    public ResponseEntity<usuarioDTO> modificarUsuario(
            @PathVariable Long idUsuario,
            @RequestBody usuarioDTO usuarioActualizado) {
        try {
            FicheroLog.logInfo("Iniciando modificación del usuario con ID: " + idUsuario);
            usuarioDTO usuarioModificado = servicio.modificarUsuario(idUsuario, usuarioActualizado);
            if (usuarioModificado != null) {
                FicheroLog.logInfo("Usuario modificado correctamente: " + usuarioModificado);
                return ResponseEntity.ok(usuarioModificado);
            } else {
                FicheroLog.logInfo("Usuario no encontrado para modificar con ID: " + idUsuario);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al modificar el usuario con ID: " + idUsuario, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * Endpoint para obtener todos los usuarios.
     * <p>
     * Retorna una lista de usuarios. Si no se encuentran usuarios, se devuelve un estado HTTP 204 (No Content).
     * </p>
     * 27/02/2025 - CHI
     * 
     * @return ResponseEntity con una lista de usuarioDTO y estado HTTP 200, o HTTP 204/500 en caso de error.
     */
    @GetMapping("/todos")
    public ResponseEntity<List<usuarioDTO>> obtenerTodosLosUsuarios() {
        try {
            FicheroLog.logInfo("Obteniendo todos los usuarios.");
            List<usuarioDTO> usuarios = servicio.obtenerTodosLosUsuarios();
            if (!usuarios.isEmpty()) {
                FicheroLog.logInfo("Usuarios obtenidos: " + usuarios.size());
                return ResponseEntity.ok(usuarios);
            } else {
                FicheroLog.logInfo("No se encontraron usuarios.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener todos los usuarios", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
