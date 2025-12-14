package com.tiket.view;

import com.tiket.controller.PemesananController;
import com.tiket.controller.JadwalController;
import com.tiket.exception.DatabaseException;
import com.tiket.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class PesanTiketFrame extends JFrame {

    private JadwalController jadwalController;
    private PemesananController pemesananController;

    private JTextField txtIdJadwal, txtKursi;
    private JTextField txtNama, txtEmail, txtTelepon;
    private JComboBox<String> cmbMetode;

    private Jadwal jadwalAktif;

    public PesanTiketFrame(JadwalController jc, PemesananController pc) {
        this.jadwalController = jc;
        this.pemesananController = pc;

        setTitle("Pesan Tiket");
        setSize(450, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ID Jadwal
        panel.add(new JLabel("ID Jadwal"));
        txtIdJadwal = new JTextField();
        panel.add(txtIdJadwal);

        JButton btnCari = new JButton("Cari Jadwal");
        panel.add(btnCari);
        panel.add(new JLabel());

        // Kursi
        panel.add(new JLabel("Nomor Kursi"));
        JPanel kursiPanel = new JPanel(new BorderLayout());
        txtKursi = new JTextField();
        txtKursi.setEditable(false);
        JButton btnPilihKursi = new JButton("Pilih");
        kursiPanel.add(txtKursi, BorderLayout.CENTER);
        kursiPanel.add(btnPilihKursi, BorderLayout.EAST);
        panel.add(kursiPanel);

        // Nama
        panel.add(new JLabel("Nama"));
        txtNama = new JTextField();
        panel.add(txtNama);

        // Email
        panel.add(new JLabel("Email"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        // Telepon
        panel.add(new JLabel("Telepon"));
        txtTelepon = new JTextField();
        panel.add(txtTelepon);

        // Metode
        panel.add(new JLabel("Metode Pembayaran"));
        cmbMetode = new JComboBox<>(new String[]{
                "Transfer Bank", "E-Wallet", "Cash"
        });
        panel.add(cmbMetode);

        JButton btnPesan = new JButton("Pesan & Bayar");
        JButton btnBatal = new JButton("Batal");

        panel.add(btnPesan);
        panel.add(btnBatal);

        add(panel);

        // ================= EVENT =================

        btnCari.addActionListener(e -> cariJadwal());

        btnPilihKursi.addActionListener(e -> {

            if (jadwalAktif == null) {
                JOptionPane.showMessageDialog(this,
                        "Cari jadwal terlebih dahulu!");
                return;
            }

            PilihKursiDialog dialog =
                    new PilihKursiDialog(this, jadwalAktif);

            dialog.setVisible(true);

            int kursi = dialog.getKursiTerpilih();
            if (kursi != -1) {
                txtKursi.setText(String.valueOf(kursi));
            }
        });

        btnPesan.addActionListener(e -> pesanTiket());
        btnBatal.addActionListener(e -> dispose());
    }

    private void cariJadwal() {
        try {
            jadwalAktif = jadwalController
                    .getJadwalById(txtIdJadwal.getText());

            JOptionPane.showMessageDialog(this,
                    "Jadwal ditemukan:\n" +
                    jadwalAktif.getInfo() +
                    "\nKursi tersedia: " +
                    jadwalAktif.getKursiTersedia().size());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Jadwal tidak ditemukan!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesanTiket() {

        if (jadwalAktif == null) {
            JOptionPane.showMessageDialog(this,
                    "Cari jadwal terlebih dahulu!");
            return;
        }

        try {
            int nomorKursi = Integer.parseInt(txtKursi.getText());

            Pelanggan pelanggan = new Pelanggan(
                    "PLG-" + UUID.randomUUID()
                            .toString().substring(0, 8).toUpperCase(),
                    txtNama.getText(),
                    txtEmail.getText(),
                    txtTelepon.getText()
            );

            Tiket tiket = pemesananController.pesanTiket(
                    jadwalAktif, nomorKursi, pelanggan
            );

            Transaksi transaksi = pemesananController.buatTransaksi(
                    tiket,
                    cmbMetode.getSelectedItem().toString()
            );

            int konfirmasi = JOptionPane.showConfirmDialog(
                    this,
                    "Bayar Rp " + transaksi.getJumlah() + "?",
                    "Konfirmasi Pembayaran",
                    JOptionPane.YES_NO_OPTION
            );

            if (konfirmasi == JOptionPane.YES_OPTION) {
                pemesananController
                        .bayarTransaksi(transaksi.getIdTransaksi());

                String struk = pemesananController
                        .cetakStruk(tiket.getIdTiket());

                JTextArea area = new JTextArea(struk);
                area.setEditable(false);

                JOptionPane.showMessageDialog(this,
                        new JScrollPane(area),
                        "STRUK PEMBAYARAN",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
            }

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
