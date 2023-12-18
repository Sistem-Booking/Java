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

public class main {
    private static User[] users = new User[100]; // Array untuk menyimpan pengguna
    private static int userCount = 0;
    private static Map<String, User> userMap = new HashMap<>();
    private Map<String, Boolean> bookedSlots = new HashMap<>();
    private ArrayList<String[]> bookingHistory;
    private static boolean isLoggedIn = false;
    private static User loggedInUser = null;
    private String statusKonfirmasi;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    public void setStatusKonfirmasi(String status) {
        this.statusKonfirmasi = status;
    }
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
                if (!isLoggedIn) {
                    clearScreen();
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
            System.out.print("Masukkan username yang akan mengganti kata sandi: ");
            String targetUsername = scanner.nextLine();
            User targetUser = userMap.get(targetUsername);
            if (targetUser != null) {
                System.out.print("Masukkan kata sandi baru: ");
                String newPassword = scanner.nextLine();
                targetUser.changePassword(newPassword);
                clearScreen();
                System.out.println("-------------------------------------------------------");
                System.out.println("|                                                     |");
                System.out.println("|               Kata sandi Berhasil Dirubah           |");
                System.out.println("|                                                     |");
                System.out.println("-------------------------------------------------------");
                passwordChanged = true; // Setel variabel ini ke true setelah berhasil mengubah kata sandi.
            } else {
                System.out.println("----------------------------------------------------------");
                System.out.println("");
                System.out.println("    Pengguna dengan username '" + targetUsername + "' tidak ditemukan.");
                System.out.println("");
                System.out.println("----------------------------------------------------------");
                System.out.println("");
            }
        }
    }

    private static void adminMenu(Scanner scanner) {
        while (true) {
            clearScreen();
            System.out.println("-----------------------------------------------------");
            System.out.println("|                                                   |");
            System.out.println("|                 Admin Dashboard                   |");
            System.out.println("|                                                   |");
            System.out.println("-----------------------------------------------------");
            System.out.println("1. Lihat Informasi User Terdaftar");
            System.out.println("2. Cari User");
            System.out.println("3. Lihat Informasi Check-in User");
            System.out.println("4. Konfirmasi Booking");
            System.out.println("5. Keluar Sebagai Admin");
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
                    confirmBooking(scanner);
                    break;
                case 5:
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

    private static void confirmBooking(Scanner scanner) {
        clearScreen();
        System.out.println("Konfirmasi Booking:");
        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+");
        System.out.println("| Username   | Status Pemesanan| Tanggal Booking     | Waktu Booking   | Pembayaran    |");
        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+");

        for (User user : userMap.values()) {
            String username = user.getUsername();
            String statusPemesanan = user.getStatusPemesanan();
            String statusKonfirmasi = user.getStatusKonfirmasi();
            String tanggalBooking = "Belum memesan";
            String waktuBooking = "";
            String pembayaran = "";

            String[] bookingInfo = user.getBookingInfo(); // Ambil informasi pemesanan
            if (user.getStatusPemesanan().equals("Dipesan")) {
                tanggalBooking = bookingInfo[2];
                waktuBooking = bookingInfo[3];
                pembayaran = bookingInfo[1];
            }

            String row = String.format("| %-10s | %-15s | %-19s | %-15s | %-13s |", username, statusPemesanan,
                    tanggalBooking, waktuBooking, pembayaran);
            System.out.println(row);
        }

        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+");
        System.out.print("Masukkan username pengguna yang ingin dikonfirmasi: ");
        String usernameToConfirm = scanner.nextLine();

        User userToConfirm = userMap.get(usernameToConfirm);
        if (userToConfirm != null && userToConfirm.getStatusPemesanan().equals("Dipesan")) {
            userToConfirm.setStatusKonfirmasi("Konfirmasi");
            System.out.println("----------------------------------------------------------------------");
            System.out.println("|                                                                    |");
            System.out.println("|    Booking untuk " + usernameToConfirm + " telah dikonfirmasi.    |");
            System.out.println("|                                                                    |");
            System.out.println("----------------------------------------------------------------------");
        } else {
            System.out.println("Pengguna tidak ditemukan atau tidak memenuhi syarat untuk dikonfirmasi.");
        }
    }

    private static void viewPemesananInfo() {
        clearScreen();
        System.out.println("Informasi Check-in User:");
        System.out.println("+------------+--------------------+---------------------+-----------------+---------------+");
        System.out.println("| Username   | Status Pemesanan   | Tanggal Booking     | Waktu Booking   | Pembayaran     |");
        System.out.println("+------------+--------------------+---------------------+-----------------+---------------+");

        for (User user : userMap.values()) {
            String username = user.getUsername();
            String statusPemesanan = user.getStatusPemesanan();
            String tanggalBooking = "Belum memesan";
            String waktuBooking = "";
            String pembayaran = "";

            String[] bookingInfo = user.getBookingInfo(); // Ambil informasi pemesanan
            if (user.getStatusPemesanan().equals("Dipesan")) {
                tanggalBooking = bookingInfo[2];
                waktuBooking = bookingInfo[3];
                pembayaran = bookingInfo[1];
            }

            String row = String.format("| %-10s | %-15s | %-19s | %-15s | %-13s |", username, statusPemesanan,
                    tanggalBooking, waktuBooking, pembayaran);
            System.out.println(row);
        }

        System.out.println("+------------+-----------------+---------------------+-----------------+---------------+");
    }

    private static void sortUsers() {
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
        clearScreen();
        // Display the sorted list of valid users
        System.out.println("===========================================================================");
        System.out.println("                        Daftar Pengguna Terdaftar ");
        System.out.println("===========================================================================");
        System.out.println(" No  | NIK           | Username          | Alamat          | No Telephone");
        System.out.println("===========================================================================");

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

        System.out.println("===========================================================================");
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
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("|                                                                                       |");
            System.out.println("|               Batas maksimum pengguna tercapai. Tidak dapat membuat akun              |");
            System.out.println("|                                                                                       |");
            System.out.println("-----------------------------------------------------------------------------------------");
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
            System.out.println("---------------------------------------------");
            System.out.println("");
            System.out.println((char) 27 + "[01;31mUsername dan password harus diisi. Coba lagi." + (char) 27 + "[00;00m");
            System.out.println("");
            System.out.println("---------------------------------------------");
        } else if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("----------------------------------------------------");
            System.out.println("|                                                   |");
            System.out.println("|               Admin Berhasil Login !              |");
            System.out.println("|                                                   |");
            System.out.println("----------------------------------------------------");
            adminMenu(scanner);
        } else {
            User foundUser = userMap.get(username);
            if (foundUser != null && foundUser.getPassword().equals(password)) {
                clearScreen();
                System.out.println("----------------------------------------------");
                System.out.println("|                                            |");
                System.out.println("|               Selamat Datang " + username + " !           |");
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
                System.out.println("-------------------------------------------------------");
                System.out.println("");
                System.out.println((char) 27 + "[01;31mLogin gagal. Username atau password salah. Coba lagi." + (char) 27 + "[00;00m");
                System.out.println("");
                System.out.println("-------------------------------------------------------");
            }
        }
    }

    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Pesan Gedung");
            System.out.println("2. Check In");
            System.out.println("3. Akhiri Pemesanan");
            System.out.println("4. Informasi User");
            System.out.println("5. Lihat Informasi Yang Di Booking Sekarang");
            System.out.println("6. Lihat History Pemesanan");
            System.out.println("7. Keluar");
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
        System.out.print("Masukkan Username yang ingin Anda cari: ");
        String usernameToSearch = scanner.nextLine();
        User foundUser = userMap.get(usernameToSearch);

        if (foundUser != null) {
            clearScreen();
            System.out.println("===================================================================================");
            System.out.println("                           Informasi User yang Anda Cari");
            System.out.println("===================================================================================");
            System.out.println(" Nik           | Username          | Tanggal Lahir  | No Telephone | Alamat ");
            System.out.println("===================================================================================");

            // Format output menjadi tabel
            String nik = String.format("%-15s", foundUser.getNik());
            String username = String.format("%-19s", foundUser.getUsername());
            String tanggalLahir = String.format("%-16s", foundUser.getDateOfBirth());
            String noTelephone = String.format("%-13s", foundUser.getPhoneNumber());
            String alamat = String.format("%-12s", foundUser.getAddress());

            System.out.println(nik + " | " + username + " | " + tanggalLahir + " | " + noTelephone + " | " + alamat);
            System.out.println("===================================================================================");
        } else {
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
    private String[] bookingInfo = new String[5]; // Array untuk menyimpan info pemesanan
    private String[][] bookingHistory;
    private String statusPemesanan = "Belum pesan"; // Status awal
    private String statusKonfirmasi = "Belum konfirmasi";
    private String statusAkhir = "";
    private String statusBooking = ""; 
    private String statusBook = "";
    private int bookingNumber = 0; // Berikut adalah Nomor pemesanan
    private double price;
    private String statusPembayaran = "Belum Lunas";
    private static final double HARGA_PERNIKAHAN = 1500000.0;
    private static final double HARGA_OLAH_RAGA = 1200000.0;
    private static final double HARGA_RAPAT = 1000000.0;
    private static final double HARGA_PHOTOBOOTH = 130000.0;
    private static final double HARGA_BAND_LIVE = 200000.0;
    private static final double HARGA_API_BUNGA = 250000.0;
    private static final double HARGA_KATERING = 1200000.0;
    private static final double HARGA_PAPAN_SKOR = 300000.0;
    private static final double HARGA_LAYANAN_FOTOGRAFI = 250000.0;
    private static final double HARGA_PROYEKTOR = 150000.0;
    private static final double HARGA_KATERING_RAPAT = 1500000.0;
    private static final double HARGA_KONFERENSI_TELEPHONE = 150000.0;
    private boolean isCheckedIn;
    private String tanggalBooking;
    private String waktuBooking;
    private String tempatBooking;
    private String fasilitasBooking;
    private double totalPembayaran;
    private Map<String, Boolean> bookedSlots = new HashMap<>();
    private boolean slotTersedia;
    private boolean kembali = false;
    private User loggedInUser;
    private String bookedDate = "";
    private String bookedSession = "";
    private String[] sesi = {"09:00 - 15:00", "16:00 - 22-00"};
    ArrayList<String> bookedDates = new ArrayList<>();
    ArrayList<String> bookedSessions = new ArrayList<>();
    private static Map<String, User> userMap = new HashMap<>();
    User userToCheckIn = userMap.get(username); 
    HashMap<String, String[]> sessionStatus = new HashMap<>();
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
        this.bookingHistory = new String[10][8];
        this.isCheckedIn = false;
    }
    public void lihatTanggal(Scanner scanner) {
    }

    public boolean isTanggalBooked(String tanggal) {
        return false;
    }

    public void setStatusPemesanan(String string) {
    }

    public void setStatusKonfirmasi(String status){
        this.statusKonfirmasi = status;
    }

    public void setStatusAkhir(String status){
        this.statusAkhir = status;
    }

    public void setStatusBooking(String string){

    }

    public void setStatusBook(String string){

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

    public String getStatusKonfirmasi(){
        return statusKonfirmasi;
    }

    public String getStatusAkhir(){
        return statusAkhir;
    }

    public String getStatusBooking(){
        return statusBooking;
    }

    public String getStatusBook(){
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


    public void pemesananGedung(Scanner scanner) {
        clearScreen();
        System.out.println("------------------------------------------------");
        System.out.println("|                                              |");
        System.out.println("|               Halaman Pemesanan              |");
        System.out.println("|                                              |");
        System.out.println("------------------------------------------------");
        String tanggalBookingString = "";
        String waktuBookingString = "";
        
        int sesiPemesanan = 0;

        // Inisialisasi bookingInfo
        this.bookingInfo = new String[10];
        // Input tanggal
        System.out.print("Masukkan Tanggal (Format: DD-MM-YYYY): ");
        tanggalBookingString = scanner.nextLine();

        if (!tanggalBookingString.matches("\\d{2}-\\d{2}-\\d{4}")) {
            System.out.println("\u001B[31mMasukkan input tanggal dengan format yang benar (contoh: 01-01-2023).\u001B[0m");
            return;
        }
        System.out.println("Pilih Sesi Pemesanan:");
        System.out.println("1. Sesi 1 ( Jam 09:00 - 15:00 )");
        System.out.println("2. Sesi 2 ( Jam 16:00 - 22:00 )");
        System.out.print("Masukkan Pilihan Sesi: ");
        sesiPemesanan = scanner.nextInt();
        scanner.nextLine();
        if (sessionStatus.containsKey(tanggalBookingString)){
            String[] statusSesi = sessionStatus.get(tanggalBookingString);
            if (statusSesi[sesiPemesanan -  1] != null && !statusSesi[sesiPemesanan - 1].isEmpty()) {
            clearScreen();
            System.out.println("------------------------------------------------------------------------------------------------------");
            System.out.println("|                                                                                                    |");
            System.out.println("|                 Tanggal dan sesi sudah dibooking silahkan pilih tanggal dan sesi lagi              |");
            System.out.println("|                                                                                                    |");
            System.out.println("------------------------------------------------------------------------------------------------------");
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

                System.out.println("Total Harga: " + formatToRupiah(price));
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
                bookingInfo[3] = waktuBookingString;
                bookedDate = tanggalBookingString;

                // Simpan username yang melakukan check-in
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
            if(this.getStatusKonfirmasi().equals("Konfirmasi")){
                clearScreen();
                System.out.println("Pemesanan Anda Sudah Dikonfirmasi");
                System.out.println("+------------+-----------------+---------------------+---------------------+------------------+----------");
                System.out.println(" Username    | Jenis Gedung    | Fasilitas           | Tanggal Booking     | Opsi Pemesanan   | Harga    ");
                System.out.println("+------------+-----------------+---------------------+---------------------+------------------+----------");

                System.out.println("+------------+-----------------+---------------------+---------------------+------------------+-----------");
            } else if (this.getStatusPemesanan().equals("Dipesan")){
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

    public void setLoggedInUser(User user){
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
                System.out.println("Anda telah memilih pembayaran DP saat check-in. Anda harus melunasi sekarang.");
                System.out.print("1. Lunas: ");
                int paymentChoice = scanner.nextInt();
                scanner.nextLine();

                if (paymentChoice == 1) {
                    // Ubah status pembayaran dari "DP" menjadi "Lunas"
                    statusPembayaran = "Lunas";
                    bookingInfo[1] = "Lunas"; // Juga perlu memperbarui status pembayaran di informasi pemesanan
                    System.out.println("Pembayaran berhasil dilunasi.");
                } else {
                    System.out.println("Pembayaran DP masih belum dilunasi.");
                    return;
                }
            } else if ("Lunas".equals(bookingInfo[1])) {
                    clearScreen();
                    setStatusAkhir("Sampun");
                    System.out.println("------------------------------------------------");
                    System.out.println("|                                              |");
                    System.out.println("|               Anda Telah Melunasi            |");
                    System.out.println("|                                              |");
                    System.out.println("------------------------------------------------");
            } else {
                System.out.println("Pilihan pembayaran tidak valid.");
                return;
            }

            // Menampilkan struk detail pemesanan
            System.out.println("+------------+-----------------+------------------+");;
            System.out.println("              Struk Detail Pemesanan");
            System.out.println("+------------+-----------------+------------------+");;
            System.out.println("  Tanggal Pemesanan: " + bookingInfo[2]);
            System.out.println("  Jenis Gedung: " + bookingInfo[0]);
            System.out.println("  Harga Gedung: " + formatToRupiah(price));
            System.out.println("  Opsi Pembayaran: " + bookingInfo[1]);
            System.out.println("+------------+-----------------+------------------+");;
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

    public void viewBooking() {
        if (bookingInfo[2] != null && bookingInfo[3] != null && bookingInfo[0] != null && bookingInfo[1] != null) {
            clearScreen();
            System.out.println("+------------+-----------------+---------------------+------------------------+");
            System.out.println("                              Informasi Pemesanan");
            System.out.println("+------------+-----------------+---------------------+------------------------+");
            System.out.println(" No  |  Tanggal Pemesanan  |  Jenis Gedung  |  Opsi Pembayaran | Total");
            System.out.println("+------------+-----------------+---------------------+------------------------+");

            // Format output menjadi tabel
            String no = String.format("%3s", "");
            String tanggal = String.format("%18s", bookingInfo[2]);
            String jenisGedung = String.format("%14s", bookingInfo[0]);
            String opsiPemesanan = String.format("%16s", bookingInfo[1]);
            String hargaGedung = String.format("Rp %.2f", price);

            System.out.println(no + " | " + tanggal + " | " + jenisGedung + " | " + opsiPemesanan + " | "+ hargaGedung);
            System.out.println("+------------+-----------------+---------------------+------------------------+");
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

                // Simpan harga gedung
                bookingHistory[i][4] = String.valueOf(price);

                // Set status pembayaran (indeks 4) di dalam history menjadi status pembayaran
                // saat ini
                bookingHistory[i][5] = statusPembayaran;

                break;
            }
        }
    }

    public void viewBookingHistory() {
        if ("Sampun".equals(statusAkhir.trim())) {
            clearScreen();
            System.out.println("+------------+-----------------+---------------------+---------------------+------------------+");
            System.out.println(" No | Tanggal Pemesanan | Jenis Gedung | Fasilitas | Opsi Pembayaran | Harga Gedung");
            System.out.println("+------------+-----------------+---------------------+---------------------+------------------+");
            for (int i = 0; i < bookingHistory.length; i++) {
                if (bookingHistory[i][0] != null) {
                    // hasDetails = true;
                    String no = String.format("%4d", i + 1);
                    String tanggal = String.format("%16s", bookingHistory[i][2]);   
                    String jenisGedung = String.format("%12s", bookingHistory[i][0]);
                    String fasilitas = String.format("%12s", bookingHistory[i][6]); // Ambil data fasilitas dari booking history
                    String opsiPemesanan = String.format("%15s", bookingHistory[i][1]);
                    String hargaGedung = String.format("Rp %.2f", Double.parseDouble(bookingHistory[i][4]));

                    System.out.println(no + " | " + tanggal + " | " + jenisGedung + " | " + fasilitas+ " | " + opsiPemesanan + " | " + hargaGedung);
                }
            }
            System.out.println("+------------+-----------------+---------------------+---------------------+------------------+");
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
        System.out.println("+------------+-----------------+---------------------+----------+");
        System.out.println("                      Informasi User");
        System.out.println("+------------+-----------------+---------------------+----------+");
        System.out.println("NIK           | Username          | No Telephone | Alamat ");
        System.out.println("+------------+-----------------+---------------------+----------+");

        // Format output menjadi tabel
        String nikFormatted = String.format("%-15s", nik);
        String usernameFormatted = String.format("%-19s", username);
        String noTelephoneFormatted = String.format("%-13s", phoneNumber);
        String alamatFormatted = String.format("%-12s", address);

        System.out.println(
                nikFormatted + " | " + usernameFormatted + " | " + noTelephoneFormatted + " | " + alamatFormatted);
        System.out.println("+------------+-----------------+---------------------+----------+");
    }
}