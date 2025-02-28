package edu.Periodico.Prueba.servicios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.Periodico.Prueba.Repositorios.repositorioUsuario;
import edu.Periodico.Prueba.Util.FicheroLog;
import edu.Periodico.Prueba.dtos.usuarioDTO;

/**
 * Servicio para gestionar operaciones relacionadas con los usuarios.
 * <p>
 * Provee métodos para encriptar contraseñas, registrar usuarios sin contraseña,
 * actualizar la contraseña, confirmar usuarios, eliminar, modificar y obtener usuarios.
 * </p>
 * 27/02/2025 - CHI
 */
@Service
public class servicioUsuario {

    @Autowired
    private repositorioUsuario RepUsuario;

    /**
     * Encripta una contraseña utilizando SHA-256.
     * 27/02/2025 - CHI
     * 
     * @param contraseña La contraseña a encriptar.
     * @return La contraseña encriptada en formato hexadecimal.
     * @throws RuntimeException Si no se encuentra el algoritmo SHA-256.
     */
    public String encriptarContrasenya(String contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = String.format("%02x", b); // Formato hexadecimal simplificado
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registra un usuario sin contraseña.
     * <p>
     * La vista ya generó el token y configuró password = "" y confirmado = false.
     * Verifica si el correo ya está registrado y guarda el usuario en la base de datos.
     * </p>
     * 27/02/2025 - CHI
     * 
     * @param usuario Objeto usuarioDTO con los datos del usuario a registrar.
     * @return true si el usuario se registró correctamente, false de lo contrario.
     */
    public boolean registrarUsuarioSinPassword(usuarioDTO usuario) {
        try {
            long count = RepUsuario.countByCorreoUsuario(usuario.getCorreoUsuario());
            if (count > 0) {
                System.out.println("El correo " + usuario.getCorreoUsuario() + " ya está registrado (count = " + count + ").");
                return false;
            }
            usuarioDTO usuarioGuardado = RepUsuario.save(usuario);
            if (usuarioGuardado == null || usuarioGuardado.getIdUsuario() == 0) {
                System.out.println("Error al guardar el usuario en la base de datos.");
                return false;
            }
            System.out.println("Usuario guardado correctamente: " + usuarioGuardado);
            return true;
        } catch (Exception e) {
            FicheroLog.logError("Error al registrar usuario sin contraseña", e);
            return false;
        }
    }
            
    /**
     * Actualiza la contraseña del usuario, después de verificar el código de verificación.
     * 27/02/2025 - CHI
     * 
     * @param correo El correo del usuario.
     * @param codigo El código de verificación recibido.
     * @param nuevaPassword La nueva contraseña.
     * @return true si la contraseña se actualizó correctamente; false en caso contrario.
     */
    public boolean actualizarPassword(String correo, String codigo, String nuevaPassword) {
        try {
            usuarioDTO usuario = RepUsuario.findByCorreoUsuario(correo); 
            if (usuario != null && usuario.getTokenConfirmacion() != null) {
                System.out.println("Código almacenado: " + usuario.getTokenConfirmacion());
                System.out.println("Código ingresado: " + codigo);
                if (usuario.getTokenConfirmacion().equalsIgnoreCase(codigo)) {
                    String passwordEncriptada = encriptarContrasenya(nuevaPassword);
                    usuario.setPassword(passwordEncriptada);
                    usuario.setConfirmado(true);
                    usuario.setTokenConfirmacion(null);
                    RepUsuario.save(usuario);
                    return true;
                } else {
                    System.out.println("El código de verificación no coincide.");
                }
            }
            return false;
        } catch (Exception e) {
            FicheroLog.logError("Error al actualizar la contraseña para el correo: " + correo, e);
            return false;
        }
    }
    
    /**
     * Confirma el usuario a partir del token.
     * <p>
     * Busca el usuario por token, verifica que no esté ya confirmado, y si es válido, actualiza su estado.
     * </p>
     * 27/02/2025 - CHI
     * 
     * @param token El token de verificación.
     * @return true si el usuario se confirmó correctamente; false en caso contrario.
     */
    public boolean confirmarUsuario(String token) {
        try {
            System.out.println("Confirmando token: " + token);
            usuarioDTO usuario = RepUsuario.findByTokenConfirmacion(token);
            if (usuario == null) {
                System.out.println("No se encontró usuario para el token: " + token);
                return false;
            }
            if (usuario.isConfirmado()) {
                System.out.println("El usuario ya se encuentra confirmado.");
                return false;
            }
            System.out.println("Usuario encontrado: " + usuario.getNombreCompleto() + ", token: " + usuario.getTokenConfirmacion());
            
            usuario.setConfirmado(true);
            usuario.setTokenConfirmacion(null);
            usuarioDTO actualizado = RepUsuario.save(usuario);
            if (actualizado != null && actualizado.isConfirmado() && actualizado.getTokenConfirmacion() == null) {
                System.out.println("Usuario confirmado correctamente con token: " + token);
                return true;
            } else {
                System.out.println("Error al confirmar el usuario.");
                return false;
            }
        } catch (Exception e) {
            FicheroLog.logError("Error al confirmar el usuario con token: " + token, e);
            return false;
        }
    }

    /**
     * Elimina un usuario por su ID.
     * 27/02/2025 - CHI
     * 
     * @param idUsuario El ID del usuario a eliminar.
     * @return true si el usuario se eliminó correctamente; false si no existe.
     */
    public boolean eliminarUsuarioPorId(Long idUsuario) {
        try {
            if (RepUsuario.existsById(idUsuario)) {
                RepUsuario.deleteById(idUsuario);
                return true;
            }
            return false;
        } catch (Exception e) {
            FicheroLog.logError("Error al eliminar el usuario con ID: " + idUsuario, e);
            throw new RuntimeException("Error al intentar eliminar el usuario.", e);
        }
    }

    /**
     * Modifica los datos de un usuario.
     * 27/02/2025 - CHI
     * 
     * @param idUsuario El ID del usuario a modificar.
     * @param usuarioActualizado Objeto usuarioDTO con los nuevos datos.
     * @return El objeto usuarioDTO modificado, o null si el usuario no se encuentra.
     */
    public usuarioDTO modificarUsuario(Long idUsuario, usuarioDTO usuarioActualizado) {
        try {
            return RepUsuario.findById(idUsuario).map(usuario -> {
                usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
                usuario.setNumeroUsuario(usuarioActualizado.getNumeroUsuario());
                usuario.setCorreoUsuario(usuarioActualizado.getCorreoUsuario());
                usuario.setRolUsuario(usuarioActualizado.getRolUsuario());
                if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                    String contrasenaEncriptada = encriptarContrasenya(usuarioActualizado.getPassword());
                    usuario.setPassword(contrasenaEncriptada);
                }
                usuario.setImagenUsuario(usuarioActualizado.getImagenUsuario());
                return RepUsuario.save(usuario);
            }).orElse(null);
        } catch (Exception e) {
            FicheroLog.logError("Error al modificar el usuario con ID: " + idUsuario, e);
            return null;
        }
    }

