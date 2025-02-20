package edu.Periodico.Prueba.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioEquipo;
import edu.Periodico.Prueba.dtos.equipoDTO;

@Service
public class servicioEquipo {

	@Autowired
    private repositorioEquipo RepEquipo;
	
	
	
	public equipoDTO obtenerEquipoPorId(Long equipoId) {
	   return RepEquipo.findById(equipoId).orElse(null);
	}

    public equipoDTO crearEquipo(equipoDTO equipo) {
        return RepEquipo.save(equipo);
    }
    
    public boolean eliminarEquipoPorId(Long idEquipo) {
        try {
            if (RepEquipo.findById(idEquipo).isPresent()) {
                RepEquipo.deleteById(idEquipo);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al intentar eliminar el equipo: " + e.getMessage(), e);
        }
    }
    
    
    public ArrayList<equipoDTO> obtenerTodosLosEquipos() {
        return RepEquipo.findAll();
    }
    
    public equipoDTO modificarEquipo(Long idEquipo, equipoDTO equipoActualizado) {
        return RepEquipo.findById(idEquipo).map(equipoExistente -> {
            equipoExistente.setNombreEquipo(equipoActualizado.getNombreEquipo());
            equipoExistente.setImagenEquipo(equipoActualizado.getImagenEquipo());
            equipoExistente.setHistoria(equipoActualizado.getHistoria());
            return RepEquipo.save(equipoExistente);
        }).orElseThrow(() -> new RuntimeException("El equipo con ID " + idEquipo + " no existe."));
    }

}
