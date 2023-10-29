public void historyBooking(String[] bookingInfo) {
    for (int i = 0; i < bookingHistory.length; i++) {
        if (bookingHistory[i][0] == null) {
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
