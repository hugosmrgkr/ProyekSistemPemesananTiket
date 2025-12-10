package com.tiket.controller;

// Import explicit untuk Database
import com.tiket.database.JadwalDB;
import com.tiket.database.KursiDB;
import com.tiket.database.TiketDB;

// Import explicit untuk Model
import com.tiket.model.Jadwal;
import com.tiket.model.Kursi;
import com.tiket.model.Tiket;

// Import explicit untuk Exception
import com.tiket.exception.DatabaseException;
import com.tiket.exception.ValidationException;

// Import explicit untuk View - INI YANG PALING PENTING!
import com.tiket.view.MainAppView;

// Import JavaFX Collections
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Import JavaFX Geometry
import javafx.geometry.Insets;
import javafx.geometry.Pos;

// Import JavaFX Controls
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// Import JavaFX Layout
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Import JavaFX Stage
import javafx.stage.Stage;

// Import Java Time
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Import Java Utils
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main Controller untuk MainAppView
 * Handle semua panel: CRUD Jadwal, Pencarian, Pemesanan, Pilih Kursi
 * 
 * Menerapkan konsep:
 * - COLLECTIONS (ArrayList, HashMap, ObservableList)
 * - EXCEPTION HANDLING (try-catch, custom exceptions)
 */
public class AppController {
    
    private final MainAppView view;
    private final Stage stage;
    private final String modeAktif;
    
    // Database
    private final JadwalDB jadwalDB;
    private final KursiDB kursiDB;
    private final TiketDB tiketDB;
    
    // Collections
    private final ObservableList<Jadwal> dataJadwal;
    private List<Jadwal> hasilPencarian;
    private final Map<Integer, Kursi> kursiMap;
    
    private Jadwal jadwalDipilih;
    
    public AppController(Stage stage, String mode) {
        this.stage = stage;
        this.modeAktif = mode;
        this.view = new MainAppView(stage);
        
        // Initialize database
        this.jadwalDB = new JadwalDB();
        this.kursiDB = new KursiDB();
        this.tiketDB = new TiketDB();
        
        // Initialize collections
        this.dataJadwal = FXCollections.observableArrayList();
        this.hasilPencarian = new ArrayList<>();
        this.kursiMap = new HashMap<>();
        
        // Setup UI based on mode
        setupMode();
    }
    
    private void setupMode() {
        view.getLblMode().setText("Mode: " + modeAktif);
        
        if (modeAktif.equals("ADMIN")) {
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
        VBox sidebar = view.getSidebar();
        sidebar.getChildren().clear();
        
        Button btnCRUD = createMenuButton("CRUD Jadwal", "#339AF0");
        btnCRUD.setOnAction(e -> showPanelCRUDJadwal());
        
        Button btnKeluar = createMenuButton("Keluar", "#868E96");
        btnKeluar.setOnAction(e -> handleKeluar());
        
        sidebar.getChildren().addAll(btnCRUD, btnKeluar);
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
        VBox sidebar = view.getSidebar();
        sidebar.getChildren().clear();
        
        Button btnCek = createMenuButton("Cek Jadwal", "#339AF0");
        btnCek.setOnAction(e -> showPanelCekJadwal());
        
        Button btnPesan = createMenuButton("Pesan Tiket", "#51CF66");
        btnPesan.setOnAction(e -> showPanelPemesanan());
        
        Button btnKeluar = createMenuButton("Keluar", "#868E96");
        btnKeluar.setOnAction(e -> handleKeluar());
        
        sidebar.getChildren().addAll(btnCek, btnPesan, btnKeluar);
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
        StrukController strukController = new StrukController(stage, tiket);
        strukController.show();
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
        view.getPanelKonten().getChildren().clear();
        view.getPanelKonten().getChildren().add(panel);
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
    
    private void handleKeluar() {
        MainController mainController = new MainController(stage);
        mainController.show();
    }
    
    public void show() {
        view.show();
    }
}