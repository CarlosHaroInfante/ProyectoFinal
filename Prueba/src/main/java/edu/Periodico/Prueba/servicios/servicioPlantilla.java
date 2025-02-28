package edu.Periodico.Prueba.servicios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.Periodico.Prueba.Repositorios.repositorioEquipo;
import edu.Periodico.Prueba.Repositorios.repositorioPlantilla;
import edu.Periodico.Prueba.dtos.equipoDTO;
import edu.Periodico.Prueba.dtos.plantillaDTO;
import edu.Periodico.Prueba.Util.FicheroLog;

/**
 * Servicio para gestionar las operaciones relacionadas con la plantilla (miembros de equipo).
 * <p>
 * Provee métodos para crear un miembro de la plantilla, modificarlo y obtener los jugadores asociados a un equipo.
 * </p>
 * 27/02/2025 - CHI
 */
@Service
public class servicioPlantilla {

    /**
     * Repositorio para gestionar las operaciones de plantilla en la base de datos.
     */
    @Autowired
    private repositorioPlantilla repPlantilla;

    /**
     * Repositorio para gestionar las operaciones de equipos en la base de datos.
     */
    @Autowired
    private repositorioEquipo repEquipo; // Inyección del repositorio de equipos

    /**
     * Crea un nuevo miembro de la plantilla.
     * <p>
     * Verifica que el objeto miembroPlantilla tenga un equipo válido y busca el equipo en la base de datos.
     * Si se encuentra, asigna el equipo real al objeto y lo guarda.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param miembroPlantilla Objeto plantillaDTO que contiene los datos del miembro a crear.
     * @return El objeto plantillaDTO guardado en la base de datos.
     * @throws IllegalArgumentException Si el equipo es nulo o inválido, o si no se encuentra el equipo.
     */
    public plantillaDTO crearMiembroPlantilla(plantillaDTO miembroPlantilla) {
        try {
            if (miembroPlantilla.getEquipo() == null || miembroPlantilla.getEquipo().getIdEquipo() <= 0) {
                throw new IllegalArgumentException("El equipo no puede ser nulo o inválido");
            }
            // Buscar el equipo en la base de datos
            equipoDTO equipo = repEquipo.findById(miembroPlantilla.getEquipo().getIdEquipo())
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " 
                        + miembroPlantilla.getEquipo().getIdEquipo()));
            // Asignar el equipo real desde la BD antes de guardar
            miembroPlantilla.setEquipo(equipo);
            return repPlantilla.save(miembroPlantilla);
        } catch (Exception e) {
            FicheroLog.logError("Error al crear miembro de plantilla", e);
            throw e;
        }
    }

    /**
     * Modifica un miembro de la plantilla existente.
     * <p>
     * Busca la plantilla por el ID proporcionado, actualiza sus campos básicos y, si se proporciona un nuevo equipo,
     * lo valida y actualiza. Finalmente, guarda los cambios en la base de datos.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param idPlantilla   El ID del miembro de la plantilla a modificar.
     * @param nuevaPlantilla Objeto plantillaDTO con los datos actualizados.
     * @return El objeto plantillaDTO modificado.
     * @throws RuntimeException Si no se encuentra la plantilla o el equipo proporcionado es inválido.
     */
    public plantillaDTO modificarPlantilla(Long idPlantilla, plantillaDTO nuevaPlantilla) {
        try {
            return repPlantilla.findById(idPlantilla)
                .map(plantillaExistente -> {
                    // Actualizar campos básicos de la plantilla
                    plantillaExistente.setNombreCompleto(nuevaPlantilla.getNombreCompleto());
                    plantillaExistente.setPosicion(nuevaPlantilla.getPosicion());
                    plantillaExistente.setFechaNacimiento(nuevaPlantilla.getFechaNacimiento());
                    plantillaExistente.setDorsal(nuevaPlantilla.getDorsal());

                    // Cargar y asignar el equipo relacionado si se proporciona
                    if (nuevaPlantilla.getEquipo() != null && nuevaPlantilla.getEquipo().getIdEquipo() > 0) {
                        equipoDTO equipo = repEquipo.findById(nuevaPlantilla.getEquipo().getIdEquipo())
                            .orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " 
                                    + nuevaPlantilla.getEquipo().getIdEquipo()));
                        plantillaExistente.setEquipo(equipo);
                    }
                    // Guardar los cambios en la base de datos
                    return repPlantilla.save(plantillaExistente);
                })
                .orElseThrow(() -> new RuntimeException("No se encontró la plantilla con id: " + idPlantilla));
        } catch (Exception e) {
            FicheroLog.logError("Error al modificar la plantilla con id: " + idPlantilla, e);
            throw e;
        }
    }

    /**
     * Obtiene los jugadores asociados a un equipo.
     * <p>
     * Retorna una lista de objetos plantillaDTO que pertenecen al equipo identificado por el ID proporcionado.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param equipoId El ID del equipo.
     * @return Una lista de plantillaDTO correspondientes a los jugadores del equipo.
     * @throws RuntimeException Si ocurre un error durante la consulta.
     */
    public List<plantillaDTO> obtenerJugadoresPorEquipoId(long equipoId) {
        try {
            return repPlantilla.findByEquipo_IdEquipo(equipoId);
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener jugadores para el equipo con id: " + equipoId, e);
            throw e;
        }
    }
}
