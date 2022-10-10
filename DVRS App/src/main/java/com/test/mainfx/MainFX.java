package com.test.mainfx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        int x = 720, y = 720;

        primaryStage.setTitle("Hello World");
        Group root = new Group();
        Scene scene = new Scene(root,x, y);

        Label headingLbl = new Label("DVRS");
        headingLbl.setFont(new Font("Arial", 32));

        headingLbl.setLayoutX(x/2);
        headingLbl.setLayoutY(y/6);


        root.getChildren().add(headingLbl);
        primaryStage.setScene(scene);
        primaryStage.show();

        /*
        Button btn = new Button();
        btn.setLayoutX(100);
        btn.setLayoutY(80);
        btn.setText("Hello World");
        btn.setOnAction( actionEvent ->
                System.out.println("Hello World"));
        root.getChildren().add(btn);
        primaryStage.setScene(scene);
        primaryStage.show();
         */
    }
}