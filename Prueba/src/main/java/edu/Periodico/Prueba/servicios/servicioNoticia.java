package edu.Periodico.Prueba.servicios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.Periodico.Prueba.Repositorios.repositorioNoticia;
import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.dtos.noticiaDTO;
import edu.Periodico.Prueba.dtos.usuarioDTO;

/**
 * Servicio para gestionar las operaciones relacionadas con las noticias.
 * <p>
 * Provee métodos para obtener, guardar, eliminar y modificar noticias.
 * </p>
 * 27/02/2025 - CHI
 */
@Service
public class servicioNoticia {

    /**
     * Repositorio para acceder a las noticias en la base de datos.
     */
    @Autowired
    private repositorioNoticia repoNoticia;
    
    /**
     * Repositorio para acceder a los usuarios en la base de datos.
     */
    @Autowired
    private repositorioUsuario RepUsuario;

    /**
     * Obtiene las noticias de un autor en base al ID del usuario.
     * 
     * @param idUsuario El ID del usuario autor de las noticias.
     * @return Un ArrayList de noticiaDTO filtrado por el autor. Si el usuario no existe o ocurre un error, devuelve una lista vacía.
     * 27/02/2025 - CHI
     */
    public ArrayList<noticiaDTO> obtenerNoticiasPorAutor(Long idUsuario) {
        try {
            // Buscar el usuario por su ID.
            usuarioDTO usuario = RepUsuario.findById(idUsuario).orElse(null);
            if (usuario != null) {
                // Usar el objeto usuario para filtrar las noticias.
                return repoNoticia.findByAutor(usuario);
            }
            return new ArrayList<>();
        } catch (Exception e) {
            // Registrar el error y devolver una lista vacía.
            // Se asume que FicheroLog.logError() registra la excepción.
            edu.Periodico.Prueba.Util.FicheroLog.logError("Error en obtenerNoticiasPorAutor para idUsuario: " + idUsuario, e);
            return new ArrayList<>();
        }
    }

    /**
     * Guarda una nueva noticia.
     * <p>
     * Verifica que la noticia tenga un autor válido y asocia el usuario encontrado con la noticia.
     * </p>
     * 
     * @param noticia Objeto noticiaDTO que contiene los datos de la noticia a guardar.
     * @return El objeto noticiaDTO guardado.
     * @throws IllegalArgumentException Si la noticia no tiene un autor válido.
     * 27/02/2025 - CHI
     */
    public noticiaDTO guardarNoticia(noticiaDTO noticia) {
        try {
            if (noticia.getAutor() != null && noticia.getAutor().getIdUsuario() != 0) {
                // Buscar el usuario por idUsuario.
                usuarioDTO autor = RepUsuario.findById(noticia.getAutor().getIdUsuario()).orElse(null);
                if (autor != null) {
                    noticia.setAutor(autor); // Asocia el usuario encontrado con la noticia.
                    return repoNoticia.save(noticia); // Guarda la noticia con el autor.
                } else {
                    throw new IllegalArgumentException("La noticia debe tener un autor.");
                }
            } else {
                throw new IllegalArgumentException("La noticia debe tener un autor.");
            }
        } catch (Exception e) {
            edu.Periodico.Prueba.Util.FicheroLog.logError("Error al guardar la noticia", e);
            throw e;
        }
    }

    /**
     * Elimina una noticia por su ID.
     * 
     * @param idNoticia El ID de la noticia a eliminar.
     * @return true si la noticia se eliminó correctamente; false si no existe la noticia con el ID proporcionado.
     * 27/02/2025 - CHI
     */
    public boolean eliminarNoticiaPorId(Long idNoticia) {
        try {
            if (repoNoticia.existsById(idNoticia)) {
                repoNoticia.deleteById(idNoticia);
                return true;
            }
            return false;
        } catch (Exception e) {
            edu.Periodico.Prueba.Util.FicheroLog.logError("Error al eliminar la noticia con ID: " + idNoticia, e);
            return false;
        }
    }

    /**
     * Muestra todas las noticias.
     * 
     * @return Un ArrayList de noticiaDTO con todas las noticias almacenadas.
     * 27/02/2025 - CHI
     */
    public ArrayList<noticiaDTO> mostrarNoticias() {
        try {
            return repoNoticia.findAll();
        } catch (Exception e) {
            edu.Periodico.Prueba.Util.FicheroLog.logError("Error al mostrar las noticias", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Modifica una noticia existente.
     * <p>
     * Busca la noticia por su ID y, si se encuentra, actualiza sus campos con los datos proporcionados.
     * </p>
     * 
     * @param idNoticia El ID de la noticia a modificar.
     * @param noticiaActualizada Objeto noticiaDTO con los datos actualizados.
     * @return El objeto noticiaDTO modificado, o null si la noticia no se encuentra.
     * 27/02/2025 - CHI
     */
    public noticiaDTO modificarNoticia(Long idNoticia, noticiaDTO noticiaActualizada) {
        try {
            return repoNoticia.findById(idNoticia).map(noticia -> {
                // Actualizar los campos de la noticia.
                noticia.setTitulo(noticiaActualizada.getTitulo());
                noticia.setContenido(noticiaActualizada.getContenido());
                noticia.setFechaPublicacion(noticiaActualizada.getFechaPublicacion());
                noticia.setImagenNoticia(noticiaActualizada.getImagenNoticia());
                noticia.setAutor(noticiaActualizada.getAutor());
                return repoNoticia.save(noticia); // Guardar los cambios en la base de datos.
            }).orElse(null);
        } catch (Exception e) {
            edu.Periodico.Prueba.Util.FicheroLog.logError("Error al modificar la noticia con ID: " + idNoticia, e);
            return null;
        }
    }
}
