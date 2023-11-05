    import java.util.Scanner;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.Comparator;
    import java.text.NumberFormat;
    import java.util.Locale;

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
            boolean passwordChanged = false;
            while (!passwordChanged) {
                System.out.print("Masukkan username yang akan mengganti kata sandi: ");
                String targetUsername = scanner.nextLine();
        
                User targetUser = userMap.get(targetUsername);
                if (targetUser != null) {
                    System.out.print("Masukkan kata sandi baru: ");
                    String newPassword = scanner.nextLine();
                    targetUser.changePassword(newPassword);
                    System.out.println("Kata sandi berhasil diubah.");
                    passwordChanged = true; // Setel variabel ini ke true setelah berhasil mengubah kata sandi.
                } else {
                    System.out.println("Pengguna dengan username '" + targetUsername + "' tidak ditemukan.");
                }
            }
        }
        
        private static void adminMenu(Scanner scanner) {
            while (true) {
                System.out.println("Admin Menu:");
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
                        viewCheckInInfo();
                        break;
                    case 4:
                        confirmBooking(scanner);
                        break;
                    case 5:
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
        
        private static void confirmBooking(Scanner scanner) {
            System.out.println("Konfirmasi Booking:");
            System.out.print("Masukkan username pengguna yang ingin dikonfirmasi: ");
            String usernameToConfirm = scanner.nextLine();
        
            User userToConfirm = userMap.get(usernameToConfirm);
            if (userToConfirm != null && userToConfirm.getStatusPemesanan().equals("Dipesan")) {
                // Lakukan tindakan konfirmasi di sini
                // Misalnya, mengubah status pemesanan menjadi "Dikonfirmasi"
                userToConfirm.setStatusPemesanan("Dikonfirmasi");
        
                System.out.println("Booking untuk " + usernameToConfirm + " telah dikonfirmasi.");
            } else {
                System.out.println("Pengguna tidak ditemukan atau status pemesanan tidak sesuai.");
            }
        }
        
        
        private static void viewCheckInInfo() {
            System.out.println("Informasi Check-in User:");
            System.out.println("+------------+-----------------+---------------------+-----------------+---------------+");
            System.out.println("| Username   | Status Pemesanan| Tanggal Booking     | Waktu Booking   | Pembayaran     |");
            System.out.println("+------------+-----------------+---------------------+-----------------+---------------+");
        
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
        
                String row = String.format("| %-10s | %-15s | %-19s | %-15s | %-13s |", username, statusPemesanan, tanggalBooking, waktuBooking, pembayaran);
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
            System.out.println("============");
            System.out.println(" Buat Akun ");
            System.out.println("============");
        
            String nik = "";
            String username = "";
            String dateOfBirth = "";
            String phoneNumber = "";
            String address = "";
            String password = "";
        
            while (nik.isEmpty()) {
                System.out.print("Masukkan NIK : ");
                nik = scanner.nextLine();
        
                if (nik.isEmpty()) {
                    System.out.println((char)27+"[01;31m NIK tidak boleh kosong. Silakan coba lagi." +(char)27+"[00;00m");
                }
            }
        
            while (username.isEmpty() || isUsernameTaken(username)) {
                System.out.print("Masukkan Username : ");
                username = scanner.nextLine();
        
                if (username.isEmpty()) {
                    System.out.println((char)27+"[01;31m Username tidak boleh kosong. Silakan coba lagi." +(char)27+"[00;00m");
                } else if (isUsernameTaken(username)) {
                    System.out.println((char)27+"[01;31m Username telah digunakan. Silakan pilih username lain." +(char)27+"[00;00m");
                }
            }
        
            while (dateOfBirth.isEmpty()) {
                System.out.print("Masukkan Tanggal Lahir : ");
                dateOfBirth = scanner.nextLine();
        
                if (dateOfBirth.isEmpty()) {
                    System.out.println((char)27+"[01;31m Tanggal Lahir tidak boleh kosong. Silakan coba lagi." +(char)27+"[00;00m");
                }
            }
        
            while (phoneNumber.isEmpty()) {
                System.out.print("Masukkan No Telephone : ");
                phoneNumber = scanner.nextLine();
        
                if (phoneNumber.isEmpty()) {
                    System.out.println((char)27+"[01;31m Nomor Telephone tidak boleh kosong. Silakan coba lagi." +(char)27+"[00;00m");
                }
            }
        
            while (address.isEmpty()) {
                System.out.print("Masukkan Alamat : ");
                address = scanner.nextLine();
        
                if (address.isEmpty()) {
                    System.out.println((char)27+"[01;31m Alamat tidak boleh kosong. Silakan coba lagi." +(char)27+"[00;00m");
                }
            }
            while (password.isEmpty()) {
                System.out.print("Masukkan Kata Sandi : ");
                password = scanner.nextLine();

                if (password.isEmpty()) {
                    System.out.println((char)27+"[01;31m Kata Sandi tidak boleh kosong. Silakan coba lagi." +(char)27+"[00;00m");
                }
            }
        
            User newUser = new User(nik, username, dateOfBirth, phoneNumber, address, password);
            userMap.put(username, newUser);
        
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
        
            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("===========================================");
                System.out.println((char)27+"[01;31mUsername dan password harus diisi. Coba lagi."  +(char)27+"[00;00m");
                System.out.println("===========================================");
            } else if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                System.out.println("==================================");
                System.out.println("Admin berhasil login!");
                System.out.println("==================================");
                adminMenu(scanner);
            } else {
                User foundUser = userMap.get(username);
                if (foundUser != null && foundUser.getPassword().equals(password)) {    
                    System.out.println("================================================");
                    System.out.println("  Login berhasil. Selamat Datang, " + username + "!");
                    System.out.println("================================================");
                    loggedInUser = foundUser;
                    isLoggedIn = true;
                    if (username.equals("admin")) {
                        adminMenu(scanner);
                    } else {
                        userMenu(scanner);
                    }
                } else {
                    System.out.println("=======================================================");
                    System.out.println("Login gagal. Username atau password salah. Coba lagi.");
                    System.out.println("=======================================================");
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
            User foundUser = userMap.get(usernameToSearch);
        
            if (foundUser != null) {
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
            } else {
                System.out.println("===============================================");
                System.out.println("User dengan username tersebut tidak ditemukan.");
                System.out.println("===============================================");
            }
            System.out.println("===================================================================================");
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
        private String[][] bookingHistory = new String[1000][6];
        private String statusPemesanan = "Tidak Dipesan"; // Status awal
        private int bookingNumber = 0; // Berikut adalah Nomor pemesanan
        private double price;
        private String statusPembayaran = "Belum Lunas";
        private static final double HARGA_PERNIKAHAN = 1500000.0;
        private static final double HARGA_OLAH_RAGA = 1200000.0;
        private static final double HARGA_RAPAT = 1000000.0;
        private String tanggalBooking;
        private String waktuBooking;

        public User(String nik, String username, String dateOfBirth, String phoneNumber, String address, String password) {
            this.nik = nik;
            this.username = username;
            this.dateOfBirth = dateOfBirth;
            this.phoneNumber = phoneNumber;
            this.address = address;
            this.password = password;
        }

        public void setStatusPemesanan(String string) {
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
        
        public String getStatusPemesanan() {
            return statusPemesanan;
        }

        public String getTanggalBooking(){
            return tanggalBooking;
        }
        public String getWaktuBooking(){
            return waktuBooking;
        }

        public String[] getBookingInfo(){
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

        public void checkIn(Scanner scanner) {
            System.out.println("=========");
            System.out.println(" Check-in ");
            System.out.println("=========");
            System.out.print("Masukkan Tanggal : ");
            String tanggalBooking = scanner.nextLine();
            System.out.print("Masukkan Jam (HH:mm): ");
            String waktuBooking = scanner.nextLine();

            // Inisialisasi bookingInfo
            bookingInfo = new String[5];

            // Periksa apakah waktu yang diminta sudah dipesan oleh pengguna lain
            for (int i = 0; i < bookingNumber; i++) {
                if (bookingInfo[2] != null && bookingInfo[2].equals(tanggalBooking) && bookingInfo[3] != null && bookingInfo[3].equals(waktuBooking)) {
                    System.out.println((char)27+"[01;31m Gedung pada jam tersebut sudah dipesan oleh Pengguna lain, Silakan coba memesan di jam berikutnya." +(char)27+"[00;00m");
                    return;
                }
            }

            System.out.println("Pilih Opsi Gedung:");
            System.out.println("1. Pernikahan - Rp " + formatToRupiah(HARGA_PERNIKAHAN));
            System.out.println("2. Olahraga - Rp " + formatToRupiah(HARGA_OLAH_RAGA));
            System.out.println("3. Rapat - Rp " + formatToRupiah(HARGA_RAPAT));
            System.out.print("Masukkan Pilihan Gedung: ");
            int memilihGedung = scanner.nextInt();
            scanner.nextLine();

            switch (memilihGedung) {
                case 1:
                    bookingInfo[0] = "Pernikahan";
                    price = HARGA_PERNIKAHAN;
                    break;
                case 2:
                    bookingInfo[0] = "Olahraga";
                    price = HARGA_OLAH_RAGA;
                    break;
                case 3:
                    bookingInfo[0] = "Rapat";
                    price = HARGA_RAPAT;
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
                        statusPembayaran = "Lunas";
                        bookingInfo[1] = "Lunas"; // Juga perlu memperbarui status pembayaran di informasi pemesanan
                        System.out.println("Pembayaran berhasil dilunasi.");
                    } else {
                        System.out.println("Pembayaran DP masih belum dilunasi.");
                        return;
                    }
                } else if ("Lunas".equals(bookingInfo[1])) {
                    System.out.println("Anda telah melunasi pembayaran sebelumnya.");
                } else {
                    System.out.println("Pilihan pembayaran tidak valid.");
                    return;
                }

                // Menampilkan struk detail pemesanan
                System.out.println("=================================================");
                System.out.println("              Struk Detail Pemesanan");
                System.out.println("=================================================");
                System.out.println("  Tanggal Pemesanan: " + bookingInfo[2]);
                System.out.println("  Jam Pemesanan: " + bookingInfo[3]);
                System.out.println("  Jenis Gedung: " + bookingInfo[0]);
                System.out.println("  Harga Gedung: " + getHargaGedung(bookingInfo[0])); // Menampilkan harga gedung
                System.out.println("  Opsi Pembayaran: " + bookingInfo[1]);
                System.out.println("  Status Pembayaran: " + statusPembayaran); // Tampilkan status pembayaran
                System.out.println("=================================================");

                // Set status ke "Tidak Dipesan"
                statusPemesanan = "Tidak Dipesan";
                System.out.println("Status Pemesanan: " + statusPemesanan);

                // Kosongkan info pemesanan
                Arrays.fill(bookingInfo, null);
            } else {
                System.out.println("==============================");
                System.out.println("Anda belum melakukan check-in.");
                System.out.println("==============================");
            }
        }

        // Metode untuk mendapatkan harga gedung berdasarkan jenis gedung yang dipilih
        private double getHargaGedung(String jenisGedung) {
            switch (jenisGedung.toLowerCase()) {
                case "pernikahan":
                    return HARGA_PERNIKAHAN; // Assuming HARGA_PERNIKAHAN is a double
                case "olahraga":
                    return HARGA_OLAH_RAGA; // Assuming HARGA_OLAH_RAGA is a double
                case "rapat":
                    return HARGA_RAPAT; // Assuming HARGA_RAPAT is a double
                default:
                    return 0; // Or set a default numeric value if the jenisGedung is not recognized
            }
        }

        public void viewBooking() {
        if (bookingInfo[2] != null && bookingInfo[3] != null && bookingInfo[0] != null && bookingInfo[1] != null) {
            System.out.println("==================================================================================================");
            System.out.println("                              Informasi Pemesanan");
            System.out.println("==================================================================================================");
            System.out.println(" No  |  Tanggal Pemesanan  |  Jam Pemesanan  |  Jenis Gedung  |  Opsi Pembayaran | Harga Gedung");
            System.out.println("==================================================================================================");

            // Format output menjadi tabel
            String no = String.format("%3s", "");
            String tanggal = String.format("%18s", bookingInfo[2]);
            String jam = String.format("%15s", bookingInfo[3]);
            String jenisGedung = String.format("%14s", bookingInfo[0]);
            String opsiPemesanan = String.format("%16s", bookingInfo[1]);
            String hargaGedung = String.format("Rp %.2f", price);

            System.out.println(no + " | " + tanggal + " | " + jam + " | " + jenisGedung + " | " + opsiPemesanan + " | " + hargaGedung);
        } else {
            System.out.println("==============================");
            System.out.println("Anda belum melakukan pemesanan");
            System.out.println("==============================");
        }
            System.out.println("===================================================================================================");
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

                // Set status pembayaran (indeks 4) di dalam history menjadi status pembayaran saat ini
                bookingHistory[i][5] = statusPembayaran;

                break;
            }
        }
    }

    public void viewBookingHistory() {
        System.out.println("================================================================================================================");
        System.out.println("  No  | Tanggal Pemesanan | Jam Pemesanan | Jenis Gedung | Opsi Pemesanan | Harga Gedung | Status Pembayaran");
        System.out.println("================================================================================================================");

        boolean hasHistory = false;

        for (int i = 0; i < bookingHistory.length; i++) {
            if (bookingHistory[i][0] != null && "DP".equals(bookingHistory[i][5])) {
                hasHistory = true;

                String no = String.format("%4d", i + 1);
                String tanggal = String.format("%16s", bookingHistory[i][2]);
                String jam = String.format("%13s", bookingHistory[i][3]);
                String jenisGedung = String.format("%12s", bookingHistory[i][0]);
                String opsiPemesanan = String.format("%15s", bookingHistory[i][1]);
                String hargaGedung = String.format("Rp %.2f", Double.parseDouble(bookingHistory[i][4]));
                String statusPembayaran = bookingHistory[7][i]; // Ambil status pembayaran dari booking history

                System.out.println(no + " | " + tanggal + " | " + jam + " | " + jenisGedung + " | " + opsiPemesanan + " | " + hargaGedung + " | " + statusPembayaran);
            }
        }

        if (!hasHistory) {
            System.out.println("=================================");
            System.out.println("Tidak Ada Riwayat Pemesanan Anda");
            System.out.println("=================================");
        }
        System.out.println("================================================================================================================");
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

        public void viewBookingInfo() {
            for (int i = 0; i < bookingHistory.length; i++) {
                if (bookingHistory[i][0] != null) {
                    System.out.println("=======================================================================================================");
                    System.out.println(" No  | Tanggal Pemesanan | Jam Pemesanan | Jenis Gedung | Opsi Pembayaran | Harga Gedung | Status");
                    System.out.println("=========================================================================================================");
                    String no = String.format("%4d", i + 1);
                    String tanggal = String.format("%16s", bookingHistory[i][2]);
                    String jam = String.format("%13s", bookingHistory[i][3]);
                    String jenisGedung = String.format("%12s", bookingHistory[i][0]);
                    String opsiPemesanan = String.format("%15s", bookingHistory[i][1]);
                    String hargaGedung = String.format("Rp %.2f", Double.parseDouble(bookingHistory[i][4]));
                    String status = "Belum Dikonfirmasi";

                    if ("Lunas".equals(bookingHistory[i][5])) {
                        status = "Sudah Dikonfirmasi";
                    }

                    System.out.println(no + " | " + tanggal + " | " + jam + " | " + jenisGedung + " | " + opsiPemesanan + " | " + hargaGedung + " | " + status);
                }
            }
        }
}