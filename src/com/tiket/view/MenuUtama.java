package com.tiket.view;

import com.tiket.controller.*;
import com.tiket.exception.*;
import com.tiket.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class MenuUtama {
    private Scanner scanner;
    private JadwalController jadwalController;
    private PemesananController pemesananController;

    public MenuUtama() {
        this.scanner = new Scanner(System.in);
        try {
            this.jadwalController = new JadwalController();
            this.pemesananController = new PemesananController();
        } catch (DatabaseException e) {
            System.out.println("Error koneksi database: " + e.getMessage());
            System.exit(1);
        }
    }

    public void tampilkanMenu() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   SISTEM PEMESANAN TIKET BUS          ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. Kelola Jadwal");
            System.out.println("2. Cari Jadwal");
            System.out.println("3. Pesan Tiket");
            System.out.println("4. Lihat Semua Jadwal");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            
            try {
                int pilihan = scanner.nextInt();
                scanner.nextLine();
                
                switch (pilihan) {
                    case 1:
                        menuKelolaJadwal();
                        break;
                    case 2:
                        menuCariJadwal();
                        break;
                    case 3:
                        menuPesanTiket();
                        break;
                    case 4:
                        menuLihatSemuaJadwal();
                        break;
                    case 5:
                        System.out.println("\nTerima kasih telah menggunakan sistem kami!");
                        running = false;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }

    private void menuKelolaJadwal() {
        System.out.println("\n=== KELOLA JADWAL ===");
        System.out.println("1. Tambah Jadwal");
        System.out.println("2. Update Jadwal");
        System.out.println("3. Hapus Jadwal");
        System.out.print("Pilih: ");
        
        int pilihan = scanner.nextInt();
        scanner.nextLine();
        
        try {
            switch (pilihan) {
                case 1:
                    tambahJadwal();
                    break;
                case 2:
                    updateJadwal();
                    break;
                case 3:
                    hapusJadwal();
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void tambahJadwal() throws DatabaseException {
        System.out.println("\n=== TAMBAH JADWAL ===");
        
        String idJadwal = "JDW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        System.out.print("Lokasi Asal: ");
        String lokasiAsal = scanner.nextLine();
        
        System.out.print("Lokasi Tujuan: ");
        String lokasiTujuan = scanner.nextLine();
        
        System.out.print("Waktu Berangkat (yyyy-MM-dd HH:mm): ");
        String waktuBerangkatStr = scanner.nextLine();
        LocalDateTime waktuBerangkat = LocalDateTime.parse(waktuBerangkatStr, 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        System.out.print("Waktu Tiba (yyyy-MM-dd HH:mm): ");
        String waktuTibaStr = scanner.nextLine();
        LocalDateTime waktuTiba = LocalDateTime.parse(waktuTibaStr, 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        System.out.print("Harga: ");
        double harga = scanner.nextDouble();
        scanner.nextLine();
        
        Jadwal jadwal = new Jadwal(idJadwal, lokasiAsal, lokasiTujuan, waktuBerangkat, waktuTiba, harga);
        
        // Tambah kursi (misalnya 40 kursi)
        for (int i = 1; i <= 40; i++) {
            String idKursi = "KRS-" + idJadwal + "-" + i;
            Kursi kursi = new Kursi(idKursi, i);
            jadwal.tambahKursi(kursi);
        }
        
        jadwalController.tambahJadwal(jadwal);
        System.out.println("✓ Jadwal berhasil ditambahkan dengan ID: " + idJadwal);
    }

    private void updateJadwal() throws DatabaseException, JadwalNotFoundException {
        System.out.println("\n=== UPDATE JADWAL ===");
        
        System.out.print("ID Jadwal: ");
        String idJadwal = scanner.nextLine();
        
        Jadwal jadwal = jadwalController.getJadwalById(idJadwal);
        
        System.out.println("Data saat ini:");
        System.out.println(jadwal.getInfo());
        
        System.out.print("Lokasi Asal baru (kosongkan jika tidak diubah): ");
        String lokasiAsal = scanner.nextLine();
        if (!lokasiAsal.isEmpty()) {
            jadwal.setLokasiAsal(lokasiAsal);
        }
        
        System.out.print("Lokasi Tujuan baru (kosongkan jika tidak diubah): ");
        String lokasiTujuan = scanner.nextLine();
        if (!lokasiTujuan.isEmpty()) {
            jadwal.setLokasiTujuan(lokasiTujuan);
        }
        
        System.out.print("Harga baru (0 jika tidak diubah): ");
        double harga = scanner.nextDouble();
        scanner.nextLine();
        if (harga > 0) {
            jadwal.setHarga(harga);
        }
        
        jadwalController.updateJadwal(jadwal);
        System.out.println("✓ Jadwal berhasil diupdate!");
    }

    private void hapusJadwal() throws DatabaseException, JadwalNotFoundException {
        System.out.println("\n=== HAPUS JADWAL ===");
        
        System.out.print("ID Jadwal: ");
        String idJadwal = scanner.nextLine();
        
        jadwalController.hapusJadwal(idJadwal);
        System.out.println("✓ Jadwal berhasil dihapus!");
    }

    private void menuCariJadwal() {
        System.out.println("\n=== CARI JADWAL ===");
        
        System.out.print("Lokasi Asal: ");
        String lokasiAsal = scanner.nextLine();
        
        System.out.print("Lokasi Tujuan: ");
        String lokasiTujuan = scanner.nextLine();
        
        try {
            List<Jadwal> jadwalList = jadwalController.cariJadwal(lokasiAsal, lokasiTujuan);
            
            if (jadwalList.isEmpty()) {
                System.out.println("Tidak ada jadwal ditemukan.");
            } else {
                System.out.println("\n=== HASIL PENCARIAN ===");
                for (int i = 0; i < jadwalList.size(); i++) {
                    Jadwal jadwal = jadwalList.get(i);
                    System.out.println((i + 1) + ". " + jadwal.getInfo());
                    System.out.println("   ID: " + jadwal.getIdJadwal());
                    System.out.println("   Kursi Tersedia: " + jadwal.getKursiTersedia().size());
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuLihatSemuaJadwal() {
        System.out.println("\n=== SEMUA JADWAL ===");
        
        try {
            List<Jadwal> jadwalList = jadwalController.getAllJadwal();
            
            if (jadwalList.isEmpty()) {
                System.out.println("Belum ada jadwal.");
            } else {
                for (int i = 0; i < jadwalList.size(); i++) {
                    Jadwal jadwal = jadwalList.get(i);
                    System.out.println((i + 1) + ". " + jadwal.getInfo());
                    System.out.println("   ID: " + jadwal.getIdJadwal());
                }
            }
        } catch (DatabaseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuPesanTiket() {
        System.out.println("\n=== PESAN TIKET ===");
        
        try {
            System.out.print("ID Jadwal: ");
            String idJadwal = scanner.nextLine();
            
            Jadwal jadwal = jadwalController.getJadwalById(idJadwal);
            
            System.out.println("\nJadwal: " + jadwal.getInfo());
            
            System.out.print("Nomor Kursi (1-40): ");
            int nomorKursi = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println("\n=== DATA PELANGGAN ===");
            String idPelanggan = "PLG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            
            System.out.print("Nama: ");
            String nama = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Telepon: ");
            String telepon = scanner.nextLine();
            
            Pelanggan pelanggan = new Pelanggan(idPelanggan, nama, email, telepon);
            
            // Proses pemesanan
            Tiket tiket = pemesananController.pesanTiket(jadwal, nomorKursi, pelanggan);
            
            System.out.println("\n✓ Tiket berhasil dipesan!");
            System.out.println("ID Tiket: " + tiket.getIdTiket());
            
            // Proses pembayaran
            System.out.println("\n=== PEMBAYARAN ===");
            System.out.println("1. Transfer Bank");
            System.out.println("2. E-Wallet");
            System.out.println("3. Cash");
            System.out.print("Pilih metode pembayaran: ");
            int metodePilihan = scanner.nextInt();
            scanner.nextLine();
            
            String metode = "";
            switch (metodePilihan) {
                case 1: metode = "Transfer Bank"; break;
                case 2: metode = "E-Wallet"; break;
                case 3: metode = "Cash"; break;
                default: metode = "Cash";
            }
            
            Transaksi transaksi = pemesananController.buatTransaksi(tiket, metode);
            
            System.out.print("\nKonfirmasi pembayaran Rp " + transaksi.getJumlah() + "? (Y/N): ");
            String konfirmasi = scanner.nextLine();
            
            if (konfirmasi.equalsIgnoreCase("Y")) {
                pemesananController.bayarTransaksi(transaksi.getIdTransaksi());
                System.out.println("✓ Pembayaran berhasil!");
                
                // Cetak struk
                String struk = pemesananController.cetakStruk(tiket.getIdTiket());
                System.out.println(struk);
            } else {
                System.out.println("Pembayaran dibatalkan.");
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}