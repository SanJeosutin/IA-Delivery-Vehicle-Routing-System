package com.dvrs.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.Panel;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Panel panel = new Panel("Delivery Vehicle Routing System");

        panel.getStyleClass().add("panel-primary");
        BorderPane content = new BorderPane();
        content.setPadding(new Insets(20));

        Label headingtxt = new Label("D");

        Button button = new Button("Hello BootstrapFX");
        button.getStyleClass().setAll("btn", "btn-danger");

        content.setCenter(button);
        panel.setBody(content);

        button.setOnAction(actionEvent ->
                System.out.println("Hello World"));

        Scene scene = new Scene(panel);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setTitle("DVRS");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}