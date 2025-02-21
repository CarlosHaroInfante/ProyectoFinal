package edu.Periodico.Prueba.Repositorios;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.Periodico.Prueba.dtos.usuarioDTO;

/*
 * Repositorio que contiene los filtros mediante se busca el/los usuario/s.
 * 17/1/2025 - CHI 
 * */
@Repository
public interface repositorioUsuario extends JpaRepository<usuarioDTO, Long>{

	
	ArrayList<usuarioDTO> findByIdUsuario(Long idUsuario);
	
	ArrayList<usuarioDTO> findAll();
	
	usuarioDTO findByCorreoUsuario(String correoUsuario);
	
	usuarioDTO findByTokenConfirmacion(String tokenConfirmacion);
	
    long countByCorreoUsuario(String correoUsuario);

}





