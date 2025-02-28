package edu.Periodico.Prueba.dtos;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * Dto que contiene los datos de la tabla de plantilla.
 * 17/1/2025 - CHI 
 * */
@Entity
@Table(name = "plantilla", schema="Periodico")
public class plantillaDTO {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_plantilla")
    private long idPlantilla;
	
	 @Column(name = "nombre_completo")
	 private String nombreCompleto;
	 
	 @Column(name = "posicion")
	 private String posicion;
	 
	 @Column(name = "fecha_nacimiento")
	 private Date fechaNacimiento;
	 
	 @Column(name = "dorsal")
	 private int dorsal;
	 
	// Relación N-1 con Equipo
	@ManyToOne
	@JoinColumn(name = "equipo_id", referencedColumnName = "id_equipo", nullable = false)
	@JsonBackReference // Evita que se serialice en la respuesta
	private equipoDTO equipo;
	 

	public long getIdPlantilla() {
		return idPlantilla;
	}

	public void setIdPlantilla(long idPlantilla) {
		this.idPlantilla = idPlantilla;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getPosicion() {
		return posicion;
	}

	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}


	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public int getDorsal() {
		return dorsal;
	}

	public void setDorsal(int dorsal) {
		this.dorsal = dorsal;
	}

	public equipoDTO getEquipo() {
        return equipo;
    }

    public void setEquipo(equipoDTO equipo) {
        this.equipo = equipo;
    }
	
	public plantillaDTO() {
		
	}

	public plantillaDTO(long idPlantilla, String nombreCompleto, String posicion, Date fechaNacimiento, int dorsal,
			String equipoId) {
		super();
		this.idPlantilla = idPlantilla;
		this.nombreCompleto = nombreCompleto;
		this.posicion = posicion;
		this.fechaNacimiento = fechaNacimiento;
		this.dorsal = dorsal;

		this.equipo = equipo; // Asignación directa del objeto equipoDTO
	}

	
	 
	 
}
