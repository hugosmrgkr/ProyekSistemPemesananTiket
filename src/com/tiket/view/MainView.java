/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.view;

import com.tiket.database.JadwalDB;
import com.tiket.database.KursiDB;
import com.tiket.database.TiketDB;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.ValidationException;
import com.tiket.model.Jadwal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.Border;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Main View dengan DEBUG LOGS untuk tracking error
 */
public class MainView extends JFrame {

    private JPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private String modeAktif;

    // Database
    private JadwalDB jadwalDB;
    private KursiDB kursiDB;
    private TiketDB tiketDB;

    // Data
    private List<Jadwal> dataJadwal;
    private Jadwal jadwalDipilih;

    // Panels
    private CRUDJadwalPanel crudPanel;
    private CekJadwalPanel cekPanel;
    private PemesananPanel pesanPanel;

    public MainView(String mode) {
        this.modeAktif = mode;

        System.out.println("=== DEBUG: MainView Constructor ===");
        System.out.println("Mode: " + mode);

        setTitle("Sistem Tiket Bus - " + mode);
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Database
        try {
            System.out.println("DEBUG: Inisialisasi Database...");
            jadwalDB = new JadwalDB();
            kursiDB = new KursiDB();
            tiketDB = new TiketDB();
            System.out.println("✓ Database berhasil diinisialisasi");
        } catch (Exception e) {
            System.err.println("❌ ERROR saat inisialisasi database:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error inisialisasi database: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        dataJadwal = new ArrayList<>();

        initComponents();
        System.out.println("✓ MainView berhasil dibuat");
    }

    private void initComponents() {
        System.out.println("DEBUG: initComponents() dipanggil");

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 58, 64));
        sidebar.setPreferredSize(new Dimension(200, 700));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Header Sidebar
        JLabel lblMode = new JLabel(modeAktif);
        lblMode.setFont(new Font("Arial", Font.BOLD, 18));
        lblMode.setForeground(Color.WHITE);
        lblMode.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblMode);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        if (modeAktif.equals("ADMIN")) {
            System.out.println("DEBUG: Membuat menu ADMIN");
            createAdminMenu();
        } else {
            System.out.println("DEBUG: Membuat menu PELANGGAN");
            createPelangganMenu();
        }

