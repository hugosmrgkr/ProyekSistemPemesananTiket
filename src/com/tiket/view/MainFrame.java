package com.tiket.view;

import com.tiket.controller.JadwalController;
import com.tiket.controller.PemesananController;
import com.tiket.exception.DatabaseException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException; 

public class MainFrame extends JFrame {
    
    private JadwalController jadwalController;
    private PemesananController pemesananController;
    
    private JTabbedPane tabbedPane;
    private JadwalPanel jadwalPanel;
    
    public MainFrame() {
        try {
            this.jadwalController = new JadwalController();
            this.pemesananController = new PemesananController();
        } catch (DatabaseException | SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Gagal koneksi database: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Sistem Pemesanan Tiket Bus");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        initUI();
    }
    
    private void initUI() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuFile = new JMenu("File");
        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(e -> System.exit(0));
        menuFile.add(menuExit);
        
        JMenu menuPemesanan = new JMenu("Pemesanan");
        JMenuItem menuPesanTiket = new JMenuItem("Pesan Tiket");
        menuPesanTiket.addActionListener(e -> bukaFormPesanTiket());
        menuPemesanan.add(menuPesanTiket);
        
        JMenu menuHelp = new JMenu("Help");
        JMenuItem menuAbout = new JMenuItem("About");
        menuAbout.addActionListener(e -> tampilkanAbout());
        menuHelp.add(menuAbout);
        
        menuBar.add(menuFile);
        menuBar.add(menuPemesanan);
        menuBar.add(menuHelp);
        
        setJMenuBar(menuBar);
        
        tabbedPane = new JTabbedPane();
        
        jadwalPanel = new JadwalPanel(jadwalController);
        tabbedPane.addTab("Kelola Jadwal", jadwalPanel);
        
        JPanel pesanTiketTab = createPesanTiketTab();
        tabbedPane.addTab("Pesan Tiket", pesanTiketTab);
        
        add(tabbedPane);
    }
    
    private JPanel createPesanTiketTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));
        
        JLabel lblTitle = new JLabel("PEMESANAN TIKET BUS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(44, 62, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(40, 0, 50, 0));
        
        JButton btnPesanTiket = new JButton("Buat Pemesanan Baru");
        btnPesanTiket.setPreferredSize(new Dimension(250, 50));
        btnPesanTiket.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnPesanTiket.setBackground(new Color(46, 204, 113));
        btnPesanTiket.setForeground(Color.WHITE);
        btnPesanTiket.setFocusPainted(false);
        btnPesanTiket.setBorderPainted(false);
        btnPesanTiket.setOpaque(true);
        btnPesanTiket.setContentAreaFilled(true);
        btnPesanTiket.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPesanTiket.addActionListener(e -> bukaFormPesanTiket());
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(btnPesanTiket);
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void bukaFormPesanTiket() {
        PesanTiketFrame frame = new PesanTiketFrame(jadwalController, pemesananController);
        frame.setVisible(true);
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (jadwalPanel != null) {
                    jadwalPanel.loadData();
                }
            }
        });
    }
    
    private void tampilkanAbout() {
        JOptionPane.showMessageDialog(this,
            "Sistem Pemesanan Tiket Bus\n" +
            "Versi 1.0.0\n\n" +
            "Aplikasi untuk memesan tiket bus\n" +
            "dengan fitur lengkap CRUD jadwal\n" +
            "dan pemesanan tiket.",
            "About",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}