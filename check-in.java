public void checkIn(Scanner scanner) {
    if ("Dipesan".equals(statusPemesanan)) {
        System.out.println("Anda sudah memiliki pemesanan aktif. Silakan checkout terlebih dahulu.");
    } else {
        System.out.println("=========");
        System.out.println(" Check-in ");
        System.out.println("=========");
        System.out.print("Masukkan Tanggal Booking (dd/mm/yyyy): ");
        String tanggalBooking = scanner.nextLine();
        System.out.print("Masukkan Jam Booking (HH:mm): ");
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

        historyBooking(bookingInfo);

        System.out.println("Pemesanan berhasil!");
        statusPemesanan = "Dipesan";
        System.out.println("Status Pemesanan: " + statusPemesanan);
    }
}
