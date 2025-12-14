package com.tiket.view;

import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.JadwalNotFoundException;
import com.tiket.model.Jadwal;
import com.tiket.helper.Helper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JadwalPanel extends JPanel {
    private JadwalController jadwalController;
    private JTable tableJadwal;
    private DefaultTableModel tableModel;
    private JButton btnTambah, btnEdit, btnHapus, btnRefresh, btnCari;
    private JTextField txtCariAsal, txtCariTujuan;
    
    public JadwalPanel(JadwalController controller) {
        this.jadwalController = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initUI();
        loadData();
    }
    
    private void initUI() {
        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("KELOLA JADWAL BUS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Panel Pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Asal:"));
        txtCariAsal = new JTextField(15);
        searchPanel.add(txtCariAsal);
        
        searchPanel.add(new JLabel("Tujuan:"));
        txtCariTujuan = new JTextField(15);
        searchPanel.add(txtCariTujuan);
        
        btnCari = new JButton("Cari");
        btnCari.setBackground(new Color(52, 152, 219));
        btnCari.setForeground(Color.WHITE);
        searchPanel.add(btnCari);
        
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Table Panel
        String[] columns = {"ID Jadwal", "Asal", "Tujuan", "Berangkat", "Tiba", "Durasi", "Harga"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableJadwal = new JTable(tableModel);
        tableJadwal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableJadwal.setRowHeight(25);
        tableJadwal.getTableHeader().setReorderingAllowed(false);
        
        // Set column widths
        tableJadwal.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableJadwal.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableJadwal.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableJadwal.getColumnModel().getColumn(3).setPreferredWidth(130);
        tableJadwal.getColumnModel().getColumn(4).setPreferredWidth(130);
        tableJadwal.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableJadwal.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableJadwal);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnTambah = createButton("Tambah", new Color(46, 204, 113));
        btnEdit = createButton("Edit", new Color(241, 196, 15));
        btnHapus = createButton("Hapus", new Color(231, 76, 60));
        btnRefresh = createButton("Refresh", new Color(149, 165, 166));
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event Listeners
        btnTambah.addActionListener(e -> tambahJadwal());
        btnEdit.addActionListener(e -> editJadwal());
        btnHapus.addActionListener(e -> hapusJadwal());
        btnRefresh.addActionListener(e -> loadData());
        btnCari.addActionListener(e -> cariJadwal());
        
        // Double click to edit
        tableJadwal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editJadwal();
                }
            }
        });
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 35));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }
    
    public void loadData() {
    tableModel.setRowCount(0);
    
    try {
        List<Jadwal> jadwalList = jadwalController.getAllJadwal();
        
        for (Jadwal jadwal : jadwalList) {
            // Dapatkan jumlah kursi tersedia
            int kursiTersedia = jadwalController.getJumlahKursiTersedia(jadwal.getIdJadwal());
            
            Object[] row = {
                jadwal.getIdJadwal(),
                jadwal.getLokasiAsal(),
                jadwal.getLokasiTujuan(),
                Helper.formatForDisplay(jadwal.getWaktuBerangkat()),
                Helper.formatForDisplay(jadwal.getWaktuTiba()),
                Helper.getDurationText(jadwal.getWaktuBerangkat(), jadwal.getWaktuTiba()),
                Helper.formatRupiahWithoutDecimal(jadwal.getHarga())
            };
            tableModel.addRow(row);
        }
        
        if (jadwalList.isEmpty()) {
    JOptionPane.showMessageDialog(this,
        "Belum ada data jadwal",
        "Info",
        JOptionPane.INFORMATION_MESSAGE);
}

} catch (Exception e) {
    JOptionPane.showMessageDialog(this,
        "Gagal memuat data!\n" + e.getMessage(),
        "Error",
        JOptionPane.ERROR_MESSAGE);
}
}
    
    private void tambahJadwal() {
        JadwalForm form = new JadwalForm(jadwalController);
        form.setVisible(true);
        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadData();
            }
        });
    }
    
    private void editJadwal() {
        int selectedRow = tableJadwal.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih jadwal yang akan diedit!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String idJadwal = tableModel.getValueAt(selectedRow, 0).toString();
            Jadwal jadwal = jadwalController.getJadwalById(idJadwal);
            
            JadwalForm form = new JadwalForm(jadwalController, jadwal);
            form.setVisible(true);
            form.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    loadData();
                }
            });
            
        } catch (DatabaseException | JadwalNotFoundException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal membuka form edit!\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusJadwal() {
        int selectedRow = tableJadwal.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih jadwal yang akan dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idJadwal = tableModel.getValueAt(selectedRow, 0).toString();
        String asal = tableModel.getValueAt(selectedRow, 1).toString();
        String tujuan = tableModel.getValueAt(selectedRow, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus jadwal ini?\n\n" +
            "ID: " + idJadwal + "\n" +
            "Rute: " + asal + " → " + tujuan,
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                jadwalController.hapusJadwal(idJadwal);
                
                JOptionPane.showMessageDialog(this,
                    "Jadwal berhasil dihapus!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                
                loadData();
                
            } catch (DatabaseException | JadwalNotFoundException e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus jadwal!\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cariJadwal() {
        String asal = txtCariAsal.getText().trim();
        String tujuan = txtCariTujuan.getText().trim();
        
        if (asal.isEmpty() && tujuan.isEmpty()) {
            loadData();
            return;
        }
        
        tableModel.setRowCount(0);
        
        try {
            List<Jadwal> jadwalList = jadwalController.cariJadwal(
                asal.isEmpty() ? "" : asal,
                tujuan.isEmpty() ? "" : tujuan
            );
            
            for (Jadwal jadwal : jadwalList) {
                Object[] row = {
                    jadwal.getIdJadwal(),
                    jadwal.getLokasiAsal(),
                    jadwal.getLokasiTujuan(),
                    Helper.formatForDisplay(jadwal.getWaktuBerangkat()),
                    Helper.formatForDisplay(jadwal.getWaktuTiba()),
                    Helper.getDurationText(jadwal.getWaktuBerangkat(), jadwal.getWaktuTiba()),
                    Helper.formatRupiahWithoutDecimal(jadwal.getHarga())
                };
                tableModel.addRow(row);
            }
            
            if (jadwalList.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Tidak ada jadwal yang sesuai dengan pencarian",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this,
                "Gagal mencari jadwal!\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}