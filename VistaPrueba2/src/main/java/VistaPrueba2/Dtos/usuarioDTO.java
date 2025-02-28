package VistaPrueba2.Dtos;

/**
 * DTO para la capa de vista que contiene los datos de usuario.
 * Usado para transferir información entre capas de manera simplificada.
 * 21/01/2025 - CHI
 */
public class usuarioDTO {

    private long idUsuario;
    private String nombreCompleto;
    private String numeroUsuario;
    private String correoUsuario;
    private String rolUsuario;
    private String password;
    private byte[] imagenUsuario;
    
    // Nuevos campos para la verificación por correo
    private String tokenConfirmacion;
    private boolean confirmado;

    /**
     * Constructor vacío.
     */
    public usuarioDTO() {}

    /**
     * Constructor completo.
     *
     * @param idUsuario         El identificador del usuario.
     * @param nombreCompleto    El nombre completo del usuario.
     * @param numeroUsuario     El número de teléfono del usuario.
     * @param correoUsuario     El correo electrónico del usuario.
     * @param rolUsuario        El rol del usuario (p. ej., "Usuario", "Admin").
     * @param password          La contraseña del usuario.
     * @param imagenUsuario     La imagen del usuario en forma de arreglo de bytes.
     * @param tokenConfirmacion El token de verificación para el correo.
     * @param confirmado        Indica si el usuario ha sido verificado.
     * 21/01/2025 - CHI
     */
    public usuarioDTO(long idUsuario, String nombreCompleto, String numeroUsuario, 
                      String correoUsuario, String rolUsuario, String password, 
                      byte[] imagenUsuario, String tokenConfirmacion, boolean confirmado) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.numeroUsuario = numeroUsuario;
        this.correoUsuario = correoUsuario;
        this.rolUsuario = rolUsuario;
        this.password = password;
        this.imagenUsuario = imagenUsuario;
        this.tokenConfirmacion = tokenConfirmacion;
        this.confirmado = confirmado;
    }

    /**
     * Retorna el identificador del usuario.
     *
     * @return El ID del usuario.
     */
    public long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param idUsuario El nuevo ID del usuario.
     */
    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Retorna el nombre completo del usuario.
     *
     * @return El nombre completo.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Establece el nombre completo del usuario.
     *
     * @param nombreCompleto El nuevo nombre completo.
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Retorna el número de teléfono del usuario.
     *
     * @return El número de teléfono.
     */
    public String getNumeroUsuario() {
        return numeroUsuario;
    }

    /**
     * Establece el número de teléfono del usuario.
     *
     * @param numeroUsuario El nuevo número de teléfono.
     */
    public void setNumeroUsuario(String numeroUsuario) {
        this.numeroUsuario = numeroUsuario;
    }

    /**
     * Retorna el correo electrónico del usuario.
     *
     * @return El correo electrónico.
     */
    public String getCorreoUsuario() {
        return correoUsuario;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param correoUsuario El nuevo correo electrónico.
     */
    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    /**
     * Retorna el rol del usuario.
     *
     * @return El rol del usuario.
     */
    public String getRolUsuario() {
        return rolUsuario;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rolUsuario El nuevo rol del usuario.
     */
    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    /**
     * Retorna la contraseña del usuario.
     *
     * @return La contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password La nueva contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retorna la imagen del usuario.
     *
     * @return La imagen del usuario en forma de arreglo de bytes.
     */
    public byte[] getImagenUsuario() {
        return imagenUsuario;
    }

    /**
     * Establece la imagen del usuario.
     *
     * @param imagenUsuario El arreglo de bytes que contiene la imagen del usuario.
     */
    public void setImagenUsuario(byte[] imagenUsuario) {
        this.imagenUsuario = imagenUsuario;
    }
    
    /**
     * Retorna el token de verificación del usuario.
     *
     * @return El token de verificación.
     */
    public String getTokenConfirmacion() {
        return tokenConfirmacion;
    }

    /**
     * Establece el token de verificación del usuario.
     *
     * @param tokenConfirmacion El nuevo token de verificación.
     */
    public void setTokenConfirmacion(String tokenConfirmacion) {
        this.tokenConfirmacion = tokenConfirmacion;
    }

    /**
     * Indica si el usuario está confirmado.
     *
     * @return true si el usuario ha sido confirmado; false en caso contrario.
     */
    public boolean isConfirmado() {
        return confirmado;
    }

    /**
     * Establece el estado de confirmación del usuario.
     *
     * @param confirmado true si el usuario está confirmado; false en caso contrario.
     */
    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }

    /**
     * Retorna una representación en cadena del objeto usuarioDTO.
     *
     * @return Una cadena que representa el objeto usuarioDTO.
     */
    @Override
    public String toString() {
        return "usuarioDTO{" +
                "idUsuario=" + idUsuario +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", numeroUsuario='" + numeroUsuario + '\'' +
                ", correoUsuario='" + correoUsuario + '\'' +
                ", rolUsuario='" + rolUsuario + '\'' +
                ", password='[PROTEGIDO]'" +
                ", imagenUsuario=" + (imagenUsuario != null ? "EXISTE" : "NULL") +
                ", tokenConfirmacion='" + tokenConfirmacion + '\'' +
                ", confirmado=" + confirmado +
                '}';
    }
}
