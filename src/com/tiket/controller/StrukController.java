/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.controller;

import com.tiket.model.Tiket;
import com.tiket.helper.FileHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

/**
 * Controller untuk struk.fxml (popup)
 * Menampilkan struk pemesanan
 * 
 * Menerapkan konsep: FILE I/O (via FileHelper)
 */
public class StrukController {
    
    @FXML private Label lblIdTiket;
    @FXML private Label lblTanggalPesan;
    @FXML private Label lblNamaPelanggan;
    @FXML private Label lblTeleponPelanggan;
    @FXML private Label lblEmailPelanggan;
    @FXML private Label lblLokasiAsal;
    @FXML private Label lblLokasiTujuan;
    @FXML private Label lblTanggalBerangkat;
    @FXML private Label lblJamBerangkat;
    @FXML private Label lblNomorKursi;
    @FXML private Label lblHarga;
    @FXML private TextArea txtStruk;
    
    private Tiket tiket;
    private FileHelper fileHelper;
    
    @FXML
    public void initialize() {
        fileHelper = new FileHelper();
    }
    
    public void setTiket(Tiket tiket) {
        this.tiket = tiket;
        tampilkanData();
    }
    
    private void tampilkanData() {
        if (tiket == null) return;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        lblIdTiket.setText(tiket.getIdTiket());
        lblTanggalPesan.setText(tiket.getWaktuPemesanan() != null ? 
            tiket.getWaktuPemesanan().format(formatter) : "-");
        lblNamaPelanggan.setText(tiket.getNamaPelanggan());
        lblTeleponPelanggan.setText(tiket.getTeleponPelanggan());
        lblEmailPelanggan.setText(tiket.getEmailPelanggan());
        lblLokasiAsal.setText(tiket.getLokasiAsal() != null ? tiket.getLokasiAsal() : "-");
        lblLokasiTujuan.setText(tiket.getLokasiTujuan() != null ? tiket.getLokasiTujuan() : "-");
        lblTanggalBerangkat.setText(tiket.getTanggalBerangkat() != null ? tiket.getTanggalBerangkat() : "-");
        lblJamBerangkat.setText(tiket.getJamBerangkat() != null ? tiket.getJamBerangkat() : "-");
        lblNomorKursi.setText(String.valueOf(tiket.getNomorKursi()));
        lblHarga.setText("Rp " + String.format("%,.2f", tiket.getHarga()));
        
        if (txtStruk != null) {
            txtStruk.setText(tiket.cetakStruk());
        }
    }
    
    @FXML
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
    
    @FXML
    private void handleTutup() {
        Stage stage = (Stage) lblIdTiket.getScene().getWindow();
        stage.close();
    }
}