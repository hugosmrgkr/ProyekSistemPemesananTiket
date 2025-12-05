/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.controller;

import com.tiket.database.*;
import com.tiket.model.*;
import com.tiket.exception.*;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Main Controller untuk main_view.fxml
 * Handle semua panel: CRUD Jadwal, Pencarian, Pemesanan, Pilih Kursi
 * 
 * Menerapkan konsep:
 * - COLLECTIONS (ArrayList, HashMap, ObservableList)
 * - EXCEPTION HANDLING (try-catch, custom exceptions)
 */
public class AppController {
    
    @FXML private VBox sidebar;
    @FXML private Pane panelKonten;
    @FXML private Label lblMode;
    
    private String modeAktif;
    
    // Database
    private JadwalDB jadwalDB;
    private KursiDB kursiDB;
    private TiketDB tiketDB;
    
    // Collections
    private ObservableList<Jadwal> dataJadwal;
    private List<Jadwal> hasilPencarian;
    private Map<Integer, Kursi> kursiMap;
    
    private Jadwal jadwalDipilih;
    
    @FXML
    public void initialize() {
        jadwalDB = new JadwalDB();
        kursiDB = new KursiDB();
        tiketDB = new TiketDB();
        
        dataJadwal = FXCollections.observableArrayList();
        hasilPencarian = new ArrayList<>();
        kursiMap = new HashMap<>();
    }
    
    public void setMode(String mode) {
        this.modeAktif = mode;
        lblMode.setText("Mode: " + mode);
        
        if (mode.equals("ADMIN")) {
            showMenuAdmin();
            showPanelCRUDJadwal();
        } else {
            showMenuPelanggan();
            showPanelCekJadwal();
        }
    }
    
    // ================================================================
    // ADMIN
    // ================================================================
    
    private void showMenuAdmin() {
        sidebar.getChildren().clear();
        
        Button btnCRUD = createMenuButton("CRUD Jadwal", "#339AF0");
        btnCRUD.setOnAction(e -> showPanelCRUDJadwal());
        
        Button btnKeluar = createMenuButton("Keluar", "#868E96");
        btnKeluar.setOnAction(e -> handleKeluar());
        
        sidebar.getChildren().addAll(btnCRUD, btnKeluar);
        sidebar.setSpacing(10);
        sidebar.setPadding(new Insets(20));
    }
    
