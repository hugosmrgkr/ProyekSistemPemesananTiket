    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.view;

import com.tiket.model.Tiket;
import com.tiket.helper.FileHelper;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * Dialog untuk menampilkan struk pemesanan tiket
 */
public class StrukView extends JDialog {
    
    private Tiket tiket;
    private FileHelper fileHelper;
    
    public StrukView(Tiket tiket) {
        this.tiket = tiket;
        this.fileHelper = new FileHelper();
        
        setTitle("Struk Pemesanan");
        setSize(450, 650);
        setModal(true);
        setLocationRelativeTo(null);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(81, 207, 102));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("✓ PEMESANAN BERHASIL");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Struk Tiket Bus");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(Color.WHITE);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(lblTitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(lblSubtitle);
        
        // Content - Struk
        JTextArea txtStruk = new JTextArea();
        txtStruk.setEditable(false);
        txtStruk.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStruk.setMargin(new Insets(10, 10, 10, 10));
        txtStruk.setText(generateStrukText());
        
        JScrollPane scrollPane = new JScrollPane(txtStruk);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnCetakFile = new JButton("💾 Simpan ke File");
        btnCetakFile.setFont(new Font("Arial", Font.BOLD, 14));
        btnCetakFile.setBackground(new Color(51, 154, 240));
        btnCetakFile.setForeground(Color.WHITE);
        btnCetakFile.setFocusPainted(false);
        btnCetakFile.setBorderPainted(false);
        btnCetakFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCetakFile.setPreferredSize(new Dimension(160, 40));
        btnCetakFile.addActionListener(e -> handleCetakFile());
        
        JButton btnTutup = new JButton("✖ Tutup");
        btnTutup.setFont(new Font("Arial", Font.BOLD, 14));
        btnTutup.setBackground(new Color(134, 142, 150));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.setFocusPainted(false);
        btnTutup.setBorderPainted(false);
        btnTutup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutup.setPreferredSize(new Dimension(160, 40));
        btnTutup.addActionListener(e -> dispose());
        
        btnPanel.add(btnCetakFile);
        btnPanel.add(btnTutup);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private String generateStrukText() {
        if (tiket == null) return "Data tiket tidak tersedia";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(40)).append("\n");
        sb.append("        STRUK PEMESANAN TIKET BUS\n");
        sb.append("=".repeat(40)).append("\n\n");
        
        sb.append("ID Tiket       : ").append(tiket.getIdTiket()).append("\n");
        sb.append("Tanggal Pesan  : ");
        if (tiket.getWaktuPemesanan() != null) {
            sb.append(tiket.getWaktuPemesanan().format(formatter));
        } else {
            sb.append("-");
        }
        sb.append("\n\n");
        
        sb.append("-".repeat(40)).append("\n");
        sb.append("DATA PELANGGAN\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Nama           : ").append(tiket.getNamaPelanggan()).append("\n");
        sb.append("Telepon        : ").append(tiket.getTeleponPelanggan()).append("\n");
        sb.append("Email          : ").append(tiket.getEmailPelanggan()).append("\n\n");
        
        sb.append("-".repeat(40)).append("\n");
        sb.append("DETAIL PERJALANAN\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Asal           : ").append(tiket.getLokasiAsal() != null ? tiket.getLokasiAsal() : "-").append("\n");
        sb.append("Tujuan         : ").append(tiket.getLokasiTujuan() != null ? tiket.getLokasiTujuan() : "-").append("\n");
        sb.append("Tanggal        : ").append(tiket.getTanggalBerangkat() != null ? tiket.getTanggalBerangkat() : "-").append("\n");
        sb.append("Jam            : ").append(tiket.getJamBerangkat() != null ? tiket.getJamBerangkat() : "-").append("\n");
        sb.append("Nomor Kursi    : ").append(tiket.getNomorKursi()).append("\n\n");
        
        sb.append("-".repeat(40)).append("\n");
        sb.append("PEMBAYARAN\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append("Total Harga    : Rp ").append(String.format("%,.0f", tiket.getHarga())).append("\n\n");
        
        sb.append("=".repeat(40)).append("\n");
        sb.append("  Terima kasih atas kepercayaan Anda!\n");
        sb.append("     Selamat melakukan perjalanan\n");
        sb.append("=".repeat(40)).append("\n");
        
        return sb.toString();
    }
    
    private void handleCetakFile() {
        try {
            boolean sukses = fileHelper.saveStrukToFile(tiket);
            
            if (sukses) {
                JOptionPane.showMessageDialog(
                    this,
                    "Struk berhasil disimpan ke:\noutput/struk_" + tiket.getIdTiket() + ".txt",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Gagal menyimpan struk!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}