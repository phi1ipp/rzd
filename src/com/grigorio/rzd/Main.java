package com.grigorio.rzd;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;

public class Main extends Application {
    private DBHelper dbHelper = new DBHelper();

    private MainController mainScreen;

    final public DBHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public void start(final Stage primaryStage) throws Exception{
        //init DB connection

        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("main.fxml"));
        Parent root = (Parent) loader.load();

        mainScreen = loader.getController();
        mainScreen.setApp(this);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Продажа билетов РЖД");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                dbHelper.closeConnection();
                primaryStage.close();
            }
        });

    }

    public MainController getMainScreen() {
        return mainScreen;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
