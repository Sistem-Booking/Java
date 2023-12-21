import java.util.Scanner;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class main {
    private static User[] users = new User[100]; // Array untuk menyimpan pengguna
    private static int userCount = 0;
    private static Map<String, User> userMap = new HashMap<>();
    private Map<String, Boolean> bookedSlots = new HashMap<>();
    private static boolean isLoggedIn = false;
    private static User loggedInUser = null;
    private String statusKonfirmasi;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private List<String> tanggapanList;

    // Metode untuk menambahkan tanggapan ke array
    public void addTanggapan(String tanggapan) {
        this.tanggapanList.add(tanggapan);
    }

    // Metode untuk mendapatkan daftar tanggapan
    public List<String> getTanggapanList() {
        return this.tanggapanList;
    }

    public void setStatusKonfirmasi(String status) {
        this.statusKonfirmasi = status;
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        clearScreen();
        while (true) {
            if (!isLoggedIn) {
                System.out.println(
                        " _    _ _____ _     _____ ________  ___ _____                              ");
                System.out.println(
                        "| |  | |  ___| |   /  __ \\  _  |  \\/  ||  ___|                             ");
                System.out.println(
                        "| |  | | |__ | |   | /  \\/ | | | .  . || |__                               ");
                System.out.println(
                        "| |/\\| |  __|| |   | |   | | | | |\\/| ||  __|                              ");
                System.out.println(
                        "\\  /\\  / |___| |___| \\__/\\ \\_/ / |  | || |___                              ");
                System.out.println(
                        " \\/  \\/\\____/\\_____/\\____/\\___/\\_|  |_/\\____/                              ");
                System.out.println("");
                System.out.println("1. Login");
                System.out.println("2. Buat Akun");
                System.out.println("3. Ganti Password");
                System.out.println("4. Keluar");
                System.out.print("Masukkan Pilihanmu : ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        login(scanner);
                        break;
                    case 2:
                        createAccount(scanner);
                        break;
                    case 3:
                        changePasswordByUsername(scanner);
                        break;
                    case 4:
                        System.out.println("Terima kasih, selamat tinggal!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                        break;
                }
            } else {
                if (loggedInUser.getUsername().equals(ADMIN_USERNAME)) {
                    adminMenu(scanner);
                } else {
                    userMenu(scanner);
                }
            }
        }
    }

    public static void changePasswordByUsername(Scanner scanner) {
        clearScreen();
        boolean passwordChanged = false;
        while (!passwordChanged) {
            System.out.println("-----------------------------------------------------");
            System.out.println("|                                                   |");
            System.out.println("|               Halaman Ganti Password              |");
            System.out.println("|                                                   |");
            System.out.println("-----------------------------------------------------");
            System.out.print("Masukkan username yang akan mengganti kata sandi: ");
            String targetUsername = scanner.nextLine();
            User targetUser = userMap.get(targetUsername);
            if (targetUser != null) {
                System.out.println("");
                System.out.print("Masukkan kata sandi baru: ");
                String newPassword = scanner.nextLine();
                targetUser.changePassword(newPassword);
                clearScreen();
                System.out.println("");
                System.out.println("-------------------------------------------------------");
                System.out.println("|                                                     |");
                System.out.println("|               Kata sandi Berhasil Dirubah           |");
                System.out.println("|                                                     |");
                System.out.println("-------------------------------------------------------");
                passwordChanged = true; // Setel variabel ini ke true setelah berhasil mengubah kata sandi.
            } else {
                System.out.println("----------------------------------------------------------");
                System.out.println("");
                System.out.println("               Pengguna tidak dapat ditemukan.");
                System.out.println("");
                System.out.println("----------------------------------------------------------");
                System.out.println("");
            }
        }
    }

    private static void adminMenu(Scanner scanner) {
        clearScreen();
        while (true) {
            System.out.println("1. Lihat Informasi User Terdaftar");
            System.out.println("2. Cari User");
            System.out.println("3. Lihat Informasi Check-in User");
            System.out.println("4. Konfirmasi Booking");
            System.out.println("5. Cetak Laporan");
            System.out.println("6. Laporan");
            System.out.println("7. Tambahkan Kode Voucher");
            System.out.println("8. Keluar Sebagai Admin");
            System.out.print("Masukkan Pilihan Anda: ");
            int adminChoice = scanner.nextInt();
            scanner.nextLine();

            switch (adminChoice) {
                case 1:
                    sortUsers();
                    break;
                case 2:
                    searchUser(scanner);
                    break;
                case 3:
                    viewPemesananInfo();
                    break;
                case 4:
                    for (User user : userMap.values()) {
                        String[][] bookingHistory = user.getBookingHistory();
                        confirmBooking(scanner, bookingHistory);
                    }
                    break;
                case 5:
                    cetakLaporan(scanner);
                    break;
                case 6:
                    tanggapiLaporan(scanner);
                    break;
                case 7:
                    voucher(scanner);
                    break;
                case 8:
                    System.out.println("-----------------------------------------------------");
                    System.out.println("|                                                   |");
                    System.out.println("|               Anda Keluar Sebagai Admin           |");
                    System.out.println("|                                                   |");
                    System.out.println("-----------------------------------------------------");
                    return;
                default:
                    System.out.println("---------------------------------------------------------");
                    System.out.println("|                                                       |");
                    System.out.println("|               Pilihan Anda Tidak Diketahui            |");
                    System.out.println("|                                                       |");
                    System.out.println("---------------------------------------------------------");
                    break;
            }
        }
    }

    private static void tanggapiLaporan(Scanner scanner) {
        String statusLaporan = "Ada Laporan"; // Declare statusLaporan
        if ("Ada Laporan".equals(statusLaporan)) {
            clearScreen();
            System.out.println("-------------------------------------------------------------");
            System.out.println("|                                                           |");
            System.out.println("|                   Halaman Menanggapi Laporan              |");
            System.out.println("|                                                           |");
            System.out.println("-------------------------------------------------------------");
            for (User user : userMap.values()) {
                String username = user.getUsername();
                String userStatusLaporan = user.getStatusLaporan(); // Use user.getStatusLaporan() here
                String laporan = "Belum ada laporan";
                String[] bookingInfo = user.getBookingInfo();
                if (userStatusLaporan.equals("Ada Laporan")) {
                    laporan = bookingInfo[9];
                }
                System.out.println("--------------------------");
                System.out.println("");
                System.out.println("Username    : " + username);
                System.out.println("Laporan     : " + laporan);
                System.out.println("");
                System.out.println("--------------------------");
            }
            System.out.print("Masukkan username pengguna yang ingin dikonfirmasi : ");
            String usernameToConfirm = scanner.nextLine();
    
            User userToConfirm = userMap.get(usernameToConfirm);
            if (userToConfirm != null && userToConfirm.getStatusLaporan().equals("Ada Laporan")) {
                System.out.println("Masukkan tanggapan anda : ");
                String tanggapan = scanner.nextLine();
                User.setTanggapan(tanggapan);
                userToConfirm.setStatusLaporan("Sudah Ditanggapi");
                System.out.println("Laporan sudah dikirim");
            } else {
                System.out.println("Pengguna tidak ditemukan atau tidak memenuhi syarat untuk dikonfirmasi.");
            }
        }
    }
    

    private static void cetakLaporan(Scanner scanner) {
        clearScreen();
        boolean ulang = true;
        while (ulang) {
            System.out.println("");
            System.out.println("---------------------------------------------------------");
            System.out.println("|                                                       |");
            System.out.println("|                   Halaman Cetak Laporan               |");
            System.out.println("|                                                       |");
            System.out.println("---------------------------------------------------------");
            System.out.println("");
            System.out.println("Pilih jenis laporan:");
            System.out.println("1. Cetak laporan semua");
            System.out.println("2. Cetak laporan berdasarkan pembayaran DP");
            System.out.println("3. Cetak laporan pendapatan");
            System.out.println("4. Keluar");
            System.out.print("Masukkan Pilihan Anda : ");
            int pilihan = scanner.nextInt();

            switch (pilihan) {
                case 1:
                    for (User user : userMap.values()) {
                        String statusAkhir = user.getStatusAkhir();
                        String choicePembayaran = user.getChoicePembayaran();
                        String statusPembayaran = user.getStatusPembayaran();
                        String statusPemesanan = user.getStatusPemesanan();
                        cetakLaporanSemua(scanner, statusPembayaran, statusAkhir,choicePembayaran, statusPemesanan);
                    }
                    break;
                case 2:
                    for (User user : userMap.values()) {
                        String statusPembayaran = user.getStatusPembayaran();
                        String choicePembayaran = user.getChoicePembayaran();
                        String statusAkhir = user.getStatusAkhir();
                        cetakLaporanPembayaranLunas(scanner, statusPembayaran, choicePembayaran, statusAkhir);
                    }
                    break;
                case 3:
                    for (User user : userMap.values()) {
                        String[][] bookingHistory = user.getBookingHistory();
                        String statusAkhir = user.getStatusAkhir();
                        cetakLaporanPendapatan(scanner, bookingHistory, statusAkhir);
                    }
                    break;
                case 4:
                    ulang = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan pilih kembali.");
                    break;
            }
        }
    }

    private static String formatToRupiah(double harga) {
        Locale localeID = Locale.forLanguageTag("id-ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(harga);
    }

    private static void cetakLaporanPendapatan(Scanner scanner, String[][] bookingHistory, String statusAkhir) {
        clearScreen();
        if ("Berakhir".equals(statusAkhir)) {
            System.out.println("");
            System.out.println("---------------------------------------------------------");
            System.out.println("|                                                       |");
            System.out.println("|                       Detail Laporan                   |");
            System.out.println("|                                                       |");
            System.out.println("---------------------------------------------------------");
            System.out.println("");
            double totalHarga = 0;
            for (User user : userMap.values()) {
                for (int i = 0; i < bookingHistory.length; i++) {
                    if (bookingHistory[i][0] != null) {
                        String username = user.getUsername(); // Ambil username dari bookingHistory
                        String jenisGedung = bookingHistory[i][0]; // Ambil jenis gedung dari bookingHistory
                        double hargaGedung = Double.parseDouble(bookingHistory[i][4]);
                        String formattedHargaGedung = formatToRupiah(hargaGedung);
                        totalHarga += hargaGedung;
                        System.out.println("------+---------+-------+");
                        System.out.println("");
                        System.out.println(" Username       : " + username);
                        System.out.println(" Jenis Gedung   : " + jenisGedung); // Menambahkan jenis gedung
                        System.out.println(" Harga          : " + formattedHargaGedung);
                        System.out.println("");
                        System.out.println("------+---------+-------+");
                        System.out.println("");
                    }
                }
            }
            String formattedTotalHarga = formatToRupiah(totalHarga);
            System.out.println("");
            System.out.println("------+---------+-------+--------+------+");
            System.out.println("");
            System.out.println("Total Pendapatan : " + formattedTotalHarga);
            System.out.println("");
            System.out.println("------+---------+-------+--------+------+");
            System.out.println("");
            System.out.print("0. Kembali : ");
            int kembali = scanner.nextInt();

            switch (kembali) {
                case 0:
                    cetakLaporan(scanner);
            }
        } else {
            clearScreen();
            System.out.println("-------------------------------------------------------------");
            System.out.println("|                                                           |");
            System.out.println("|               Tidak Ada Laporan Yang Dicetak              |");
            System.out.println("|                                                           |");
            System.out.println("-------------------------------------------------------------");
        }
    }

    private static void cetakLaporanSemua(Scanner scanner, String statusPembayaran, String statusAkhir, String choicePembayaran, String statusPemesanan) {
        if ("Berakhir".equals(statusAkhir)) {
            clearScreen();
            System.out.println("-------------------------------------------------------------");
            System.out.println("|                                                           |");
            System.out.println("|                   Laporan Berhasil Dicetak !              |");
            System.out.println("|                                                           |");
            System.out.println("-------------------------------------------------------------");

            int nomorPemesanan = 1;
            for (User user : userMap.values()) {
                String username = user.getUsername();
                String tanggalBooking = "Belum memesan";
                String jenisGedung = "Belum memesan";
                String fasilitas = "Belum memesan";
                String opsiPembayaran = "Belum memesan";
                String rating = "Belum memesan";
                String ulasan = "Belum memesan";
                String [][] bookingHistory = user.getBookingHistory();
                for (int i = 0; i < bookingHistory.length; i++) {
                    if (bookingHistory[i][0] != null){
                        if (user.getStatusPemesanan().equals("Dipesan")) {
                            tanggalBooking = bookingHistory[i][2];
                            jenisGedung = bookingHistory[i][0];
                            fasilitas = bookingHistory[i][6];
                            opsiPembayaran = bookingHistory[i][1];
                            rating = bookingHistory[i][7];
                            ulasan = bookingHistory[i][8];
                        }
                    }
                }
                System.out.println("");
                System.out.println(" ." + nomorPemesanan);
                System.out.println("\t\tusername        : " + username);
                System.out.println("\t\tTanggal Booking : " + tanggalBooking);
                System.out.println("\t\tJenis Gedung    : " + jenisGedung);
                System.out.println("\t\tFasilitas       : " + fasilitas);
                System.out.println("\t\tOpsi Pembayaran : " + opsiPembayaran);
                System.out.println("\t\tRating          : " + rating);
                System.out.println("\t\tUlasan          : " + ulasan);
                System.out.println("");
                System.out.println("-------------------------------------------------------------");

                nomorPemesanan += 1;
            }
            System.out.println("");
            System.out.print("0. Kembali : ");
            int kembali = scanner.nextInt();

            switch (kembali) {
                case 0:
                    cetakLaporan(scanner);
            }

        } else {
            clearScreen();
            System.out.println("-------------------------------------------------------------");
            System.out.println("|                                                           |");
            System.out.println("|               Tidak Ada Laporan Yang Dicetak              |");
            System.out.println("|                                                           |");
            System.out.println("-------------------------------------------------------------");
        }
    }

    private static void cetakLaporanPembayaranLunas(Scanner scanner, String statusPembayaran, String choicePembayaran, String statusAkhir) {
        if ("Berakhir".equals(statusAkhir)) {
            if ("Lunas".equals(choicePembayaran)) {
                clearScreen();
                System.out.println("-------------------------------------------------------------");
                System.out.println("|                                                           |");
                System.out.println("|                   Laporan Berhasil Dicetak !              |");
                System.out.println("|                                                           |");
                System.out.println("-------------------------------------------------------------");

                int nomorPemesanan = 1;
                for (User user : userMap.values()) {
                    String username = user.getUsername();
                    String tanggalBooking = "Belum memesan";
                    String jenisGedung = "Belum memesan";
                    String fasilitas = "Belum memesan";
                    String opsiPembayaran = "Belum memesan";
                    String rating = "Belum memesan";
                    String ulasan = "Belum memesan";
                    String [][] bookingHistory = user.getBookingHistory();
                    for (int i = 0; i < bookingHistory.length; i++) {
                        if (bookingHistory[i][0] != null){
                            if (user.getStatusPemesanan().equals("Dipesan")) {
                                tanggalBooking = bookingHistory[i][2];
                                jenisGedung = bookingHistory[i][0];
                                fasilitas = bookingHistory[i][6];
                                opsiPembayaran = bookingHistory[i][1];
                                rating = bookingHistory[i][7];
                                ulasan = bookingHistory[i][8];
                            }
                        }
                    }
                    System.out.println("");
                    System.out.println(" ." + nomorPemesanan);
                    System.out.println("\t\tusername        : " + username);
                    System.out.println("\t\tTanggal Booking : " + tanggalBooking);
                    System.out.println("\t\tJenis Gedung    : " + jenisGedung);
                    System.out.println("\t\tFasilitas       : " + fasilitas);
                    System.out.println("\t\tOpsi Pembayaran : " + opsiPembayaran);
                    System.out.println("\t\tRating          : " + rating);
                    System.out.println("\t\tUlasan          : " + ulasan);
                    System.out.println("");
                    System.out.println("-------------------------------------------------------------");

                    nomorPemesanan += 1;
                }
                System.out.println("");
                System.out.print("0. Kembali : ");
                int kembali = scanner.nextInt();

                switch (kembali) {
                    case 0:
                        cetakLaporan(scanner);
                        break; // Pastikan untuk menambahkan break di akhir case
                }
            } else {
                clearScreen();
                System.out.println("-------------------------------------------------------------");
                System.out.println("|                                                           |");
                System.out.println("|               Tidak Ada Laporan Yang Dicetak              |");
                System.out.println("|                                                           |");
                System.out.println("-------------------------------------------------------------");
            }
        } else {
            clearScreen();
            System.out.println("-------------------------------------------------------------");
            System.out.println("|                                                           |");
            System.out.println("|               Tidak Ada Laporan Yang Dicetak              |");
            System.out.println("|                                                           |");
            System.out.println("-------------------------------------------------------------");
        }
    }

    private static void confirmBooking(Scanner scanner, String [][] bookingHistory) {
        clearScreen();
        System.out.println("");
        System.out.println("Informasi Check-in User:");
        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+------------------+");
        System.out.println("| Username   | Status Pemesanan| Tanggal Booking     | Jenis Gedung    | Fasilitas     | Opsi Pembayaran  |");
        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+------------------+");

        for (User user : userMap.values()) {
            String username = user.getUsername();
            String statusPemesanan = user.getStatusPemesanan();
            String tanggalBooking = "Belum memesan";
            String jenisGedung = "Belum memesan";
            String fasilitas = "Belum memesan";
            String opsiPembayaran = "Belum memesan";
            String[] bookingInfo = user.getBookingInfo();
            if (user.getStatusPemesanan().equals("Dipesan")) {
                tanggalBooking = bookingInfo[2];
                jenisGedung = bookingInfo[0];
                fasilitas = bookingInfo[5];
                opsiPembayaran = bookingInfo[1];
            }

            String row = String.format("| %-10s | %-15s | %-19s | %-15s | %-13s | %-15s |", username, statusPemesanan, tanggalBooking, jenisGedung, fasilitas, opsiPembayaran);
            System.out.println(row);
        }

        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+------------------+");
        System.out.println("");
        System.out.print("Masukkan username pengguna yang ingin dikonfirmasi: ");
        String usernameToConfirm = scanner.nextLine();

        User userToConfirm = userMap.get(usernameToConfirm);
        if (userToConfirm != null && userToConfirm.getStatusPemesanan().equals("Dipesan")) {
            userToConfirm.setStatusKonfirmasi("Konfirmasi");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("|                                                                    |");
            System.out.println("|                       Booking untuk " + usernameToConfirm + " telah dikonfirmasi.    |");
            System.out.println("|                                                                    |");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("");
        } else {
            System.out.println("Pengguna tidak ditemukan atau tidak memenuhi syarat untuk dikonfirmasi.");
        }
    }

    private static void viewPemesananInfo() {
        System.out.println("Informasi Check-in User:");
        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+------------------+");
        System.out.println("| Username   | Status Pemesanan| Tanggal Booking     | Jenis Gedung    | Fasilitas     | Opsi Pembayaran  |");
        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+------------------+");

        for (User user : userMap.values()) {
            String username = user.getUsername();
            String statusPemesanan = user.getStatusPemesanan();
            String tanggalBooking = "Belum memesan";
            String jenisGedung = "Belum memesan";
            String fasilitas = "Belum memesan";
            String opsiPembayaran = "Belum memesan";

            String[] bookingInfo = user.getBookingInfo(); // Ambil informasi pemesanan
            if (user.getStatusPemesanan().equals("Dipesan")) {
                tanggalBooking = bookingInfo[2];
                jenisGedung = bookingInfo[0];
                fasilitas = bookingInfo[5];
                opsiPembayaran = bookingInfo[1];
            }

            String row = String.format("| %-10s | %-15s | %-19s | %-15s | %-13s | %-15s |", username, statusPemesanan, tanggalBooking, jenisGedung, fasilitas, opsiPembayaran);
            System.out.println(row);
        }

        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+------------------+");
    }

    private static void sortUsers() {
        clearScreen();
        // Create a list to store valid users (excluding admin)
        List<User> validUsers = new ArrayList<>();
        for (User user : userMap.values()) {
            if (!user.getUsername().equals(ADMIN_USERNAME)) {
                validUsers.add(user);
            }
        }
        // Sort the valid users based on their NIK
        Collections.sort(validUsers, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getNik().compareTo(user2.getNik());
            }
        });
        // Display the sorted list of valid users
        System.out.println("+------------+-------------------+---------------------+------------------+");
        System.out.println("                        Daftar Pengguna Terdaftar ");
        System.out.println("+------------+-------------------+---------------------+------------------+");
        System.out.println(" No  | NIK           | Username          | Alamat          | No Telephone");
        System.out.println("+------------+-------------------+---------------------+------------------+");

        if (validUsers.isEmpty()) {
            System.out.println("                       Tidak ada pengguna terdaftar");
        } else {
            int count = 1;
            for (User user : validUsers) {
                String no = String.format("%3d", count);
                String nik = String.format("%-15s", user.getNik());
                String username = String.format("%-19s", user.getUsername());
                String alamat = String.format("%-15s", user.getAddress());
                String noTelephone = String.format("%-13s", user.getPhoneNumber());
                System.out.println(no + " | " + nik + " | " + username + " | " + alamat + " | " + noTelephone);
                count++;
            }
        }

        System.out.println("+------------+-------------------+---------------------+------------------+");
    }

    private static void createAccount(Scanner scanner) {
        clearScreen();
        System.out.println("------------------------------------------------");
        System.out.println("|                                              |");
        System.out.println("|               Halaman Buat Akun              |");
        System.out.println("|                                              |");
        System.out.println("------------------------------------------------");
        String nik = "";
        String username = "";
        String dateOfBirth = "";
        String phoneNumber = "";
        String address = "";
        String password = "";

        do {
            do {
                System.out.print("Masukkan NIK : ");
                nik = scanner.nextLine();
                if (nik.isEmpty()) {
                    System.out.println(
                            (char) 27 + "[01;31m NIK tidak boleh kosong. Silakan coba lagi." + (char) 27 + "[00;00m");
                }
            } while (nik.isEmpty());

        } while (nik.isEmpty());

        do {
            do {
                System.out.print("Masukkan Username: ");
                username = scanner.nextLine();
                if (username.isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Username tidak boleh kosong. Silakan coba lagi." + (char) 27
                            + "[00;00m");
                } else if (isUsernameTaken(username)) {
                    System.out.println((char) 27 + "[01;31m Username telah digunakan. Silakan pilih username lain."
                            + (char) 27 + "[00;00m");
                }
            } while (username.isEmpty() || isUsernameTaken(username));

        } while (username.isEmpty() || isUsernameTaken(username));

        do {
            do {
                System.out.print("Masukkan Tanggal Lahir : ");
                dateOfBirth = scanner.nextLine();

                if (dateOfBirth.isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Tanggal Lahir tidak boleh kosong. Silakan coba lagi."
                            + (char) 27 + "[00;00m");
                }
            } while (dateOfBirth.isEmpty());

        } while (dateOfBirth.isEmpty());

        do {
            do {
                System.out.print("Masukkan No Telephone : ");
                phoneNumber = scanner.nextLine();

                if (phoneNumber.isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Nomor Telephone tidak boleh kosong. Silakan coba lagi."
                            + (char) 27 + "[00;00m");
                }
            } while (phoneNumber.isEmpty());

        } while (phoneNumber.isEmpty());

        do {
            do {
                System.out.print("Masukkan Alamat : ");
                address = scanner.nextLine();

                if (address.isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Alamat tidak boleh kosong. Silakan coba lagi." + (char) 27
                            + "[00;00m");
                }
            } while (address.isEmpty());

        } while (address.isEmpty());

        do {
            do {
                System.out.print("Masukkan Kata Sandi : ");
                password = scanner.nextLine();

                if (password.isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Kata Sandi tidak boleh kosong. Silakan coba lagi."
                            + (char) 27 + "[00;00m");
                }
            } while (password.isEmpty());
        } while (password.isEmpty());

        User newUser = new User(nik, username, dateOfBirth, phoneNumber, address, password);
        userMap.put(username, newUser);

        if (userCount < users.length) {
            users[userCount] = newUser;
            userCount++;
            clearScreen();
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("|                                                                            |");
            System.out.println("|               Akun berhasil dibuat, anda sekarang sudah login              |");
            System.out.println("|                                                                            |");
            System.out.println("------------------------------------------------------------------------------");
            isLoggedIn = true;
            loggedInUser = newUser;
        } else {
            System.out.println(
                    "-----------------------------------------------------------------------------------------");
            System.out.println(
                    "|                                                                                       |");
            System.out.println(
                    "|               Batas maksimum pengguna tercapai. Tidak dapat membuat akun              |");
            System.out.println(
                    "|                                                                                       |");
            System.out.println(
                    "-----------------------------------------------------------------------------------------");
        }
    }

    private static boolean isUsernameTaken(String username) {
        return userMap.containsKey(username);
    }

    private static void login(Scanner scanner) {
        clearScreen();
        System.out.println("-------------------------------------------------");
        System.out.println("|                                               |");
        System.out.println("|               Halaman Masuk                   |");
        System.out.println("|                                               |");
        System.out.println("-------------------------------------------------");
        System.out.print("Masukkan Username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan Kata Sandi: ");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()) {
            clearScreen();
            System.out.println("---------------------------------------------");
            System.out.println("");
            System.out.println((char) 27 + "[01;31mUsername dan password harus diisi. Coba lagi." + (char) 27 + "[00;00m");
            System.out.println("");
            System.out.println("---------------------------------------------");
        } else if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            clearScreen();
            System.out.println("-----------------------------------------------------");
            System.out.println("|                                                   |");
            System.out.println("|               Admin Berhasil Login !              |");
            System.out.println("|                                                   |");
            System.out.println("-----------------------------------------------------");
            adminMenu(scanner);
        } else {
            User foundUser = userMap.get(username);
            if (foundUser != null && foundUser.getPassword().equals(password)) {
                clearScreen();
                System.out.println("----------------------------------------------");
                System.out.println("|                                            |");
                System.out.println("|            Selamat Datang " + username + " !       |");
                System.out.println("|                                            |");
                System.out.println("----------------------------------------------");
                loggedInUser = foundUser;
                isLoggedIn = true;
                if (username.equals("admin")) {
                    adminMenu(scanner);
                } else {
                    userMenu(scanner);
                }
            } else {
                clearScreen();
                System.out.println("-------------------------------------------------------");
                System.out.println("");
                System.out.println((char) 27 + "[01;31mLogin gagal. Username atau password salah. Coba lagi."+ (char) 27 + "[00;00m");
                System.out.println("");
                System.out.println("-------------------------------------------------------");
            }
        }
    }

    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("");
            System.out.println("1. Pemesanan Gedung");
            System.out.println("2. Check In");
            System.out.println("3. Akhiri Pemesanan");
            System.out.println("4. Informasi User");
            System.out.println("5. Pemesanan Saat Ini");
            System.out.println("6. History Pemesanan");
            System.out.println("7. Lapor");
            System.out.println("8. Keluar");
            System.out.print("Masukkan Pilihanmu : ");
            int userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 1:
                    loggedInUser.pemesananGedung(scanner);
                    break;
                case 2:
                    loggedInUser.checkIn(scanner);
                    break;
                case 3:
                    loggedInUser.akhiriPemesanan(scanner);
                    break;
                case 4:
                    loggedInUser.informasiUser();
                    break;
                case 5:
                    loggedInUser.viewBooking();
                    break;
                case 6:
                    loggedInUser.viewBookingHistory();
                    break;
                case 7:
                    loggedInUser.lapor();
                    break;
                case 8:
                    System.out.println("-------------------------------------------------------");
                    System.out.println("|                                                     |");
                    System.out.println("|                 Anda sudah Logout                   |");
                    System.out.println("|                                                     |");
                    System.out.println("-------------------------------------------------------");
                    isLoggedIn = false;
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("---------------------------------------------------------");
                    System.out.println("|                                                       |");
                    System.out.println("|               Pilihan Anda Tidak Diketahui            |");
                    System.out.println("|                                                       |");
                    System.out.println("---------------------------------------------------------");
                    break;
            }
        }
    }

    private static void searchUser(Scanner scanner) {
        clearScreen();
        System.out.println("---------------------------------------------------------");
        System.out.println("|                                                       |");
        System.out.println("|                  Search User Dashboard                |");
        System.out.println("|                                                       |");
        System.out.println("---------------------------------------------------------");
        System.out.println("");
        System.out.print("Masukkan Username yang ingin Anda cari: ");
        System.out.println("");
        String usernameToSearch = scanner.nextLine();
        User foundUser = userMap.get(usernameToSearch);

        if (foundUser != null) {
            clearScreen();
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("                                    Informasi User yang Anda Cari");
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println(" Nik           | Username          | Tanggal Lahir  | No Telephone | Alamat ");
            System.out.println("------------------------------------------------------------------------------------------------------");

            // Format output menjadi tabel
            String nik = String.format("%-15s", foundUser.getNik());
            String username = String.format("%-19s", foundUser.getUsername());
            String tanggalLahir = String.format("%-16s", foundUser.getDateOfBirth());
            String noTelephone = String.format("%-13s", foundUser.getPhoneNumber());
            String alamat = String.format("%-12s", foundUser.getAddress());

            System.out.println(nik + " | " + username + " | " + tanggalLahir + " | " + noTelephone + " | " + alamat);
            System.out.println("------------------------------------------------------------------------------------------------------");
        } else {
            clearScreen();
            System.out.println("----------------------------------------------------------------------------");
            System.out.println("|                                                                          |");
            System.out.println("|               Username yang anda cari tidak dapat ditemukan              |");
            System.out.println("|                                                                          |");
            System.out.println("----------------------------------------------------------------------------");
        }
    }
}

