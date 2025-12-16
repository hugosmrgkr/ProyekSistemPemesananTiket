package com.tiket.view;

import com.tiket.controller.PemesananController;
import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.KursiTidakTersediaException;
import com.tiket.helper.Helper;
import com.tiket.model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class PesanTiketFrame extends JFrame {

    private JadwalController jadwalController;
    private PemesananController pemesananController;

    private JTextField txtIdJadwal, txtKursi;
    private JTextField txtNama, txtEmail, txtTelepon;
    private JComboBox<String> cmbMetode;
    private int kursiTerpilih = -1;

    private Jadwal jadwalAktif;

    public PesanTiketFrame(JadwalController jc, PemesananController pc) {
        this.jadwalController = jc;
        this.pemesananController = pc;

        setTitle("Pesan Tiket Bus");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ID Jadwal
        panel.add(new JLabel("ID Jadwal"));
        txtIdJadwal = new JTextField();
        panel.add(txtIdJadwal);

        JButton btnCari = new JButton("Cari Jadwal");
        btnCari.setBackground(new Color(52, 152, 219));
        btnCari.setForeground(Color.WHITE);
        btnCari.setFocusPainted(false);
        panel.add(btnCari);
        panel.add(new JLabel());

        // Nomor Kursi
        panel.add(new JLabel("Nomor Kursi"));
        JPanel kursiPanel = new JPanel(new BorderLayout(5, 0));
        txtKursi = new JTextField();
        txtKursi.setEditable(false);
        txtKursi.setBackground(Color.WHITE);
        JButton btnPilih = new JButton("Pilih");
        btnPilih.setBackground(new Color(46, 204, 113));
        btnPilih.setForeground(Color.WHITE);
        btnPilih.setFocusPainted(false);
        kursiPanel.add(txtKursi, BorderLayout.CENTER);
        kursiPanel.add(btnPilih, BorderLayout.EAST);
        panel.add(kursiPanel);

        // Nama
        panel.add(new JLabel("Nama"));
        txtNama = new JTextField();
        panel.add(txtNama);

        // Email
        panel.add(new JLabel("Email"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        // Telepon
        panel.add(new JLabel("Telepon"));
        txtTelepon = new JTextField();
        panel.add(txtTelepon);

        // Metode Pembayaran
        panel.add(new JLabel("Metode Pembayaran"));
        cmbMetode = new JComboBox<>(new String[]{
                "Transfer Bank", "E-Wallet", "Cash"
        });
        panel.add(cmbMetode);

        JButton btnPesan = new JButton("Pesan & Bayar");
        btnPesan.setBackground(new Color(46, 204, 113));
        btnPesan.setForeground(Color.WHITE);
        btnPesan.setFont(new Font("Arial", Font.BOLD, 12));
        btnPesan.setFocusPainted(false);
        
        JButton btnBatal = new JButton("Batal");
        btnBatal.setBackground(new Color(231, 76, 60));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFont(new Font("Arial", Font.BOLD, 12));
        btnBatal.setFocusPainted(false);

        panel.add(btnPesan);
        panel.add(btnBatal);

        add(panel);

        // ================= EVENT =================
        btnCari.addActionListener(e -> cariJadwal());
        
        btnPilih.addActionListener(e -> {
            if (jadwalAktif == null) {
                JOptionPane.showMessageDialog(this,
                        "Cari jadwal terlebih dahulu!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            tampilkanDialogPilihKursi();
        });

        btnPesan.addActionListener(e -> pesanTiket());
        btnBatal.addActionListener(e -> dispose());
    }

    private void cariJadwal() {
        try {
            String idJadwal = txtIdJadwal.getText().trim();
            
            if (idJadwal.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Masukkan ID Jadwal terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            jadwalAktif = jadwalController.getJadwalById(idJadwal);

            int kursiTersedia = jadwalController.getJumlahKursiTersedia(idJadwal);
            
            // Reset kursi terpilih
            kursiTerpilih = -1;
            txtKursi.setText("");
            
            JOptionPane.showMessageDialog(this,
                String.format(
                    "Jadwal ditemukan!\n\n" +
                    "Rute: %s → %s\n" +
                    "Berangkat: %s\n" +
                    "Harga: %s\n\n" +
                    "Klik tombol 'Pilih' untuk memilih kursi",
                    jadwalAktif.getLokasiAsal(),
                    jadwalAktif.getLokasiTujuan(),
                    Helper.formatForDisplay(jadwalAktif.getWaktuBerangkat()),
                    Helper.formatRupiahWithoutDecimal(jadwalAktif.getHarga())
                ),
                "Jadwal Ditemukan",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Jadwal tidak ditemukan!\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tampilkanDialogPilihKursi() {
        JDialog dialog = new JDialog(this, "Pilih Kursi", true);
        dialog.setSize(450, 550);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel lblHeader = new JLabel("Pilih Nomor Kursi", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(lblHeader, BorderLayout.NORTH);
        
        // Panel Legenda
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        legendPanel.add(createLegendItem("Tersedia", new Color(46, 204, 113)));
        legendPanel.add(createLegendItem("Terisi", new Color(231, 76, 60)));
        legendPanel.add(createLegendItem("Terpilih", new Color(241, 196, 15)));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(legendPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Panel Kursi
        JPanel panelKursi = new JPanel(new GridLayout(10, 4, 8, 8));
        panelKursi.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton[] btnKursi = new JButton[40];
        final int[] kursiDipilih = {-1};
        
        // Load status kursi
        try {
            List<Kursi> allKursi = pemesananController.getAllKursiByJadwal(jadwalAktif.getIdJadwal());
            boolean[] statusKursi = new boolean[40]; // true = tersedia
            
            // Default semua tersedia
            for (int i = 0; i < 40; i++) {
                statusKursi[i] = true;
            }
            
            // Update status dari database
            for (Kursi kursi : allKursi) {
                int idx = kursi.getNomorKursi() - 1;
                if (idx >= 0 && idx < 40) {
                    statusKursi[idx] = kursi.isTersedia();
                }
            }
            
            // Buat tombol kursi
            for (int i = 0; i < 40; i++) {
                final int nomorKursi = i + 1;
                btnKursi[i] = new JButton(String.valueOf(nomorKursi));
                btnKursi[i].setPreferredSize(new Dimension(60, 50));
                btnKursi[i].setFont(new Font("Arial", Font.BOLD, 14));
                btnKursi[i].setFocusPainted(false);
                
                if (statusKursi[i]) {
                    // Kursi tersedia
                    btnKursi[i].setEnabled(true);
                    btnKursi[i].setBackground(new Color(46, 204, 113));
                    btnKursi[i].setForeground(Color.WHITE);
                    
                    final int idx = i;
                    btnKursi[i].addActionListener(e -> {
                        // Reset kursi sebelumnya
                        if (kursiDipilih[0] != -1) {
                            btnKursi[kursiDipilih[0] - 1].setBackground(new Color(46, 204, 113));
                        }
                        
                        // Set kursi baru
                        kursiDipilih[0] = nomorKursi;
                        btnKursi[idx].setBackground(new Color(241, 196, 15));
                    });
                } else {
                    // Kursi sudah terisi
                    btnKursi[i].setEnabled(false);
                    btnKursi[i].setBackground(new Color(231, 76, 60));
                    btnKursi[i].setForeground(Color.WHITE);
                }
                
                panelKursi.add(btnKursi[i]);
            }
            
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(dialog,
                "Error memuat data kursi: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        JScrollPane scrollPane = new JScrollPane(panelKursi);
        scrollPane.setPreferredSize(new Dimension(400, 350));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnOK = new JButton("OK");
        btnOK.setPreferredSize(new Dimension(100, 35));
        btnOK.setBackground(new Color(46, 204, 113));
        btnOK.setForeground(Color.WHITE);
        btnOK.setFocusPainted(false);
        
        JButton btnCancel = new JButton("Batal");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        
        btnOK.addActionListener(e -> {
            if (kursiDipilih[0] != -1) {
                kursiTerpilih = kursiDipilih[0];
                txtKursi.setText(String.valueOf(kursiTerpilih));
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Pilih kursi terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    private JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        
        JLabel colorBox = new JLabel("  ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        colorBox.setPreferredSize(new Dimension(20, 20));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        
        panel.add(colorBox);
        panel.add(label);
        
        return panel;
    }

    private void pesanTiket() {
        if (jadwalAktif == null) {
            JOptionPane.showMessageDialog(this,
                "Cari jadwal terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (kursiTerpilih == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih kursi terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validasi input
        if (!Helper.isNotEmpty(txtNama.getText())) {
            JOptionPane.showMessageDialog(this,
                "Nama tidak boleh kosong!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus();
            return;
        }
        
        if (!Helper.isValidEmail(txtEmail.getText())) {
            JOptionPane.showMessageDialog(this,
                "Format email tidak valid!",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        
        if (!Helper.isValidPhone(txtTelepon.getText())) {
            JOptionPane.showMessageDialog(this,
                "Format telepon tidak valid! (10-13 digit)",
                "Validasi Error",
                JOptionPane.WARNING_MESSAGE);
            txtTelepon.requestFocus();
            return;
        }

        try {
            // Buat pelanggan
            Pelanggan pelanggan = new Pelanggan(
                Helper.generatePelangganId(),
                Helper.sanitizeInput(txtNama.getText()),
                Helper.sanitizeInput(txtEmail.getText()),
                Helper.sanitizeInput(txtTelepon.getText())
            );

            // Pesan tiket
            Tiket tiket = pemesananController.pesanTiket(
                jadwalAktif.getIdJadwal(),
                kursiTerpilih,
                pelanggan
            );

            // Buat transaksi
            Transaksi transaksi = pemesananController.buatTransaksi(
                tiket,
                cmbMetode.getSelectedItem().toString(),
                pelanggan
            );

            // Konfirmasi pembayaran
            int konfirmasi = JOptionPane.showConfirmDialog(
                this,
                String.format(
                    "Detail Pemesanan:\n\n" +
                    "Nama: %s\n" +
                    "Rute: %s → %s\n" +
                    "Kursi: %d\n" +
                    "Harga: %s\n" +
                    "Metode: %s\n\n" +
                    "Lanjutkan pembayaran?",
                    pelanggan.getNama(),
                    jadwalAktif.getLokasiAsal(),
                    jadwalAktif.getLokasiTujuan(),
                    kursiTerpilih,
                    Helper.formatRupiahWithoutDecimal(transaksi.getJumlah()),
                    cmbMetode.getSelectedItem().toString()
                ),
                "Konfirmasi Pembayaran",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (konfirmasi == JOptionPane.YES_OPTION) {
                // Proses pembayaran
                pemesananController.bayarTransaksi(transaksi.getIdTransaksi());

                // Cetak struk
                String struk = pemesananController.cetakStruk(
                    tiket.getIdTiket(),
                    jadwalAktif.getIdJadwal(),
                    pelanggan
                );

                // Tampilkan struk
                JTextArea area = new JTextArea(struk);
                area.setEditable(false);
                area.setFont(new Font("Monospaced", Font.PLAIN, 12));

                JScrollPane scrollPane = new JScrollPane(area);
                scrollPane.setPreferredSize(new Dimension(500, 400));

                JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "STRUK PEMBAYARAN",
                    JOptionPane.INFORMATION_MESSAGE);

                dispose();
            }

        } catch (KursiTidakTersediaException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Kursi Tidak Tersedia",
                JOptionPane.ERROR_MESSAGE);
            
            // Reset kursi
            kursiTerpilih = -1;
            txtKursi.setText("");
            
        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this,
                "Error database: " + ex.getMessage(),
                "Error Database",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Terjadi kesalahan: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}