        // Content Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Create panels
        if (modeAktif.equals("ADMIN")) {
            System.out.println("DEBUG: Membuat CRUDJadwalPanel");
            crudPanel = new CRUDJadwalPanel();
            contentPanel.add(crudPanel, "CRUD");
            cardLayout.show(contentPanel, "CRUD");
        } else {
            System.out.println("DEBUG: Membuat panel Pelanggan");
            // FIXED: panggil constructor CekJadwalPanel dengan this (MainView) sebagai frameUtama
            cekPanel = new CekJadwalPanel(this); // <-- FIXED
            // FIXED: sediakan placeholder PemesananPanel agar kompilasi sukses
            pesanPanel = new PemesananPanel();
            contentPanel.add(cekPanel, "CEK");
            contentPanel.add(pesanPanel, "PESAN");
            cardLayout.show(contentPanel, "CEK");
        }

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        System.out.println("✓ initComponents() selesai");
    }

    private void createAdminMenu() {
        JButton btnCRUD = createMenuButton("📋 CRUD Jadwal", new Color(51, 154, 240));
        btnCRUD.addActionListener(e -> {
            System.out.println("DEBUG: Button CRUD Jadwal diklik");
            cardLayout.show(contentPanel, "CRUD");
        });

        JButton btnKeluar = createMenuButton("🚪 Keluar", new Color(134, 142, 150));
        btnKeluar.addActionListener(e -> kembaliKePilihMode());

        sidebar.add(btnCRUD);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnKeluar);
    }

    private void createPelangganMenu() {
        JButton btnCek = createMenuButton("🔍 Cek Jadwal", new Color(51, 154, 240));
        btnCek.addActionListener(e -> {
            System.out.println("DEBUG: Button Cek Jadwal diklik");
            cardLayout.show(contentPanel, "CEK");
        });

        JButton btnPesan = createMenuButton("🎫 Pesan Tiket", new Color(81, 207, 102));
        btnPesan.addActionListener(e -> {
            System.out.println("DEBUG: Button Pesan Tiket diklik");
            pesanPanel.updateInfo();
            cardLayout.show(contentPanel, "PESAN");
        });

        JButton btnKeluar = createMenuButton("🚪 Keluar", new Color(134, 142, 150));
        btnKeluar.addActionListener(e -> kembaliKePilihMode());

        sidebar.add(btnCek);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnPesan);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnKeluar);
    }

    private JButton createMenuButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void kembaliKePilihMode() {
        System.out.println("DEBUG: Kembali ke PilihModeView");
        // SAFE action: dispose current window.
        // NOTE: Pastikan kamu punya implementasi layar PilihMode di project.
        this.dispose();
        // TODO: panggil PilihModeView / LoginRoleView di sini jika sudah ada:
        // new PilihModeView().setVisible(true);
    }

    // ================================================================
    // CRUD JADWAL PANEL (ADMIN) - WITH DEBUG
    // ================================================================

    class CRUDJadwalPanel extends JPanel {
        private JTextField tfKode, tfAsal, tfTujuan, tfTanggal, tfJam, tfHarga;
        private JTable table;
        private DefaultTableModel tableModel;

        public CRUDJadwalPanel() {
            System.out.println("=== DEBUG: CRUDJadwalPanel Constructor ===");

            setLayout(new BorderLayout(10, 10));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Title
            JLabel title = new JLabel("CRUD JADWAL BUS");
            title.setFont(new Font("Arial", Font.BOLD, 24));
            title.setForeground(new Color(51, 154, 240));

            // Form
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            tfKode = new JTextField(15);
            tfAsal = new JTextField(15);
            tfTujuan = new JTextField(15);
            tfTanggal = new JTextField(15);
            tfJam = new JTextField(15);
            tfHarga = new JTextField(15);

            tfKode.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            tfAsal.setBorder(tfKode.getBorder());
            tfTujuan.setBorder(tfKode.getBorder());
            tfTanggal.setBorder(tfKode.getBorder());
            tfJam.setBorder(tfKode.getBorder());
            tfHarga.setBorder(tfKode.getBorder());

            int row = 0;
            addFormField(formPanel, gbc, "Kode Jadwal:", tfKode, row++);
            addFormField(formPanel, gbc, "Lokasi Asal:", tfAsal, row++);
            addFormField(formPanel, gbc, "Lokasi Tujuan:", tfTujuan, row++);
            addFormField(formPanel, gbc, "Tanggal (yyyy-MM-dd):", tfTanggal, row++);
            addFormField(formPanel, gbc, "Jam (HH:mm):", tfJam, row++);
            addFormField(formPanel, gbc, "Harga:", tfHarga, row++);

            // Buttons
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            btnPanel.setBackground(Color.WHITE);

            JButton btnAdd = createActionButton("➕ Tambah", new Color(81, 207, 102));
            JButton btnUpdate = createActionButton("✏️ Update", new Color(51, 154, 240));
            JButton btnDelete = createActionButton("🗑️ Hapus", new Color(255, 107, 107));
            JButton btnRefresh = createActionButton("🔄 Refresh", new Color(134, 142, 150));

            btnAdd.addActionListener(e -> {
                System.out.println("\n=== DEBUG: Button Tambah diklik ===");
                handleAdd();
            });

            btnUpdate.addActionListener(e -> {
                System.out.println("\n=== DEBUG: Button Update diklik ===");
                handleUpdate();
            });

            btnDelete.addActionListener(e -> {
                System.out.println("\n=== DEBUG: Button Hapus diklik ===");
                handleDelete();
            });

            btnRefresh.addActionListener(e -> {
                System.out.println("\n=== DEBUG: Button Refresh diklik ===");
                loadData();
            });

            btnPanel.add(btnAdd);
            btnPanel.add(btnUpdate);
            btnPanel.add(btnDelete);
            btnPanel.add(btnRefresh);

            // Table
            String[] columns = {"Kode", "Asal", "Tujuan", "Tanggal", "Jam", "Harga"};
            tableModel = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            table = new JTable(tableModel);
            table.setRowHeight(30);
            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    System.out.println("DEBUG: Baris tabel dipilih: " + table.getSelectedRow());
                    loadToForm();
                }
            });

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(750, 300));

            // Layout
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.setBackground(Color.WHITE);
            topPanel.add(title, BorderLayout.NORTH);
            topPanel.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.CENTER);

            JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
            centerPanel.setBackground(Color.WHITE);
            centerPanel.add(formPanel, BorderLayout.NORTH);
            centerPanel.add(btnPanel, BorderLayout.CENTER);

            add(topPanel, BorderLayout.NORTH);
            add(centerPanel, BorderLayout.CENTER);
            add(scrollPane, BorderLayout.SOUTH);

            System.out.println("DEBUG: Memanggil loadData() pertama kali...");
            loadData();
            System.out.println("✓ CRUDJadwalPanel berhasil dibuat");
        }

        private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Arial", Font.PLAIN, 14));
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            panel.add(field, gbc);
        }

        private JButton createActionButton(String text, Color color) {
            JButton btn = new JButton(text);
            btn.setBackground(color);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(120, 35));
            return btn;
        }

        private void handleAdd() {
            System.out.println(">>> handleAdd() dipanggil");

            try {
                // Validasi input
                System.out.println("STEP 1: Validasi input");
                if (tfKode.getText().trim().isEmpty()) {
                    throw new ValidationException("Kode jadwal harus diisi!");
                }
                if (tfAsal.getText().trim().isEmpty()) {
                    throw new ValidationException("Lokasi asal harus diisi!");
                }
                if (tfTujuan.getText().trim().isEmpty()) {
                    throw new ValidationException("Lokasi tujuan harus diisi!");
                }
                if (tfTanggal.getText().trim().isEmpty()) {
                    throw new ValidationException("Tanggal harus diisi!");
                }
                if (tfJam.getText().trim().isEmpty()) {
                    throw new ValidationException("Jam harus diisi!");
                }
                if (tfHarga.getText().trim().isEmpty()) {
                    throw new ValidationException("Harga harus diisi!");
                }

                System.out.println("✓ Validasi input berhasil");

                // Buat objek Jadwal
                System.out.println("\nSTEP 2: Membuat objek Jadwal");
                Jadwal jadwal = new Jadwal();
                jadwal.setIdJadwal(tfKode.getText().trim());
                jadwal.setIdBus("BUS001");
                jadwal.setLokasiAsal(tfAsal.getText().trim());
                jadwal.setLokasiTujuan(tfTujuan.getText().trim());

                try {
                    double harga = Double.parseDouble(tfHarga.getText().trim());
                    jadwal.setHarga(harga);
                    System.out.println("  - Harga parsed: " + harga);
                } catch (NumberFormatException e) {
                    throw new ValidationException("Harga harus berupa angka!");
                }

                jadwal.setKursiTersedia(40);

                // Parse datetime
                System.out.println("\nSTEP 3: Parse DateTime");
                String dateTimeStr = tfTanggal.getText().trim() + " " + tfJam.getText().trim() + ":00";
                System.out.println("  - DateTime string: " + dateTimeStr);

                try {
                    LocalDateTime waktuBerangkat = LocalDateTime.parse(dateTimeStr,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    jadwal.setWaktuBerangkat(waktuBerangkat);
                    jadwal.setWaktuTiba(waktuBerangkat.plusHours(3));
                    System.out.println("  - Waktu berangkat: " + waktuBerangkat);
                    System.out.println("  - Waktu tiba: " + jadwal.getWaktuTiba());
                } catch (Exception e) {
                    System.err.println("❌ Error parsing datetime: " + e.getMessage());
                    throw new ValidationException("Format tanggal/jam salah! Gunakan: yyyy-MM-dd dan HH:mm");
                }

                System.out.println("✓ Objek Jadwal berhasil dibuat");

                // Simpan ke database
                System.out.println("\nSTEP 4: Simpan ke database");
                System.out.println("  - Memanggil jadwalDB.create()...");

                boolean result = jadwalDB.create(jadwal);
                System.out.println("  - Result: " + result);

                if (result) {
                    System.out.println("✓ Jadwal berhasil disimpan ke database");
                    JOptionPane.showMessageDialog(this,
                            "Jadwal berhasil ditambahkan!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);

                    System.out.println("\nSTEP 5: Refresh tabel");
                    loadData();
                    clearForm();
                    System.out.println("✓ handleAdd() selesai dengan sukses");
                } else {
                    System.err.println("❌ Database return false");
                    JOptionPane.showMessageDialog(this,
                            "Gagal menambahkan jadwal! Database return false",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (ValidationException ex) {
                System.err.println("❌ ValidationException: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (DatabaseException ex) {
                System.err.println("❌ DatabaseException: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error database: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.err.println("❌ Exception: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error tidak terduga: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void handleUpdate() {
            System.out.println(">>> handleUpdate() dipanggil");

            int row = table.getSelectedRow();
            System.out.println("  - Selected row: " + row);

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih jadwal terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String kode = (String) tableModel.getValueAt(row, 0);
                System.out.println("  - Kode jadwal: " + kode);

                Jadwal jadwal = findJadwalByKode(kode);

                if (jadwal != null) {
                    System.out.println("  - Jadwal ditemukan, update data...");
                    jadwal.setLokasiAsal(tfAsal.getText().trim());
                    jadwal.setLokasiTujuan(tfTujuan.getText().trim());
                    jadwal.setHarga(Double.parseDouble(tfHarga.getText().trim()));

                    System.out.println("  - Memanggil jadwalDB.update()...");
                    if (jadwalDB.update(jadwal)) {
                        System.out.println("✓ Update berhasil");
                        JOptionPane.showMessageDialog(this, "Jadwal berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                    } else {
                        System.err.println("❌ Update gagal");
                    }
                } else {
                    System.err.println("❌ Jadwal tidak ditemukan");
                }
            } catch (Exception ex) {
                System.err.println("❌ Error update: " + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void handleDelete() {
            System.out.println(">>> handleDelete() dipanggil");

            int row = table.getSelectedRow();
            System.out.println("  - Selected row: " + row);

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih jadwal terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus jadwal ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String kode = (String) tableModel.getValueAt(row, 0);
                    System.out.println("  - Kode jadwal: " + kode);
                    System.out.println("  - Memanggil jadwalDB.delete()...");

                    if (jadwalDB.delete(kode)) {
                        System.out.println("✓ Delete berhasil");
                        JOptionPane.showMessageDialog(this, "Jadwal berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        clearForm();
                    } else {
                        System.err.println("❌ Delete gagal");
                    }
                } catch (Exception ex) {
                    System.err.println("❌ Error delete: " + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.out.println("  - Delete dibatalkan user");
            }
        }

        private void loadData() {
            System.out.println(">>> loadData() dipanggil");

            try {
                System.out.println("  - Memanggil jadwalDB.getAll()...");
                dataJadwal = jadwalDB.getAll();
                System.out.println("  - Jumlah data: " + dataJadwal.size());

                tableModel.setRowCount(0);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                int count = 0;
                for (Jadwal j : dataJadwal) {
                    count++;
                    System.out.println("  - Load jadwal " + count + ": " + j.getIdJadwal());

                    Object[] row = {
                            j.getIdJadwal(),
                            j.getLokasiAsal(),
                            j.getLokasiTujuan(),
                            j.getWaktuBerangkat() != null ? j.getWaktuBerangkat().format(dateFormatter) : "-",
                            j.getWaktuBerangkat() != null ? j.getWaktuBerangkat().format(timeFormatter) : "-",
                            String.format("Rp %.0f", j.getHarga())
                    };
                    tableModel.addRow(row);
                }

                System.out.println("✓ loadData() berhasil, " + count + " baris ditambahkan ke tabel");

            } catch (DatabaseException e) {
                System.err.println("❌ DatabaseException saat loadData: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error load data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                System.err.println("❌ Exception saat loadData: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error tidak terduga: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void loadToForm() {
            int row = table.getSelectedRow();
            if (row != -1) {
                tfKode.setText((String) tableModel.getValueAt(row, 0));
                tfAsal.setText((String) tableModel.getValueAt(row, 1));
                tfTujuan.setText((String) tableModel.getValueAt(row, 2));
                tfTanggal.setText((String) tableModel.getValueAt(row, 3));
                tfJam.setText((String) tableModel.getValueAt(row, 4));
                String harga = (String) tableModel.getValueAt(row, 5);
                tfHarga.setText(harga.replace("Rp ", "").replace(".", "").replace(",", "").trim());
            }
        }

        private void clearForm() {
            System.out.println("DEBUG: clearForm() dipanggil");
            tfKode.setText("");
            tfAsal.setText("");
            tfTujuan.setText("");
            tfTanggal.setText("");
            tfJam.setText("");
            tfHarga.setText("");
        }

        private Jadwal findJadwalByKode(String kode) {
            for (Jadwal j : dataJadwal) {
                if (j.getIdJadwal().equals(kode)) {
                    return j;
                }
            }
            return null;
        }
    }

    // ================================================================
    // CEK JADWAL PANEL (PELANGGAN)
    // ================================================================

    class CekJadwalPanel extends JPanel {

        private JTextField tfAsal, tfTujuan, tfTanggal;
        private JTable table;
        private DefaultTableModel tableModel;

        private final JFrame frameUtama;

        public CekJadwalPanel(JFrame frameUtama) {
            this.frameUtama = frameUtama;

            // ====== KONFIG PANEL ======
            setLayout(new BorderLayout(15, 15));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // ====== TITLE ======
            JLabel title = new JLabel("CEK JADWAL BUS");
            title.setFont(new Font("Arial", Font.BOLD, 26));
            title.setForeground(new Color(51, 154, 240));
            title.setHorizontalAlignment(SwingConstants.CENTER);

            add(title, BorderLayout.NORTH);

            // ============================================================
            // FORM INPUT
            // ============================================================
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 6, 6, 6);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            tfAsal = new JTextField(15);
            tfTujuan = new JTextField(15);
            tfTanggal = new JTextField(15);

            // Style input
            Border border = BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    BorderFactory.createEmptyBorder(6, 6, 6, 6)
            );

            tfAsal.setBorder(border);
            tfTujuan.setBorder(border);
            tfTanggal.setBorder(border);

            // Label + input
            gbc.gridx = 0;
            gbc.gridy = 0;
            formPanel.add(new JLabel("Lokasi Asal:"), gbc);

            gbc.gridx = 1;
            formPanel.add(tfAsal, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            formPanel.add(new JLabel("Lokasi Tujuan:"), gbc);

            gbc.gridx = 1;
            formPanel.add(tfTujuan, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            formPanel.add(new JLabel("Tanggal (yyyy-MM-dd):"), gbc);

            gbc.gridx = 1;
            formPanel.add(tfTanggal, gbc);

            // ============================================================
            // BUTTON PANEL
            // ============================================================
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            buttonPanel.setBackground(Color.WHITE);

            JButton btnCari = new JButton("Cari Jadwal");
            JButton btnReset = new JButton("Reset");
            JButton btnKembali = new JButton("Kembali");

            btnCari.setBackground(new Color(51, 154, 240));
            btnCari.setForeground(Color.WHITE);
            btnCari.setFocusPainted(false);

            buttonPanel.add(btnCari);
            buttonPanel.add(btnReset);
            buttonPanel.add(btnKembali);

            // Masukkan form + tombol
            JPanel middle = new JPanel(new BorderLayout());
            middle.setBackground(Color.WHITE);

            middle.add(formPanel, BorderLayout.CENTER);
            middle.add(buttonPanel, BorderLayout.SOUTH);

            add(middle, BorderLayout.CENTER);

            // ============================================================
            // TABLE TAMPIL JADWAL
            // ============================================================
            String[] kolom = {"ID", "Bus", "Asal", "Tujuan", "Berangkat", "Tiba", "Harga"};

            tableModel = new DefaultTableModel(kolom, 0);
            table = new JTable(tableModel);

            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(750, 300));
            add(scroll, BorderLayout.SOUTH);

            // ============================================================
            // EVENT BUTTON
            // ============================================================

            // 🔍 CARI
            btnCari.addActionListener(e -> {
                String asal = tfAsal.getText();
                String tujuan = tfTujuan.getText();
                String tanggal = tfTanggal.getText();

                try {
                    JadwalDB db = new JadwalDB();
                    List<Jadwal> hasil = db.search(asal, tujuan, tanggal);

                    tableModel.setRowCount(0); // clear table

                    for (Jadwal j : hasil) {
                        tableModel.addRow(new Object[]{
                                j.getIdJadwal(),
                                j.getIdBus(),
                                j.getLokasiAsal(),
                                j.getLokasiTujuan(),
                                j.getWaktuBerangkat(),
                                j.getWaktuTiba(),
                                j.getHarga()
                        });
                    }

                    if (hasil.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Tidak ada jadwal ditemukan.");
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Terjadi kesalahan saat mencari jadwal:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            // 🔄 RESET
            btnReset.addActionListener(e -> {
                tfAsal.setText("");
                tfTujuan.setText("");
                tfTanggal.setText("");
                tableModel.setRowCount(0);
            });

            // ⬅ KEMBALI
            btnKembali.addActionListener(e -> {
                // SAFE: dispose parent frame and return to pilih mode.
                frameUtama.dispose();
                // TODO: Panggil layar PilihModeView / LoginRoleView jika ada:
                // new PilihModeView(...).setVisible(true);
            });
        }
    }

    // ================================================================
    // Simple placeholder PemesananPanel agar file kompilasi (ganti dgn implementasimu)
    // ================================================================
    class PemesananPanel extends JPanel {
        public PemesananPanel() {
            setLayout(new BorderLayout());
            add(new JLabel("Panel Pemesanan - placeholder"), BorderLayout.CENTER);
        }

        public void updateInfo() {
            // TODO: implementasi update info sebelum tampil
            System.out.println("PemesananPanel.updateInfo() dipanggil");
        }
    }
}
