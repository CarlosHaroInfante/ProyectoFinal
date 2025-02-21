package edu.Periodico.Prueba.Repositorios;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.Periodico.Prueba.dtos.noticiaDTO;
import edu.Periodico.Prueba.dtos.usuarioDTO;

/*
 * Repositorio que contiene los filtros mediante se busca las noticias.
 * 17/1/2025 - CHI 
 * */
@Repository
public interface repositorioNoticia extends JpaRepository<noticiaDTO, Long>{
	
	ArrayList<noticiaDTO> findAll();
	
	ArrayList<noticiaDTO> findByAutor(usuarioDTO autor);
}