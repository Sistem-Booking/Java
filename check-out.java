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
