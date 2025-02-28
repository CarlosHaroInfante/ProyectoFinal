package edu.Periodico.Prueba.dtos;

import java.time.LocalDateTime;
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
 * Dto que contiene los datos de la tabla de usuarios.
 * 17/1/2025 - CHI 
 * */
@Entity
@Table(name = "usuario", schema="Periodico")
public class usuarioDTO {
    

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private long idUsuario;
    
    @Column(name = "nombre_usuario")
    private String nombreCompleto;
    
    @Column(name = "movil_usuario")
    private String numeroUsuario;
    
    @Column(name = "correo_electronico")
    private String correoUsuario;
    
    @Column(name = "rol_usuario")
    private String rolUsuario;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "foto_usuario")
    private byte[] imagenUsuario;
    
    @Column(name = "token_confirmacion")
    private String tokenConfirmacion;
    
    @Column(name = "token_confirmado")
    private boolean confirmado; // o correoConfirmado
    
    // Relación 1-N con Noticia
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Evita la serialización circular
    private List<noticiaDTO> noticias;
    
    public usuarioDTO() {}
    
    public usuarioDTO(long idUsuario, String nombreCompleto, String numeroUsuario, String correoUsuario,
			String rolUsuario, String password, byte[] imagenUsuario, String tokenConfirmacion, boolean confirmado,
			List<noticiaDTO> noticias) {
		super();
		this.idUsuario = idUsuario;
		this.nombreCompleto = nombreCompleto;
		this.numeroUsuario = numeroUsuario;
		this.correoUsuario = correoUsuario;
		this.rolUsuario = rolUsuario;
		this.password = password;
		this.imagenUsuario = imagenUsuario;
		this.tokenConfirmacion = tokenConfirmacion;
		this.confirmado = confirmado;
		this.noticias = noticias;
	}

    

    // Getters y setters
    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNumeroUsuario() {
        return numeroUsuario;
    }

    public void setNumeroUsuario(String numeroUsuario) {
        this.numeroUsuario = numeroUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImagenUsuario() {
        return imagenUsuario;
    }

    public void setImagenUsuario(byte[] imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }

	public String getTokenConfirmacion() {
		return tokenConfirmacion;
	}

	public void setTokenConfirmacion(String tokenConfirmacion) {
		this.tokenConfirmacion = tokenConfirmacion;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public List<noticiaDTO> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<noticiaDTO> noticias) {
		this.noticias = noticias;
	}
}
