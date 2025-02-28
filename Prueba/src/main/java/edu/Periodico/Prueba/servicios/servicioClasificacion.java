package edu.Periodico.Prueba.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.Periodico.Prueba.Repositorios.repositorioClasificacion;
import edu.Periodico.Prueba.Repositorios.repositorioEquipo;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.clasificacionDTO;
import edu.Periodico.Prueba.dtos.equipoDTO;

/**
 * Servicio que contiene la lógica de los métodos relacionados con la clasificación.
 * 17/1/2025 - CHI
 */
@Service
public class servicioClasificacion {
	
    /**
     * Repositorio para gestionar las operaciones de clasificación en la base de datos.
     */
	@Autowired
	private repositorioClasificacion RepClasificacion;
	
    /**
     * Repositorio para gestionar las operaciones de equipo en la base de datos.
     */
	@Autowired
	private repositorioEquipo RepEquipo;
	
	/**
	 * Constructor con inyección de dependencias.
	 * 
	 * @param RepositorioClasificacion El repositorio de clasificación.
	 * @param RepositorioEquipo El repositorio de equipo.
	 * 28/2/2025 - CHI
	 */
	public servicioClasificacion(repositorioClasificacion RepositorioClasificacion, repositorioEquipo RepositorioEquipo) {
        this.RepClasificacion = RepositorioClasificacion;
        this.RepEquipo = RepositorioEquipo;
    }
	
	/**
	 * Devuelve la clasificación filtrada por temporada.
	 * 
	 * @param temporada La temporada por la que filtrar.
	 * @return Una lista de clasificacionDTO correspondientes a la temporada; 
	 *         si ocurre un error, se devuelve una lista vacía.
	 * 28/2/2025 - CHI
	 */
	public ArrayList<clasificacionDTO> mostrarClasificacionPorTemporada(String temporada) {
	    try {
	        FicheroLog.logInfo("Mostrando clasificación para la temporada: " + temporada);
	        return (ArrayList<clasificacionDTO>) RepClasificacion.findByTemporada(temporada);
	    } catch (Exception e) {
	        FicheroLog.logError("Error al mostrar clasificación para la temporada: " + temporada, e);
	        return new ArrayList<>();
	    }
	}
	
	/**
	 * Devuelve la clasificación para un equipo específico.
	 * 
	 * @param equipo El objeto equipoDTO del que se desea la clasificación.
	 * @return Una lista de clasificacionDTO para el equipo; en caso de error, devuelve una lista vacía.
	 * 28/2/2025 - CHI
	 */
	public ArrayList<clasificacionDTO> mostrarClasificacionPorEquipo(equipoDTO equipo) {
	    try {
	        FicheroLog.logInfo("Mostrando clasificación para el equipo: " + equipo.getIdEquipo());
	        return (ArrayList<clasificacionDTO>) RepClasificacion.findByEquipo(equipo);
	    } catch (Exception e) {
	        FicheroLog.logError("Error al mostrar clasificación para el equipo: " + equipo.getIdEquipo(), e);
	        return new ArrayList<>();
	    }
	}
	
	/**
	 * Guarda una nueva clasificación.
	 * Verifica que el equipo exista y que la temporada esté definida.
	 * 
	 * @param nuevaClasificacion El objeto clasificacionDTO con los datos a guardar.
	 * @return El objeto clasificacionDTO guardado.
	 * @throws RuntimeException Si el equipo no se encuentra o la temporada no está definida.
	 * 28/2/2025 - CHI
	 */
	public clasificacionDTO guardarClasificacion(clasificacionDTO nuevaClasificacion) {
	    try {
	        // Buscar el equipo asociado al miembro de la clasificación.
	        equipoDTO equipo = RepEquipo.findByIdEquipo(nuevaClasificacion.getEquipo().getIdEquipo());
	        
	        if (equipo != null) {
	            nuevaClasificacion.setEquipo(equipo); // Asigna el equipo encontrado.
	            FicheroLog.logInfo("Equipo encontrado para la clasificación: " + equipo.getIdEquipo());
	        } else {
	            FicheroLog.logError("GuardarClasificación: Equipo no encontrado para el ID: " 
	                    + nuevaClasificacion.getEquipo().getIdEquipo(), new Exception("Equipo no encontrado"));
	            throw new RuntimeException("Equipo no encontrado.");
	        }

	        // Verificar que la temporada esté definida.
	        if (nuevaClasificacion.getTemporada() == null || nuevaClasificacion.getTemporada().isEmpty()) {
	            FicheroLog.logError("GuardarClasificación: La temporada es obligatoria.", new Exception("Temporada nula o vacía"));
	            throw new RuntimeException("La temporada es obligatoria.");
	        }
	        
	        clasificacionDTO guardada = RepClasificacion.save(nuevaClasificacion);
	        FicheroLog.logInfo("Clasificación guardada correctamente: " + guardada);
	        return guardada;
	    } catch (Exception e) {
	        FicheroLog.logError("Error al guardar la clasificación", e);
	        throw e; // Propaga la excepción para que el controlador la maneje.
	    }
	}

