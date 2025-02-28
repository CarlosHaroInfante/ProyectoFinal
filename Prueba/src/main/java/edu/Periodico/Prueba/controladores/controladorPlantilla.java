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
import edu.Periodico.Prueba.dtos.plantillaDTO;
import edu.Periodico.Prueba.servicios.servicioPlantilla;

/**
 * Controlador para gestionar el CRUD de la plantilla (miembros de equipo).
 * <p>
 * Este controlador expone endpoints para crear, modificar y obtener jugadores asociados a un equipo.
 * </p>
 * 27/02/2025 - CHI
 */
@RestController // Indica que la clase es un controlador REST y devuelve respuestas en JSON.
@RequestMapping("/api/plantilla") // Define la ruta base para todos los endpoints de este controlador.
public class controladorPlantilla {

    /**
     * Servicio que maneja la lógica de plantilla.
     */
    @Autowired
    private servicioPlantilla servPlantilla;

    /**
     * Endpoint para crear un miembro de la plantilla.
     * <p>
     * Recibe un objeto plantillaDTO en el cuerpo de la solicitud, valida que el equipo no sea nulo
     * y tenga un ID válido, y luego crea el miembro de la plantilla.
     * </p>
     * 
     * @param miembroPlantilla Objeto plantillaDTO con los datos del miembro a crear.
     * @return ResponseEntity con el objeto plantillaDTO creado y estado HTTP 201, o un mensaje de error.
     * 27/02/2025 - CHI
     */
    @PostMapping("/alta")
    public ResponseEntity<?> altaMiembroPlantilla(@RequestBody plantillaDTO miembroPlantilla) {
        try {
            // Registrar la recepción del JSON
            FicheroLog.logInfo("Alta plantilla: JSON recibido: " + miembroPlantilla);
            
            // Validar que el equipo no sea nulo o tenga un ID inválido
            if (miembroPlantilla.getEquipo() == null || miembroPlantilla.getEquipo().getIdEquipo() <= 0) {
                FicheroLog.logInfo("Alta plantilla: El equipo es nulo o inválido para: " + miembroPlantilla);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El equipo no puede ser nulo o inválido");
            }
            
            // Crear el miembro de la plantilla usando el servicio
            plantillaDTO nuevoMiembro = servPlantilla.crearMiembroPlantilla(miembroPlantilla);
            FicheroLog.logInfo("Alta plantilla: Miembro creado exitosamente: " + nuevoMiembro);
            return new ResponseEntity<>(nuevoMiembro, HttpStatus.CREATED);
        } catch (Exception e) {
            // Registrar el error y devolver un error interno
            FicheroLog.logError("Alta plantilla: Error al crear miembro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al crear el miembro de plantilla.\"}");
        }
    }
    
    /**
     * Endpoint para modificar un miembro de la plantilla.
     * <p>
     * Recibe el ID del miembro de la plantilla en la URL y un objeto plantillaDTO con los datos actualizados.
     * Devuelve el miembro modificado o un error en caso de fallo.
     * </p>
     * 
     * @param idPlantilla      ID del miembro de la plantilla a modificar.
     * @param plantilla        Objeto plantillaDTO con los datos actualizados.
     * @return ResponseEntity con el miembro modificado y estado HTTP 200, o un error HTTP 500.
     * 27/02/2025 - CHI
     */
    @PutMapping("/modificar/{idPlantilla}")
    public ResponseEntity<?> modificarPlantilla(@PathVariable Long idPlantilla, @RequestBody plantillaDTO plantilla) {
        try {
            FicheroLog.logInfo("Modificar plantilla: Modificando miembro con ID: " + idPlantilla);
            plantillaDTO plantillaActualizada = servPlantilla.modificarPlantilla(idPlantilla, plantilla);
            FicheroLog.logInfo("Modificar plantilla: Miembro modificado: " + plantillaActualizada);
            return ResponseEntity.ok(plantillaActualizada);
        } catch (Exception e) {
            FicheroLog.logError("Modificar plantilla: Error al modificar el miembro con ID: " + idPlantilla, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al modificar el miembro de plantilla.\"}");
        }
    }
    
    /**
     * Endpoint para obtener los jugadores asociados a un equipo.
     * <p>
     * Recibe el ID del equipo en la URL y retorna una lista de miembros de la plantilla asociados al equipo.
     * Si no se encuentran jugadores, se devuelve un error HTTP 404.
     * </p>
     * 
     * @param equipoId ID del equipo del que se desean obtener los jugadores.
     * @return ResponseEntity con un ArrayList de plantillaDTO y estado HTTP 200, o HTTP 404/500 en caso de error.
     * 27/02/2025 - CHI
     */
    @GetMapping("/jugadores/{equipoId}")
    public ResponseEntity<?> obtenerJugadores(@PathVariable long equipoId) {
        try {
            FicheroLog.logInfo("Obtener jugadores: Solicitando jugadores para el equipo con ID: " + equipoId);
            List<plantillaDTO> jugadores = servPlantilla.obtenerJugadoresPorEquipoId(equipoId);
            if (jugadores == null || jugadores.isEmpty()) {
                FicheroLog.logInfo("Obtener jugadores: No se encontraron jugadores para el equipo con ID: " + equipoId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\":\"No se encontraron jugadores para el equipo.\"}");
            }
            FicheroLog.logInfo("Obtener jugadores: Se obtuvieron " + jugadores.size() + " jugadores para el equipo con ID: " + equipoId);
            return ResponseEntity.ok(jugadores);
        } catch (Exception e) {
            FicheroLog.logError("Obtener jugadores: Error al obtener jugadores para el equipo con ID: " + equipoId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"error\":\"Error interno al obtener jugadores.\"}");
        }
    }
}
