package edu.Periodico.Prueba.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.Periodico.Prueba.dtos.plantillaDTO;

/*
 * Repositorio que contiene los filtros mediante se busca la clasificación.
 * 17/1/2025 - CHI 
 * */

@Repository
public interface repositorioPlantilla extends JpaRepository<plantillaDTO, Long> {
	
	List<plantillaDTO> findByEquipo_IdEquipo(long idEquipo);
	
	
}

