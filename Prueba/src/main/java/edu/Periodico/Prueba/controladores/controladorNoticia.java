package edu.Periodico.Prueba.controladores;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.noticiaDTO;
import edu.Periodico.Prueba.servicios.servicioNoticia;

/**
 * Controlador para gestionar el CRUD de noticias.
 * <p>
 * Este controlador expone endpoints para crear, eliminar, obtener y modificar noticias.
 * </p>
 * 
 * 27/02/2025 - CHI
 */
@RestController
@RequestMapping("/api/noticias")
public class controladorNoticia {

    /**
     * Servicio encargado de la lógica de negocio para noticias.
     * 27/02/2025 - CHI
     */
    @Autowired
    private servicioNoticia servNoticia;

    /**
     * Endpoint para crear una nueva noticia.
     * <p>
     * Crea una noticia a partir del objeto noticiaDTO recibido en el cuerpo de la solicitud y
     * retorna la noticia creada.
     * </p>
     *
     * @param noticia Objeto noticiaDTO con los datos de la noticia a crear.
     * @return ResponseEntity con el objeto noticiaDTO creado y estado HTTP 200, o un error HTTP 500 en caso de fallo.
     * 27/02/2025 - CHI
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearNoticia(@RequestBody noticiaDTO noticia) {
        try {
            FicheroLog.logInfo("Creando noticia: " + noticia);
            noticiaDTO noticiaGuardada = servNoticia.guardarNoticia(noticia);
            FicheroLog.logInfo("Noticia creada exitosamente: " + noticiaGuardada);
            return ResponseEntity.ok(noticiaGuardada);
        } catch (Exception e) {
            FicheroLog.logError("Error al crear la noticia", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error al crear la noticia.\"}");
        }
    }
    
    /**
     * Endpoint para eliminar una noticia por su ID.
     * <p>
     * Elimina la noticia identificada por el ID recibido y retorna un mensaje de éxito o error.
     * </p>
     *
     * @param id El ID de la noticia a eliminar.
     * @return ResponseEntity con un mensaje de éxito (HTTP 200) o error (HTTP 404 o 500).
     * 27/02/2025 - CHI
     */
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<?> eliminarNoticia(@PathVariable Long id) {
        try {
            FicheroLog.logInfo("Intentando eliminar noticia con ID: " + id);
            boolean eliminado = servNoticia.eliminarNoticiaPorId(id);
            if (eliminado) {
                FicheroLog.logInfo("Noticia eliminada exitosamente con ID: " + id);
                return ResponseEntity.ok("Noticia eliminada exitosamente.");
            } else {
                FicheroLog.logInfo("Noticia no encontrada con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Noticia no encontrada con ID: " + id);
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al eliminar noticia con ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno al eliminar la noticia.");
        }
    }
    
    /**
     * Endpoint para mostrar todas las noticias.
     * <p>
     * Retorna una lista de noticias. Si no existen noticias, devuelve un estado HTTP 204 (No Content).
     * </p>
     *
     * @return ResponseEntity con una lista de objetos noticiaDTO y estado HTTP 200,
     *         o HTTP 204 si la lista está vacía, o HTTP 500 en caso de error.
     * 27/02/2025 - CHI
     */
    @GetMapping("/todos")
    public ResponseEntity<?> mostrarNoticias() {
        try {
            FicheroLog.logInfo("Obteniendo todas las noticias");
            List<noticiaDTO> noticias = servNoticia.mostrarNoticias();
            if (noticias.isEmpty()) {
                FicheroLog.logInfo("No se encontraron noticias.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            FicheroLog.logInfo("Noticias obtenidas: " + noticias.size());
            return ResponseEntity.ok(noticias);
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener las noticias", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error al obtener las noticias.\"}");
        }
    }
    
    /**
     * Endpoint para obtener noticias por autor.
     * <p>
     * Retorna las noticias asociadas a un autor identificado por su ID. Si no se encuentran noticias, 
     * se devuelve un estado HTTP 204 (No Content).
     * </p>
     *
     * @param usuarioId El ID del autor.
     * @return ResponseEntity con un ArrayList de noticiaDTO y estado HTTP 200, o HTTP 204 si no hay noticias,
     *         o HTTP 500 en caso de error.
     * 27/02/2025 - CHI
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerNoticiasPorUsuario(@PathVariable Long usuarioId) {
        try {
            FicheroLog.logInfo("Obteniendo noticias para el autor con ID: " + usuarioId);
            ArrayList<noticiaDTO> noticias = servNoticia.obtenerNoticiasPorAutor(usuarioId);
            if (noticias == null || noticias.isEmpty()) {
                FicheroLog.logInfo("No se encontraron noticias para el autor con ID: " + usuarioId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            FicheroLog.logInfo("Noticias obtenidas para el autor con ID " + usuarioId + ": " + noticias.size());
            return ResponseEntity.ok(noticias);
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener noticias para el autor con ID: " + usuarioId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error al obtener las noticias del autor.\"}");
        }
    }
    
    /**
     * Endpoint para modificar una noticia.
     * <p>
     * Actualiza la noticia identificada por el ID con los datos proporcionados. Si la noticia no se encuentra,
     * devuelve un estado HTTP 404.
     * </p>
     *
     * @param id El ID de la noticia a modificar.
     * @param noticiaActualizada Objeto noticiaDTO con los datos actualizados.
     * @return ResponseEntity con la noticia modificada y estado HTTP 200, o HTTP 404/500 en caso de error.
     * 27/02/2025 - CHI
     */
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> modificarNoticia(@PathVariable Long id, @RequestBody noticiaDTO noticiaActualizada) {
        try {
            FicheroLog.logInfo("Modificando noticia con ID: " + id);
            noticiaDTO noticiaModificada = servNoticia.modificarNoticia(id, noticiaActualizada);
            if (noticiaModificada == null) {
                FicheroLog.logInfo("Noticia no encontrada para modificar con ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("{\"error\":\"Noticia no encontrada.\"}");
            }
            FicheroLog.logInfo("Noticia modificada exitosamente: " + noticiaModificada);
            return ResponseEntity.ok(noticiaModificada);
        } catch (Exception e) {
            FicheroLog.logError("Error al modificar la noticia con ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al modificar la noticia.\"}");
        }
    }
}
