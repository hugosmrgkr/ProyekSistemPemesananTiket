package com.tiket.view;

import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.JadwalNotFoundException;
import com.tiket.model.Jadwal;
import com.tiket.helper.Helper;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class JadwalForm extends JFrame {
    private JadwalController jadwalController;
    private JTextField txtAsal, txtTujuan, txtHarga;
    
    // Components for DateTime picker
    private JSpinner spnBerangkatTanggal, spnBerangkatJam, spnBerangkatMenit;
    private JSpinner spnTibaTanggal, spnTibaJam, spnTibaMenit;
    
    private JButton btnSimpan, btnBatal;
    
    private String mode;
    private String idJadwal;
    
    public JadwalForm(JadwalController controller) {
        this.jadwalController = controller;
        this.mode = "tambah";
        setTitle("Tambah Jadwal");
        setSize(520, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }
    
    public JadwalForm(JadwalController controller, Jadwal jadwal) {
        this.jadwalController = controller;
        this.mode = "edit";
        this.idJadwal = jadwal.getIdJadwal();
        setTitle("Edit Jadwal - " + idJadwal);
        setSize(520, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        loadData(jadwal);
    }
    
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 247, 250));
        
        // Header
        JLabel lblTitle = new JLabel(
            mode.equals("tambah") ? "FORM TAMBAH JADWAL" : "FORM EDIT JADWAL",
            SwingConstants.CENTER
        );
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        int row = 0;
        
        // Lokasi Asal
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Lokasi Asal:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtAsal = createTextField();
        formPanel.add(txtAsal, gbc);
        row++;
        
        // Lokasi Tujuan
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Lokasi Tujuan:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtTujuan = createTextField();
        formPanel.add(txtTujuan, gbc);
        row++;
        
        // Waktu Berangkat Label
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Waktu Berangkat:"), gbc);
        row++;
        
        // Waktu Berangkat Input
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel berangkatPanel = createDateTimePanel(true);
        formPanel.add(berangkatPanel, gbc);
        row++;
        
        // Waktu Tiba Label
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Waktu Tiba:"), gbc);
        row++;
        
        // Waktu Tiba Input
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel tibaPanel = createDateTimePanel(false);
        formPanel.add(tibaPanel, gbc);
        row++;
        
        // Harga
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1; gbc.weightx = 0;
        formPanel.add(createLabel("Harga (Rp):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtHarga = createTextField();
        formPanel.add(txtHarga, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        btnSimpan = new JButton(mode.equals("tambah") ? "Simpan" : "Update");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSimpan.setPreferredSize(new Dimension(130, 38));
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setBorderPainted(false);
        btnSimpan.setOpaque(true);
        btnSimpan.setContentAreaFilled(true);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnBatal = new JButton("Batal");
        btnBatal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBatal.setPreferredSize(new Dimension(130, 38));
        btnBatal.setBackground(new Color(231, 76, 60));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);
        btnBatal.setBorderPainted(false);
        btnBatal.setOpaque(true);
        btnBatal.setContentAreaFilled(true);
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }
    
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return textField;
    }
    
    private JPanel createDateTimePanel(boolean isBerangkat) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setBackground(Color.WHITE);
        
        // Date Spinner
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateSpinner.setPreferredSize(new Dimension(120, 32));
        ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setEditable(false);
        
        // Hour Spinner
        SpinnerNumberModel hourModel = new SpinnerNumberModel(8, 0, 23, 1);
        JSpinner hourSpinner = new JSpinner(hourModel);
        hourSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hourSpinner.setPreferredSize(new Dimension(60, 32));
        ((JSpinner.DefaultEditor) hourSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        
        // Minute Spinner
        SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, 0, 59, 15);
        JSpinner minuteSpinner = new JSpinner(minuteModel);
        minuteSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        minuteSpinner.setPreferredSize(new Dimension(60, 32));
        ((JSpinner.DefaultEditor) minuteSpinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
        
        // Store references
        if (isBerangkat) {
            spnBerangkatTanggal = dateSpinner;
            spnBerangkatJam = hourSpinner;
            spnBerangkatMenit = minuteSpinner;
        } else {
            spnTibaTanggal = dateSpinner;
            spnTibaJam = hourSpinner;
            spnTibaMenit = minuteSpinner;
        }
        
        // Labels
        JLabel lblDate = new JLabel("Tanggal:");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel lblTime = new JLabel("Jam:");
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JLabel lblColon = new JLabel(":");
        lblColon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        panel.add(lblDate);
        panel.add(dateSpinner);
        panel.add(lblTime);
        panel.add(hourSpinner);
        panel.add(lblColon);
        panel.add(minuteSpinner);
        
        return panel;
    }
    
    private LocalDateTime getDateTimeFromSpinners(JSpinner dateSpinner, JSpinner hourSpinner, JSpinner minuteSpinner) {
        java.util.Date date = (java.util.Date) dateSpinner.getValue();
        int hour = (Integer) hourSpinner.getValue();
        int minute = (Integer) minuteSpinner.getValue();
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        cal.set(java.util.Calendar.HOUR_OF_DAY, hour);
        cal.set(java.util.Calendar.MINUTE, minute);
        cal.set(java.util.Calendar.SECOND, 0);
        
        return LocalDateTime.of(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH) + 1,
            cal.get(java.util.Calendar.DAY_OF_MONTH),
            hour,
            minute
        );
    }
    
    private void setSpinnersFromDateTime(LocalDateTime dateTime, JSpinner dateSpinner, JSpinner hourSpinner, JSpinner minuteSpinner) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(dateTime.getYear(), dateTime.getMonthValue() - 1, dateTime.getDayOfMonth());
        
        dateSpinner.setValue(cal.getTime());
        hourSpinner.setValue(dateTime.getHour());
        minuteSpinner.setValue(dateTime.getMinute());
    }
    
    private void loadData(Jadwal jadwal) {
        txtAsal.setText(jadwal.getLokasiAsal());
        txtTujuan.setText(jadwal.getLokasiTujuan());
        
        setSpinnersFromDateTime(jadwal.getWaktuBerangkat(), spnBerangkatTanggal, spnBerangkatJam, spnBerangkatMenit);
        setSpinnersFromDateTime(jadwal.getWaktuTiba(), spnTibaTanggal, spnTibaJam, spnTibaMenit);
        
        txtHarga.setText(String.valueOf(jadwal.getHarga()));
    }
    
    private boolean validateInput() {
        if (!Helper.isNotEmpty(txtAsal.getText())) {
            JOptionPane.showMessageDialog(this,
                "Lokasi asal tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtAsal.requestFocus();
            return false;
        }
        
        if (!Helper.isNotEmpty(txtTujuan.getText())) {
            JOptionPane.showMessageDialog(this,
                "Lokasi tujuan tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtTujuan.requestFocus();
            return false;
        }
        
        try {
            LocalDateTime berangkat = getDateTimeFromSpinners(spnBerangkatTanggal, spnBerangkatJam, spnBerangkatMenit);
            LocalDateTime tiba = getDateTimeFromSpinners(spnTibaTanggal, spnTibaJam, spnTibaMenit);
            
            if (!berangkat.isBefore(tiba)) {
                JOptionPane.showMessageDialog(this,
                    "Waktu berangkat harus sebelum waktu tiba!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            if (mode.equals("tambah") && !Helper.isFutureDateTime(berangkat)) {
                JOptionPane.showMessageDialog(this,
                    "Waktu berangkat harus di masa depan!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error pada format tanggal/waktu!",
                "Validasi Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
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
            
            LocalDateTime berangkat = getDateTimeFromSpinners(spnBerangkatTanggal, spnBerangkatJam, spnBerangkatMenit);
            LocalDateTime tiba = getDateTimeFromSpinners(spnTibaTanggal, spnTibaJam, spnTibaMenit);
            
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
            
            LocalDateTime berangkat = getDateTimeFromSpinners(spnBerangkatTanggal, spnBerangkatJam, spnBerangkatMenit);
            LocalDateTime tiba = getDateTimeFromSpinners(spnTibaTanggal, spnTibaJam, spnTibaMenit);
            
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
}