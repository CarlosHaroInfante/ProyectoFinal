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

import edu.Periodico.Prueba.dtos.equipoDTO;
import edu.Periodico.Prueba.servicios.servicioEquipo;

@RestController
@RequestMapping("/api/equipos")
public class controladorEquipo {

    @Autowired
    private servicioEquipo servEquipo;

    @PostMapping("/alta")
    public ResponseEntity<equipoDTO> crearEquipo(@RequestBody equipoDTO equipo) {
        equipoDTO nuevoEquipo = servEquipo.crearEquipo(equipo);
        return new ResponseEntity<>(nuevoEquipo, HttpStatus.CREATED);
    }
    
    /*
	 * Controlador que da de borra a un usuario en la base de datos mediante el id de usuario.
	 * 17/1/2025 - CHI 
	 * */
    @DeleteMapping("/baja/{idEquipo}")
    public ResponseEntity<String> bajaEquipo(@PathVariable Long idEquipo) {
        boolean eliminado = servEquipo.eliminarEquipoPorId(idEquipo);
        if (eliminado) {
            return ResponseEntity.ok("Equipo eliminado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equipo no encontrado.");
        }
    }
    
    
    @GetMapping("/todos")
    public ResponseEntity<List<equipoDTO>> obtenerTodosLosEquipos() {
        List<equipoDTO> equipos = servEquipo.obtenerTodosLosEquipos();
        if (equipos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(equipos);
    }
    
    @PutMapping("/modificar/{idEquipo}")
    public ResponseEntity<equipoDTO> modificarEquipo(@PathVariable Long idEquipo, @RequestBody equipoDTO equipoActualizado) {
        try {
            equipoDTO equipoModificado = servEquipo.modificarEquipo(idEquipo, equipoActualizado);
            return ResponseEntity.ok(equipoModificado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}