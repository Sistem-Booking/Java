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