    private void showPanelCRUDJadwal() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        
        Label title = new Label("Form CRUD Jadwal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField tfKode = new TextField();
        TextField tfAsal = new TextField();
        TextField tfTujuan = new TextField();
        TextField tfTanggal = new TextField();
        TextField tfJam = new TextField();
        TextField tfHarga = new TextField();
        
        tfKode.setPromptText("JDW001");
        tfTanggal.setPromptText("2024-12-31");
        tfJam.setPromptText("08:00");
        
        form.add(new Label("KODE:"), 0, 0);
        form.add(tfKode, 1, 0);
        form.add(new Label("ASAL:"), 0, 1);
        form.add(tfAsal, 1, 1);
        form.add(new Label("TUJUAN:"), 0, 2);
        form.add(tfTujuan, 1, 2);
        form.add(new Label("TANGGAL:"), 0, 3);
        form.add(tfTanggal, 1, 3);
        form.add(new Label("JAM:"), 0, 4);
        form.add(tfJam, 1, 4);
        form.add(new Label("HARGA:"), 0, 5);
        form.add(tfHarga, 1, 5);
        
        // Buttons
        HBox buttons = new HBox(10);
        Button btnAdd = new Button("Add");
        Button btnUpdate = new Button("Update");
        Button btnDelete = new Button("Delete");
        Button btnRefresh = new Button("Refresh");
        
        btnAdd.setStyle("-fx-background-color: #51CF66; -fx-text-fill: white;");
        btnUpdate.setStyle("-fx-background-color: #339AF0; -fx-text-fill: white;");
        btnDelete.setStyle("-fx-background-color: #FF6B6B; -fx-text-fill: white;");
        
        buttons.getChildren().addAll(btnAdd, btnUpdate, btnDelete, btnRefresh);
        
        // Table
        TableView<Jadwal> table = createJadwalTable();
        
        // Actions
        btnAdd.setOnAction(e -> handleAddJadwal(tfKode, tfAsal, tfTujuan, tfTanggal, tfJam, tfHarga, table));
        btnUpdate.setOnAction(e -> handleUpdateJadwal(table, tfAsal, tfTujuan, tfHarga));
        btnDelete.setOnAction(e -> handleDeleteJadwal(table));
        btnRefresh.setOnAction(e -> loadAllJadwal(table));
        
        // Selection listener
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tfKode.setText(newVal.getIdJadwal());
                tfAsal.setText(newVal.getLokasiAsal());
                tfTujuan.setText(newVal.getLokasiTujuan());
                tfHarga.setText(String.valueOf(newVal.getHarga()));
                
                if (newVal.getWaktuBerangkat() != null) {
                    tfTanggal.setText(newVal.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    tfJam.setText(newVal.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
        });
        
        panel.getChildren().addAll(title, form, buttons, table);
        loadAllJadwal(table);
        switchPanel(panel);
    }
    
    private void handleAddJadwal(TextField tfKode, TextField tfAsal, TextField tfTujuan, 
                                  TextField tfTanggal, TextField tfJam, TextField tfHarga, TableView<Jadwal> table) {
        try {
            // Validasi
            if (tfKode.getText().isEmpty() || tfAsal.getText().isEmpty()) {
                throw new ValidationException("Semua field harus diisi!");
            }
            
            Jadwal jadwal = new Jadwal();
            jadwal.setIdJadwal(tfKode.getText());
            jadwal.setIdBus("BUS001");
            jadwal.setLokasiAsal(tfAsal.getText());
            jadwal.setLokasiTujuan(tfTujuan.getText());
            jadwal.setHarga(Double.parseDouble(tfHarga.getText()));
            jadwal.setKursiTersedia(40);
            
            String dateTimeStr = tfTanggal.getText() + " " + tfJam.getText() + ":00";
            jadwal.setWaktuBerangkat(LocalDateTime.parse(dateTimeStr, 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            jadwal.setWaktuTiba(jadwal.getWaktuBerangkat().plusHours(3));
            
            if (jadwalDB.create(jadwal)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal berhasil ditambahkan!");
                loadAllJadwal(table);
                clearForm(tfKode, tfAsal, tfTujuan, tfTanggal, tfJam, tfHarga);
            }
            
        } catch (ValidationException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Harga harus angka!");
        } catch (DatabaseException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Format tanggal/jam salah!");
        }
    }
    
    private void handleUpdateJadwal(TableView<Jadwal> table, TextField tfAsal, TextField tfTujuan, TextField tfHarga) {
        Jadwal selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih jadwal terlebih dahulu!");
            return;
        }
        
        try {
            selected.setLokasiAsal(tfAsal.getText());
            selected.setLokasiTujuan(tfTujuan.getText());
            selected.setHarga(Double.parseDouble(tfHarga.getText()));
            
            if (jadwalDB.update(selected)) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal berhasil diupdate!");
                loadAllJadwal(table);
            }
        } catch (DatabaseException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        }
    }
    
    private void handleDeleteJadwal(TableView<Jadwal> table) {
        Jadwal selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih jadwal terlebih dahulu!");
            return;
        }
        
        try {
            if (jadwalDB.delete(selected.getIdJadwal())) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal berhasil dihapus!");
                loadAllJadwal(table);
            }
        } catch (DatabaseException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        }
    }
    
    private void loadAllJadwal(TableView<Jadwal> table) {
        try {
            List<Jadwal> list = jadwalDB.getAll();
            dataJadwal.clear();
            dataJadwal.addAll(list);
            table.setItems(dataJadwal);
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }
    
    // ================================================================
    // PELANGGAN
    // ================================================================
    
    private void showMenuPelanggan() {
        sidebar.getChildren().clear();
        
        Button btnCek = createMenuButton("Cek Jadwal", "#339AF0");
        btnCek.setOnAction(e -> showPanelCekJadwal());
        
        Button btnPesan = createMenuButton("Pesan Tiket", "#51CF66");
        btnPesan.setOnAction(e -> showPanelPemesanan());
        
        Button btnKeluar = createMenuButton("Keluar", "#868E96");
        btnKeluar.setOnAction(e -> handleKeluar());
        
        sidebar.getChildren().addAll(btnCek, btnPesan, btnKeluar);
        sidebar.setSpacing(10);
        sidebar.setPadding(new Insets(20));
    }
    
    private void showPanelCekJadwal() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        
        Label title = new Label("Cek Jadwal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Form
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField tfAsal = new TextField();
        TextField tfTujuan = new TextField();
        TextField tfTanggal = new TextField();
        
        tfAsal.setPromptText("Jakarta");
        tfTujuan.setPromptText("Bandung");
        tfTanggal.setPromptText("2024-12-31");
        
        form.add(new Label("ASAL:"), 0, 0);
        form.add(tfAsal, 1, 0);
        form.add(new Label("TUJUAN:"), 0, 1);
        form.add(tfTujuan, 1, 1);
        form.add(new Label("TANGGAL:"), 0, 2);
        form.add(tfTanggal, 1, 2);
        
        Button btnCari = new Button("Cari Jadwal");
        btnCari.setStyle("-fx-background-color: #339AF0; -fx-text-fill: white;");
        
        TableView<Jadwal> table = createJadwalTable();
        
        Button btnPilih = new Button("Pilih Jadwal Ini");
        btnPilih.setDisable(true);
        btnPilih.setStyle("-fx-background-color: #51CF66; -fx-text-fill: white;");
        
        btnCari.setOnAction(e -> handleCariJadwal(tfAsal, tfTujuan, tfTanggal, table, btnPilih));
        btnPilih.setOnAction(e -> handlePilihJadwal(table));
        
        panel.getChildren().addAll(title, form, btnCari, table, btnPilih);
        switchPanel(panel);
    }
    
    private void handleCariJadwal(TextField tfAsal, TextField tfTujuan, TextField tfTanggal, 
                                   TableView<Jadwal> table, Button btnPilih) {
        try {
            if (tfAsal.getText().isEmpty()) {
                throw new ValidationException("Field pencarian harus diisi!");
            }
            
            hasilPencarian = jadwalDB.search(tfAsal.getText(), tfTujuan.getText(), tfTanggal.getText());
            
            if (hasilPencarian.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Info", "Tidak ada jadwal ditemukan");
            } else {
                ObservableList<Jadwal> obs = FXCollections.observableArrayList(hasilPencarian);
                table.setItems(obs);
                btnPilih.setDisable(false);
            }
            
        } catch (ValidationException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        } catch (DatabaseException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
        }
    }
    
    private void handlePilihJadwal(TableView<Jadwal> table) {
        jadwalDipilih = table.getSelectionModel().getSelectedItem();
        if (jadwalDipilih == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih jadwal terlebih dahulu!");
        } else {
            showPanelPemesanan();
        }
    }
    
    private void showPanelPemesanan() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        
        Label title = new Label("Form Pemesanan Tiket");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        VBox infoBox = new VBox(5);
        if (jadwalDipilih != null) {
            Label lblInfo = new Label("Jadwal: " + jadwalDipilih.toString());
            lblInfo.setStyle("-fx-font-weight: bold;");
            infoBox.getChildren().add(lblInfo);
        } else {
            infoBox.getChildren().add(new Label("Pilih jadwal dari menu Cek Jadwal"));
        }
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField tfNama = new TextField();
        TextField tfTelepon = new TextField();
        TextField tfEmail = new TextField();
        
        tfNama.setPromptText("Nama Lengkap");
        tfTelepon.setPromptText("08123456789");
        tfEmail.setPromptText("email@example.com");
        
        form.add(new Label("NAMA:"), 0, 0);
        form.add(tfNama, 1, 0);
        form.add(new Label("TELEPON:"), 0, 1);
        form.add(tfTelepon, 1, 1);
        form.add(new Label("EMAIL:"), 0, 2);
        form.add(tfEmail, 1, 2);
        
        Button btnPilihKursi = new Button("Pilih Kursi");
        btnPilihKursi.setDisable(jadwalDipilih == null);
        btnPilihKursi.setStyle("-fx-background-color: #51CF66; -fx-text-fill: white;");
        
        btnPilihKursi.setOnAction(e -> {
            try {
                if (tfNama.getText().isEmpty()) {
                    throw new ValidationException("Data pelanggan harus diisi!");
                }
                showPanelPilihKursi(tfNama.getText(), tfTelepon.getText(), tfEmail.getText());
            } catch (ValidationException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });
        
        panel.getChildren().addAll(title, infoBox, form, btnPilihKursi);
        switchPanel(panel);
    }
    
    private void showPanelPilihKursi(String nama, String telepon, String email) {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(20));
        
        Label title = new Label("Pilih Nomor Kursi");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        GridPane layoutKursi = new GridPane();
        layoutKursi.setHgap(10);
        layoutKursi.setVgap(10);
        layoutKursi.setAlignment(Pos.CENTER);
        
        try {
            List<Kursi> kursiList = kursiDB.getByJadwal(jadwalDipilih.getIdJadwal());
            
            kursiMap.clear();
            for (Kursi k : kursiList) {
                kursiMap.put(k.getNomorKursi(), k);
            }
            
            for (int i = 1; i <= 40; i++) {
                Button btnKursi = new Button(String.valueOf(i));
                btnKursi.setPrefSize(50, 50);
                
                int nomorKursi = i;
                Kursi kursi = kursiMap.get(nomorKursi);
                
                if (kursi != null && !kursi.isTersedia()) {
                    btnKursi.setStyle("-fx-background-color: #FF6B6B;");
                    btnKursi.setDisable(true);
                } else {
                    btnKursi.setStyle("-fx-background-color: #51CF66;");
                    btnKursi.setOnAction(e -> handleKonfirmasiPesan(nama, telepon, email, nomorKursi));
                }
                
                layoutKursi.add(btnKursi, (i - 1) % 5, (i - 1) / 5);
            }
            
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
        
        HBox legend = new HBox(20);
        legend.setAlignment(Pos.CENTER);
        legend.getChildren().addAll(new Label("🟢 Tersedia"), new Label("🔴 Terisi"));
        
        panel.getChildren().addAll(title, layoutKursi, legend);
        switchPanel(panel);
    }
    
    private void handleKonfirmasiPesan(String nama, String telepon, String email, int nomorKursi) {
        try {
            String idTiket = "TIK" + System.currentTimeMillis();
            
            Tiket tiket = new Tiket();
            tiket.setIdTiket(idTiket);
            tiket.setIdJadwal(jadwalDipilih.getIdJadwal());
            tiket.setNamaPelanggan(nama);
            tiket.setTeleponPelanggan(telepon);
            tiket.setEmailPelanggan(email);
            tiket.setNomorKursi(nomorKursi);
            tiket.setHarga(jadwalDipilih.getHarga());
            tiket.setWaktuPemesanan(LocalDateTime.now());
            
            if (tiketDB.create(tiket)) {
                kursiDB.updateStatus("KRS" + jadwalDipilih.getIdJadwal() + nomorKursi, false);
                
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pemesanan berhasil!");
                
                tiket.setLokasiAsal(jadwalDipilih.getLokasiAsal());
                tiket.setLokasiTujuan(jadwalDipilih.getLokasiTujuan());
                
                if (jadwalDipilih.getWaktuBerangkat() != null) {
                    tiket.setTanggalBerangkat(jadwalDipilih.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    tiket.setJamBerangkat(jadwalDipilih.getWaktuBerangkat().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                
                showStrukPopup(tiket);
                jadwalDipilih = null;
                showPanelCekJadwal();
            }
            
        } catch (DatabaseException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }
    
    private void showStrukPopup(Tiket tiket) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/struk.fxml"));
            Parent root = loader.load();
            
            StrukController controller = loader.getController();
            controller.setTiket(tiket);
            
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Struk Pemesanan");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ================================================================
    // HELPER
    // ================================================================
    
    private Button createMenuButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(150);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-font-size: 14px; -fx-background-color: " + color + "; -fx-text-fill: white;");
        return btn;
    }
    
    private TableView<Jadwal> createJadwalTable() {
        TableView<Jadwal> table = new TableView<>();
        table.setPrefHeight(300);
        
        TableColumn<Jadwal, String> colKode = new TableColumn<>("Kode");
        colKode.setCellValueFactory(new PropertyValueFactory<>("idJadwal"));
        
        TableColumn<Jadwal, String> colAsal = new TableColumn<>("Asal");
        colAsal.setCellValueFactory(new PropertyValueFactory<>("lokasiAsal"));
        
        TableColumn<Jadwal, String> colTujuan = new TableColumn<>("Tujuan");
        colTujuan.setCellValueFactory(new PropertyValueFactory<>("lokasiTujuan"));
        
        TableColumn<Jadwal, Double> colHarga = new TableColumn<>("Harga");
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        
        table.getColumns().addAll(colKode, colAsal, colTujuan, colHarga);
        return table;
    }
    
    private void switchPanel(javafx.scene.Node panel) {
        panelKonten.getChildren().clear();
        panelKonten.getChildren().add(panel);
    }
    
    private void clearForm(TextField... fields) {
        for (TextField f : fields) f.clear();
    }
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleKeluar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/pilih_mode.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) sidebar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Sistem Tiket Bus");
            stage.centerOnScreen();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
