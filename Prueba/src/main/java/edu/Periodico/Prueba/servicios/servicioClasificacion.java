package edu.Periodico.Prueba.servicios;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioClasificacion;
import edu.Periodico.Prueba.Repositorios.repositorioEquipo;
import edu.Periodico.Prueba.dtos.clasificacionDTO;
import edu.Periodico.Prueba.dtos.equipoDTO;

/*
 * Servicio que contiene la lógica de los métodos relacionados con las clasificación.
 * 17/1/2025 - CHI 
 * */
@Service
public class servicioClasificacion {
	
	/*
	 * Llamada al repositorio de usuario.
	 * 17/1/2025 - CHI 
	 * */
	@Autowired
	private repositorioClasificacion RepClasificacion;
	
	@Autowired
	private repositorioEquipo RepEquipo;
	
	
	public servicioClasificacion(repositorioClasificacion RepositorioClasificacion, repositorioEquipo RepositorioEquipo) {
        this.RepClasificacion = RepositorioClasificacion;
        this.RepEquipo = RepositorioEquipo;
    }
	
	public ArrayList<clasificacionDTO> mostrarClasificacionPorTemporada(String temporada){
		return (ArrayList<clasificacionDTO>) RepClasificacion.findByTemporada(temporada);
	}
	
	public ArrayList<clasificacionDTO> mostrarClasificacionPorEquipo(equipoDTO equipo){
		return (ArrayList<clasificacionDTO>) RepClasificacion.findByEquipo(equipo);
	}
	
	public clasificacionDTO guardarClasificacion(clasificacionDTO nuevaClasificacion) {
	    equipoDTO equipo = RepEquipo.findByIdEquipo(nuevaClasificacion.getEquipo().getIdEquipo());
	    
	    if (equipo != null) {
	        nuevaClasificacion.setEquipo(equipo); // Asignar el equipo encontrado al DTO
	    } else {
	        throw new RuntimeException("Equipo no encontrado.");
	    }

	    // Verificar si todos los campos necesarios están presentes y son válidos
	    if (nuevaClasificacion.getTemporada() == null || nuevaClasificacion.getTemporada().isEmpty()) {
	        throw new RuntimeException("La temporada es obligatoria.");
	    }

	    return RepClasificacion.save(nuevaClasificacion);
	}


	
	public boolean eliminarClasificacion(Long idClasificacion) {
	    if (RepClasificacion.existsById(idClasificacion)) {
	        RepClasificacion.deleteById(idClasificacion);
	        return true;
	    } else {
	        return false;
	    }
	}
	
	public ArrayList<clasificacionDTO> obtenerTodasLasClasificaciones() {
	    return (ArrayList<clasificacionDTO>) RepClasificacion.findAll();
	}
	
	// Modificar clasificación sin Optional
	public clasificacionDTO modificarClasificacion(Long idClasificacion, clasificacionDTO clasificacionActualizada) {
	    clasificacionDTO clasificacion = RepClasificacion.findByIdClasificacion(idClasificacion);
	    
	    if (clasificacion == null) {
	        throw new RuntimeException("Clasificación con ID " + idClasificacion + " no encontrada.");
	    }

	    // Actualizar los campos de la clasificación
	    clasificacion.setTemporada(clasificacionActualizada.getTemporada());
	    clasificacion.setPuesto(clasificacionActualizada.getPuesto());
	    clasificacion.setPuntos(clasificacionActualizada.getPuntos());
	    clasificacion.setGolesAFavor(clasificacionActualizada.getGolesAFavor());
	    clasificacion.setGolesEnContra(clasificacionActualizada.getGolesEnContra());

	    // Verificar y actualizar el equipo si es necesario
	    if (clasificacionActualizada.getEquipo() != null && clasificacionActualizada.getEquipo().getIdEquipo() > 0) {
	        equipoDTO equipoExistente = RepEquipo.findByIdEquipo(clasificacionActualizada.getEquipo().getIdEquipo());

	        if (equipoExistente != null) {
	            clasificacion.setEquipo(equipoExistente);
	        } else {
	            throw new RuntimeException("El equipo con ID " + clasificacionActualizada.getEquipo().getIdEquipo() + " no existe.");
	        }
	    } else {
	        throw new RuntimeException("Debe proporcionar un equipo válido.");
	    }

	    // Guardar la clasificación actualizada
	    return RepClasificacion.save(clasificacion);
	}


    
	
}
