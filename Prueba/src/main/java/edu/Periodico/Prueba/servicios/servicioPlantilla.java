package edu.Periodico.Prueba.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioEquipo;
import edu.Periodico.Prueba.Repositorios.repositorioPlantilla;
import edu.Periodico.Prueba.dtos.equipoDTO;
import edu.Periodico.Prueba.dtos.plantillaDTO;

@Service
public class servicioPlantilla {

    @Autowired
    private repositorioPlantilla repPlantilla;

    @Autowired
    private repositorioEquipo repEquipo; // Inyección del repositorio de equipos

    // Método para crear una nueva entrada en la plantilla
    /*public plantillaDTO crearMiembroPlantilla(plantillaDTO miembroPlantilla) {
        if (miembroPlantilla.getEquipo() == null || miembroPlantilla.getEquipo().getIdEquipo() <= 0) {
            throw new IllegalArgumentException("El equipo no puede ser nulo o inválido");
        }
        return repPlantilla.save(miembroPlantilla);
    }*/
    public plantillaDTO crearMiembroPlantilla(plantillaDTO miembroPlantilla) {
        if (miembroPlantilla.getEquipo() == null || miembroPlantilla.getEquipo().getIdEquipo() <= 0) {
            throw new IllegalArgumentException("El equipo no puede ser nulo o inválido");
        }

        // Buscar el equipo en la base de datos
        equipoDTO equipo = repEquipo.findById(miembroPlantilla.getEquipo().getIdEquipo())
            .orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " + miembroPlantilla.getEquipo().getIdEquipo()));

        // Asignar el equipo real desde la BD antes de guardar
        miembroPlantilla.setEquipo(equipo);

        return repPlantilla.save(miembroPlantilla);
    }


    


    
    public plantillaDTO modificarPlantilla(Long idPlantilla, plantillaDTO nuevaPlantilla) {
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
                        .orElseThrow(() -> new RuntimeException("Equipo no encontrado con id: " + nuevaPlantilla.getEquipo().getIdEquipo()));
                    plantillaExistente.setEquipo(equipo);
                }

                // Guardar los cambios en la base de datos
                return repPlantilla.save(plantillaExistente);
            })
            .orElseThrow(() -> new RuntimeException("No se encontró la plantilla con id: " + idPlantilla));
    }
    
    /*public List<plantillaDTO> obtenerJugadoresPorEquipoId(String equipoId) {
        // Buscar el equipo por ID
        equipoDTO equipo = repEquipo.findById(Long.parseLong(equipoId))
            .orElseThrow(() -> new RuntimeException("No se encontró el equipo con id: " + equipoId));

        // Usar el objeto equipo para buscar los jugadores
        return repPlantilla.findByEquipo_IdEquipo(equipo);
    }
    public List<plantillaDTO> obtenerJugadoresPorEquipoId(long equipoId) {
        // Buscar el equipo por ID
        equipoDTO equipo = repEquipo.findById(equipoId)
            .orElseThrow(() -> new RuntimeException("No se encontró el equipo con id: " + equipoId));

        // Usar el objeto equipo para buscar los jugadores
        return repPlantilla.findByEquipo_IdEquipo(equipo.getIdEquipo());
    }*/
    
    public List<plantillaDTO> obtenerJugadoresPorEquipoId(long equipoId) {
        return repPlantilla.findByEquipo_IdEquipo(equipoId);
    }

    
}
