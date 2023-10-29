public void informasiUser() {
    System.out.println("============================================================");
    System.out.println("                      Informasi User");
    System.out.println("============================================================");
    System.out.println("NIK           | Username          | Tanggal Lahir  | No Telephone | Alamat ");
    System.out.println("============================================================");

    // Format output menjadi tabel
    String nikFormatted = String.format("%-15s", nik);
    String usernameFormatted = String.format("%-19s", username);
    String tanggalLahirFormatted = String.format("%-16s", dateOfBirth);
    String noTelephoneFormatted = String.format("%-13s", phoneNumber);
    String alamatFormatted = String.format("%-12s", address);

    System.out.println(nikFormatted + " | " + usernameFormatted + " | " + tanggalLahirFormatted + " | " + noTelephoneFormatted + " | " + alamatFormatted);
    System.out.println("============================================================");
}
