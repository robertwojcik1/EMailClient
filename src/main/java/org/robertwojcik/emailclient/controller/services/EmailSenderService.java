package org.robertwojcik.emailclient.controller.services;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.robertwojcik.emailclient.controller.EmailSendingResult;
import org.robertwojcik.emailclient.model.EmailAccount;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EmailSenderService extends Service<EmailSendingResult> {

    private EmailAccount emailAccount;
    private String subject;
    private String recipient;
    private String content;
    private List<File> attachments;

    public EmailSenderService(EmailAccount emailAccount, String subject,
                              String recipient, String content, List<File> attachments) {
        this.emailAccount = emailAccount;
        this.subject = subject;
        this.recipient = recipient;
        this.content = content;
        this.attachments = attachments;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected EmailSendingResult call() {
                try {
                    MimeMessage mimeMessage = new MimeMessage(emailAccount.getSession());
                    mimeMessage.setFrom(emailAccount.getAddress());
                    mimeMessage.addRecipients(Message.RecipientType.TO, recipient);
                    mimeMessage.setSubject(subject);

                    Multipart multipart = new MimeMultipart();
                    BodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setContent(content, "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    mimeMessage.setContent(multipart);

                    if(!attachments.isEmpty()) {
                        for (File file : attachments) {
                            MimeBodyPart mimeBodyPart = new MimeBodyPart();
                            DataSource source = new FileDataSource(file.getAbsolutePath());
                            mimeBodyPart.setDataHandler(new DataHandler(source));
                            mimeBodyPart.setFileName(file.getName());
                            multipart.addBodyPart(mimeBodyPart);
                        }
                    }

                    Transport transport = emailAccount.getSession().getTransport();
                    transport.connect(
                            emailAccount.getProperties().getProperty("outgoingHost"),
                            emailAccount.getAddress(),
                            emailAccount.getPassword());
                    transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                    transport.close();
                    return EmailSendingResult.SUCCESS;
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_PROVIDER;
                } catch (Exception e) {
                    e.printStackTrace();
                    return EmailSendingResult.FAILED_BY_UNEXPECTED_ERROR;
                }
            }
        };
    }
}