class User {
    private String nik;
    private String username;
    private String dateOfBirth;
    private String phoneNumber;
    private String address;
    private String password;
    private String[] bookingInfo = new String[100]; // Array untuk menyimpan info pemesanan
    private String[][] bookingHistory;
    private String statusPemesanan = "Belum pesan"; // Status awal
    private String statusKonfirmasi = "Belum konfirmasi";
    private String statusLaporan = "";
    private String statusTanggal = "";
    private String statusAkhir = "";
    private String statusBooking = "";
    private String statusBook = "";
    private static String tanggapan;
    private String choicePembayaran = "";
    private int bookingNumber = 0; // Berikut adalah Nomor pemesanan
    private double price;
    private String statusPembayaran = "Belum Lunas";
    private static final double HARGA_PERNIKAHAN = 1500000.0;
    private static final double HARGA_OLAH_RAGA = 1500000.0;
    private static final double HARGA_RAPAT = 1500000.0;
    private static final double HARGA_PHOTOBOOTH = 100000.0;
    private static final double HARGA_BAND_LIVE = 100000.0;
    private static final double HARGA_API_BUNGA = 100000.0;
    private static final double HARGA_KATERING = 100000.0;
    private static final double HARGA_PAPAN_SKOR = 100000.0;
    private static final double HARGA_LAYANAN_FOTOGRAFI = 100000.0;
    private static final double HARGA_PROYEKTOR = 100000.0;
    private static final double HARGA_KATERING_RAPAT = 100000.0;
    private static final double HARGA_KONFERENSI_TELEPHONE = 100000.0;
    private boolean isCheckedIn;
    private String tanggalBooking;
    private String waktuBooking;
    private String tempatBooking;
    private String fasilitasBooking;
    private String jenisGedung;
    private double totalPembayaran;
    private Map<String, Boolean> bookedSlots = new HashMap<>();
    private boolean slotTersedia;
    private boolean kembali = false;
    private User loggedInUser;
    private String bookedDate = "";
    private String bookedSession = "";
    private Date parsedTanggal;
    private String[] sesi = { "09:00 - 15:00", "16:00 - 22-00" };
    ArrayList<String> bookedDates = new ArrayList<>();
    ArrayList<String> bookedSessions = new ArrayList<>();
    private static Map<String, User> userMap = new HashMap<>();
    User userToCheckIn = userMap.get(username);
    HashMap<String, String[]> sessionStatus = new HashMap<>();
    List<String> existingUsernames = new ArrayList<>();

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public User(String nik, String username, String dateOfBirth, String phoneNumber, String address, String password) {
        this.nik = nik;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        this.bookingHistory = new String[10][15];
        this.isCheckedIn = false;
    }

