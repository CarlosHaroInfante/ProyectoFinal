package VistaPrueba2.Dtos;

import java.sql.Date;

public class noticiaDTO {
    
    private long id_noticia;
    private String titulo;
    private String contenido;
    private Date fechaPublicacion;
    private byte[] imagenNoticia;
    private usuarioDTO autor;  // Campo para el autor

    public noticiaDTO() {}

    public noticiaDTO(long id_noticia, String titulo, String contenido, Date fechaPublicacion, byte[] imagenNoticia, usuarioDTO autor) {
        this.id_noticia = id_noticia;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenNoticia = imagenNoticia;
        this.autor = autor;
    }

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
