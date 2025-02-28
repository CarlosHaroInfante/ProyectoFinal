package edu.Periodico.Prueba.servicios;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.Periodico.Prueba.Repositorios.repositorioEquipo;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.equipoDTO;

/*
 * Servicio que contiene la lógica de los métodos relacionados con el equipo.
 * 17/1/2025 - CHI 
 */
@Service // Indica que esta clase es un servicio de Spring
public class servicioEquipo {

    @Autowired // Inyecta el repositorio de equipos
    private repositorioEquipo RepEquipo;
    
    /**
     * Obtiene un equipo por su ID.
     * @param equipoId El ID del equipo.
     * @return El objeto equipoDTO si se encuentra, o null en caso de error.
     * 28/2/2025 - CHI
     */
    public equipoDTO obtenerEquipoPorId(Long equipoId) {
        try {
            FicheroLog.logInfo("Obteniendo equipo con ID: " + equipoId);
            return RepEquipo.findById(equipoId).orElse(null);
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener equipo con ID: " + equipoId, e);
            return null;
        }
    }

    /**
     * Crea un nuevo equipo.
     * @param equipo El objeto equipoDTO a crear.
     * @return El objeto equipoDTO guardado.
     * 28/2/2025 - CHI
     */
    public equipoDTO crearEquipo(equipoDTO equipo) {
        try {
            FicheroLog.logInfo("Creando equipo: " + equipo);
            equipoDTO equipoGuardado = RepEquipo.save(equipo);
            FicheroLog.logInfo("Equipo creado exitosamente: " + equipoGuardado);
            return equipoGuardado;
        } catch (Exception e) {
            FicheroLog.logError("Error al crear equipo: " + equipo, e);
            throw new RuntimeException("Error al crear equipo.", e);
        }
    }
    
    /**
     * Elimina un equipo por su ID.
     * @param idEquipo El ID del equipo a eliminar.
     * @return true si se elimina correctamente, false en caso contrario.
     * 28/2/2025 - CHI
     */
    public boolean eliminarEquipoPorId(Long idEquipo) {
        try {
            FicheroLog.logInfo("Intentando eliminar equipo con ID: " + idEquipo);
            if (RepEquipo.findById(idEquipo).isPresent()) {
                RepEquipo.deleteById(idEquipo);
                FicheroLog.logInfo("Equipo eliminado correctamente con ID: " + idEquipo);
                return true;
            } else {
                FicheroLog.logInfo("Equipo no encontrado con ID: " + idEquipo);
                return false;
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al eliminar equipo con ID: " + idEquipo, e);
            throw new RuntimeException("Error al intentar eliminar el equipo: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtiene todos los equipos.
     * @return Una lista de equipos (ArrayList de equipoDTO).
     * 28/2/2025 - CHI
     */
    public ArrayList<equipoDTO> obtenerTodosLosEquipos() {
        try {
            FicheroLog.logInfo("Obteniendo todos los equipos.");
            return RepEquipo.findAll();
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener todos los equipos", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Modifica un equipo existente.
     * @param idEquipo El ID del equipo a modificar.
     * @param equipoActualizado El objeto equipoDTO con los datos actualizados.
     * @return El objeto equipoDTO modificado.
     * @throws RuntimeException si el equipo no existe o ocurre algún error.
     * 28/2/2025 - CHI
     */
    public equipoDTO modificarEquipo(Long idEquipo, equipoDTO equipoActualizado) {
        try {
            FicheroLog.logInfo("Modificando equipo con ID: " + idEquipo);
            // Buscar el equipo y, si existe, actualizar sus campos
            equipoDTO equipoModificado = RepEquipo.findById(idEquipo).map(equipoExistente -> {
                equipoExistente.setNombreEquipo(equipoActualizado.getNombreEquipo());
                equipoExistente.setImagenEquipo(equipoActualizado.getImagenEquipo());
                equipoExistente.setHistoria(equipoActualizado.getHistoria());
                return RepEquipo.save(equipoExistente);
            }).orElseThrow(() -> new RuntimeException("El equipo con ID " + idEquipo + " no existe."));
            
            FicheroLog.logInfo("Equipo modificado correctamente: " + equipoModificado);
            return equipoModificado;
        } catch (Exception e) {
            FicheroLog.logError("Error al modificar el equipo con ID: " + idEquipo, e);
            throw new RuntimeException("Error al modificar el equipo.", e);
        }
    }
}
