package com.automation.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotifier {
    public static void sendEmail(String subject, String body) {
        String from = ConfigReader.getProperty("email.from");
        String to = ConfigReader.getProperty("email.to");
        String host = ConfigReader.getProperty("email.smtp.host");
        String port = ConfigReader.getProperty("email.smtp.port");
        
        String passwordFromEnv = System.getenv("EMAIL_PASSWORD");
        String passwordFromConfig = ConfigReader.getProperty("email.password");
        final String password = (passwordFromEnv != null && !passwordFromEnv.isEmpty()) 
                ? passwordFromEnv 
                : passwordFromConfig;

        // If no password, skip sending email (e.g., in Docker/CI without secrets)
        if (password == null || password.isEmpty()) {
            System.out.println("Email not configured – skipping email alert for: " + subject);
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}