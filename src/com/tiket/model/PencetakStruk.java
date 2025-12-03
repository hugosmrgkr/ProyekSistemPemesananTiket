/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.model;

/**
 * Class PencetakStruk
 * Sesuai Class Diagram: PencetakStruk menggunakan Tiket untuk cetak struk
 * Utility class untuk mencetak struk (bisa juga di helper package)
 */
public class PencetakStruk {
    
    /**
     * Method untuk mencetak struk dari Tiket
     * Sesuai diagram: cetak(t: Tiket): String
     * @param tiket object Tiket yang akan dicetak
     * @return String berisi struk
     */
    public String cetak(Tiket tiket) {
        if (tiket == null) {
            return "Error: Tiket tidak valid!";
        }
        
        // Gunakan method cetakStruk() dari Tiket (implements Printable)
        return tiket.cetakStruk();
    }
    

//     Method alternatif untuk cetak dengan custom format
//     @param tiket object Tiket
//     @return String struk dengan format singkat

    public String cetakSingkat(Tiket tiket) {
        if (tiket == null) {
            return "Error: Tiket tidak valid!";
        }
        
        StringBuilder struk = new StringBuilder();
        struk.append("==================================\n");
        struk.append("         TIKET BUS\n");
        struk.append("==================================\n");
        struk.append("ID       : ").append(tiket.getIdTiket()).append("\n");
        struk.append("Nama     : ").append(tiket.getNamaPelanggan()).append("\n");
        struk.append("Kursi    : ").append(tiket.getNomorKursi()).append("\n");
        struk.append("Harga    : Rp ").append(String.format("%,.0f", tiket.getHarga())).append("\n");
        struk.append("==================================\n");
        
        return struk.toString();
    }
}
