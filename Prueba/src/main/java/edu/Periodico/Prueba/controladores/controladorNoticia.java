package edu.Periodico.Prueba.controladores;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.Periodico.Prueba.dtos.noticiaDTO;
import edu.Periodico.Prueba.servicios.servicioNoticia;

@RestController
@RequestMapping("/api/noticias")
public class controladorNoticia {

	 @Autowired
	    private servicioNoticia servNoticia;

	    // Endpoint para crear una nueva noticia
	    @PostMapping("/crear")
	    public ResponseEntity<noticiaDTO> crearNoticia(@RequestBody noticiaDTO noticia) {
	        try {
	            noticiaDTO noticiaGuardada = servNoticia.guardarNoticia(noticia);
	            return ResponseEntity.ok(noticiaGuardada);
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(null);
	        }
	    }
	    
	    @DeleteMapping("/borrar/{id}")
	    public ResponseEntity<String> eliminarNoticia(@PathVariable Long id) {
	        boolean eliminado = servNoticia.eliminarNoticiaPorId(id);
	        if (eliminado) {
	            return ResponseEntity.ok("Noticia eliminada exitosamente.");
	        } else {
	            return ResponseEntity.status(404).body("Noticia no encontrada con ID: " + id);
	        }
	    }
	    
	    /*
		 * Metodo que muestra todos los usuarios en la base de datos.
		 * 17/1/2025 - CHI 
		 * */
	    @GetMapping("/todos")
	    public ResponseEntity<List<noticiaDTO>> mostrarNoticias() {
	        List<noticiaDTO> usuarios = servNoticia.mostrarNoticias();

	        if (!usuarios.isEmpty()) {
	            return ResponseEntity.ok(usuarios);
	        } else {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); // Si no hay usuarios, devuelve un 204
	        }
	    }
	    
	    
	 // Endpoint para obtener noticias por autor
	    @GetMapping("/usuario/{usuarioId}")
	    public ArrayList<noticiaDTO> obtenerNoticiasPorUsuario(@PathVariable Long usuarioId) {
	        return servNoticia.obtenerNoticiasPorAutor(usuarioId);
	    }
	    
	    
	 // Endpoint para modificar una noticia
	    @PutMapping("/editar/{id}")
	    public ResponseEntity<noticiaDTO> modificarNoticia(
	            @PathVariable Long id,
	            @RequestBody noticiaDTO noticiaActualizada) {
	        noticiaDTO noticiaModificada = servNoticia.modificarNoticia(id, noticiaActualizada);

	        if (noticiaModificada == null) {
	            return ResponseEntity.notFound().build(); // Retorna 404 si no se encuentra la noticia
	        }
	        return ResponseEntity.ok(noticiaModificada); // Retorna 200 con la noticia modificada
	    }
}
