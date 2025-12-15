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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class PesanTiketFrame extends JFrame {

    private JadwalController jadwalController;
    private PemesananController pemesananController;

    private JComboBox<String> cmbJadwal;
    private JTextField txtKursi;
    private JTextField txtNama, txtEmail, txtTelepon;
    private JComboBox<String> cmbMetode;
    private int kursiTerpilih = -1;

    private Jadwal jadwalAktif;
    private List<Jadwal> daftarJadwal;

    public PesanTiketFrame(JadwalController jc, PemesananController pc) {
        this.jadwalController = jc;
        this.pemesananController = pc;

        setTitle("Pesan Tiket Bus");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 247, 250));
        
        // Title
        JLabel lblTitle = new JLabel("FORM PEMESANAN TIKET", SwingConstants.CENTER);
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
        
        // Pilih Jadwal
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Pilih Jadwal:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmbJadwal = new JComboBox<>();
        cmbJadwal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbJadwal.setBackground(Color.WHITE);
        formPanel.add(cmbJadwal, gbc);
        row++;

        // Nomor Kursi - INISIALISASI DULU SEBELUM LOAD DATA
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblKursi = createLabel("Nomor Kursi:");
        JLabel lblKursiInfo = new JLabel("(Klik 'Pilih' untuk memilih kursi)");
        lblKursiInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblKursiInfo.setForeground(new Color(127, 140, 141));
        JPanel lblKursiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lblKursiPanel.setBackground(Color.WHITE);
        lblKursiPanel.add(lblKursi);
        lblKursiPanel.add(Box.createHorizontalStrut(5));
        lblKursiPanel.add(lblKursiInfo);
        formPanel.add(lblKursiPanel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        JPanel kursiPanel = new JPanel(new BorderLayout(5, 0));
        kursiPanel.setBackground(Color.WHITE);
        txtKursi = createTextField();
        txtKursi.setEditable(false);
        txtKursi.setBackground(new Color(248, 249, 250));
        
        JButton btnPilih = new JButton("Pilih");
        btnPilih.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnPilih.setBackground(new Color(46, 204, 113));
        btnPilih.setForeground(Color.WHITE);
        btnPilih.setFocusPainted(false);
        btnPilih.setBorderPainted(false);
        btnPilih.setOpaque(true);
        btnPilih.setContentAreaFilled(true);
        btnPilih.setPreferredSize(new Dimension(70, 30));
        btnPilih.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPilih.addActionListener(e -> {
            if (jadwalAktif == null) {
                JOptionPane.showMessageDialog(this,
                        "Pilih jadwal terlebih dahulu!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            tampilkanDialogPilihKursi();
        });
        
        kursiPanel.add(txtKursi, BorderLayout.CENTER);
        kursiPanel.add(btnPilih, BorderLayout.EAST);
        formPanel.add(kursiPanel, gbc);
        row++;

        // Nama
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Nama:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtNama = createTextField();
        formPanel.add(txtNama, gbc);
        row++;

        // Email
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtEmail = createTextField();
        formPanel.add(txtEmail, gbc);
        row++;

        // Telepon
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Telepon:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtTelepon = createTextField();
        formPanel.add(txtTelepon, gbc);
        row++;

        // Metode Pembayaran
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(createLabel("Metode Pembayaran:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        cmbMetode = new JComboBox<>(new String[]{
                "Transfer Bank", "E-Wallet", "Cash"
        });
        cmbMetode.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbMetode.setBackground(Color.WHITE);
        formPanel.add(cmbMetode, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton btnPesan = new JButton("Pesan & Bayar");
        btnPesan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPesan.setPreferredSize(new Dimension(150, 38));
        btnPesan.setBackground(new Color(46, 204, 113));
        btnPesan.setForeground(Color.WHITE);
        btnPesan.setFocusPainted(false);
        btnPesan.setBorderPainted(false);
        btnPesan.setOpaque(true);
        btnPesan.setContentAreaFilled(true);
        btnPesan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPesan.addActionListener(e -> pesanTiket());
        
        JButton btnBatal = new JButton("Batal");
        btnBatal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBatal.setPreferredSize(new Dimension(150, 38));
        btnBatal.setBackground(new Color(231, 76, 60));
        btnBatal.setForeground(Color.WHITE);
        btnBatal.setFocusPainted(false);
        btnBatal.setBorderPainted(false);
        btnBatal.setOpaque(true);
        btnBatal.setContentAreaFilled(true);
        btnBatal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBatal.addActionListener(e -> dispose());

        buttonPanel.add(btnPesan);
        buttonPanel.add(btnBatal);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        
        // LOAD DATA DAN SET ACTION LISTENER DI AKHIR SETELAH SEMUA KOMPONEN SIAP
        loadJadwalTersedia();
        cmbJadwal.addActionListener(e -> onJadwalSelected());
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

    private void loadJadwalTersedia() {
        try {
            daftarJadwal = jadwalController.getAllJadwal();
            cmbJadwal.removeAllItems();
            
            if (daftarJadwal.isEmpty()) {
                cmbJadwal.addItem("-- Belum ada jadwal tersedia --");
            } else {
                cmbJadwal.addItem("-- Pilih Jadwal --");
                for (Jadwal jadwal : daftarJadwal) {
                    String item = String.format("%s | %s → %s | %s | %s",
                        jadwal.getIdJadwal(),
                        jadwal.getLokasiAsal(),
                        jadwal.getLokasiTujuan(),
                        Helper.formatForDisplay(jadwal.getWaktuBerangkat()),
                        Helper.formatRupiahWithoutDecimal(jadwal.getHarga())
                    );
                    cmbJadwal.addItem(item);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal memuat jadwal: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void onJadwalSelected() {
        int selectedIndex = cmbJadwal.getSelectedIndex();
        
        if (selectedIndex <= 0 || daftarJadwal == null || daftarJadwal.isEmpty()) {
            jadwalAktif = null;
            kursiTerpilih = -1;
            txtKursi.setText("");
            return;
        }
        
        // Index 0 adalah "-- Pilih Jadwal --", jadi jadwal mulai dari index 1
        jadwalAktif = daftarJadwal.get(selectedIndex - 1);
        
        // Reset kursi
        kursiTerpilih = -1;
        txtKursi.setText("");
        
        try {
            int kursiTersedia = jadwalController.getJumlahKursiTersedia(jadwalAktif.getIdJadwal());
            
            JOptionPane.showMessageDialog(this,
                String.format(
                    "Jadwal dipilih:\n\n" +
                    "ID: %s\n" +
                    "Rute: %s → %s\n" +
                    "Berangkat: %s\n" +
                    "Harga: %s\n" +
                    "Kursi Tersedia: %d/40\n\n" +
                    "Silakan pilih nomor kursi!",
                    jadwalAktif.getIdJadwal(),
                    jadwalAktif.getLokasiAsal(),
                    jadwalAktif.getLokasiTujuan(),
                    Helper.formatForDisplay(jadwalAktif.getWaktuBerangkat()),
                    Helper.formatRupiahWithoutDecimal(jadwalAktif.getHarga()),
                    kursiTersedia
                ),
                "Info Jadwal",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariJadwal() {
        // Method ini tidak digunakan lagi karena sudah pakai combo box
    }
    
    private void tampilkanDialogPilihKursi() {
        JDialog dialog = new JDialog(this, "Pilih Kursi", true);
        dialog.setSize(450, 550);
        dialog.setLocationRelativeTo(this);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel lblHeader = new JLabel("Pilih Nomor Kursi", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainPanel.add(lblHeader, BorderLayout.NORTH);
        
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        legendPanel.setBackground(Color.WHITE);
        legendPanel.add(createLegendItem("Tersedia", new Color(46, 204, 113)));
        legendPanel.add(createLegendItem("Terisi", new Color(231, 76, 60)));
        legendPanel.add(createLegendItem("Terpilih", new Color(241, 196, 15)));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(legendPanel, BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel panelKursi = new JPanel(new GridLayout(10, 4, 8, 8));
        panelKursi.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelKursi.setBackground(Color.WHITE);
        
        JButton[] btnKursi = new JButton[40];
        final int[] kursiDipilih = {-1};
        
        try {
            List<Kursi> allKursi = pemesananController.getAllKursiByJadwal(jadwalAktif.getIdJadwal());
            boolean[] statusKursi = new boolean[40];
            
            for (int i = 0; i < 40; i++) {
                statusKursi[i] = true;
            }
            
            for (Kursi kursi : allKursi) {
                int idx = kursi.getNomorKursi() - 1;
                if (idx >= 0 && idx < 40) {
                    statusKursi[idx] = kursi.isTersedia();
                }
            }
            
            for (int i = 0; i < 40; i++) {
                final int nomorKursi = i + 1;
                btnKursi[i] = new JButton(String.valueOf(nomorKursi));
                btnKursi[i].setPreferredSize(new Dimension(60, 50));
                btnKursi[i].setFont(new Font("Segoe UI", Font.BOLD, 14));
                btnKursi[i].setFocusPainted(false);
                btnKursi[i].setBorderPainted(false);
                btnKursi[i].setOpaque(true);
                btnKursi[i].setContentAreaFilled(true);
                
                if (statusKursi[i]) {
                    btnKursi[i].setEnabled(true);
                    btnKursi[i].setBackground(new Color(46, 204, 113));
                    btnKursi[i].setForeground(Color.WHITE);
                    btnKursi[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    final int idx = i;
                    btnKursi[i].addActionListener(e -> {
                        if (kursiDipilih[0] != -1) {
                            btnKursi[kursiDipilih[0] - 1].setBackground(new Color(46, 204, 113));
                        }
                        kursiDipilih[0] = nomorKursi;
                        btnKursi[idx].setBackground(new Color(241, 196, 15));
                    });
                } else {
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
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnOK = new JButton("OK");
        btnOK.setPreferredSize(new Dimension(100, 35));
        btnOK.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnOK.setBackground(new Color(46, 204, 113));
        btnOK.setForeground(Color.WHITE);
        btnOK.setFocusPainted(false);
        btnOK.setBorderPainted(false);
        btnOK.setOpaque(true);
        btnOK.setContentAreaFilled(true);
        btnOK.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton btnCancel = new JButton("Batal");
        btnCancel.setPreferredSize(new Dimension(100, 35));
        btnCancel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancel.setBackground(new Color(231, 76, 60));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setOpaque(true);
        btnCancel.setContentAreaFilled(true);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
        panel.setBackground(Color.WHITE);
        
        JLabel colorBox = new JLabel("  ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        colorBox.setPreferredSize(new Dimension(20, 20));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
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
            Pelanggan pelanggan = new Pelanggan(
                Helper.generatePelangganId(),
                Helper.sanitizeInput(txtNama.getText()),
                Helper.sanitizeInput(txtEmail.getText()),
                Helper.sanitizeInput(txtTelepon.getText())
            );

            Tiket tiket = pemesananController.pesanTiket(
                jadwalAktif.getIdJadwal(),
                kursiTerpilih,
                pelanggan
            );

            Transaksi transaksi = pemesananController.buatTransaksi(
                tiket,
                cmbMetode.getSelectedItem().toString(),
                pelanggan
            );

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
                pemesananController.bayarTransaksi(transaksi.getIdTransaksi());

               String struk = pemesananController.cetakStrukSederhana(
                    tiket.getIdTiket()
                );


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