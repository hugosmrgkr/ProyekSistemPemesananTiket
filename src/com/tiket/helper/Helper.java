/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tiket.helper;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class Helper {
    
    // ==================== VALIDATION ====================
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10,13}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && !phone.isEmpty() && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isValidNomorKursi(int nomorKursi, int maxKursi) {
        return nomorKursi > 0 && nomorKursi <= maxKursi;
    }
    
    public static boolean isValidHarga(double harga) {
        return harga > 0;
    }
    
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    public static String sanitizeInput(String input) {
        return input == null ? "" : input.trim();
    }
    
    // ==================== DATETIME ====================
    
    private static final DateTimeFormatter DISPLAY_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private static final DateTimeFormatter INPUT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm");
    
    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DISPLAY_FORMATTER);
    }
    
    public static String formatDateOnly(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DATE_ONLY_FORMATTER);
    }
    
    public static String formatTimeOnly(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(TIME_ONLY_FORMATTER);
    }
    
    public static LocalDateTime parseFromInput(String input) throws DateTimeParseException {
        if (input == null || input.trim().isEmpty()) {
            throw new DateTimeParseException("Input tidak boleh kosong", input, 0);
        }
        return LocalDateTime.parse(input.trim(), INPUT_FORMATTER);
    }
    
    public static boolean isValidDateTime(String input) {
        try {
            parseFromInput(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }
    
    public static boolean isBeforeDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1 != null && dateTime2 != null && dateTime1.isBefore(dateTime2);
    }
    
    public static long getDurationInMinutes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return 0;
        return java.time.Duration.between(start, end).toMinutes();
    }
    
    public static String getDurationText(LocalDateTime start, LocalDateTime end) {
        long minutes = getDurationInMinutes(start, end);
        long hours = minutes / 60;
        long mins = minutes % 60;
        return String.format("%d jam %d menit", hours, mins);
    }
    
    // ==================== CURRENCY ====================
    
    private static final NumberFormat CURRENCY_FORMAT = 
        NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    
    private static final DecimalFormat DECIMAL_FORMAT = 
        new DecimalFormat("#,###.00");
    
    public static String formatRupiah(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }
    
    public static String formatRupiahSimple(double amount) {
        return "Rp " + DECIMAL_FORMAT.format(amount);
    }
    
    public static String formatRupiahWithoutDecimal(double amount) {
        DecimalFormat format = new DecimalFormat("#,###");
        return "Rp " + format.format(amount);
    }
    
    public static double parseRupiah(String rupiahString) {
        if (rupiahString == null || rupiahString.isEmpty()) return 0;
        
        String cleaned = rupiahString.replace("Rp", "")
                                    .replace(".", "")
                                    .replace(",", ".")
                                    .trim();
        
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount < 100000000;
    }
    
    // ==================== ID GENERATOR ====================
    
    public static String generateJadwalId() {
        return "JDW-" + generateRandomId();
    }
    
    public static String generateTiketId() {
        return "TKT-" + generateRandomId();
    }
    
    public static String generateTransaksiId() {
        return "TRX-" + generateRandomId();
    }
    
    public static String generatePelangganId() {
        return "PLG-" + generateRandomId();
    }
    
    public static String generateKursiId(String jadwalId, int nomorKursi) {
        return "KRS-" + jadwalId + "-" + String.format("%02d", nomorKursi);
    }
    
    public static String generateStrukId() {
        return "STR-" + generateRandomId();
    }
    
    public static String generateBusId() {
        return "BUS-" + generateRandomId();
    }
    
    public static String generateAdminId() {
        return "ADM-" + generateRandomId();
    }
    
    private static String generateRandomId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public static String generateTimestampedId(String prefix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        return prefix + "-" + timestamp;
    }
    
    // ==================== PRINT ====================
    
    private static final int CONSOLE_WIDTH = 60;
    
    public static void printHeader(String title) {
        printLine();
        printCentered(title);
        printLine();
    }
    
    public static void printLine() {
        System.out.println("=".repeat(CONSOLE_WIDTH));
    }
    
    public static void printDoubleLine() {
        System.out.println("═".repeat(CONSOLE_WIDTH));
    }
    
    public static void printCentered(String text) {
        int padding = (CONSOLE_WIDTH - text.length()) / 2;
        System.out.println(" ".repeat(Math.max(0, padding)) + text);
    }
    
    public static void printBox(String title) {
        System.out.println("╔" + "═".repeat(CONSOLE_WIDTH - 2) + "╗");
        printCenteredInBox(title);
        System.out.println("╚" + "═".repeat(CONSOLE_WIDTH - 2) + "╝");
    }
    
    public static void printCenteredInBox(String text) {
        int padding = (CONSOLE_WIDTH - 2 - text.length()) / 2;
        System.out.println("║" + " ".repeat(Math.max(0, padding)) + text + 
                          " ".repeat(Math.max(0, CONSOLE_WIDTH - 2 - padding - text.length())) + "║");
    }
    
    public static void printKeyValue(String key, String value) {
        System.out.printf("%-20s: %s%n", key, value);
    }
    
    public static void printKeyValue(String key, double value) {
        System.out.printf("%-20s: %s%n", key, formatRupiah(value));
    }
    
    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }
    
    public static void printError(String message) {
        System.out.println("✗ " + message);
    }
    
    public static void printWarning(String message) {
        System.out.println("⚠ " + message);
    }
    
    public static void printInfo(String message) {
        System.out.println("ℹ " + message);
    }
    
    public static void printSeparator() {
        System.out.println("-".repeat(CONSOLE_WIDTH));
    }
    
    public static void printEmptyLine() {
        System.out.println();
    }
    
    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    
    // ==================== PASSWORD ====================
    
    private static final String ALGORITHM = "SHA-256";
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public static boolean verifyPassword(String password, String hashedPassword) {
        String newHash = hashPassword(password);
        return newHash.equals(hashedPassword);
    }
    
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) return false;
        
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    // ==================== FILE ====================
    
    private static final String EXPORT_DIR = "exports/";
    
    public static void createDirectoryIfNotExists(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public static boolean exportToFile(String filename, String content) {
        createDirectoryIfNotExists(EXPORT_DIR);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EXPORT_DIR + filename))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            printError("Gagal export file: " + e.getMessage());
            return false;
        }
    }
    
    public static String readFromFile(String filename) {
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(EXPORT_DIR + filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            printError("Gagal membaca file: " + e.getMessage());
            return null;
        }
        
        return content.toString();
    }
    
    public static String generateFilename(String prefix, String extension) {
        String timestamp = formatForDisplay(LocalDateTime.now())
                          .replace("/", "")
                          .replace(":", "")
                          .replace(" ", "_");
        return prefix + "_" + timestamp + "." + extension;
    }
    
    public static boolean deleteFile(String filename) {
        File file = new File(EXPORT_DIR + filename);
        return file.delete();
    }
    
    public static boolean fileExists(String filename) {
        File file = new File(EXPORT_DIR + filename);
        return file.exists();
    }
    
    // ==================== CONFIG ====================
    
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties = new Properties();
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            createDefaultConfig();
        }
    }
    
    private static void createDefaultConfig() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/tiket_bus_db");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "");
        properties.setProperty("app.name", "Sistem Pemesanan Tiket Bus");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("bus.max.kursi", "40");
        properties.setProperty("export.directory", "exports/");
        
        saveConfig();
    }
    
    private static void saveConfig() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Tiket Bus Configuration");
        } catch (IOException e) {
            printError("Gagal menyimpan konfigurasi: " + e.getMessage());
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getPropertyAsInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveConfig();
    }
    
    public static String getDbUrl() {
        return getProperty("db.url");
    }
    
    public static String getDbUsername() {
        return getProperty("db.username");
    }
    
    public static String getDbPassword() {
        return getProperty("db.password");
    }
    
    public static int getMaxKursi() {
        return getPropertyAsInt("bus.max.kursi", 40);
    }
    
    public static String getAppName() {
        return getProperty("app.name", "Sistem Pemesanan Tiket");
    }
    
    public static String getAppVersion() {
        return getProperty("app.version", "1.0.0");
    }
}