package com.grigorio.rzd;

import com.grigorio.rzd.OrderProcessor.Order;
import com.grigorio.rzd.OrderProcessor.OrderProcessor;
import com.grigorio.rzd.OrderProcessor.TicketServiceJob;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.*;

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
        public static final String stridNoInsurance = "Auto uncheck passenger's insurance";

        public static final String strLogLevel = "Level of logging";
        public static final String strLogFileName = "Log file name";
    }

    public static Map<String, Level> mapLogLevels = new HashMap<>();
    static {
        mapLogLevels.put("OFF", Level.OFF);
        mapLogLevels.put("SEVERE", Level.SEVERE);
        mapLogLevels.put("WARNING", Level.WARNING);
        mapLogLevels.put("INFO", Level.INFO);
        mapLogLevels.put("CONFIG", Level.CONFIG);
        mapLogLevels.put("FINE", Level.FINE);
        mapLogLevels.put("FINER", Level.FINER);
        mapLogLevels.put("FINEST", Level.FINEST);
        mapLogLevels.put("ALL", Level.ALL);
    }

    private Logger logger = Logger.getLogger("com.grigorio");

    // database helper
    private DBHelper dbHelper = new DBHelper();
    final public DBHelper getDbHelper() {
        return dbHelper;
    }

    // a queue to put orders for processing
    private final BlockingQueue<TicketServiceJob> queue = new ArrayBlockingQueue<>(10);

    public BlockingQueue<TicketServiceJob> getQueue() {
        return queue;
    }

    // a worker thread for order processing
    private final OrderProcessor wsWorker = new OrderProcessor(queue);

    private MainController mainScreen;

    @Override
    public void start(final Stage primaryStage) throws Exception{
        String strVersion = "0.0.12";

        setupLogger();
        logger.info("Application started");

        FXMLLoader loader = new FXMLLoader(MainController.class.getResource("main.fxml"));
        Parent root = loader.load();

        mainScreen = loader.getController();
        mainScreen.setApp(this);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Продажа билетов РЖД - Версия " + strVersion);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {
            dbHelper.closeConnection();

            Stage stageClientSearch = mainScreen.getStageClientSearch();
            if (stageClientSearch != null) {
                stageClientSearch.close();
            }

            primaryStage.close();

            // add end of job signal in a queue
            queue.add(new Order(0, ""));
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

    public final Logger getLogger() {
        return logger;
    }

    private void setupLogger() throws IOException {
        java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userRoot().node("com.grigorio.rzd");

        Handler fh = new FileHandler(prefs.get(Preferences.strLogFileName, "ticket.log"));
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        logger.setLevel(mapLogLevels.get(prefs.get(Preferences.strLogLevel, Level.SEVERE.toString())));
    }
}
