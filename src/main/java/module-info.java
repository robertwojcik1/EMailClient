module org.robertwojcik.emailclient.emailclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.activation;
    requires java.mail;

    opens org.robertwojcik.emailclient to javafx.fxml;
    exports org.robertwojcik.emailclient;
    exports org.robertwojcik.emailclient.controller;
    opens org.robertwojcik.emailclient.controller to javafx.fxml;
    exports org.robertwojcik.emailclient.view;
    opens org.robertwojcik.emailclient.view to javafx.fxml;
}