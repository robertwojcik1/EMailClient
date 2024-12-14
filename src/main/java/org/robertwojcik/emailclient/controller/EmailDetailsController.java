package org.robertwojcik.emailclient.controller;

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
                    Button button = new Button(mimeBodyPart.getFileName());
                    hBoxDownloads.getChildren().add(button);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            attachmentLabel.setText("");
        }
    }
}
