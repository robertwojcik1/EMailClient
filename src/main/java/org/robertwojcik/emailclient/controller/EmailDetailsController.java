package org.robertwojcik.emailclient.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.robertwojcik.emailclient.EmailManager;
import org.robertwojcik.emailclient.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebView;
import org.robertwojcik.emailclient.controller.services.MessageRendererService;
import org.robertwojcik.emailclient.model.EmailMessage;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EmailDetailsController extends BaseController implements Initializable {

    private String DOWNLOADS_LOCATION = System.getProperty("user.home") + "/Downloads/";
    @FXML
    private Label attachmentLabel;

    @FXML
    private HBox hBoxDownloads;

    @FXML
    private Label senderLabel;

    @FXML
    private Label subjectLabel;

    @FXML
    private WebView webView;

    public EmailDetailsController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EmailMessage emailMessage = emailManager.getSelectedMessage();
        subjectLabel.setText(emailMessage.getSubject());
        senderLabel.setText(emailMessage.getSender());
        loadAttachments(emailMessage);

        MessageRendererService messageRendererService = new MessageRendererService(webView.getEngine());
        messageRendererService.setEmailMessage(emailMessage);
        messageRendererService.restart();
    }

    private void loadAttachments(EmailMessage emailMessage) {
        if(emailMessage.hasAttachments()) {
            for(MimeBodyPart mimeBodyPart : emailMessage.getAttachmentList()) {
                try {
                    AttachmentButton button = new AttachmentButton(mimeBodyPart);
                    hBoxDownloads.getChildren().add(button);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            attachmentLabel.setText("");
        }
    }

    private class AttachmentButton extends Button {

        private MimeBodyPart mimeBodyPart;
        private String downloadedFilePath;

        public AttachmentButton(MimeBodyPart mimeBodyPart) throws MessagingException {
            this.mimeBodyPart = mimeBodyPart;
            this.setText(mimeBodyPart.getFileName());
            this.downloadedFilePath = DOWNLOADS_LOCATION + mimeBodyPart.getFileName();
            this.setOnAction(e -> downloadAttachment());
        }

        private void downloadAttachment() {
            Service service = new Service() {
                @Override
                protected Task createTask() {
                    return new Task() {
                        @Override
                        protected Object call() throws Exception {
                            mimeBodyPart.saveFile(downloadedFilePath);
                            return null;
                        }
                    };
                }
            };
            service.restart();
            service.setOnSucceeded(e -> {
                colorGreen();
                this.setOnAction(e2 -> {
                    File file = new File(downloadedFilePath);
                    Desktop desktop = Desktop.getDesktop();
                    if(file.exists()) {
                        try {
                            desktop.open(file);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            });
        }

        private void colorBlue() {
            this.setStyle("-fx-background-color: LightBlue");
        }

        private void colorGreen() {
            this.setStyle("-fx-background-color: Green");
        }
    }
}
