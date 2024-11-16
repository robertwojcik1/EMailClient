package org.robertwojcik.emailclient;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.robertwojcik.emailclient.controller.BaseController;
import org.robertwojcik.emailclient.controller.LoginWindowController;
import org.robertwojcik.emailclient.controller.MainWindowController;
import org.robertwojcik.emailclient.controller.OptionsWindowController;
import org.robertwojcik.emailclient.view.ColorTheme;
import org.robertwojcik.emailclient.view.FontSize;

import java.io.IOException;
import java.util.ArrayList;

public class ViewFactory {

    private EmailManager emailManager;
    private ColorTheme colorTheme = ColorTheme.LIGHT;
    private ArrayList<Stage> activeStages;

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    private FontSize fontSize = FontSize.MEDIUM;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<>();
    }

    public void showLoginWindow() {
        System.out.println("login window called");

        BaseController controller = new LoginWindowController(emailManager,
                this, "LoginWindow.fxml");
        initializeStage(controller);
    }

    public void showMainWindow() {
        System.out.println("main window called");

        BaseController controller = new MainWindowController(emailManager,
                this, "MainWindow.fxml");
        initializeStage(controller);
    }

    public void showOptionsWindow() {
        System.out.println("opts window called");

        BaseController controller = new OptionsWindowController(emailManager,
                this, "OptionsWindow.fxml");
        initializeStage(controller);
    }

    private void initializeStage(BaseController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller
                .getFxmlName()));
        fxmlLoader.setController(controller);
        Parent parent;

        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        activeStages.add(stage);
    }

    public void closeStage(Stage stageToClose) {
        stageToClose.close();
        activeStages.remove(stageToClose);
    }

    public void updateStyles() {
        for (Stage stage : activeStages) {
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
            scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());

        }
    }
}
