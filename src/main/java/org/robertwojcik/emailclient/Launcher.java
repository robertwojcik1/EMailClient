package org.robertwojcik.emailclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.robertwojcik.emailclient.controller.persistence.PersistenceAccess;
import org.robertwojcik.emailclient.controller.persistence.ValidAccount;
import org.robertwojcik.emailclient.controller.services.LoginService;
import org.robertwojcik.emailclient.model.EmailAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {

    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();

    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory viewFactory = new ViewFactory(emailManager);
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
        if(!validAccountList.isEmpty()) {
            viewFactory.showMainWindow();
            for(ValidAccount validAccount : validAccountList) {
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(),
                        validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountList = new ArrayList<>();
        for(EmailAccount emailAccount : emailManager.getEmailAccounts()) {
            validAccountList.add(new ValidAccount(emailAccount.getAddress(),
                    emailAccount.getPassword()));
        }
        persistenceAccess.saveToPersistence(validAccountList);
    }
}