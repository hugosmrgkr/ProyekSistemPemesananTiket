/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.view;

import javax.swing.*;
import java.awt.*;

/**
 * Halaman awal untuk memilih mode: Admin atau Pelanggan
 */
public class PilihModeView extends JFrame {
    
    public PilihModeView() {
        setTitle("Sistem Pemesanan Tiket Bus");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    PilihModeView(JFrame frameUtama) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(51, 154, 240));
        headerPanel.setPreferredSize(new Dimension(500, 100));
        
        JLabel lblTitle = new JLabel("SISTEM PEMESANAN TIKET BUS");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);
        
        // Center - Buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 245));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblPilih = new JLabel("Pilih Mode Akses:");
        lblPilih.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(lblPilih, gbc);
        
        // Button Admin
        JButton btnAdmin = new JButton("🔐 ADMIN");
        btnAdmin.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdmin.setPreferredSize(new Dimension(200, 60));
        btnAdmin.setBackground(new Color(255, 107, 107));
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setFocusPainted(false);
        btnAdmin.setBorderPainted(false);
        btnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnAdmin.addActionListener(e -> openMainView("ADMIN"));
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(btnAdmin, gbc);
        
        // Button Pelanggan
        JButton btnPelanggan = new JButton("👤 PELANGGAN");
        btnPelanggan.setFont(new Font("Arial", Font.BOLD, 18));
        btnPelanggan.setPreferredSize(new Dimension(200, 60));
        btnPelanggan.setBackground(new Color(81, 207, 102));
        btnPelanggan.setForeground(Color.WHITE);
        btnPelanggan.setFocusPainted(false);
        btnPelanggan.setBorderPainted(false);
        btnPelanggan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPelanggan.addActionListener(e -> openMainView("PELANGGAN"));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(btnPelanggan, gbc);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 245));
        JLabel lblFooter = new JLabel("© 2024 Sistem Tiket Bus");
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        lblFooter.setForeground(Color.GRAY);
        footerPanel.add(lblFooter);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void openMainView(String mode) {
        MainView mainView = new MainView(mode);
        mainView.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PilihModeView().setVisible(true);
        });
    }
}