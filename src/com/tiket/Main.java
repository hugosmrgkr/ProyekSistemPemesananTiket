package com.tiket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("=== SISTEM PEMESANAN TIKET BUS ===");
            System.out.println("Memuat halaman awal...");

            Parent root = FXMLLoader.load(getClass().getResource("/view/pilih_mode.fxml"));

            primaryStage.setTitle("Sistem Pemesanan Tiket Bus");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            System.out.println("ERROR: Gagal memuat halaman pilih_mode.fxml");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
