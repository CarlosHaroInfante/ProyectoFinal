package edu.Periodico.Prueba.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioNoticia;
import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.dtos.noticiaDTO;
import edu.Periodico.Prueba.dtos.usuarioDTO;

@Service
public class servicioNoticia {

    @Autowired
    private repositorioNoticia repoNoticia;
    
    @Autowired
    private repositorioUsuario RepUsuario;

    // Obtener noticias por autor (usuario)
    /*public ArrayList<noticiaDTO> obtenerNoticiasPorAutor(Long idUsuario) {
        // Buscar el usuario por su ID
        usuarioDTO usuario = RepUsuario.findById(idUsuario).orElse(null);
        if (usuario != null) {
            // Usar el objeto usuario para filtrar las noticias
            return repoNoticia.findByAutor(usuario);
        }
        return new ArrayList<>();
    }*/
    public ArrayList<noticiaDTO> obtenerNoticiasPorAutor(Long idUsuario) {
        // Buscar el usuario por su ID
        usuarioDTO usuario = RepUsuario.findById(idUsuario).orElse(null);
        if (usuario != null) {
            // Usar el objeto usuario para filtrar las noticias
            return repoNoticia.findByAutor(usuario);
        }
        
        return new ArrayList<>();
    }


    // Método para guardar una nueva noticia
    public noticiaDTO guardarNoticia(noticiaDTO noticia) {
        if (noticia.getAutor() != null && noticia.getAutor().getIdUsuario() != 0) {
            // Buscar el usuario por idUsuario
            usuarioDTO autor = RepUsuario.findById(noticia.getAutor().getIdUsuario()).orElse(null);
            if (autor != null) {
                noticia.setAutor(autor); // Asocia el usuario encontrado con la noticia
                return repoNoticia.save(noticia); // Guarda la noticia con el autor
            } else {
                throw new IllegalArgumentException("La noticia debe tener un autor.");
            }
        } else {
            throw new IllegalArgumentException("La noticia debe tener un autor.");
        }
    }

    
    // Método para eliminar una noticia por su ID
    public boolean eliminarNoticiaPorId(Long idNoticia) {
        if (repoNoticia.existsById(idNoticia)) {
            repoNoticia.deleteById(idNoticia);
            return true;
        }
        return false; // Devuelve false si no existe la noticia con el ID proporcionado
    }
    
    public ArrayList<noticiaDTO> mostrarNoticias() {
        return repoNoticia.findAll();
    }
    
    
 // Método para modificar una noticia
    public noticiaDTO modificarNoticia(Long idNoticia, noticiaDTO noticiaActualizada) {
        return repoNoticia.findById(idNoticia).map(noticia -> {
            // Actualizar los campos de la noticia
            noticia.setTitulo(noticiaActualizada.getTitulo());
            noticia.setContenido(noticiaActualizada.getContenido());
            noticia.setFechaPublicacion(noticiaActualizada.getFechaPublicacion());
            noticia.setImagenNoticia(noticiaActualizada.getImagenNoticia());
            noticia.setAutor(noticiaActualizada.getAutor());
            return repoNoticia.save(noticia); // Guardar los cambios en la base de datos
        }).orElse(null); // Si no se encuentra la noticia, devolver null
    }
    
}