    public static void setTanggapan(String newTanggapan) {
        tanggapan = newTanggapan;
    }

    public static String getTanggapan() {
        return tanggapan;
    }

    public void lihatTanggal(Scanner scanner) {
    }

    public User(String[][] bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public String[][] getBookingHistory() {
        return bookingHistory;
    }

    public boolean isTanggalBooked(String tanggal) {
        return false;
    }

    public void setStatusPemesanan(String string) {
    }

    public void setStatusLaporan(String status) {
        this.statusLaporan = status;
    }

    public void setStatusKonfirmasi(String status) {
        this.statusKonfirmasi = status;
    }

    public void setStatusAkhir(String status) {
        this.statusAkhir = status;
    }

    public void setChoicePembayaran(String status) {
        this.choicePembayaran = status;
    }

    public void setStatusTanggal(String status) {
        this.statusTanggal = status;
    }

    public void setStatusBooking(String string) {

    }

    public void setStatusBook(String string) {

    }

    public double getPrice() {
        return price;
    }

    public int getBookingNumber() {
        return bookingNumber;
    }

    public String getTempatBooking() {
        return tempatBooking;
    }

    public String getFasilitasBooking() {
        return fasilitasBooking;
    }

    public double getTotalPembayaran() {
        return totalPembayaran;
    }

    public String getPassword() {
        return password;
    }

    public String getNik() {
        return nik;
    }

    public String getUsername() {
        return username;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getStatusPemesanan() {
        return statusPemesanan;
    }

    public String getStatusKonfirmasi() {
        return statusKonfirmasi;
    }

    public String getStatusAkhir() {
        return statusAkhir;
    }

    public String getStatusBooking() {
        return statusBooking;
    }

    public String getStatusBook() {
        return statusBook;
    }

    public String getTanggalBooking() {
        return tanggalBooking;
    }

    public String getWaktuBooking() {
        return waktuBooking;
    }

    public String[] getBookingInfo() {
        return bookingInfo;
    }

    public String jenisGedung() {
        return jenisGedung;
    }

    public String getJenisGedung() {
        return jenisGedung();
    }

    public String getStatusPembayaran() {
        return statusPembayaran;
    }

    public String getChoicePembayaran() {
        return choicePembayaran;
    }

    public String getStatusTanggal() {
        return statusTanggal;
    }

    public String getStatusLaporan(){
        return statusLaporan;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    private static String formatToRupiah(double harga) {
        Locale localeID = Locale.forLanguageTag("id-ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(harga);
    }

    public boolean isCheckedIn() {
        return isCheckedIn;
    }

    public void parseTanggal(String tanggalString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("DD-MM-YYYY");
        this.parsedTanggal = dateFormat.parse(tanggalString);
    }

    public Date getParsedTanggal() {
        return parsedTanggal;
    }

    public void pemesananGedung(Scanner scanner) {
        clearScreen();
        System.out.println("------------------------------------------------");
        System.out.println("|                                              |");
        System.out.println("|               Halaman Pemesanan              |");
        System.out.println("|                                              |");
        System.out.println("------------------------------------------------");
        String tanggalBookingString = "";

        int sesiPemesanan = 0;

        // Inisialisasi bookingInfo
        this.bookingInfo = new String[15];
        // Input tanggal
        System.out.print("Masukkan Tanggal (Format: DD-MM-YYYY): ");
        tanggalBookingString = scanner.nextLine();

        if (!tanggalBookingString.matches("\\d{2}-\\d{2}-\\d{4}")) {
            System.out.println( "\u001B[31mMasukkan input tanggal dengan format yang benar (contoh: 01-01-2023).\u001B[0m");
            return;
        }
        System.out.println("Pilih Sesi Pemesanan:");
        System.out.println("1. Sesi 1 ( Jam 09:00 - 15:00 )");
        System.out.println("2. Sesi 2 ( Jam 16:00 - 22:00 )");
        System.out.print("Masukkan Pilihan Sesi: ");
        sesiPemesanan = scanner.nextInt();
        scanner.nextLine();
        if (sessionStatus.containsKey(tanggalBookingString)) {
            String[] statusSesi = sessionStatus.get(tanggalBookingString);
            if (statusSesi[sesiPemesanan - 1] != null && !statusSesi[sesiPemesanan - 1].isEmpty()) {
                clearScreen();
                System.out.println(
                        "------------------------------------------------------------------------------------------------------");
                System.out.println(
                        "|                                                                                                    |");
                System.out.println(
                        "|                 Tanggal dan sesi sudah dibooking silahkan pilih tanggal dan sesi lagi              |");
                System.out.println(
                        "|                                                                                                    |");
                System.out.println(
                        "------------------------------------------------------------------------------------------------------");
                return;
            }
        } else {
            String[] newStatusSesi = new String[2];
            sessionStatus.put(tanggalBookingString, newStatusSesi);
        }

        String[] statusSesi = sessionStatus.get(tanggalBookingString);
        statusSesi[sesiPemesanan - 1] = "Dipesan";
        sessionStatus.put(tanggalBookingString, statusSesi);

        if (sesiPemesanan != 1 && sesiPemesanan != 2) {
            clearScreen();
            System.out.println("-------------------------------------------------------");
            System.out.println("|                                                     |");
            System.out.println("|               Pilihan Sesi Tidak Valid              |");
            System.out.println("|                                                     |");
            System.out.println("-------------------------------------------------------");
            return;
        }
        System.out.println("Pilih Opsi Gedung:");
        System.out.println("1. Pernikahan - " + formatToRupiah(HARGA_PERNIKAHAN));
        System.out.println("2. Olahraga - " + formatToRupiah(HARGA_OLAH_RAGA));
        System.out.println("3. Rapat - " + formatToRupiah(HARGA_RAPAT));
        System.out.print("Masukkan Pilihan Gedung: ");
        int memilihGedung = scanner.nextInt();
        scanner.nextLine();

        switch (memilihGedung) {
            case 1:
                bookingInfo[0] = "Pernikahan";
                price = HARGA_PERNIKAHAN;
                System.out.println("Pilih Fasilitas Pernikahan:");
                System.out.println("1. PhotoBooth - " + formatToRupiah(HARGA_PHOTOBOOTH));
                System.out.println("2. Band Live - " + formatToRupiah(HARGA_BAND_LIVE));
                System.out.println("3. Pertunjukan Api Bunga - " + formatToRupiah(HARGA_API_BUNGA));
                System.out.print("Masukkan Pilihan Fasilitas: ");

                int fasilitasPernikahan = scanner.nextInt();
                scanner.nextLine();
                switch (fasilitasPernikahan) {
                    case 1:
                        bookingInfo[5] = "PhotoBooth";
                        price += HARGA_PHOTOBOOTH;
                        break;
                    case 2:
                        bookingInfo[5] = "Band Live";
                        price += HARGA_BAND_LIVE;
                        break;
                    case 3:
                        bookingInfo[5] = "Pertunjukan Api Bunga";
                        price += HARGA_API_BUNGA;
                        break;
                    default:
                        System.out.println("Pilihan Fasilitas tidak valid.");
                        return;
                }
                break;
            case 2:
                bookingInfo[0] = "Olahraga";
                price = HARGA_OLAH_RAGA;
                System.out.println("Pilih Fasilitas Olahraga:");
                System.out.println("1. Katering - " + formatToRupiah(HARGA_KATERING));
                System.out.println("2. Papan Skor - " + formatToRupiah(HARGA_PAPAN_SKOR));
                System.out.println("3. Layanan Fotografi - " + formatToRupiah(HARGA_LAYANAN_FOTOGRAFI));
                System.out.print("Masukkan Pilihan Fasilitas: ");

                int fasilitasOlahraga = scanner.nextInt();
                scanner.nextLine();
                switch (fasilitasOlahraga) {
                    case 1:
                        bookingInfo[5] = "Katering";
                        price += HARGA_KATERING;
                        break;
                    case 2:
                        bookingInfo[5] = "Papan Skor";
                        price += HARGA_PAPAN_SKOR;
                        break;
                    case 3:
                        bookingInfo[5] = "Layanan Fotografi";
                        price += HARGA_LAYANAN_FOTOGRAFI;
                        break;
                    default:
                        System.out.println("Pilihan Fasilitas tidak valid.");
                        return;
                }
                break;
            case 3:
                bookingInfo[0] = "Rapat";
                price = HARGA_RAPAT;
                System.out.println("Pilih Fasilitas Rapat:");
                System.out.println("1. Proyektor - " + formatToRupiah(HARGA_PROYEKTOR));
                System.out.println("2. Katering - " + formatToRupiah(HARGA_KATERING_RAPAT));
                System.out.println("3. Sistem Konferensi Telephone - " + formatToRupiah(HARGA_KONFERENSI_TELEPHONE));
                System.out.print("Masukkan Pilihan Fasilitas: ");

                int fasilitasRapat = scanner.nextInt();
                scanner.nextLine();
                switch (fasilitasRapat) {
                    case 1:
                        bookingInfo[5] = "Proyektor";
                        price += HARGA_PROYEKTOR;
                        break;
                    case 2:
                        bookingInfo[5] = "Katering";
                        price += HARGA_KATERING_RAPAT;
                        break;
                    case 3:
                        bookingInfo[5] = "Sistem Konferensi Telephone";
                        price += HARGA_KONFERENSI_TELEPHONE;
                        break;
                    default:
                        System.out.println("Pilihan Fasilitas tidak valid.");
                        return;
                }
                break;
            default:
                System.out.println("Pilihan Gedung tidak valid.");
                return;
        }
        System.out.println("-------------------------------------");
        System.out.println("");
        System.out.println("Total Harga: " + formatToRupiah(price));
        System.out.println("");
        System.out.println("-------------------------------------");
        System.out.println("Pilih Opsi Pembayaran:");
        System.out.println("1. DP");
        System.out.println("2. Lunas");
        System.out.print("Masukkan Pilihan Pembayaran: ");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine();

        switch (paymentChoice) {
            case 1:
                bookingInfo[1] = "DP";
                break;
            case 2:
                bookingInfo[1] = "Lunas";
                break;
            default:
                System.out.println("Pilihan Pembayaran tidak valid.");
                return;
        }

        // Menyimpan info pemesanan
        bookingInfo[2] = tanggalBookingString;
        bookingInfo[4] = username; // Username disimpan di indeks 4
        historyBooking(bookingInfo);
        clearScreen();
        System.out.println("------------------------------------------------");
        System.out.println("|                                              |");
        System.out.println("|               Pemesanan Berhasil             |");
        System.out.println("|                                              |");
        System.out.println("------------------------------------------------");
        statusPemesanan = "Dipesan";
        bookingNumber++;
    }

    public void checkIn(Scanner scanner) {
        if (this.getStatusKonfirmasi().equals("Konfirmasi")) {
            clearScreen();
            System.out.println("----------------------------------------------------------------------");
            System.out.println("|                                                                    |");
            System.out.println("|                  Pemesanan Anda Sudah Dikonfirmasi !!              |");
            System.out.println("|                                                                    |");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("+------------+-----------------+---------------------+------------------------+");
            System.out.println("  No |  Tanggal Pemesanan  |  Jenis Gedung  |  Opsi Pembayaran | Harga Gedung |");
            System.out.println("+------------+-----------------+---------------------+------------------------+");
            // Format output menjadi tabel
            for (int i = 0; i < bookingHistory.length; i++) {
                if (bookingHistory[i][0] != null) {
                    String no = String.format("%3.1s ", i + 1);
                    String tanggal = String.format("%14s     ", bookingInfo[2]);
                    String jenisGedung = String.format("%12s  ", bookingInfo[0]);
                    String opsiPemesanan = String.format("%11s     ", bookingInfo[1]);
                    String hargaGedung = String.format("Rp %.2f", price);

                    System.out.println(no + " | " + tanggal + " | " + jenisGedung + " | " + opsiPemesanan + " | "
                            + hargaGedung + "|");
                    System.out
                            .println("+------------+-----------------+---------------------+------------------------+");
                    System.out.println("");
                    keluar();
                }
            }
        } else if (this.getStatusPemesanan().equals("Dipesan")) {
            clearScreen();
            System.out.println("--------------------------------------------------------------");
            System.out.println("|                                                            |");
            System.out.println("|               Pemesanan Anda Belum Dikonfirmasi            |");
            System.out.println("|                                                            |");
            System.out.println("--------------------------------------------------------------");
        } else {
            clearScreen();
            System.out.println("------------------------------------------------");
            System.out.println("|                                              |");
            System.out.println("|               Belum ada Pemesanan            |");
            System.out.println("|                                              |");
            System.out.println("------------------------------------------------");
        }
    }

    public boolean isSessionBooked(String session) {
        for (int i = 0; i < bookingHistory.length; i++) {
            if (bookingHistory[i][0] != null && bookingHistory[i][2].equals(session)) {
                return true;
            }
        }
        return false;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void akhiriPemesanan(Scanner scanner) {
        if ("Dipesan".equals(statusPemesanan)) {
            clearScreen();
            System.out.println("------------------------------------------------");
            System.out.println("|                                              |");
            System.out.println("|               Halaman Check-Out              |");
            System.out.println("|                                              |");
            System.out.println("------------------------------------------------");
            if ("DP".equals(bookingInfo[1])) {
                System.out.println("Anda telah memilih pembayaran DP saat memesan, dan anda harus melunasi jika ingin check-out.");
                System.out.print("1. Lunas: ");
                int paymentChoice = scanner.nextInt();
                scanner.nextLine();
                if (paymentChoice == 1) {
                    // Ubah status pembayaran dari "DP" menjadi "Lunas"
                    statusPembayaran = "Lunas";
                    bookingInfo[1] = "Lunas"; // Juga perlu memperbarui status pembayaran di informasi pemesanan
                    System.out.println("------------------------------------------------");
                    System.out.println("|                                              |");
                    System.out.println("|               Anda Telah Melunasi            |");
                    System.out.println("|                                              |");
                    System.out.println("------------------------------------------------");
                    setStatusAkhir("Berakhir");
                    setChoicePembayaran("DP");
                } else {
                    System.out.println("Pembayaran DP masih belum dilunasi.");
                    return;
                }
            } else if ("Lunas".equals(bookingInfo[1])) {
                clearScreen();
                setStatusAkhir("Berakhir");
                setChoicePembayaran("Lunas");
                System.out.println("------------------------------------------------");
                System.out.println("|                                              |");
                System.out.println("|               Anda Telah Melunasi            |");
                System.out.println("|                                              |");
                System.out.println("------------------------------------------------");
            } else {
                System.out.println("--------------------------------------------------");
                System.out.println("|                                                |");
                System.out.println("|               Belum ada Pemesanan              |");
                System.out.println("|                                                |");
                System.out.println("--------------------------------------------------");
                return;
            }

            // Menampilkan struk detail pemesanan
            System.out.println("+------------+-----------------+------------------+");

            System.out.println("              Struk Detail Pemesanan");
            System.out.println("+------------+-----------------+------------------+");

            System.out.println("  Tanggal Pemesanan: " + bookingInfo[2]);
            System.out.println("  Jenis Gedung: " + bookingInfo[0]);
            System.out.println("  Fasilitas : " + bookingInfo[5]);
            System.out.println("  Harga Gedung: " + formatToRupiah(price));
            System.out.println("  Opsi Pembayaran: " + bookingInfo[1]);
            System.out.println("+------------+-----------------+------------------+");

            System.out.print("Beri rating 1-5 : ");
            int rating = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Berikan Ulasan : ");
            String ulasan = scanner.nextLine();
            addRatingAndUlasan(rating, ulasan);
            System.out.println("");
            System.out.println("-----------------------------------------------------------");
            System.out.println("|                                                         |");
            System.out.println("|               Terima Kasih Telah Memesan !              |");
            System.out.println("|                                                         |");
            System.out.println("-----------------------------------------------------------");

            // Kosongkan info pemesanan
            Arrays.fill(bookingInfo, null);
        } else {
            clearScreen();
            System.out.println("--------------------------------------------------");
            System.out.println("|                                                |");
            System.out.println("|               Belum ada Pemesanan              |");
            System.out.println("|                                                |");
            System.out.println("--------------------------------------------------");
        }
    }

    public void addRatingAndUlasan(int rating, String ulasan){
        if (bookingInfo == null){
            bookingInfo = new String [15];
        }
        bookingInfo[7] = String.valueOf(rating);
        bookingInfo[8] = ulasan;
        historyBooking(bookingInfo);
    }

    public void viewBooking() {
        if ("Dipesan".equals(statusPemesanan.trim())) {
            clearScreen();
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("|                                                                                |");
            System.out.println("|                  Informasi Pemesanan Yang Anda Booking Sekarang                |");
            System.out.println("|                                                                                |");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("+------------+-----------------+---------------------+------------------------+");
            System.out.println("                                     Informasi");
            System.out.println("+------------+-----------------+---------------------+------------------------+");
            System.out.println("  No |  Tanggal Pemesanan  |  Jenis Gedung  |  Opsi Pembayaran | Harga Gedung |");
            System.out.println("+------------+-----------------+---------------------+------------------------+");
            // Format output menjadi tabel
            for (int i = 0; i < bookingHistory.length; i++) {
                if (bookingHistory[i][0] != null) {
                    String no = String.format("%3.1s ", i + 1);
                    String tanggal = String.format("%14s     ", bookingInfo[2]);
                    String jenisGedung = String.format("%12s  ", bookingInfo[0]);
                    String opsiPemesanan = String.format("%11s     ", bookingInfo[1]);
                    String hargaGedung = String.format("Rp %.2f", price);

                    System.out.println(no + " | " + tanggal + " | " + jenisGedung + " | " + opsiPemesanan + " | "
                            + hargaGedung + "|");
                    System.out
                            .println("+------------+-----------------+---------------------+------------------------+");
                    System.out.println("");
                    keluar();
                }
            }
        } else {
            clearScreen();
            System.out.println("--------------------------------------------------");
            System.out.println("|                                                |");
            System.out.println("|               Belum ada Pemesanan              |");
            System.out.println("|                                                |");
            System.out.println("--------------------------------------------------");
        }
    }

    public void historyBooking(String[] bookingInfo) {
        for (int i = 0; i < bookingHistory.length; i++) {
            if (bookingHistory[i][0] == null) {
                // Copy informasi pemesanan dari bookingInfo
                for (int j = 0; j < bookingInfo.length && j < bookingHistory[i].length; j++) {
                    bookingHistory[i][j] = bookingInfo[j];
                }
                bookingHistory[i][4] = String.valueOf(price);
                bookingHistory[i][5] = statusPembayaran;
                bookingHistory[i][6] = bookingInfo[5];
                bookingHistory[i][7] = bookingInfo[7];
                bookingHistory[i][8] = bookingInfo[8];
                bookingHistory[i][9] = bookingInfo[9];
                break;
            }
        }
    }

    public void viewBookingHistory() {
        if ("Berakhir".equals(statusAkhir.trim())) {
            clearScreen();
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("|                                                                                |");
            System.out.println("|                  Informasi Yang Pernah Anda Booking Sebelumnya                 |");
            System.out.println("|                                                                                |");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("");
            System.out.println("+------------+-----------------+---------------------+---------------------+------------------+");
            System.out.println(" No | Tanggal Pemesanan | Jenis Gedung | Fasilitas  | Opsi Pembayaran | Harga Gedung");
            System.out.println("+------------+-----------------+---------------------+---------------------+------------------+");
            for (int i = 0; i < bookingHistory.length; i++) {
                if (bookingHistory[i][0] != null) {
                    // hasDetails = true;
                    String no = String.format("%3.1s", i + 1);
                    String tanggal = String.format("%13s    ", bookingHistory[i][2]);
                    String jenisGedung = String.format("%11s ", bookingHistory[i][0]);
                    String fasilitas = String.format("%5s", bookingHistory[i][6]);
                    String opsiPemesanan = String.format("%10s     ", bookingHistory[i][1]);
                    String hargaGedung = String.format("Rp %.2f", Double.parseDouble(bookingHistory[i][4]));
                    System.out.println(no + " | " + tanggal + " | " + jenisGedung + " | " + fasilitas + " | "
                            + opsiPemesanan + " | " + hargaGedung);
                }
            }
            System.out.println("+------------+-----------------+---------------------+---------------------+------------------+");
            System.out.println("");
            keluar();
        } else {
            clearScreen();
            System.out.println("------------------------------------------------");
            System.out.println("|                                              |");
            System.out.println("|               Belum ada Pemesanan            |");
            System.out.println("|                                              |");
            System.out.println("------------------------------------------------");
        }
    }

    public void informasiUser() {
        clearScreen();
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("|                                                                                |");
        System.out.println("|                              Informasi Pribadi Anda                            |");
        System.out.println("|                                                                                |");
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("");
        System.out
                .println("+------------+-----------------+---------------------+-----------------+-------------------");
        System.out.println("                                    Informasi User");
        System.out
                .println("+------------+-----------------+---------------------+-----------------+-------------------");
        System.out
                .println("NIK          | Username        | Tanggal Lahir       | Alamat          | No Telephone      ");
        System.out
                .println("+------------+-----------------+---------------------+-----------------+-------------------");

        // Format output menjadi tabel
        String nikFormatted = String.format("%-15s", nik);
        String usernameFormatted = String.format("%-19s", username);
        String alamatFormatted = String.format("%-12s", address);
        String noTelephoneFormatted = String.format("%-13s", phoneNumber);
        String tanggalLahirFormatted = String.format("%-12s", dateOfBirth);

        System.out.println(nikFormatted + " | " + usernameFormatted + " | " + tanggalLahirFormatted +  " | "  + alamatFormatted + " | " + noTelephoneFormatted );
        System.out.println("+------------+-----------------+---------------------+-----------------+-------------------");
        System.out.println("");
        editinfo();
    }

    public void editinfo() {
        Scanner scanner = new Scanner(System.in);
        int pilihEdit;
        System.out.println("------------------------------------------------");
        System.out.println("|                                              |");
        System.out.println("|               Edit Informasi Anda            |");
        System.out.println("|                                              |");
        System.out.println("------------------------------------------------");

        System.out.println("1. NIK");
        System.out.println("2. Alamat");
        System.out.println("3. Nomor Telephone");
        System.out.println("4. Tanggal Lahir");
        System.out.println("0. Keluar");
        System.out.print("Pilihan Anda: ");
        pilihEdit = scanner.nextInt();

        switch (pilihEdit) {
            case 1:
                scanner.nextLine();
                System.out.print("Masukkan NIK baru: ");
                nik = scanner.nextLine();
                while (nik.trim().isEmpty()){
                    System.out.println((char) 27 + "[01;31m Nik tidak boleh kosong" + (char) 27+ "[00;00m");
                    System.out.print("Masukkan NIK baru : ");
                    nik = scanner.nextLine();
                }
                break;
            case 2:
                scanner.nextLine();
                System.out.println("Masukkan Alamat Baru : ");
                address = scanner.nextLine();
                while (address.trim().isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Alamat tidak boleh kosong" + (char) 27+ "[00;00m");
                    System.out.println("Masukkan Alamat Baru : ");
                    address = scanner.nextLine();
                }
                break;
            case 3:
                scanner.nextLine();
                System.out.println("Masukkan Nomor Telephone Baru : ");
                phoneNumber = scanner.nextLine();
                while (phoneNumber.trim().isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Nomor Telephone tidak boleh kosong" + (char) 27+ "[00;00m");
                    System.out.println("Masukkan Nomor Telephone Baru : ");
                    phoneNumber = scanner.nextLine();
                }
                break;
            case 4:
                scanner.nextLine();
                System.out.println("Masukkan Tanggal Lahir Baru : ");
                dateOfBirth = scanner.nextLine();
                while (dateOfBirth.trim().isEmpty()) {
                    System.out.println((char) 27 + "[01;31m Tanggal lahir tidak boleh kosong" + (char) 27+ "[00;00m");
                    System.out.println("Masukkan Tanggal Lahir Baru : ");
                    dateOfBirth = scanner.nextLine();
                }
            case 0:
                clearScreen();
                return;
            default:
                System.out.println("Pilihan tidak valid, Silahkan Coba Lagi");
                break;
        }
        clearScreen();
        informasiUser();
    }

    public void lapor() {
        Scanner scanner = new Scanner(System.in);
        int pilihKendala;
        String kendala; // Declare the variable
    
        clearScreen();
        System.out.println("-----------------------------------------------------");
        System.out.println("|                                                   |");
        System.out.println("|               Lapor jika ada masalah !            |");
        System.out.println("|                                                   |");
        System.out.println("-----------------------------------------------------");
        System.out.println("");
        System.out.println("1. Lapor");
        System.out.println("2. Lihat laporan anda");
        System.out.println("0. Keluar");
        System.out.print("Pilihan anda : ");
        pilihKendala = scanner.nextInt();
    
        switch (pilihKendala) {
            case 1:
                buatLaporan(scanner);
                break;
            case 2:
                lihatLaporan();
                break;
            case 0:
                clearScreen();
                return;
            default:
                break;
        }
    }

    public void buatLaporan(Scanner scanner){
            String kendala; // Declare the variable
            clearScreen();
            System.out.println("-----------------------------------------------------");
            System.out.println("|                                                   |");
            System.out.println("|                Lapor Kendala Anda                 |");
            System.out.println("|                                                   |");
            System.out.println("-----------------------------------------------------");
            scanner.nextLine();
            System.out.print("Tulis kendala anda: ");
            kendala = scanner.nextLine();
            while (kendala.trim().isEmpty()){
                System.out.println((char) 27 + "[01;31m Laporan tidak boleh kosong" + (char) 27+ "[00;00m");
                System.out.print("Tulis kendala anda : ");
                kendala = scanner.nextLine();
            addLaporan(kendala);
            setStatusLaporan("Ada Laporan");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("|                                                                              |");
            System.out.println("|           Kendala berhasil dilaporkan tunggu balasan dari admin !            |");
            System.out.println("|                                                                              |");
            System.out.println("--------------------------------------------------------------------------------");
        }
    }

    public void lihatLaporan(){
        if (this.getStatusLaporan().equals("Sudah Ditanggapi")){
            clearScreen();
            String tanggapan = getTanggapan();
            System.out.println("Ini tanggapannya : " + tanggapan); 
        } else if(this.getStatusLaporan().equals("Ada Laporan")){
            clearScreen();
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("|                                                                              |");
            System.out.println("|                   Laporan Belum Di Tanggapi Oleh Admin                       |");
            System.out.println("|                                                                              |");
            System.out.println("--------------------------------------------------------------------------------");
        } else {
            System.out.println("anu");
        }
    }

    public void addLaporan(String kendala){
        if (bookingInfo == null){
            bookingInfo = new String [15];
        }
        bookingInfo[9] = kendala;
        historyBooking(bookingInfo);
    }
    
    public void keluar() {
        Scanner scanner = new Scanner(System.in);
        int kembali;
        System.out.print("1. Kembali ke Dashboard : ");
        kembali = scanner.nextInt();

        switch (kembali) {
            case 1:
                return;
        }
    }
}