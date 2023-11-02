import java.util.Scanner;

public class coba {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);

        String[] username = {"User1", "Gabriel", "Mera", "Chiko"};
        String[] password = {"1111", "2222", "3333", "4444"};

        String inputUsername, inputPassword;
        int hasil = -1;

        System.out.println("=====Selamat Datang Di Sistem Booking Apart=====");
        System.out.println("Silahkan login terlebih dahulu");
        System.out.println("================================================");

        System.out.print("Masukkan Username: ");
        inputUsername = input.nextLine();
        System.out.print("Masukkan Password: ");
        inputPassword = input.nextLine();
        
        for (int i = 0; i < username.length; i++) {
            if(username[i].equalsIgnoreCase(inputUsername)){
                hasil = i;
                break;
            }
        }

        if (hasil == -1) {
            System.out.println("Username tidak ditemukan");
        } else {
            System.out.println("Berhasil Login");
        }

        int hargapermalam, jmlmalam, biayatambahan, totalbiaya;

        
        System.out.println("Tipe Kamar");
        System.out.println("1 : Studio (Rp.100000)");
        System.out.println("2 : Duplex (Rp.150000)");
        System.out.println("3 : Triplex (Rp.200.000)");
        System.out.print("Masukkan tipe kamar yang diinginkan: ");

        int tipekamar = input.nextInt();

        System.out.print("Masukkan jumlah malam yang diinginkan: " );
        jmlmalam = input.nextInt();

       switch (tipekamar) {
        case 1:
            totalbiaya = jmlmalam * 100000;

            System.out.print("Ingin tambahan fasilitas (1: Laundry, 2: Sarapan, 3: Tidak ada)? ");
                int tambahan1 = input.nextInt();
                
                if (tambahan1 == 1) {
                    biayatambahan = 20000 * jmlmalam; // Biaya laundry
                    totalbiaya += biayatambahan;
                    System.out.println("Biaya laundry: Rp." + biayatambahan);
                } else if (tambahan1 == 2) {
                    biayatambahan = 25000 * jmlmalam; // Biaya sarapan
                    totalbiaya += biayatambahan;
                    System.out.println("Biaya sarapan: Rp." + biayatambahan);
                } else {
                    System.out.println("Tidak ada tambahan fasilitas.");
                }

            System.out.println("Harga yang harus dibayarkan adalah " + totalbiaya);
            break;

        case 2:
            totalbiaya = jmlmalam * 150000;

            System.out.print("Ingin tambahan fasilitas (1: Laundry, 2: Sarapan, 3: Tidak ada)? ");
                int tambahan2 = input.nextInt();
                
                if (tambahan2 == 1) {
                    biayatambahan = 20000 * jmlmalam; // Biaya laundry
                    totalbiaya += biayatambahan;
                    System.out.println("Biaya laundry: Rp." + biayatambahan);
                } else if (tambahan2 == 2) {
                    biayatambahan = 25000 * jmlmalam; // Biaya sarapan
                    totalbiaya += biayatambahan;
                    System.out.println("Biaya sarapan: Rp." + biayatambahan);
                } else {
                    System.out.println("Tidak ada tambahan fasilitas.");
                }


            System.out.println("Harga yang harus dibayarkan adalah " + totalbiaya);
            break;

        case 3:
            totalbiaya = jmlmalam * 200000;

            System.out.print("Ingin tambahan fasilitas (1: Laundry, 2: Sarapan, 3: Tidak ada)? ");
                int tambahan3 = input.nextInt();
                
                if (tambahan3 == 1) {
                    biayatambahan = 20000 * jmlmalam; // Biaya laundry
                    totalbiaya += biayatambahan;
                    System.out.println("Biaya laundry: Rp." + biayatambahan);
                } else if (tambahan3 == 2) {
                    biayatambahan = 25000 * jmlmalam; // Biaya sarapan
                    totalbiaya += biayatambahan;
                    System.out.println("Biaya sarapan: Rp." + biayatambahan);
                } else {
                    System.out.println("Tidak ada tambahan fasilitas.");
                }


            System.out.println("Harga yang harus dibayarkan adalah " + totalbiaya);
            break;
       
        default:
            break;
       }
    }
}