package com.tiket.view;

import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.model.Jadwal;
import com.tiket.model.Kursi;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class JadwalForm extends JFrame {

    private JadwalController jadwalController;

    private JTextField txtAsal, txtTujuan, txtBerangkat, txtTiba, txtHarga;

    public JadwalForm(JadwalController controller) {
        this.jadwalController = controller;

        setTitle("Tambah Jadwal");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Lokasi Asal"));
        txtAsal = new JTextField();
        panel.add(txtAsal);

        panel.add(new JLabel("Lokasi Tujuan"));
        txtTujuan = new JTextField();
        panel.add(txtTujuan);

        panel.add(new JLabel("Waktu Berangkat"));
        txtBerangkat = new JTextField("yyyy-MM-dd HH:mm");
        panel.add(txtBerangkat);

        panel.add(new JLabel("Waktu Tiba"));
        txtTiba = new JTextField("yyyy-MM-dd HH:mm");
        panel.add(txtTiba);

        panel.add(new JLabel("Harga"));
        txtHarga = new JTextField();
        panel.add(txtHarga);

        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");

        panel.add(btnSimpan);
        panel.add(btnBatal);

        add(panel);

        // EVENT
        btnSimpan.addActionListener(e -> simpanJadwal());
        btnBatal.addActionListener(e -> dispose());
    }

    private void simpanJadwal() {
        try {
            String idJadwal = "JDW-" + UUID.randomUUID()
                    .toString().substring(0, 8).toUpperCase();

            String asal = txtAsal.getText();
            String tujuan = txtTujuan.getText();

            LocalDateTime berangkat = LocalDateTime.parse(
                    txtBerangkat.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            LocalDateTime tiba = LocalDateTime.parse(
                    txtTiba.getText(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            double harga = Double.parseDouble(txtHarga.getText());

            Jadwal jadwal = new Jadwal(
                    idJadwal, asal, tujuan, berangkat, tiba, harga
            );

            // Tambah 40 kursi
            for (int i = 1; i <= 40; i++) {
                Kursi kursi = new Kursi(
                        "KRS-" + idJadwal + "-" + i, i
                );
                jadwal.tambahKursi(kursi);
            }

            jadwalController.tambahJadwal(jadwal);

            JOptionPane.showMessageDialog(this,
                    "Jadwal berhasil ditambahkan!\nID: " + idJadwal);

            dispose();

        } catch (DatabaseException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error Database",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Input tidak valid!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
