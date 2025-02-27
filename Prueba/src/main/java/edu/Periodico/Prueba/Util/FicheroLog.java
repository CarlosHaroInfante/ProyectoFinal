package edu.Periodico.Prueba.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FicheroLog {
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "/api.log";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Bloque estático para crear el directorio de logs si no existe
    static {
        try {
            Files.createDirectories(Paths.get(LOG_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logInfo(String message) {
        log("INFO", message);
    }

    public static void logError(String message, Exception e) {
        log("ERROR", message + " - " + e.getMessage());
    }

    public static void log(String level, String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String logMessage = String.format("%s [%s] - %s", dtf.format(LocalDateTime.now()), level, message);
            pw.println(logMessage);
        } catch (IOException ex) {
            // Si falla el log, lo mostramos en consola
            ex.printStackTrace();
        }
    }
}
