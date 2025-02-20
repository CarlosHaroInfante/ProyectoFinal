package edu.Periodico.Prueba.dtos;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/*
 * Dto que contiene los datos de la tabla de clasificación.
 * 17/1/2025 - CHI 
 * */
@Entity
@Table(name = "equipo", schema = "Periodico")
public class equipoDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private long idEquipo;

    @Column(name = "nombre_equipo")
    private String nombreEquipo;

    @Column(name = "imagen_equipo")
    private byte[] imagenEquipo;

    @Column(name = "historia")
    private String historia;

    // Relación 1-N con Plantilla
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<plantillaDTO> plantilla;

    // Relación 1-N con Clasificación
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Permite serializar la lista de jugadores correctamente
    private List<clasificacionDTO> clasificacion;

    public long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public byte[] getImagenEquipo() {
        return imagenEquipo;
    }

    public void setImagenEquipo(byte[] imagenEquipo) {
        this.imagenEquipo = imagenEquipo;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public List<plantillaDTO> getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(List<plantillaDTO> plantilla) {
        this.plantilla = plantilla;
    }

    public List<clasificacionDTO> getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(List<clasificacionDTO> clasificacion) {
        this.clasificacion = clasificacion;
    }

    public equipoDTO() {
    }


	public equipoDTO(long idEquipo, String nombreEquipo, byte[] imagenEquipo, String historia) {
		super();
		this.idEquipo = idEquipo;
		this.nombreEquipo = nombreEquipo;
		this.imagenEquipo = imagenEquipo;
		this.historia = historia;
	}

}
