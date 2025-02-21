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

import edu.Periodico.Prueba.dtos.clasificacionDTO;
import edu.Periodico.Prueba.dtos.equipoDTO;
import edu.Periodico.Prueba.servicios.servicioClasificacion;
import edu.Periodico.Prueba.servicios.servicioEquipo;

/**
 * Clase donde se encuentran los GET, POST, DELETE o PUT de la aplicación relacionado con la clasificación,
 * 17/01/2025 - CHI
 * **/
@RestController
@RequestMapping("/api/clasificacion")


public class controladorClasificacion {
	
	/*
	 * Es la llamada al servicio de clasificación
	 * 17/1/2025 - CHI 
	 * */
	@Autowired
	servicioClasificacion Servclasificacion;
	
	 @Autowired
	 servicioEquipo ServEquipo;
	
	/*
	 * Controlador que filtra la clasificación por temporada
	 * 17/1/2025 - CHI 
	 * */
	@GetMapping("/temporada/{temporada}")
	public ArrayList<clasificacionDTO> obtenerPorTemporada(@PathVariable String temporada) {
        return this.Servclasificacion.mostrarClasificacionPorTemporada(temporada);
    }
	
	/*
	 * Controlador que filtra la clasificación por equipo
	 * 17/1/2025 - CHI 
	 * */
	@GetMapping("/equipo/{equipoId}")
	public ArrayList<clasificacionDTO> findByEquipoId(@PathVariable Long equipoId) {
	    equipoDTO equipo = ServEquipo.obtenerEquipoPorId(equipoId); // Asegúrate de que este método exista
	    if (equipo == null) {
	        return null;  // O manejar el error de otra forma, como lanzar una excepción
	    }
	    return Servclasificacion.mostrarClasificacionPorEquipo(equipo);
	}
    
    /*
	 * Controlador que crea la clasificación
	 * 17/1/2025 - CHI 
	 * */
   @PostMapping("/crear")
    public clasificacionDTO crearClasificacion(@RequestBody clasificacionDTO nuevaClasificacion) {
        return Servclasificacion.guardarClasificacion(nuevaClasificacion);
    }
    
    /*
	 * Controlador que elimina una clasificación a través de una id;
	 * 17/1/2025 - CHI 
	 * */
    @DeleteMapping("/borrar/{idClasificacion}")
    public String borrarClasificacion(@PathVariable Long idClasificacion) {
        boolean eliminado = Servclasificacion.eliminarClasificacion(idClasificacion);
        if (eliminado) {
            return "La clasificación con ID " + idClasificacion + " ha sido eliminada correctamente.";
        } else {
            return "Error: La clasificación con ID " + idClasificacion + " no fue encontrada.";
        }
    }
    
    /*
	 * Controlador que muestea todas las clasificaciones;
	 * 17/1/2025 - CHI 
	 * */
    @GetMapping("/todas")
    public ArrayList<clasificacionDTO> obtenerTodasLasClasificaciones() {
        return Servclasificacion.obtenerTodasLasClasificaciones();
    }
    
    /*
	 * Controlador que modifica la clasificación por el id;
	 * 17/1/2025 - CHI 
	 * */
    
    @PutMapping("/modificar/{idClasificacion}")
    public ResponseEntity<clasificacionDTO> modificarClasificacion(
            @PathVariable Long idClasificacion,
            @RequestBody clasificacionDTO clasificacionActualizada) {

        try {
            clasificacionDTO clasificacion = Servclasificacion.modificarClasificacion(idClasificacion, clasificacionActualizada);
            return ResponseEntity.ok(clasificacion); // Responder con el objeto actualizado

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Responder con 404 si no se encuentra la clasificación
        }
    }




    
}
