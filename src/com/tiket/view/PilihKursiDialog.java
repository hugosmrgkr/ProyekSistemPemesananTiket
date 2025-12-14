package com.tiket.view;

import com.tiket.model.Jadwal;
import com.tiket.model.Kursi;

import javax.swing.*;
import java.awt.*;

public class PilihKursiDialog extends JDialog {

    private int kursiTerpilih = -1;

    public PilihKursiDialog(JFrame parent, Jadwal jadwal) {
        super(parent, "Pilih Kursi", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(5, 8, 5, 5));

        for (Kursi kursi : jadwal.getKursiList()) {

            JButton btn = new JButton(
                    String.valueOf(kursi.getNomorKursi())
            );

            btn.addActionListener(e -> {
                kursiTerpilih = kursi.getNomorKursi();
                dispose();
            });

            panel.add(btn);
        }

        add(panel);
    }

    public int getKursiTerpilih() {
        return kursiTerpilih;
    }
}
