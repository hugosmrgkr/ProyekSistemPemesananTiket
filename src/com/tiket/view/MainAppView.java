/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main View dengan Sidebar dan Panel Konten
 * Pure Java - Tanpa FXML
 */
public class MainAppView {
    
    private Stage stage;
    private BorderPane root;
    private VBox sidebar;
    private Pane panelKonten;
    private Label lblMode;
    
    public MainAppView(Stage stage) {
        this.stage = stage;
        initializeUI();
    }
    
    private void initializeUI() {
        // Root Layout
        root = new BorderPane();
        
        // Header
        VBox header = createHeader();
        root.setTop(header);
        
        // Sidebar (Left)
        sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2C3E50; -fx-min-width: 200px;");
        root.setLeft(sidebar);
        
        // Panel Konten (Center)
        panelKonten = new Pane();
        panelKonten.setStyle("-fx-background-color: #ECF0F1;");
        root.setCenter(panelKonten);
    }
    
    private VBox createHeader() {
        VBox header = new VBox();
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #34495E;");
        
        Label lblTitle = new Label("🚌 SISTEM PEMESANAN TIKET BUS");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        lblMode = new Label("Mode: -");
        lblMode.setStyle("-fx-font-size: 14px; -fx-text-fill: #BDC3C7;");
        
        header.getChildren().addAll(lblTitle, lblMode);
        return header;
    }
    
    public void show() {
        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Sistem Tiket Bus");
        stage.centerOnScreen();
        stage.show();
    }
    
    // Getters
    public VBox getSidebar() {
        return sidebar;
    }
    
    public Pane getPanelKonten() {
        return panelKonten;
    }
    
    public Label getLblMode() {
        return lblMode;
    }
    
    public Stage getStage() {
        return stage;
    }
}