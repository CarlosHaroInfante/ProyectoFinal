package edu.Periodico.Prueba.controladores;

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
import edu.Periodico.Prueba.dtos.equipoDTO;
import edu.Periodico.Prueba.servicios.servicioEquipo;

/**
 * Controlador para gestionar equipos.
 * <p>
 * Este controlador expone endpoints para crear, eliminar, obtener y modificar equipos.
 * </p>
 * 
 * 27/02/2025 - CHI
 */
@RestController
@RequestMapping("/api/equipos")
public class controladorEquipo {

    /**
     * Servicio encargado de la lógica de negocio para equipos.
     * 27/02/2025 - CHI
     */
    @Autowired
    private servicioEquipo servEquipo;

    /**
     * Endpoint para crear un equipo.
     * <p>
     * Crea un nuevo equipo y retorna el objeto creado con el estado HTTP 201 (CREATED).
     * </p>
     *
     * @param equipo Objeto equipoDTO a crear.
     * @return ResponseEntity con el equipo creado o, en caso de error, con estado HTTP 500.
     * 27/02/2025 - CHI
     */
    @PostMapping("/alta")
    public ResponseEntity<equipoDTO> crearEquipo(@RequestBody equipoDTO equipo) {
        try {
            FicheroLog.logInfo("Creando equipo: " + equipo);
            equipoDTO nuevoEquipo = servEquipo.crearEquipo(equipo);
            FicheroLog.logInfo("Equipo creado exitosamente: " + nuevoEquipo);
            return new ResponseEntity<>(nuevoEquipo, HttpStatus.CREATED);
        } catch (Exception e) {
            FicheroLog.logError("Error al crear equipo", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Endpoint para eliminar un equipo por ID.
     * <p>
     * Elimina un equipo según su ID y retorna un mensaje de éxito (HTTP 200) o de error (HTTP 404/500).
     * </p>
     *
     * @param idEquipo ID del equipo a eliminar.
     * @return ResponseEntity con un mensaje indicando el resultado de la operación.
     * 27/02/2025 - CHI
     */
    @DeleteMapping("/baja/{idEquipo}")
    public ResponseEntity<String> bajaEquipo(@PathVariable Long idEquipo) {
        try {
            FicheroLog.logInfo("Intentando eliminar equipo con ID: " + idEquipo);
            boolean eliminado = servEquipo.eliminarEquipoPorId(idEquipo);
            if (eliminado) {
                FicheroLog.logInfo("Equipo eliminado correctamente con ID: " + idEquipo);
                return ResponseEntity.ok("Equipo eliminado correctamente.");
            } else {
                FicheroLog.logInfo("Equipo no encontrado para ID: " + idEquipo);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equipo no encontrado.");
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al eliminar equipo con ID: " + idEquipo, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno al eliminar el equipo.");
        }
    }
    
    /**
     * Endpoint para obtener todos los equipos.
     * <p>
     * Retorna una lista de todos los equipos. Si la lista está vacía, se devuelve un estado HTTP 204 (NO CONTENT).
     * </p>
     *
     * @return ResponseEntity con la lista de equipos y estado HTTP 200, o HTTP 204 si no hay equipos,
     *         o HTTP 500 en caso de error.
     * 27/02/2025 - CHI        
     */
    @GetMapping("/todos")
    public ResponseEntity<List<equipoDTO>> obtenerTodosLosEquipos() {
        try {
            FicheroLog.logInfo("Obteniendo todos los equipos");
            List<equipoDTO> equipos = servEquipo.obtenerTodosLosEquipos();
            if (equipos.isEmpty()) {
                FicheroLog.logInfo("No se encontraron equipos.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            FicheroLog.logInfo("Equipos obtenidos: " + equipos.size());
            return ResponseEntity.ok(equipos);
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener equipos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    /**
     * Endpoint para modificar un equipo por ID.
     * <p>
     * Modifica los datos de un equipo existente y retorna el equipo modificado. 
     * En caso de error, retorna un estado HTTP 404 o 500.
     * </p>
     *
     * @param idEquipo         ID del equipo a modificar.
     * @param equipoActualizado Objeto equipoDTO con los datos actualizados.
     * @return ResponseEntity con el equipo modificado y estado HTTP 200, o con estado HTTP 404/500 en caso de error.
     * 27/02/2025 - CHI
     */
    @PutMapping("/modificar/{idEquipo}")
    public ResponseEntity<equipoDTO> modificarEquipo(@PathVariable Long idEquipo, @RequestBody equipoDTO equipoActualizado) {
        try {
            FicheroLog.logInfo("Modificando equipo con ID: " + idEquipo);
            equipoDTO equipoModificado = servEquipo.modificarEquipo(idEquipo, equipoActualizado);
            FicheroLog.logInfo("Equipo modificado correctamente: " + equipoModificado);
            return ResponseEntity.ok(equipoModificado);
        } catch (RuntimeException e) {
            FicheroLog.logError("Error al modificar el equipo con ID: " + idEquipo, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            FicheroLog.logError("Error inesperado al modificar el equipo con ID: " + idEquipo, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
