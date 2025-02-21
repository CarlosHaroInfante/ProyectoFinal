package edu.Periodico.Prueba.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.Periodico.Prueba.dtos.plantillaDTO;
import edu.Periodico.Prueba.servicios.servicioPlantilla;

@RestController
@RequestMapping("/api/plantilla")
public class controladorPlantilla {

    @Autowired
    private servicioPlantilla servPlantilla;

    @PostMapping("/alta")
    public ResponseEntity<?> altaMiembroPlantilla(@RequestBody plantillaDTO miembroPlantilla) {
        System.out.println("JSON Recibido: " + miembroPlantilla);

        try {
            if (miembroPlantilla.getEquipo() == null || miembroPlantilla.getEquipo().getIdEquipo() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El equipo no puede ser nulo o inválido");
            }

            plantillaDTO nuevoMiembro = servPlantilla.crearMiembroPlantilla(miembroPlantilla);
            return new ResponseEntity<>(nuevoMiembro, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @PutMapping("/modificar/{idPlantilla}")
    public ResponseEntity<plantillaDTO> modificarPlantilla(@PathVariable Long idPlantilla, @RequestBody plantillaDTO plantilla) {
        try {
            plantillaDTO plantillaActualizada = servPlantilla.modificarPlantilla(idPlantilla, plantilla);
            return ResponseEntity.ok(plantillaActualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/jugadores/{equipoId}")
    public ResponseEntity<List<plantillaDTO>> obtenerJugadores(@PathVariable long equipoId) {
        List<plantillaDTO> jugadores = servPlantilla.obtenerJugadoresPorEquipoId(equipoId);
        
        if (jugadores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Si no se encuentran jugadores
        }
        return ResponseEntity.ok(jugadores); // Si se encuentran jugadores
    }



}


