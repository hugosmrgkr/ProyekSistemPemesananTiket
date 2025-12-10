/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.controller;

import com.tiket.view.MainAppView;
import com.tiket.view.PilihModeView;
import javafx.stage.Stage;

/**
 * Controller untuk PilihModeView
 * Handle pemilihan mode: Admin atau Pelanggan
 */
public class MainController {
    
    private PilihModeView view;
    
    public MainController(Stage stage) {
        this.view = new PilihModeView(stage);
        setupEventHandlers();
    }
    
    private void setupEventHandlers() {
        view.getBtnAdmin().setOnAction(e -> handleAdminMode());
        view.getBtnPelanggan().setOnAction(e -> handlePelangganMode());
    }
    
    /**
     * Handle button Admin
     */
    private void handleAdminMode() {
        openMainView("ADMIN");
    }
    
    /**
     * Handle button Pelanggan
     */
    private void handlePelangganMode() {
        openMainView("PELANGGAN");
    }
    
    /**
     * Buka MainAppView dengan mode tertentu
     */
    private void openMainView(String mode) {
        Stage stage = view.getStage();
        AppController appController = new AppController(stage, mode);
        appController.show();
    }
    
    public void show() {
        view.show();
    }
}