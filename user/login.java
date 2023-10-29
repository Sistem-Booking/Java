private static User login(Scanner scanner) {
    System.out.println("========");
    System.out.println(" Masuk ");
    System.out.println("=======");

    while (true) {
        System.out.print("Masukkan Username : ");
        String username = scanner.nextLine();
        System.out.print("Masukkan Kata Sandi : ");
        String password = scanner.nextLine();

        User foundUser = null;
        for (int i = 0; i < userCount; i++) {
            if (users[i] != null && users[i].getUsername().equals(username)) {
                foundUser = users[i];
                break;
            }
        }

        if (foundUser != null && foundUser.getPassword().equals(password)) {
            System.out.println("==================================================");
            System.out.println(" Masuk Berhasil. Selamat Datang, " + username + "!");
            System.out.println("==================================================");
            return foundUser;
        } else {
            System.out.println("=======================================================");
            System.out.println("Login gagal. Username atau password salah. Coba lagi.");
            System.out.println("=======================================================");
            System.out.println("1. Coba lagi");
            System.out.println("2. Kembali ke menu utama");
            System.out.print("Masukkan pilihan: ");
            int loginChoice = scanner.nextInt();
            scanner.nextLine();
            if (loginChoice == 2) {
                return null;  // Kembali ke menu utama
            }
        }
    }
}
