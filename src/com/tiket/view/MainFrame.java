package com.tiket.view;

import com.tiket.controller.*;
import com.tiket.exception.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JadwalController jadwalController;
    private PemesananController pemesananController;

    public MainFrame() {
        try {
            jadwalController = new JadwalController();
            pemesananController = new PemesananController();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this,
                    "Gagal koneksi database:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Sistem Pemesanan Tiket Bus");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("SISTEM PEMESANAN TIKET BUS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JButton btnKelola = new JButton("Kelola Jadwal");
        JButton btnCari = new JButton("Cari Jadwal");
        JButton btnPesan = new JButton("Pesan Tiket");
        JButton btnLihat = new JButton("Lihat Semua Jadwal");
        JButton btnKeluar = new JButton("Keluar");

        panel.add(title);
        panel.add(btnKelola);
        panel.add(btnCari);
        panel.add(btnPesan);
        panel.add(btnLihat);

        add(panel, BorderLayout.CENTER);
        add(btnKeluar, BorderLayout.SOUTH);

        // === EVENT ===
        btnKelola.addActionListener(e ->
                new JadwalForm(jadwalController).setVisible(true)
        );

        btnCari.addActionListener(e ->
                new CariJadwalFrame(jadwalController).setVisible(true)
        );

        btnPesan.addActionListener(e ->
                new PesanTiketFrame(
                        jadwalController,
                        pemesananController
                ).setVisible(true)
        );

        btnLihat.addActionListener(e -> lihatSemuaJadwal());

        btnKeluar.addActionListener(e -> System.exit(0));
    }

    private void lihatSemuaJadwal() {
        try {
            var list = jadwalController.getAllJadwal();
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Belum ada jadwal.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append(i + 1).append(". ")
                  .append(list.get(i).getInfo())
                  .append("\nID: ").append(list.get(i).getIdJadwal())
                  .append("\n\n");
            }

            JTextArea area = new JTextArea(sb.toString());
            area.setEditable(false);

            JOptionPane.showMessageDialog(this,
                    new JScrollPane(area),
                    "Daftar Jadwal",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
