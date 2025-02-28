package edu.Periodico.Prueba.Util;

import java.util.Properties;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Utilidad para enviar correos electrónicos utilizando SMTP.
 * <p>
 * Este método utiliza la configuración SMTP de Gmail para enviar mensajes.
 * </p>
 * 27/02/2025 - CHI
 */
public class EmailUtil {

    /**
     * Envía un correo electrónico.
     * <p>
     * Configura la sesión SMTP y envía un mensaje de correo con el asunto y cuerpo especificados.
     * </p>
     * 27/02/2025 - CHI
     *
     * @param to      La dirección de correo del destinatario.
     * @param subject El asunto del correo.
     * @param body    El cuerpo del mensaje.
     */
    public static void sendEmail(String to, String subject, String body) {
        // Configuración del servidor SMTP de Gmail
        final String host = "smtp.gmail.com";
        final String port = "587";
        final String from = "pasesevillano@gmail.com";     // Tu dirección de correo
        final String username = "pasesevillano@gmail.com";   // Usualmente el mismo correo
        final String password = "pliy dkxv nygu jabu";       // Tu contraseña o "app password" si usas 2FA

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + to);
            FicheroLog.logInfo("Correo enviado exitosamente a " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            FicheroLog.logError("Error al enviar el correo ", e);
        }
    }
}