	/**
	 * Elimina una clasificación por su ID.
	 * 
	 * @param idClasificacion El ID de la clasificación a eliminar.
	 * @return true si se elimina correctamente; false en caso contrario.
	 * 28/2/2025 - CHI
	 */
	public boolean eliminarClasificacion(Long idClasificacion) {
	    try {
	        FicheroLog.logInfo("Eliminando clasificación con ID: " + idClasificacion);
	        if (RepClasificacion.existsById(idClasificacion)) {
	            RepClasificacion.deleteById(idClasificacion);
	            FicheroLog.logInfo("Clasificación eliminada correctamente.");
	            return true;
	        } else {
	            FicheroLog.logInfo("Clasificación no encontrada para el ID: " + idClasificacion);
	            return false;
	        }
	    } catch (Exception e) {
	        FicheroLog.logError("Error al eliminar la clasificación con ID: " + idClasificacion, e);
	        return false;
	    }
	}
	
	/**
	 * Devuelve todas las clasificaciones.
	 * 
	 * @return Una lista con todas las clasificaciones; en caso de error, devuelve una lista vacía.
	 * 28/2/2025 - CHI
	 */
	public ArrayList<clasificacionDTO> obtenerTodasLasClasificaciones() {
	    try {
	        FicheroLog.logInfo("Obteniendo todas las clasificaciones.");
	        return (ArrayList<clasificacionDTO>) RepClasificacion.findAll();
	    } catch (Exception e) {
	        FicheroLog.logError("Error al obtener todas las clasificaciones", e);
	        return new ArrayList<>();
	    }
	}
	
	/**
	 * Modifica una clasificación existente.
	 * Busca la clasificación por ID, actualiza sus campos y verifica la validez del equipo.
	 * 
	 * @param idClasificacion El ID de la clasificación a modificar.
	 * @param clasificacionActualizada El objeto clasificacionDTO con los datos actualizados.
	 * @return La clasificación modificada.
	 * @throws RuntimeException Si la clasificación o el equipo no se encuentran o no son válidos.
	 * 28/2/2025 - CHI
	 */
	public clasificacionDTO modificarClasificacion(Long idClasificacion, clasificacionDTO clasificacionActualizada) {
	    try {
	        FicheroLog.logInfo("Modificando clasificación con ID: " + idClasificacion);
	        clasificacionDTO clasificacion = RepClasificacion.findByIdClasificacion(idClasificacion);
	        
	        if (clasificacion == null) {
	            FicheroLog.logError("ModificarClasificación: Clasificación no encontrada para ID: " + idClasificacion, new Exception("Clasificación no encontrada"));
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
	                FicheroLog.logInfo("Equipo actualizado en la clasificación: " + equipoExistente.getIdEquipo());
	            } else {
	                FicheroLog.logError("ModificarClasificación: El equipo con ID " 
	                        + clasificacionActualizada.getEquipo().getIdEquipo() + " no existe.", new Exception("Equipo no existe"));
	                throw new RuntimeException("El equipo con ID " + clasificacionActualizada.getEquipo().getIdEquipo() + " no existe.");
	            }
	        } else {
	            FicheroLog.logError("ModificarClasificación: Debe proporcionar un equipo válido.", new Exception("Equipo inválido"));
	            throw new RuntimeException("Debe proporcionar un equipo válido.");
	        }
	
	        clasificacionDTO actualizado = RepClasificacion.save(clasificacion);
	        FicheroLog.logInfo("Clasificación modificada correctamente: " + actualizado);
	        return actualizado;
	    } catch (Exception e) {
	        FicheroLog.logError("Error al modificar la clasificación con ID: " + idClasificacion, e);
	        throw e;
	    }
	}
}
