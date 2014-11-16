package com.grigorio.rzd;

/**
 * Created by philipp on 11/16/14.
 */
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TranslateWindowExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button moveButton = new Button("Move");

        moveButton.setOnAction(event -> {
            double currentX = primaryStage.getX() ;
            DoubleProperty x = new SimpleDoubleProperty(currentX);
            x.addListener((obs, oldX, newX) -> primaryStage.setX(newX.doubleValue()));
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new KeyValue(x, currentX + 100));
            Timeline animation = new Timeline(keyFrame);
            animation.play();
        });

        StackPane root = new StackPane(moveButton);
        Scene scene = new Scene(root, 250, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}