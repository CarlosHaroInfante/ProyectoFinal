package VistaPrueba2.Dtos;

import java.sql.Date;

/**
 * Data Transfer Object (DTO) para representar una noticia.
 * <p>
 * Contiene información como el identificador, título, contenido, fecha de publicación,
 * imagen de la noticia y el autor de la noticia.
 * </p>
 * 27/02/2025 - CHI
 */
public class noticiaDTO {
    
    private long id_noticia;
    private String titulo;
    private String contenido;
    private Date fechaPublicacion;
    private byte[] imagenNoticia;
    private usuarioDTO autor;  // Campo para el autor

    /**
     * Constructor vacío.
     */
    public noticiaDTO() {}

    /**
     * Constructor completo que inicializa todos los campos de la noticia.
     *
     * @param id_noticia       El identificador de la noticia.
     * @param titulo           El título de la noticia.
     * @param contenido        El contenido de la noticia.
     * @param fechaPublicacion La fecha de publicación de la noticia.
     * @param imagenNoticia    La imagen asociada a la noticia, en forma de arreglo de bytes.
     * @param autor            El autor de la noticia.
     */
    public noticiaDTO(long id_noticia, String titulo, String contenido, Date fechaPublicacion, byte[] imagenNoticia, usuarioDTO autor) {
        this.id_noticia = id_noticia;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.imagenNoticia = imagenNoticia;
        this.autor = autor;
    }

    /**
     * Obtiene el identificador de la noticia.
     *
     * @return El ID de la noticia.
     */
    public long getId_noticia() {
        return id_noticia;
    }

    /**
     * Establece el identificador de la noticia.
     *
     * @param id_noticia El nuevo ID de la noticia.
     */
    public void setId_noticia(long id_noticia) {
        this.id_noticia = id_noticia;
    }

    /**
     * Obtiene el título de la noticia.
     *
     * @return El título de la noticia.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título de la noticia.
     *
     * @param titulo El nuevo título de la noticia.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el contenido de la noticia.
     *
     * @return El contenido de la noticia.
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido de la noticia.
     *
     * @param contenido El nuevo contenido de la noticia.
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene la fecha de publicación de la noticia.
     *
     * @return La fecha de publicación de la noticia.
     */
    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de publicación de la noticia.
     *
     * @param fechaPublicacion La nueva fecha de publicación.
     */
    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene la imagen de la noticia.
     *
     * @return Un arreglo de bytes que representa la imagen de la noticia.
     */
    public byte[] getImagenNoticia() {
        return imagenNoticia;
    }

    /**
     * Establece la imagen de la noticia.
     *
     * @param imagenNoticia El arreglo de bytes que contiene la imagen.
     */
    public void setImagenNoticia(byte[] imagenNoticia) {
        this.imagenNoticia = imagenNoticia;
    }

    /**
     * Obtiene el autor de la noticia.
     *
     * @return Un objeto usuarioDTO que representa el autor de la noticia.
     */
    public usuarioDTO getAutor() {
        return autor;
    }

    /**
     * Establece el autor de la noticia.
     *
     * @param autor El objeto usuarioDTO que se asignará como autor.
     */
    public void setAutor(usuarioDTO autor) {
        this.autor = autor;
    }
}
