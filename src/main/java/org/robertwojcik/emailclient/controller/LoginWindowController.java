package org.robertwojcik.emailclient.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.robertwojcik.emailclient.EmailManager;
import org.robertwojcik.emailclient.ViewFactory;
import org.robertwojcik.emailclient.controller.services.LoginService;
import org.robertwojcik.emailclient.model.EmailAccount;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginWindowController extends BaseController {

    @FXML
    private TextField emailAddressField;

    @FXML
    private Label errorLabel;

    @FXML
    private PasswordField passwordField;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void loginButtonAction() {
        if (fieldsAreValid()) {
            EmailAccount emailAccount = new EmailAccount(emailAddressField.getText(), passwordField.getText());
            LoginService loginService = new LoginService(emailAccount, emailManager);
            loginService.start();
            loginService.setOnSucceeded(event -> {
                EmailLoginResult loginResult = loginService.getValue();

                switch (loginResult) {
                    case SUCCESS:
                        System.out.println("Login successfull");
                        if(!viewFactory.isMainViewInitialized()) {
                            viewFactory.showMainWindow();
                        }
                        Stage stage = (Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                        return;
                    case FAILED_BY_CREDENTIALS:
                        errorLabel.setText("Invalid credentials");
                        return;
                    case FAILED_BY_UNEXPECTED_ERROR:
                        errorLabel.setText("Unexpected error");
                        return;
                    case FAILED_BY_NETWORK:
                        errorLabel.setText("Network error");
                        return;
                    default:
                        return;
                }
            });
        }
    }

    private boolean fieldsAreValid() {
        if(emailAddressField.getText().isEmpty()) {
            errorLabel.setText("Please enter a valid email address");
            return false;
        }
        if(passwordField.getText().isEmpty()) {
            errorLabel.setText("Please enter a password");
            return false;
        }
        return true;
    }


}
