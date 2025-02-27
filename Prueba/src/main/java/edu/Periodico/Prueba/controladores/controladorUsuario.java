package edu.Periodico.Prueba.controladores;

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

import edu.Periodico.Prueba.dtos.usuarioDTO;
import edu.Periodico.Prueba.servicios.servicioUsuario;

/*
 * Clase donde se encuentran los GET, POST, DELETE o PUT de la aplicación relacionado con los usuarios,
 * 17/1/2025 - CHI 
 * */
@RestController
@RequestMapping("/api/usuarios")
public class controladorUsuario {
	
	
	/*
	 * Es la llamada al servicio del usuario.
	 * 17/1/2025 - CHI 
	 * */
    @Autowired
    servicioUsuario servicio;

    /*
	 * Controlador que da de alta a un usuario en la base de datos.
	 * 17/1/2025 - CHI 
	 * 
    @PostMapping("/alta")
    public ResponseEntity<usuarioDTO> altaUsuario(@RequestBody usuarioDTO nuevoUsuario) {
        try {
            // Aquí se asume que 'nuevoUsuario' ya contiene:
            // - password vacío
            // - tokenConfirmacion generado y asignado en la vista
            // - confirmado = false
            usuarioDTO usuarioGuardado = servicio.altaUsuario(nuevoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioGuardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }*/
    
    /*
	 * Controlador que da de borra a un usuario en la base de datos mediante el id de usuario.
	 * 17/1/2025 - CHI 
	 * */
    @DeleteMapping("/baja/{idUsuario}")
    public ResponseEntity<String> bajaUsuario(@PathVariable Long idUsuario) {
        try {
            boolean eliminado = servicio.eliminarUsuarioPorId(idUsuario);

            if (eliminado) {
                return ResponseEntity.ok("Usuario eliminado correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al eliminar el usuario.");
        }
    }
    
    /*
	 * Controlador que modifica a un usuario en la base de datos mediante el id del usuario.
	 * 17/1/2025 - CHI 
	 * */
    @PutMapping("/modificar/{idUsuario}")
    public ResponseEntity<usuarioDTO> modificarUsuario(
        @PathVariable Long idUsuario,
        @RequestBody usuarioDTO usuarioActualizado
    ) {
        usuarioDTO usuarioModificado = servicio.modificarUsuario(idUsuario, usuarioActualizado);

        if (usuarioModificado != null) {
            return ResponseEntity.ok(usuarioModificado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Usuario no encontrado
        }
    }
    
    /*
	 * Controlador que muestra todos los usuarios en la base de datos.
	 * 17/1/2025 - CHI 
	 * */
    @GetMapping("/todos")
    public ResponseEntity<List<usuarioDTO>> obtenerTodosLosUsuarios() {
        List<usuarioDTO> usuarios = servicio.obtenerTodosLosUsuarios();

        if (!usuarios.isEmpty()) {
            return ResponseEntity.ok(usuarios);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null); // Si no hay usuarios, devuelve un 204
        }
    }



}