    /**
     * Obtiene todos los usuarios.
     * 27/02/2025 - CHI
     * 
     * @return Un ArrayList de usuarioDTO con todos los usuarios, o una lista vacía en caso de error.
     */
    public ArrayList<usuarioDTO> obtenerTodosLosUsuarios() {
        try {
            return RepUsuario.findAll();
        } catch (Exception e) {
            FicheroLog.logError("Error al obtener todos los usuarios", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Autentica un usuario utilizando el correo y la contraseña.
     * 27/02/2025 - CHI
     * 
     * @param correoUsuario El correo del usuario.
     * @param password La contraseña proporcionada.
     * @return El objeto usuarioDTO autenticado.
     * @throws RuntimeException Si el usuario no se encuentra o la contraseña es incorrecta.
     */
    public usuarioDTO autenticarUsuario(String correoUsuario, String password) {
        try {
            usuarioDTO usuario = RepUsuario.findByCorreoUsuario(correoUsuario);
            System.out.println(correoUsuario);
            System.out.println(password);
            if (usuario == null) {
                throw new RuntimeException("Usuario no encontrado");
            }
            if (!password.equals(usuario.getPassword())) {
                throw new RuntimeException("Contraseña incorrecta");
            }
            return usuario;
        } catch (Exception e) {
            FicheroLog.logError("Error al autenticar usuario con correo: " + correoUsuario, e);
            throw e;
        }
    }
}
