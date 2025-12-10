/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * View untuk memilih mode: Admin atau Pelanggan
 * Pure Java - Tanpa FXML
 */
public class PilihModeView {
    
    private Stage stage;
    private VBox root;
    private Button btnAdmin;
    private Button btnPelanggan;
    
    public PilihModeView(Stage stage) {
        this.stage = stage;
        initializeUI();
    }
    
    private void initializeUI() {
        // Root Container
        root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");
        
        // Title
        Label lblTitle = new Label("SISTEM PEMESANAN TIKET BUS");
        lblTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label lblSubtitle = new Label("Pilih Mode Akses");
        lblSubtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #E0E0E0;");
        
        // Button Admin
        btnAdmin = createStyledButton("🔐 ADMIN", "#339AF0");
        btnAdmin.setPrefSize(250, 60);
        
        // Button Pelanggan
        btnPelanggan = createStyledButton("👤 PELANGGAN", "#51CF66");
        btnPelanggan.setPrefSize(250, 60);
        
        // Add to root
        root.getChildren().addAll(lblTitle, lblSubtitle, btnAdmin, btnPelanggan);
    }
    
    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: derive(" + color + ", -10%);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;"
        ));
        
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        ));
        
        return btn;
    }
    
    public void show() {
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Sistem Tiket Bus - Pilih Mode");
        stage.centerOnScreen();
        stage.show();
    }
    
    // Getters untuk Controller
    public Button getBtnAdmin() {
        return btnAdmin;
    }
    
    public Button getBtnPelanggan() {
        return btnPelanggan;
    }
    
    public Stage getStage() {
        return stage;
    }
}