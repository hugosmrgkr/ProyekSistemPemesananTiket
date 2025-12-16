package com.tiket.view;

import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.JadwalNotFoundException;
import com.tiket.model.Jadwal;
import com.tiket.model.Kursi;
import com.tiket.helper.Helper;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JadwalForm extends JFrame {
    private JadwalController jadwalController;
    private JTextField txtAsal, txtTujuan, txtBerangkat, txtTiba, txtHarga;
    private JButton btnSimpan, btnBatal;
    
    private String mode; // "tambah" atau "edit"
    private String idJadwal; // untuk mode edit
    
    // Constructor untuk mode tambah
    public JadwalForm(JadwalController controller) {
        this.jadwalController = controller;
        this.mode = "tambah";
        setTitle("Tambah Jadwal");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }
    
    // Constructor untuk mode edit
    public JadwalForm(JadwalController controller, Jadwal jadwal) {
        this.jadwalController = controller;
        this.mode = "edit";
        this.idJadwal = jadwal.getIdJadwal();
        setTitle("Edit Jadwal - " + idJadwal);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadData(jadwal);
    }
    
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel lblTitle = new JLabel(
            mode.equals("tambah") ? "FORM TAMBAH JADWAL" : "FORM EDIT JADWAL",
            SwingConstants.CENTER
        );
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        formPanel.add(new JLabel("Lokasi Asal:"));
        txtAsal = new JTextField();
        formPanel.add(txtAsal);
        
        formPanel.add(new JLabel("Lokasi Tujuan:"));
        txtTujuan = new JTextField();
        formPanel.add(txtTujuan);
        
        formPanel.add(new JLabel("Waktu Berangkat:"));
        txtBerangkat = new JTextField("yyyy-MM-dd HH:mm");
        txtBerangkat.setForeground(Color.GRAY);
        txtBerangkat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtBerangkat.getText().equals("yyyy-MM-dd HH:mm")) {
                    txtBerangkat.setText("");
                    txtBerangkat.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtBerangkat.getText().isEmpty()) {
                    txtBerangkat.setText("yyyy-MM-dd HH:mm");
                    txtBerangkat.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(txtBerangkat);
        
        formPanel.add(new JLabel("Waktu Tiba:"));
        txtTiba = new JTextField("yyyy-MM-dd HH:mm");
        txtTiba.setForeground(Color.GRAY);
        txtTiba.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtTiba.getText().equals("yyyy-MM-dd HH:mm")) {
                    txtTiba.setText("");
                    txtTiba.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtTiba.getText().isEmpty()) {
                    txtTiba.setText("yyyy-MM-dd HH:mm");
                    txtTiba.setForeground(Color.GRAY);
                }
            }
        });
        formPanel.add(txtTiba);
        
        formPanel.add(new JLabel("Harga (Rp):"));
        txtHarga = new JTextField();
        formPanel.add(txtHarga);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        btnSimpan = new JButton(mode.equals("tambah") ? "Simpan" : "Update");
        btnSimpan.setPreferredSize(new Dimension(100, 30));
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        
        btnBatal = new JButton("Batal");
        btnBatal.setPreferredSize(new Dimension(100, 30));
        btnBatal.setBackground(new Color(231, 76, 60));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);
        
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event Listeners
        btnSimpan.addActionListener(e -> {
            if (mode.equals("tambah")) {
                simpanJadwal();
            } else {
                updateJadwal();
            }
        });
        
        btnBatal.addActionListener(e -> dispose());
    }
    
    private void loadData(Jadwal jadwal) {
        txtAsal.setText(jadwal.getLokasiAsal());
        txtTujuan.setText(jadwal.getLokasiTujuan());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        txtBerangkat.setText(jadwal.getWaktuBerangkat().format(formatter));
        txtBerangkat.setForeground(Color.BLACK);
        
        txtTiba.setText(jadwal.getWaktuTiba().format(formatter));
        txtTiba.setForeground(Color.BLACK);
        
        txtHarga.setText(String.valueOf(jadwal.getHarga()));
    }
    
    private boolean validateInput() {
        // Validasi lokasi asal
        if (!Helper.isNotEmpty(txtAsal.getText())) {
            JOptionPane.showMessageDialog(this,
                "Lokasi asal tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtAsal.requestFocus();
            return false;
        }
        
        // Validasi lokasi tujuan
        if (!Helper.isNotEmpty(txtTujuan.getText())) {
            JOptionPane.showMessageDialog(this,
                "Lokasi tujuan tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtTujuan.requestFocus();
            return false;
        }
        
        // Validasi waktu berangkat
        String berangkatText = txtBerangkat.getText();
        if (berangkatText.equals("yyyy-MM-dd HH:mm") || berangkatText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Waktu berangkat tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtBerangkat.requestFocus();
            return false;
        }
        
        // Validasi waktu tiba
        String tibaText = txtTiba.getText();
        if (tibaText.equals("yyyy-MM-dd HH:mm") || tibaText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Waktu tiba tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtTiba.requestFocus();
            return false;
        }
        
        // Validasi format datetime
        try {
            LocalDateTime berangkat = LocalDateTime.parse(berangkatText,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime tiba = LocalDateTime.parse(tibaText,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            // Validasi waktu berangkat harus sebelum waktu tiba
            if (!berangkat.isBefore(tiba)) {
                JOptionPane.showMessageDialog(this,
                    "Waktu berangkat harus sebelum waktu tiba!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            // Validasi waktu berangkat harus di masa depan (untuk mode tambah)
            if (mode.equals("tambah") && !Helper.isFutureDateTime(berangkat)) {
                JOptionPane.showMessageDialog(this,
                    "Waktu berangkat harus di masa depan!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Format tanggal tidak valid!\nGunakan format: yyyy-MM-dd HH:mm\nContoh: 2024-12-25 10:30",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validasi harga
        try {
            double harga = Double.parseDouble(txtHarga.getText());
            if (!Helper.isValidHarga(harga)) {
                JOptionPane.showMessageDialog(this,
                    "Harga harus lebih besar dari 0!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
                txtHarga.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Harga harus berupa angka!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            txtHarga.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void simpanJadwal() {
        if (!validateInput()) {
            return;
        }
        
        try {
            String newIdJadwal = Helper.generateJadwalId();
            String asal = Helper.sanitizeInput(txtAsal.getText());
            String tujuan = Helper.sanitizeInput(txtTujuan.getText());
            
            LocalDateTime berangkat = LocalDateTime.parse(
                txtBerangkat.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            
            LocalDateTime tiba = LocalDateTime.parse(
                txtTiba.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            
            double harga = Double.parseDouble(txtHarga.getText());
            
            Jadwal jadwal = new Jadwal(newIdJadwal, asal, tujuan, berangkat, tiba, harga);
            
            jadwalController.tambahJadwal(jadwal);
            
            JOptionPane.showMessageDialog(this,
                "Jadwal berhasil ditambahkan!\n" +
                "ID Jadwal: " + newIdJadwal + "\n" +
                "Rute: " + asal + " → " + tujuan + "\n" +
                "Jumlah Kursi: 40",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            dispose();
            
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan jadwal!\n" + ex.getMessage(),
                "Error Database",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Terjadi kesalahan!\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void updateJadwal() {
        if (!validateInput()) {
            return;
        }
        
        try {
            String asal = Helper.sanitizeInput(txtAsal.getText());
            String tujuan = Helper.sanitizeInput(txtTujuan.getText());
            
            LocalDateTime berangkat = LocalDateTime.parse(
                txtBerangkat.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            
            LocalDateTime tiba = LocalDateTime.parse(
                txtTiba.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            
            double harga = Double.parseDouble(txtHarga.getText());
            
            Jadwal jadwal = new Jadwal(idJadwal, asal, tujuan, berangkat, tiba, harga);
            
            jadwalController.updateJadwal(jadwal);
            
            JOptionPane.showMessageDialog(this,
                "Jadwal berhasil diupdate!\n" +
                "ID Jadwal: " + idJadwal + "\n" +
                "Rute: " + asal + " → " + tujuan,
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
            
        } catch (JadwalNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                "Jadwal tidak ditemukan!\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal mengupdate jadwal!\n" + ex.getMessage(),
                "Error Database",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Terjadi kesalahan!\n" + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void clearForm() {
        txtAsal.setText("");
        txtTujuan.setText("");
        txtBerangkat.setText("yyyy-MM-dd HH:mm");
        txtBerangkat.setForeground(Color.GRAY);
        txtTiba.setText("yyyy-MM-dd HH:mm");
        txtTiba.setForeground(Color.GRAY);
        txtHarga.setText("");
    }
    
    // Method untuk dipanggil dari luar jika ingin refresh data
    public void refreshData() {
        clearForm();
    }
}