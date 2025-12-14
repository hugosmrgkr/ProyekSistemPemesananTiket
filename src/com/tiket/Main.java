package com.tiket;

import com.tiket.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║  SELAMAT DATANG DI SISTEM PEMESANAN TIKET BUS ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        
        try {
            // Set Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}