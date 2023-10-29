public void viewCurrentBooking() {
    if ("Dipesan".equals(statusPemesanan)) {
        System.out.println("===================================================");
        System.out.println("        Informasi Pemesanan yang Dibooking Saat Ini");
        System.out.println("===================================================");
        System.out.println("Tanggal Pemesanan: " + bookingInfo[2]);
        System.out.println("Jam Pemesanan: " + bookingInfo[3]);
        System.out.println("Jenis Gedung: " + bookingInfo[0]);
        System.out.println("Opsi Pembayaran: " + bookingInfo[1]);
        System.out.println("Status Pemesanan: " + statusPemesanan);
    } else {
        System.out.println("========================================");
        System.out.println("Anda belum melakukan pemesanan saat ini.");
        System.out.println("========================================");
    }
}
