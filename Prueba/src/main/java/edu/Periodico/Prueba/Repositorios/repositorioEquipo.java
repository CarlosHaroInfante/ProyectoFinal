package edu.Periodico.Prueba.Repositorios;
/*
 * Repositorio que contiene los filtros mediante se busca el/los usuario/s.
 * 17/1/2025 - CHI 
 * */

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.Periodico.Prueba.dtos.equipoDTO;

/*
 * Repositorio que contiene los filtros mediante se busca el equipo.
 * 17/1/2025 - CHI 
 * */
@Repository
public interface repositorioEquipo extends JpaRepository<equipoDTO, Long>{
	
	//ArrayList<equipoDTO> findByIdEquipo(Long idEquipo);
	
	ArrayList<equipoDTO> findAll();
	
	equipoDTO findByIdEquipo(Long idEquipo);

}
