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
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * View untuk menampilkan Struk Tiket (Popup)
 * Pure Java - Tanpa FXML
 */
public class StrukView {
    
    private Stage stage;
    private VBox root;
    
    // Labels untuk data tiket
    private Label lblIdTiket;
    private Label lblTanggalPesan;
    private Label lblNamaPelanggan;
    private Label lblTeleponPelanggan;
    private Label lblEmailPelanggan;
    private Label lblLokasiAsal;
    private Label lblLokasiTujuan;
    private Label lblTanggalBerangkat;
    private Label lblJamBerangkat;
    private Label lblNomorKursi;
    private Label lblHarga;
    private TextArea txtStruk;
    
    private Button btnCetakFile;
    private Button btnTutup;
    
    public StrukView(Stage parentStage) {
        this.stage = new Stage();
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.initOwner(parentStage);
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");
        
        // Title
        Label title = new Label("✅ STRUK PEMESANAN TIKET");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");
        
        // Info Section
        VBox infoSection = createInfoSection();
        
        // Struk Text Area
        Label lblStrukTitle = new Label("📄 Detail Struk:");
        lblStrukTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        txtStruk = new TextArea();
        txtStruk.setEditable(false);
        txtStruk.setPrefHeight(200);
        txtStruk.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        
        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        btnCetakFile = new Button("💾 Simpan ke File");
        btnCetakFile.setStyle(
            "-fx-background-color: #339AF0; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10 20;"
        );
        
        btnTutup = new Button("❌ Tutup");
        btnTutup.setStyle(
            "-fx-background-color: #868E96; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-padding: 10 20;"
        );
        
        buttonBox.getChildren().addAll(btnCetakFile, btnTutup);
        
        // Add all to root
        root.getChildren().addAll(title, infoSection, lblStrukTitle, txtStruk, buttonBox);
    }
    
    private VBox createInfoSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 15; -fx-background-radius: 5;");
        
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(8);
        
        // Initialize labels
        lblIdTiket = new Label("-");
        lblTanggalPesan = new Label("-");
        lblNamaPelanggan = new Label("-");
        lblTeleponPelanggan = new Label("-");
        lblEmailPelanggan = new Label("-");
        lblLokasiAsal = new Label("-");
        lblLokasiTujuan = new Label("-");
        lblTanggalBerangkat = new Label("-");
        lblJamBerangkat = new Label("-");
        lblNomorKursi = new Label("-");
        lblHarga = new Label("-");
        
        // Style for data labels
        String dataStyle = "-fx-font-weight: bold; -fx-text-fill: #2C3E50;";
        lblIdTiket.setStyle(dataStyle);
        lblTanggalPesan.setStyle(dataStyle);
        lblNamaPelanggan.setStyle(dataStyle);
        lblTeleponPelanggan.setStyle(dataStyle);
        lblEmailPelanggan.setStyle(dataStyle);
        lblLokasiAsal.setStyle(dataStyle);
        lblLokasiTujuan.setStyle(dataStyle);
        lblTanggalBerangkat.setStyle(dataStyle);
        lblJamBerangkat.setStyle(dataStyle);
        lblNomorKursi.setStyle(dataStyle);
        lblHarga.setStyle(dataStyle);
        
        // Add to grid
        int row = 0;
        grid.add(createLabel("ID Tiket:"), 0, row);
        grid.add(lblIdTiket, 1, row++);
        
        grid.add(createLabel("Tanggal Pesan:"), 0, row);
        grid.add(lblTanggalPesan, 1, row++);
        
        grid.add(createLabel("Nama:"), 0, row);
        grid.add(lblNamaPelanggan, 1, row++);
        
        grid.add(createLabel("Telepon:"), 0, row);
        grid.add(lblTeleponPelanggan, 1, row++);
        
        grid.add(createLabel("Email:"), 0, row);
        grid.add(lblEmailPelanggan, 1, row++);
        
        grid.add(createLabel("Asal:"), 0, row);
        grid.add(lblLokasiAsal, 1, row++);
        
        grid.add(createLabel("Tujuan:"), 0, row);
        grid.add(lblLokasiTujuan, 1, row++);
        
        grid.add(createLabel("Tanggal Berangkat:"), 0, row);
        grid.add(lblTanggalBerangkat, 1, row++);
        
        grid.add(createLabel("Jam Berangkat:"), 0, row);
        grid.add(lblJamBerangkat, 1, row++);
        
        grid.add(createLabel("Nomor Kursi:"), 0, row);
        grid.add(lblNomorKursi, 1, row++);
        
        grid.add(createLabel("Harga:"), 0, row);
        grid.add(lblHarga, 1, row++);
        
        section.getChildren().add(grid);
        return section;
    }
    
    private Label createLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12px;");
        return lbl;
    }
    
    public void show() {
        Scene scene = new Scene(root, 600, 700);
        stage.setScene(scene);
        stage.setTitle("Struk Pemesanan");
        stage.showAndWait();
    }
    
    // Getters untuk Controller
    public Label getLblIdTiket() { return lblIdTiket; }
    public Label getLblTanggalPesan() { return lblTanggalPesan; }
    public Label getLblNamaPelanggan() { return lblNamaPelanggan; }
    public Label getLblTeleponPelanggan() { return lblTeleponPelanggan; }
    public Label getLblEmailPelanggan() { return lblEmailPelanggan; }
    public Label getLblLokasiAsal() { return lblLokasiAsal; }
    public Label getLblLokasiTujuan() { return lblLokasiTujuan; }
    public Label getLblTanggalBerangkat() { return lblTanggalBerangkat; }
    public Label getLblJamBerangkat() { return lblJamBerangkat; }
    public Label getLblNomorKursi() { return lblNomorKursi; }
    public Label getLblHarga() { return lblHarga; }
    public TextArea getTxtStruk() { return txtStruk; }
    public Button getBtnCetakFile() { return btnCetakFile; }
    public Button getBtnTutup() { return btnTutup; }
    public Stage getStage() { return stage; }
}