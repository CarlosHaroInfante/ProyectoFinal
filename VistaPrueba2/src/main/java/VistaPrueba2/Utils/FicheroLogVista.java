package VistaPrueba2.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para el registro de logs en un archivo.
 * <p>
 * Esta clase permite escribir mensajes de log en un archivo de texto ubicado en un directorio de logs.
 * Los mensajes pueden ser de nivel INFO o ERROR, y se registran con la fecha y hora actual.
 * Se configura el directorio de logs a una ruta específica:
 * "C:\Users\Carlos\Desktop\ProyectoFinal-master\VistaPrueba2\logs".
 * </p>
 * 27/02/2025 - CHI
 */
public class FicheroLogVista {
    // Ruta absoluta para el directorio de logs.
	private static final String LOG_DIR = "C:\\Users\\Carlos\\Desktop\\ProyectoFinal-master\\VistaPrueba2\\logs";
	private static final String LOG_FILE = LOG_DIR + "\\vista.log";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Bloque estático para crear el directorio de logs si no existe.
     */
    static {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
            // Imprimir la ruta absoluta para verificar:
            System.out.println("Directorio de logs: " + Paths.get(LOG_DIR).toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registra un mensaje de información en el archivo de log.
     *
     * @param message El mensaje de log a registrar.
     */
    public static void logInfo(String message) {
        log("INFO", message);
    }

    /**
     * Registra un mensaje de error en el archivo de log, incluyendo la descripción de la excepción.
     *
     * @param message El mensaje de error a registrar.
     * @param e       La excepción que se produjo.
     */
    public static void logError(String message, Exception e) {
        log("ERROR", message + " - " + e.getMessage());
    }

    /**
     * Registra un mensaje en el archivo de log.
     * <p>
     * Utiliza un bloque try-with-resources para asegurar que el FileWriter y PrintWriter se cierren correctamente.
     * </p>
     *
     * @param level   El nivel del mensaje (por ejemplo, "INFO" o "ERROR").
     * @param message El mensaje a registrar.
     */
    public static void log(String level, String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String logMessage = String.format("%s [%s] - %s", dtf.format(LocalDateTime.now()), level, message);
            pw.println(logMessage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
