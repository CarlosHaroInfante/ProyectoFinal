package edu.Periodico.Prueba.dtos;

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
 * Dto que contiene los datos de la tabla de clasificación.
 * 17/1/2025 - CHI 
 * */
@Entity
@Table(name = "clasificacion", schema="Periodico")
public class clasificacionDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clasificacion")
    private long idClasificacion;

    @Column(name = "temporada")
    private String temporada;

    @ManyToOne
    @JoinColumn(name = "equipo_id", referencedColumnName = "id_equipo", nullable = false)
    @JsonBackReference
    private equipoDTO equipo;

    
    @Column(name = "puesto")
    private int puesto;

    @Column(name = "puntos")
    private int puntos;

    @Column(name = "goles_a_favor")
    private int golesAFavor;

    @Column(name = "goles_en_contra")
    private int golesEnContra;

    public clasificacionDTO(long idClasificacion, String temporada, equipoDTO equipo, int puesto, int puntos, int golesAFavor, int golesEnContra) {
        super();
        this.idClasificacion = idClasificacion;
        this.temporada = temporada;
        this.equipo = equipo;  // Se pasa el objeto completo de equipoDTO
        this.puesto = puesto;
        this.puntos = puntos;
        this.golesAFavor = golesAFavor;
        this.golesEnContra = golesEnContra;
    }
	
	public clasificacionDTO() {
	    // Constructor vacío requerido por JPA
	}

	public long getIdClasificacion() {
		return idClasificacion;
	}

	public void setIdClasificacion(long idClasificacion) {
		this.idClasificacion = idClasificacion;
	}

	public String getTemporada() {
		return temporada;
	}

	public void setTemporada(String temporada) {
		this.temporada = temporada;
	}

	public equipoDTO getEquipo() {
        return equipo;
    }

    public void setEquipo(equipoDTO equipo) {
        this.equipo = equipo;
    }
	
	public int getPuesto() {
		return puesto;
	}

	public void setPuesto(int puesto) {
		this.puesto = puesto;
	}


	public int getPuntos() {
		return puntos;
	}

	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}

	public int getGolesAFavor() {
		return golesAFavor;
	}

	public void setGolesAFavor(int golesAFavor) {
		this.golesAFavor = golesAFavor;
	}

	public int getGolesEnContra() {
		return golesEnContra;
	}

	public void setGolesEnContra(int golesEnContra) {
		this.golesEnContra = golesEnContra;
	}
    
}

   