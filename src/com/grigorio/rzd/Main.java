package com.grigorio.rzd;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main extends Application {
    // database helper
    private DBHelper dbHelper = new DBHelper();
    final public DBHelper getDbHelper() {
        return dbHelper;
    }

    // a queue to put orders for processing
    private final BlockingQueue<Order> queue = new ArrayBlockingQueue<Order>(10);
    public BlockingQueue<Order> getQueue() {
        return queue;
    }

    private final OrderProcessor wsWorker = new OrderProcessor(queue);

    private MainController mainScreen;

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
                // add end of job signal in a queue
                queue.add(new Order(0));
            }
        });

        // start processor thread
        new Thread(wsWorker).start();
    }

    public MainController getMainScreen() {
        return mainScreen;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
