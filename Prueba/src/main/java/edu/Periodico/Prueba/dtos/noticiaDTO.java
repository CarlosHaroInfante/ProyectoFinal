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

@Entity
@Table(name = "noticia", schema="Periodico")
public class noticiaDTO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_noticia")
    private long id_noticia;
    
    @Column(name = "titulo")
    private String titulo;
    
    @Column(name = "contenido")
    private String contenido;
    
    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion;
    
    @Column(name = "foto_noticia")
    private byte[] imagenNoticia;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario", nullable = false)
    @JsonBackReference // Evita la serialización en el DTO de Noticias
    private usuarioDTO autor;
    
    public noticiaDTO() {}

    // Constructor con todos los campos, incluyendo el autor (UsuarioDTO)
    public noticiaDTO(long id_noticia, String titulo, String contenido, Date fechaPublicacion, byte[] imagenNoticia,
            usuarioDTO autor) {
        this.id_noticia = id_noticia;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenNoticia = imagenNoticia;
        this.autor = autor;
    }

    // Getters y setters
    public long getId_noticia() {
        return id_noticia;
    }

    public void setId_noticia(long id_noticia) {
        this.id_noticia = id_noticia;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public byte[] getImagenNoticia() {
        return imagenNoticia;
    }

    public void setImagenNoticia(byte[] imagenNoticia) {
        this.imagenNoticia = imagenNoticia;
    }

    public usuarioDTO getAutor() {
        return autor;
    }

    public void setAutor(usuarioDTO autor) {
        this.autor = autor;
    }
}
