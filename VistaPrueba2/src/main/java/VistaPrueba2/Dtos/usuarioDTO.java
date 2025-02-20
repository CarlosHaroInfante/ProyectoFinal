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

    // Constructor vacío
    public usuarioDTO() {}

    // Constructor completo (actualizado para incluir los nuevos campos)
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

    // Getters y Setters existentes
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
    
    // Getters y Setters para los nuevos campos
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
