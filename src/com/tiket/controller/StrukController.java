/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.controller;

import com.tiket.model.Tiket;
import com.tiket.helper.FileHelper;
import com.tiket.view.StrukView;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

/**
 * Controller untuk StrukView (popup)
 * Menampilkan struk pemesanan
 * 
 * Menerapkan konsep: FILE I/O (via FileHelper)
 */
public class StrukController {
    
    private StrukView view;
    private Tiket tiket;
    private FileHelper fileHelper;
    
    public StrukController(Stage parentStage, Tiket tiket) {
        this.view = new StrukView(parentStage);
        this.tiket = tiket;
        this.fileHelper = new FileHelper();
        setupEventHandlers();
        tampilkanData();
    }
    
    private void setupEventHandlers() {
        view.getBtnCetakFile().setOnAction(e -> handleCetakFile());
        view.getBtnTutup().setOnAction(e -> handleTutup());
    }
    
    private void tampilkanData() {
        if (tiket == null) return;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        view.getLblIdTiket().setText(tiket.getIdTiket());
        view.getLblTanggalPesan().setText(tiket.getWaktuPemesanan() != null ? 
            tiket.getWaktuPemesanan().format(formatter) : "-");
        view.getLblNamaPelanggan().setText(tiket.getNamaPelanggan());
        view.getLblTeleponPelanggan().setText(tiket.getTeleponPelanggan());
        view.getLblEmailPelanggan().setText(tiket.getEmailPelanggan());
        view.getLblLokasiAsal().setText(tiket.getLokasiAsal() != null ? tiket.getLokasiAsal() : "-");
        view.getLblLokasiTujuan().setText(tiket.getLokasiTujuan() != null ? tiket.getLokasiTujuan() : "-");
        view.getLblTanggalBerangkat().setText(tiket.getTanggalBerangkat() != null ? tiket.getTanggalBerangkat() : "-");
        view.getLblJamBerangkat().setText(tiket.getJamBerangkat() != null ? tiket.getJamBerangkat() : "-");
        view.getLblNomorKursi().setText(String.valueOf(tiket.getNomorKursi()));
        view.getLblHarga().setText("Rp " + String.format("%,.2f", tiket.getHarga()));
        
        view.getTxtStruk().setText(tiket.cetakStruk());
    }
    
    private void handleCetakFile() {
        try {
            boolean sukses = fileHelper.saveStrukToFile(tiket);
            
            if (sukses) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sukses");
                alert.setContentText("Struk berhasil disimpan ke:\noutput/struk_" + tiket.getIdTiket() + ".txt");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Gagal menyimpan struk!");
                alert.showAndWait();
            }
            
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void handleTutup() {
        view.getStage().close();
    }
    
    public void show() {
        view.show();
    }
}