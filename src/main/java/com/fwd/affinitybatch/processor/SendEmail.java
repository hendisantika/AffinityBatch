/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch.processor;

import static com.fwd.affinitybatch.JobApp.input;
import static com.fwd.affinitybatch.JobApp.prop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author idnhsn
 */
public class SendEmail {

    public void SendEmail(String reciever, String subject, String body) throws UnsupportedEncodingException, IOException {
        String path = new File(".").getCanonicalPath();
        input = new FileInputStream(path + "\\conf\\affinity_config.properties");
        // load a properties file
        prop.load(input);

        // Recipient's email ID needs to be mentioned.
        String to = reciever;//change accordingly

        // Sender's email ID needs to be mentioned
//        String from = "noreply.id@fwd.com";//change accordingly
        String from = prop.getProperty("EmailFrom");//change accordingly
        String cc = prop.getProperty("EmailCC");
        String port = prop.getProperty("EmailPort");

        final String username = prop.getProperty("EmailUsername");
        final String password = prop.getProperty("EmailPassword");

        // Outlook's SMTP server
//        String host = "10.17.50.14";
        // Yahoo's SMTP server
        String host = prop.getProperty("EmailSMTP");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
//        props.put("mail.smtp.port", "25");
//        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host);

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Create a default Multipart.
            Multipart multipart = new MimeMultipart();

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from, "FWD Life Indonesia"));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set CC : header field of the header.
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(cc));

            // Set Subject: header field
//            message.setSubject("Affinity - FWD LIFE Email Test");
            message.setSubject(subject);

            // Now set the actual message
//            message.setText(body);
//            message.setText("Hello, this is sample email to check/send "
//                    + "email using Java Mail API from Affinity");
            message.setContent(body, "text/html");

            // Send message
            Transport.send(message);

            System.out.println("Message from Affinity sent successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailAttachment(String filename) throws IOException {
//    public void sendEmailAttachment() throws IOException {
        String path = new File(".").getCanonicalPath();
        input = new FileInputStream(path + "\\conf\\affinity_config.properties");
        prop.load(input);

        // Strings that contain from, to, subject, body and file path to the attachment
        String from = prop.getProperty("EmailFrom");

        String to = prop.getProperty("EmailReciever");//change accordingly

        String subject = prop.getProperty("EmailSubject");

//        String body = prop.getProperty("EmailBody");
        String body = "<body>"
                + "<p>Hallo Admin PT MOMEN GLOBAL INTERNASIONAL, </p>"
                + "<p> Melalui e-mail ini, kami ingin menyampaikan bahwa ada data yang duplikat (sama) sebagaimana file terlampir. </p>"
                + "<p>Mohon dicek lagi dengan teliti supaya tidak ada data yang duplikat. Selanjutnya, upload kembali file yang sudah direvisi tersebut.</p>"
                + "<p>Terima kasih telah memilih produk perlindungan asuransi dari  FWD Life.</p><br>"
                + "Untuk informasi lebih lanjut, silakan menghubungi Customer Care kami di nomor 1500 391 atau melalui email di <a href="
                + "mailto:" + "cs.id@fwd.com" + ">cs.id@fwd.com</a>.</p>"
                + "<p>&nbsp;</p>"
                + "<p>Salam #BebaskanLangkah<br>"
                + "</p><p>  <b>FWD Life </b></p>"
                + "</body>";

//        String filename = "D:/Tes/ftp/output/index.php";
        // Set smtp properties
        Properties properties = new Properties();

        // Yahoo's SMTP server
        String host = prop.getProperty("EmailHost");
        String port = prop.getProperty("EmailPort");

        properties.put("mail.smtp.host", host);

        properties.put("mail.smtp.port", port);

        final String username = prop.getProperty("EmailFrom");
        final String password = prop.getProperty("EmailPassword");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.trust", host);

//        Session session = Session.getDefaultInstance(properties, null);
        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from, "FWD Life Indonesia"));

            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject);

            message.setSentDate(new Date());

// Set the email body
            MimeBodyPart messagePart = new MimeBodyPart();

//            messagePart.setText(body);
            messagePart.setContent(body, "text/html");

// Set the email attachment file
            MimeBodyPart attachmentPart = new MimeBodyPart();

            FileDataSource fileDataSource = new FileDataSource(filename) {

                @Override

                public String getContentType() {

                    return "application/octet-stream";

                }

            };

            attachmentPart.setDataHandler(new DataHandler(fileDataSource));

            attachmentPart.setFileName(fileDataSource.getName());

// Add all parts of the email to Multipart object
            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messagePart);

            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

// Send email
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {

            e.printStackTrace();

        }
    }

//    public static void main(String[] args) throws IOException {
//        SendEmail send = new SendEmail();
//        send.sendEmailAttachment();
//    }
}
