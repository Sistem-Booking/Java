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