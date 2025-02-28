package edu.Periodico.Prueba.controladores;

import java.util.ArrayList;
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
import edu.Periodico.Prueba.dtos.clasificacionDTO;
import edu.Periodico.Prueba.dtos.equipoDTO;
import edu.Periodico.Prueba.servicios.servicioClasificacion;
import edu.Periodico.Prueba.servicios.servicioEquipo;

/**
 * Controlador de la aplicación que contiene los endpoints (GET, POST, PUT y DELETE)
 * relacionados con la clasificación.
 * <p>
 * Rutas principales:
 * <ul>
 *   <li>GET /api/clasificacion/temporada/{temporada} - Obtener clasificación por temporada.</li>
 *   <li>GET /api/clasificacion/equipo/{equipoId} - Obtener clasificación por equipo.</li>
 *   <li>POST /api/clasificacion/crear - Crear una nueva clasificación.</li>
 *   <li>DELETE /api/clasificacion/borrar/{idClasificacion} - Eliminar una clasificación por ID.</li>
 *   <li>GET /api/clasificacion/todas - Obtener todas las clasificaciones.</li>
 *   <li>PUT /api/clasificacion/modificar/{idClasificacion} - Modificar una clasificación por ID.</li>
 * </ul>
 * </p>
 * 28/2/2025 - CHI
 */
@RestController
@RequestMapping("/api/clasificacion")
public class controladorClasificacion {
	
    @Autowired // Inyecta automáticamente el bean de servicioClasificación.
    private servicioClasificacion Servclasificacion;
	
    @Autowired // Inyecta automáticamente el bean de servicioEquipo.
    private servicioEquipo ServEquipo;
	
    /**
     * Obtiene la clasificación filtrada por temporada.
     * 
     * @param temporada La temporada para la que se desea la clasificación.
     * @return Un ArrayList de clasificacionDTO con los datos de la clasificación; si ocurre un error, se devuelve una lista vacía.
     * 28/2/2025 - CHI
     */
    @GetMapping("/temporada/{temporada}")
    public ArrayList<clasificacionDTO> obtenerPorTemporada(@PathVariable String temporada) {
        try {
            // Registrar inicio de la consulta para la temporada.
            FicheroLog.logInfo("Obteniendo clasificación para la temporada: " + temporada);
            ArrayList<clasificacionDTO> clasificaciones = this.Servclasificacion.mostrarClasificacionPorTemporada(temporada);
            FicheroLog.logInfo("Clasificación obtenida correctamente para la temporada: " + temporada);
            return clasificaciones;
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener la clasificación para la temporada: " + temporada, e);
            return new ArrayList<>();
        }
    }
	
    /**
     * Obtiene la clasificación para un equipo específico.
     * 
     * @param equipoId El ID del equipo.
     * @return Un ArrayList de clasificacionDTO con la clasificación del equipo; 
     *         si no se encuentra el equipo, se devuelve null; en caso de error, una lista vacía.
     * 28/2/2025 - CHI
     */
    @GetMapping("/equipo/{equipoId}")
    public ArrayList<clasificacionDTO> findByEquipoId(@PathVariable Long equipoId) {
        try {
            // Registrar inicio del proceso para obtener la clasificación de un equipo.
            FicheroLog.logInfo("Obteniendo clasificación por el equipo con ID: " + equipoId);
            equipoDTO equipo = ServEquipo.obtenerEquipoPorId(equipoId);
            if (equipo == null) {
                FicheroLog.logInfo("No se encontró equipo con el ID: " + equipoId);
                return null;
            }
            ArrayList<clasificacionDTO> clasificacion = Servclasificacion.mostrarClasificacionPorEquipo(equipo);
            FicheroLog.logInfo("Clasificación obtenida para el equipo con ID: " + equipoId);
            return clasificacion;
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener la clasificación para el equipo con ID: " + equipoId, e);
            return new ArrayList<>();
        }
    }
	
