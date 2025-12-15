package com.tiket.view;

import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.exception.JadwalNotFoundException;
import com.tiket.model.Jadwal;
import com.tiket.helper.Helper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
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
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250));
        initUI();
        loadData();
    }
    
    private void initUI() {
        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(245, 247, 250));
        
        JLabel lblTitle = new JLabel("KELOLA JADWAL BUS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Panel Pencarian dengan border dan background
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblAsal = new JLabel("Asal:");
        lblAsal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAsal.setForeground(new Color(52, 73, 94));
        searchPanel.add(lblAsal);
        
        txtCariAsal = new JTextField(15);
        txtCariAsal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCariAsal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchPanel.add(txtCariAsal);
        
        JLabel lblTujuan = new JLabel("Tujuan:");
        lblTujuan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTujuan.setForeground(new Color(52, 73, 94));
        searchPanel.add(lblTujuan);
        
        txtCariTujuan = new JTextField(15);
        txtCariTujuan.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCariTujuan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchPanel.add(txtCariTujuan);
        
        btnCari = new JButton("Cari");
        btnCari.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCari.setBackground(new Color(52, 152, 219));
        btnCari.setForeground(Color.WHITE);
        btnCari.setFocusPainted(false);
        btnCari.setBorderPainted(false);
        btnCari.setOpaque(true);
        btnCari.setContentAreaFilled(true);
        btnCari.setPreferredSize(new Dimension(80, 32));
        btnCari.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchPanel.add(btnCari);
        
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Table Panel dengan styling
        String[] columns = {"ID Jadwal", "Asal", "Tujuan", "Berangkat", "Tiba", "Durasi", "Harga"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableJadwal = new JTable(tableModel);
        tableJadwal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableJadwal.setRowHeight(30);
        tableJadwal.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableJadwal.setSelectionBackground(new Color(232, 240, 254));
        tableJadwal.setSelectionForeground(new Color(44, 62, 80));
        tableJadwal.setGridColor(new Color(220, 225, 230));
        tableJadwal.setShowVerticalLines(true);
        tableJadwal.setShowHorizontalLines(true);
        tableJadwal.setBackground(Color.WHITE);
        tableJadwal.getTableHeader().setReorderingAllowed(false);
        
        // Custom Header Renderer - Fixed color issue
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(41, 128, 185));
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                label.setOpaque(true);
                return label;
            }
        };
        
        // Apply custom renderer to all columns
        for (int i = 0; i < tableJadwal.getColumnModel().getColumnCount(); i++) {
            tableJadwal.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        tableJadwal.getTableHeader().setPreferredSize(new Dimension(0, 35));
        tableJadwal.getTableHeader().setBackground(new Color(41, 128, 185));
        tableJadwal.getTableHeader().setForeground(Color.WHITE);
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tableJadwal.getColumnCount(); i++) {
            tableJadwal.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Set column widths
        tableJadwal.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableJadwal.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableJadwal.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableJadwal.getColumnModel().getColumn(3).setPreferredWidth(140);
        tableJadwal.getColumnModel().getColumn(4).setPreferredWidth(140);
        tableJadwal.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableJadwal.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableJadwal);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel dengan spacing yang lebih baik
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 15));
        buttonPanel.setBackground(new Color(245, 247, 250));
        
        btnTambah = createStyledButton("Tambah", new Color(46, 204, 113));
        btnEdit = createStyledButton("Edit", new Color(241, 196, 15));
        btnHapus = createStyledButton("Hapus", new Color(231, 76, 60));
        btnRefresh = createStyledButton("Refresh", new Color(149, 165, 166));
        
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
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(120, 38));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    public void loadData() {
        tableModel.setRowCount(0);
        
        try {
            List<Jadwal> jadwalList = jadwalController.getAllJadwal();
            
            for (Jadwal jadwal : jadwalList) {
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