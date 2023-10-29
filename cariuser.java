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
        System.out.println("NIK           | Username          | Tanggal Lahir  | No Telephone | Alamat ");
        System.out.println("===================================================================================");

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
