package com.grigorio.rzd;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main extends Application {
    // preferences
    public static class Preferences {
        public static final String stridDBLoc = "Database Location";
        public static final String stridOutDir = "Output Directory";
        public static final String stridPrivKeyLoc = "Private Key Location";
        public static final String stridPrivKeyPwd = "Private Key Password";
        public static final String stridUsername = "Server Username";

        public static final String stridSSAutofill = "Autofill RZD Self-service credentials";
        public static final String stridSSUser = "RZD Self-service username";
        public static final String stridSSPassword = "RZD Self-service password";
    }

    private String strVersion = "0.0.10";

    // database helper
    private DBHelper dbHelper = new DBHelper();
    final public DBHelper getDbHelper() {
        return dbHelper;
    }

    // a queue to put orders for processing
    private final BlockingQueue<TicketServiceJob> queue = new ArrayBlockingQueue<TicketServiceJob>(10);
    public BlockingQueue<TicketServiceJob> getQueue() {
        return queue;
    }

    // a worker thread for order processing
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
        primaryStage.setTitle("Продажа билетов РЖД - Версия " + strVersion);
        primaryStage.setScene(scene);

        // get screen size
        Rectangle2D screen = Screen.getPrimary().getBounds();
        primaryStage.setX(screen.getMinX());
        primaryStage.setY(screen.getMinY());
        primaryStage.setWidth(screen.getWidth());
        primaryStage.setHeight(screen.getHeight());

        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                dbHelper.closeConnection();

                Stage stageClientSearch = mainScreen.getStageClientSearch();
                if (stageClientSearch != null) {
                    stageClientSearch.close();
                }

                primaryStage.close();

                // add end of job signal in a queue
                queue.add(new Order(0, ""));
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