    /**
     * Crea una nueva clasificación.
     * <p>
     * Se verifica que el equipo exista y que la temporada esté definida.
     * </p>
     * 
     * @param nuevaClasificacion El objeto clasificacionDTO que contiene los datos de la nueva clasificación.
     * @return Un ResponseEntity con el objeto clasificacionDTO guardado y un estado HTTP 200 en caso de éxito,
     *         o un error HTTP 500 y mensaje en caso de fallo.
     * 28/2/2025 - CHI
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearClasificacion(@RequestBody clasificacionDTO nuevaClasificacion) {
        try {
            FicheroLog.logInfo("Creando clasificación: " + nuevaClasificacion);
            clasificacionDTO clasificacionGuardada = Servclasificacion.guardarClasificacion(nuevaClasificacion);
            FicheroLog.logInfo("Clasificación creada: " + clasificacionGuardada);
            return ResponseEntity.ok(clasificacionGuardada);
        } catch (Exception e) {
            FicheroLog.logError("Error al crear clasificación", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body("{\"error\":\"Error al crear la clasificación.\"}");
        }
    }
	
    /**
     * Elimina una clasificación por su ID.
     * 
     * @param idClasificacion El ID de la clasificación a eliminar.
     * @return Un ResponseEntity con un mensaje de éxito (HTTP 200) si se elimina correctamente,
     *         o un mensaje de error (HTTP 400 o 500) si ocurre un problema.
     * 28/2/2025 - CHI
     */
    @DeleteMapping("/borrar/{idClasificacion}")
    public ResponseEntity<?> borrarClasificacion(@PathVariable Long idClasificacion) {
        try {
            FicheroLog.logInfo("Intentando borrar clasificación con ID: " + idClasificacion);
            boolean eliminado = Servclasificacion.eliminarClasificacion(idClasificacion);
            if (eliminado) {
                FicheroLog.logInfo("Clasificación eliminada correctamente con ID: " + idClasificacion);
                return ResponseEntity.ok("La clasificación con ID " + idClasificacion + " ha sido eliminada correctamente.");
            } else {
                FicheroLog.logInfo("No se encontró clasificación con ID: " + idClasificacion);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("Error: La clasificación con ID " + idClasificacion + " no fue encontrada.");
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al borrar clasificación con ID: " + idClasificacion, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno al borrar la clasificación.");
        }
    }
	
    /**
     * Obtiene todas las clasificaciones.
     * 
     * @return Un ResponseEntity con un ArrayList de clasificacionDTO y estado HTTP 200 si hay datos,
     *         o HTTP 500 en caso de error.
     * 28/2/2025 - CHI
     */
    @GetMapping("/todas")
    public ResponseEntity<?> obtenerTodasLasClasificaciones() {
        try {
            FicheroLog.logInfo("Obteniendo todas las clasificaciones");
            ArrayList<clasificacionDTO> clasificaciones = Servclasificacion.obtenerTodasLasClasificaciones();
            FicheroLog.logInfo("Clasificaciones obtenidas: " + (clasificaciones != null ? clasificaciones.size() : 0));
            return ResponseEntity.ok(clasificaciones);
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener todas las clasificaciones", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body("{\"error\":\"Error al obtener las clasificaciones.\"}");
        }
    }
	
    /**
     * Modifica una clasificación existente.
     * <p>
     * Busca la clasificación por su ID, actualiza sus campos y verifica que se proporcione un equipo válido.
     * </p>
     * 
     * @param idClasificacion El ID de la clasificación a modificar.
     * @param clasificacionActualizada El objeto clasificacionDTO con los datos actualizados.
     * @return Un ResponseEntity con la clasificación modificada y estado HTTP 200 en caso de éxito, 
     *         o HTTP 404 si no se encuentra la clasificación.
     * 28/2/2025 - CHI
     */
    @PutMapping("/modificar/{idClasificacion}")
    public ResponseEntity<clasificacionDTO> modificarClasificacion(
            @PathVariable Long idClasificacion,
            @RequestBody clasificacionDTO clasificacionActualizada
    ) {
        try {
            FicheroLog.logInfo("Modificando clasificación con ID: " + idClasificacion);
            clasificacionDTO clasificacion = Servclasificacion.modificarClasificacion(idClasificacion, clasificacionActualizada);
            FicheroLog.logInfo("Clasificación modificada correctamente: " + clasificacion);
            return ResponseEntity.ok(clasificacion);
        } catch (RuntimeException e) {
            FicheroLog.logError("Error al modificar la clasificación con ID: " + idClasificacion, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
