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

    // Validasi input kosong
    if (nik.isEmpty() || username.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty() || dateOfBirth.isEmpty()) {
        System.out.println("======================================================");
        System.out.println("Tolong isi semua data yang diperlukan untuk mendaftar");
        System.out.println("======================================================");
        return;
    }

    User newUser = new User(nik, username, dateOfBirth, phoneNumber, address, password);

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
