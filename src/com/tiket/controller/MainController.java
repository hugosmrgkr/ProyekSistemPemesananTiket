/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller untuk pilih_mode.fxml
 * Handle pemilihan mode: Admin atau Pelanggan
 */
public class MainController {
    
    @FXML
    private Button btnAdmin;
    
    @FXML
    private Button btnPelanggan;
    
    /**
     * Handle button Admin
     */
    @FXML
    private void handleAdminMode() {
        openMainView("ADMIN");
    }
    
    /**
     * Handle button Pelanggan
     */
    @FXML
    private void handlePelangganMode() {
        openMainView("PELANGGAN");
    }
    
    /**
     * Buka main_view.fxml dengan mode tertentu
     */
    private void openMainView(String mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main_view.fxml"));
            Parent root = loader.load();
            
            // Set mode
            AppController controller = loader.getController();
            controller.setMode(mode);
            
            // Ganti scene
            Stage stage = (Stage) btnAdmin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistem Tiket Bus - " + mode);
            stage.centerOnScreen();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}