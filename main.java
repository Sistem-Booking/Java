import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Comparator;

public class main {
    private static User[] users = new User[100]; // Array untuk menyimpan pengguna
    private static int userCount = 0;
    private static Map<String, User> userMap = new HashMap<>();
    private static boolean isLoggedIn = false;
    private static User loggedInUser = null;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (!isLoggedIn) {
                System.out.println("1. Login");
                System.out.println("2. Buat Akun");
                System.out.println("3. Keluar");
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
    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. Lihat Informasi User Terdaftar");
            System.out.println("2. Keluar Sebagai Admin");
            System.out.print("Masukkan Pilihan Anda: ");
            int adminChoice = scanner.nextInt();
            scanner.nextLine();
    
            switch (adminChoice) {
                case 1:
                    sortUsers();
                    break;
                case 2:
                    System.out.println("=====================");
                    System.out.println("Anda telah keluar sebagai admin");
                    System.out.println("=====================");
                    return;
                default:
                    System.out.println("===============================================");
                    System.out.println("Pilihan admin tidak diketahui, mohon coba lagi");
                    System.out.println("===============================================");
                    break;
            }
        }
    }
    
    
    private static void sortUsers() {
        // Buat salinan array pengguna yang tidak null (tidak termasuk admin)
        User[] validUsers = new User[userCount];
        int validUserCount = 0;
    
        for (int i = 0; i < userCount; i++) {
            if (users[i] != null && !users[i].getUsername().equals(ADMIN_USERNAME)) {
                validUsers[validUserCount] = users[i];
                validUserCount++;
            }
        }
    
        // Lakukan sorting berdasarkan nomor NIK
        Arrays.sort(validUsers, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                // Ganti compareTo dengan perbandingan yang sesuai
                return user1.getNik().compareTo(user2.getNik());
            }
        });
    
        // Tampilkan daftar pengguna yang sudah diurutkan
        System.out.println("===========================================================================");
        System.out.println("                         Daftar Pengguna Terdaftar");
        System.out.println("===========================================================================");
        System.out.println(" No  | NIK           | Username          | Alamat          | No Telephone");
        System.out.println("===========================================================================");
    
        for (int i = 0; i < validUserCount; i++) {
            String no = String.format("%3d", i + 1);
            String nik = String.format("%-15s", validUsers[i].getNik());
            String username = String.format("%-19s", validUsers[i].getUsername());
            String alamat = String.format("%-15s", validUsers[i].getAddress());
            String noTelephone = String.format("%-13s", validUsers[i].getPhoneNumber());
            System.out.println(no + " | " + nik + " | " + username + " | " + alamat + " | " + noTelephone);
        }
    
        System.out.println("=======================================");
    }
    private static void createAccount(Scanner scanner) {
        System.out.println("============");
        System.out.println(" Buat Akun ");
        System.out.println("============");
        System.out.print("Masukkan NIK : ");
        String nik = scanner.nextLine();
        
        // Tambahkan kode untuk memeriksa apakah username telah digunakan
        System.out.print("Masukkan Username : ");
        String username = scanner.nextLine();
        if (isUsernameTaken(username)) {
            System.out.println("====================================");
            System.out.println("Username telah digunakan. Silakan pilih username lain.");
            System.out.println("====================================");
            return;
        }
        
    
        // Lanjutkan dengan meminta input lainnya
        System.out.print("Masukkan Tanggal Lahir : ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Masukkan No Telephone : ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Masukkan Alamat : ");
        String address = scanner.nextLine();
        System.out.print("Masukkan Kata Sandi : ");
        String password = scanner.nextLine();
        User newUser = new User(nik, username, dateOfBirth, phoneNumber, address, password);
        userMap.put(username, newUser);
        // Validasi input kosong
        if (nik.isEmpty() || username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || dateOfBirth.isEmpty()) {
            System.out.println("======================================================");
            System.out.println("Tolong isi semua data yang diperlukan untuk mendaftar");
            System.out.println("======================================================");
            return;
        }
    
        // Tambahkan pengguna ke array
        if (userCount < users.length) {
            users[userCount] = newUser;
            userCount++;
    
            System.out.println("==================================================");
            System.out.println(" Akun berhasil dibuat, anda sekarang sudah login ");
            System.out.println("==================================================");
            isLoggedIn = true;
            loggedInUser = newUser;
        } else {
            System.out.println("==================================================");
            System.out.println("Batas maksimum pengguna tercapai. Tidak dapat membuat akun.");
            System.out.println("==================================================");
        }
    }
    private static boolean isUsernameTaken(String username) {
        return userMap.containsKey(username);
    }

    private static void login(Scanner scanner) {
        System.out.println("========");
        System.out.println(" Masuk ");
        System.out.println("========");
        System.out.print("Masukkan Username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan Kata Sandi: ");
        String password = scanner.nextLine();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("==================================");
            System.out.println("Admin berhasil login!");
            System.out.println("==================================");
            adminMenu(scanner);
        } else {
            User foundUser = userMap.get(username);
            if (foundUser != null && foundUser.getPassword().equals(password)) {    
                System.out.println("==================================");
                System.out.println("Login berhasil. Selamat Datang, " + username + "!");
                System.out.println("==================================");
                loggedInUser = foundUser;
                isLoggedIn = true;
                if (username.equals("admin")) {
                    adminMenu(scanner);
                } else {
                    userMenu(scanner);
                }
            } else {
                System.out.println("=======================================");
                System.out.println("Login gagal. Username atau password salah. Coba lagi.");
                System.out.println("=======================================");
            }
        }
    }
    
    

    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Check-in");
            System.out.println("2. Check-out");
            System.out.println("3. Informasi User");
            System.out.println("4. Lihat Informasi Yang Di Booking Sekarang");
            System.out.println("5. Lihat History Pemesanan");
            System.out.println("6. Cari User");
            System.out.println("7. Keluar");
            System.out.print("Masukkan Pilihanmu : ");
            int userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 1:
                    loggedInUser.checkIn(scanner);
                    break;
                case 2:
                    loggedInUser.checkOut(scanner);
                    break;
                case 3:
                    loggedInUser.informasiUser();
                    break;
                case 4:
                    loggedInUser.viewBooking();
                    break;
                case 5:
                    loggedInUser.viewBookingHistory();
                    break;
                case 6:
                    searchUser(scanner);
                    break;
                case 7:
                    System.out.println("=====================");
                    System.out.println(" Anda Sudah Logout ");
                    System.out.println("=====================");
                    isLoggedIn = false;
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("=============================================");
                    System.out.println(" Pilihanmu tidak diketahui, mohon coba lagi ");
                    System.out.println("=============================================");
                    break;
            }
        }
    }

   private static void searchUser(Scanner scanner) {
    System.out.print("Masukkan Username yang ingin Anda cari: ");
    String usernameToSearch = scanner.nextLine();
    User foundUser = null;
        for (int i = 0; i < userCount; i++) {
            if (users[i] != null && users[i].getUsername().equals(usernameToSearch)) {
                foundUser = users[i];
                break;
            }
        }

    if (foundUser != null) {
        System.out.println("===================================================================================");
        System.out.println("            Informasi User yang Anda Cari");
        System.out.println("===================================================================================");
        System.out.println(" NIk           | Username          | Tanggal Lahir  | No Telephone | Alamat ");
        System.out.println("====================================================================================");

        // Format output menjadi tabel
        String nik = String.format("%-15s", foundUser.getNik());
        String username = String.format("%-19s", foundUser.getUsername());
        String tanggalLahir = String.format("%-16s", foundUser.getDateOfBirth());
        String noTelephone = String.format("%-13s", foundUser.getPhoneNumber());
        String alamat = String.format("%-12s", foundUser.getAddress());

        System.out.println(nik + " | " + username + " | " + tanggalLahir + " | " + noTelephone + " | " + alamat);
    } else {
        System.out.println("====================================");
        System.out.println("User dengan username tersebut tidak ditemukan.");
        System.out.println("====================================");
    }
        System.out.println("=============================================================================");
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
    private String[][] bookingHistory = new String[1000][4];
    private String statusPemesanan = "Tidak Dipesan"; // Status awal
    private int bookingNumber = 0; // Berikut adalah Nomor pemesanan
    private String[][] checkInInfo = new String[1000][6]; // Fungsi untuk Menyimpan informasi check-in

    public User(String nik, String username, String dateOfBirth, String phoneNumber, String address, String password) {
        this.nik = nik;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
    }
    public String[] getCheckInInfo(int index) {
        if (index >= 0 && index < bookingNumber) {
            return checkInInfo[index];
        }
        return null;
    }

    public int getBookingNumber() {
        return bookingNumber;
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

    public void checkInInfo(Scanner scanner) {
        if ("Dipesan".equals(statusPemesanan)) {
            System.out.println("=========");
            System.out.println(" Check-In Informasi ");
            System.out.println("=========");

            // Masukkan nomor, tanggal, jam, jenis gedung, dan opsi pembayaran
            System.out.print("Masukkan Nomor: ");
            checkInInfo[bookingNumber][0] = scanner.nextLine();

            System.out.print("Masukkan Tanggal: ");
            checkInInfo[bookingNumber][1] = scanner.nextLine();

            System.out.print("Masukkan Jam (HH:mm): ");
            checkInInfo[bookingNumber][2] = scanner.nextLine();

            checkInInfo[bookingNumber][3] = bookingInfo[0]; // Jenis Gedung
            checkInInfo[bookingNumber][4] = bookingInfo[1]; // Opsi Pembayaran

            System.out.println("Check-in berhasil!");
        } else {
            System.out.println("==============================");
            System.out.println("Anda belum melakukan pemesanan");
            System.out.println("==============================");
        }
    }


    public void checkIn(Scanner scanner) {
        System.out.println("=========");
        System.out.println(" Check-in ");
        System.out.println("=========");
        System.out.print("Masukkan Tanggal : ");
        String tanggalBooking = scanner.nextLine();
        System.out.print("Masukkan Jam (HH:mm): ");
        String waktuBooking = scanner.nextLine();

        System.out.println("Pilih Opsi Gedung:");
        System.out.println("1. Pernikahan");
        System.out.println("2. Olahraga");
        System.out.println("3. Rapat");
        System.out.print("Masukkan Pilihan Gedung: ");
        int memilihGedung = scanner.nextInt();
        scanner.nextLine();

        switch (memilihGedung) {
            case 1:
                bookingInfo[0] = "Pernikahan";
                break;
            case 2:
                bookingInfo[0] = "Olahraga";
                break;
            case 3:
                bookingInfo[0] = "Rapat";
                break;
            default:
                System.out.println("Pilihan Gedung tidak valid.");
                return;
        }

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
        bookingInfo[2] = tanggalBooking;
        bookingInfo[3] = waktuBooking;
        
        // Simpan username yang melakukan check-in
        bookingInfo[4] = username; // Username disimpan di indeks 4

        historyBooking(bookingInfo);

        System.out.println("Pemesanan berhasil!");
        statusPemesanan = "Dipesan";
        System.out.println("Status Pemesanan: " + statusPemesanan);
        bookingNumber++;
    }

    
    public void checkOut(Scanner scanner) {
    if ("Dipesan".equals(statusPemesanan)) {
        System.out.println("=========");
        System.out.println(" Check-out ");
        System.out.println("=========");

        if ("DP".equals(bookingInfo[1])) {
            System.out.println("Anda telah memilih pembayaran DP saat check-in. Anda harus melunasi sekarang.");
            System.out.print("1. Lunas: ");
            int paymentChoice = scanner.nextInt();
            scanner.nextLine();

            if (paymentChoice == 1) {
                // Ubah status pembayaran dari "DP" menjadi "Lunas"
                bookingInfo[1] = "Lunas";
                System.out.println("Pembayaran berhasil dilunasi.");
            } else {
                System.out.println("Pembayaran DP masih belum dilunasi.");
                return;
            }
        }

        // Menampilkan struk detail pemesanan
        System.out.println("=================================================");
        System.out.println("              Struk Detail Pemesanan");
        System.out.println("=================================================");
        System.out.println("  Tanggal Pemesanan: " + bookingInfo[2]);
        System.out.println("  Jam Pemesanan: " + bookingInfo[3]);
        System.out.println("  Jenis Gedung: " + bookingInfo[0]);
        System.out.println("  Opsi Pembayaran: " + bookingInfo[1]);
        System.out.println("=================================================");

        // Set status ke "Tidak Dipesan"
        statusPemesanan = "Tidak Dipesan";
        System.out.println("Status Pemesanan: " + statusPemesanan);

        // Kosongkan info pemesanan
        Arrays.fill(bookingInfo, null);
    } else {
        System.out.println("===============================");
        System.out.println("Anda belum melakukan check-in.");
        System.out.println("===============================");
    }
}

    public void viewBooking() {
    if (bookingInfo[2] != null && bookingInfo[3] != null && bookingInfo[0] != null && bookingInfo[1] != null) {
        System.out.println("=====================================================================================");
        System.out.println("                              Informasi Pemesanan");
        System.out.println("=====================================================================================");
        System.out.println(" No  |  Tanggal Pemesanan  |  Jam Pemesanan  |  Jenis Gedung  |  Opsi Pembayaran ");
        System.out.println("=====================================================================================");

        // Format output menjadi tabel
        String no = String.format("%3s", "");
        String tanggal = String.format("%18s", bookingInfo[2]);
        String jam = String.format("%15s", bookingInfo[3]);
        String jenisGedung = String.format("%14s", bookingInfo[0]);
        String opsiPemesanan = String.format("%16s", bookingInfo[1]);

        System.out.println(no + " | " + tanggal + " | " + jam + " | " + jenisGedung + " | " + opsiPemesanan);
    } else {
        System.out.println("==============================");
        System.out.println("Anda belum melakukan pemesanan");
        System.out.println("==============================");
    }
        System.out.println("======================================================================================");
}

    public void historyBooking (String[] bookingInfo) {
        for (int i = 0; i < bookingHistory.length; i++){
            if (bookingHistory[i][0] == null){
                bookingHistory[i] = Arrays.copyOf(bookingInfo, bookingInfo.length);
                break;
            }
        }
    }
   public void viewBookingHistory() {
    System.out.println("=========================================================================");
    System.out.println("  No  | Tanggal Pemesanan | Jam Pemesanan | Jenis Gedung | Opsi Pemesanan");
    System.out.println("=========================================================================");

    boolean hasHistory = false;  // Gunakan ini untuk memeriksa apakah ada riwayat pemesanan

    for (int i = 0; i < bookingHistory.length; i++) {
        if (bookingHistory[i][0] != null) {  // Periksa apakah ada data riwayat pada indeks i
            hasHistory = true;

            // Format output menjadi tabel
            String no = String.format("%4d", i + 1);
            String tanggal = String.format("%16s", bookingHistory[i][2]);
            String jam = String.format("%13s", bookingHistory[i][3]);
            String jenisGedung = String.format("%12s", bookingHistory[i][0]);
            String opsiPemesanan = String.format("%15s", bookingHistory[i][1]);

            System.out.println(no + " | " + tanggal + " | " + jam + " | " + jenisGedung + " | " + opsiPemesanan);
        }
    }

    if (!hasHistory) {
        System.out.println("==============================");
        System.out.println("Tidak Ada Riwayat Pemesanan Anda");
        System.out.println("==============================");
    }
    System.out.println("=========================================================================");
}

    public void informasiUser() {
    System.out.println("============================================================");
    System.out.println("                      Informasi User");
    System.out.println("============================================================");
    System.out.println("NIK           | Username          | No Telephone | Alamat ");
    System.out.println("============================================================");

    // Format output menjadi tabel
    String nikFormatted = String.format("%-15s", nik);
    String usernameFormatted = String.format("%-19s", username);
    String noTelephoneFormatted = String.format("%-13s", phoneNumber);
    String alamatFormatted = String.format("%-12s", address);

    System.out.println(nikFormatted + " | " + usernameFormatted + " | " + noTelephoneFormatted + " | " + alamatFormatted);
    System.out.println("============================================================");
}

}
