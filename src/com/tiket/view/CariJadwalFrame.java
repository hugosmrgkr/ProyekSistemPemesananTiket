package com.tiket.view;

import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.model.Jadwal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CariJadwalFrame extends JFrame {

    private JadwalController jadwalController;

    private JTextField txtAsal, txtTujuan;
    private JTable table;
    private DefaultTableModel model;

    public CariJadwalFrame(JadwalController controller) {
        this.jadwalController = controller;

        setTitle("Cari Jadwal");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        // Panel atas (form)
        JPanel top = new JPanel(new GridLayout(2, 3, 10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        top.add(new JLabel("Lokasi Asal"));
        txtAsal = new JTextField();
        top.add(txtAsal);

        JButton btnCari = new JButton("Cari");
        top.add(btnCari);

        top.add(new JLabel("Lokasi Tujuan"));
        txtTujuan = new JTextField();
        top.add(txtTujuan);

        JButton btnTutup = new JButton("Tutup");
        top.add(btnTutup);

        add(top, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(
                new String[]{
                        "ID Jadwal",
                        "Asal",
                        "Tujuan",
                        "Berangkat",
                        "Tiba",
                        "Harga",
                        "Kursi Tersedia"
                }, 0
        );

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // EVENT
        btnCari.addActionListener(e -> cari());
        btnTutup.addActionListener(e -> dispose());
    }

    private void cari() {
        try {
            model.setRowCount(0);

            List<Jadwal> list = jadwalController
                    .cariJadwal(
                            txtAsal.getText(),
                            txtTujuan.getText()
                    );

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Jadwal tidak ditemukan");
                return;
            }

            for (Jadwal j : list) {
                model.addRow(new Object[]{
                        j.getIdJadwal(),
                        j.getLokasiAsal(),
                        j.getLokasiTujuan(),
                        j.getWaktuBerangkat(),
                        j.getWaktuTiba(),
                        j.getHarga(),
                        j.getKursiTersedia().size()
                });
            }

        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
