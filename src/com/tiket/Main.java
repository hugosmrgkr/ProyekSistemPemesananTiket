package com.tiket;

import com.tiket.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class untuk menjalankan aplikasi
 * Entry point JavaFX
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            MainController controller = new MainController(primaryStage);
            controller.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}