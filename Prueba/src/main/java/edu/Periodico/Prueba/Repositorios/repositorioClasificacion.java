package edu.Periodico.Prueba.Repositorios;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.Periodico.Prueba.dtos.clasificacionDTO;
import edu.Periodico.Prueba.dtos.equipoDTO;  // Asegúrate de importar el DTO de equipo

/*
 * Repositorio que contiene los filtros mediante se busca la clasificación.
 * 17/1/2025 - CHI 
 * */
@Repository
public interface repositorioClasificacion extends JpaRepository<clasificacionDTO, Long> {
    
    ArrayList<clasificacionDTO> findByTemporada(String temporada);
    
    // Cambiar para buscar por equipo (entidad completa, no solo el ID)
    ArrayList<clasificacionDTO> findByEquipo(equipoDTO equipo);
    
    clasificacionDTO findByIdClasificacion(Long idClasificacion);

}
