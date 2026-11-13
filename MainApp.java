/*
 * ============================================================================
 *  FILE  : app/MainApp.java
 *  PAKET : app  (kelas utama pengeksekusi - sesuai soal Pertemuan 2)
 *  PERAN : "Otak program." MainApp yang berbicara dengan pengguna (Scanner),
 *          menyimpan semua objek Mahasiswa di ArrayList, menghitung statistik
 *          kelas, lalu menulis laporan_nilai.txt.
 *
 *  ALUR PROGRAM (4 langkah - hafalkan urutan ini untuk presentasi):
 *    1. INPUT   : baca data mahasiswa dari keyboard (Scanner).
 *    2. SIMPAN  : tiap mahasiswa jadi objek Mahasiswa, masuk ke ArrayList.
 *    3. HITUNG  : rata-rata + jumlah lulus / tidak lulus (for-each).
 *    4. LAPORAN : tulis semuanya ke laporan_nilai.txt (FileWriter), lalu
 *                 baca kembali file itu dan tampilkan di konsol sebagai bukti.
 *
 *  CARA MENJALANKAN:
 *    javac -d out src/model/*.java src/app/*.java
 *    java -ea -cp out app.MainApp
 *    (-ea = mengaktifkan assert di Mahasiswa.java; -cp out = cari class di
 *     folder out; app.MainApp = nama lengkap class + paketnya.)
 * ============================================================================
 */
package app;   // package app = kelas utama pengeksekusi (sesuai soal).

// --- IMPORT (Pertemuan 6): "meminjam" class dari library Java ---------------
// import = supaya nama class-nya bisa ditulis pendek (cukup "Scanner",
// bukan "java.util.Scanner" yang panjang) di seluruh file ini.
import model.Mahasiswa;                // Class data (paket model).
import model.NilaiInvalidException;    // Custom exception (paket model).
import java.io.BufferedReader;         // Membaca file teks baris demi baris.
import java.io.BufferedWriter;         // Menulis file teks baris demi baris.
import java.io.FileReader;             // Membuka file teks untuk DIBACA.
import java.io.FileWriter;             // Membuka/membuat file teks untuk DITULIS.
import java.io.IOException;            // Error file (gagal buat/baca/tulis).
import java.util.ArrayList;            // List dinamis (Collection, Pertemuan 5).
import java.util.InputMismatchException; // Error: input bukan angka.
import java.util.Locale;               // Aturan format angka (titik vs koma).
import java.util.Scanner;              // Membaca input dari keyboard.

// public class MainApp = "wadah" utama program.
// Nama file HARUS sama dengan nama class: MainApp.java
public class MainApp {

    // public static void main(String[] args) = TITIK AWAL program.
    // JVM selalu mencari method main() ini pertama kali saat program dijalankan.
    // String[] args = argumen dari command line (di sini tidak dipakai).
    public static void main(String[] args) {

        // ====================================================================
        //  LANGKAH 1a: SIAPKAN SCANNER (Console I/O - Pertemuan 6)
        // ====================================================================
        // new Scanner(System.in) = membuat objek pembaca dari keyboard.
        // System.in = aliran input standar, yaitu keyboard.
        Scanner scanner = new Scanner(System.in);

        // useLocale(Locale.US) = pakai TITIK (.) sebagai pemisah desimal,
        // contoh: 85.5 - bukan koma (85,5). Tanpa ini, di laptop berbahasa
        // Indonesia Scanner bisa menolak angka desimal bertitik.
        scanner.useLocale(Locale.US);

        // ====================================================================
        //  LANGKAH 1b: SIAPKAN ARRAYLIST (Collection - Pertemuan 5)
        // ====================================================================
        // ArrayList<Mahasiswa> = "list dinamis" yang isinya objek Mahasiswa.
        // <Mahasiswa> = GENERICS: "list ini HANYA boleh berisi Mahasiswa".
        //   Kalau ada yang mencoba memasukkan String, compiler menolak.
        // Bedanya dengan array biasa (Mahasiswa[]):
        //   - array ukurannya TETAP (harus tahu jumlah dari awal),
        //   - ArrayList ukurannya DINAMIS (tambah dengan add(), dia membesar
        //     sendiri - tidak perlu tahu jumlah mahasiswa di awal).
        // Metode ArrayList yang dipakai di program ini:
        //   add(mhs)  = tambah satu objek ke akhir list.
        //   size()    = jumlah objek di dalam list.
        //   get(i)    = ambil objek ke-i (tidak dipakai di sini, karena
        //               kita memakai for-each yang lebih mudah dibaca).
        ArrayList<Mahasiswa> daftarMahasiswa = new ArrayList<>();

        // ====================================================================
        //  LANGKAH 1c: MINTA JUMLAH MAHASISWA (validasi: harus angka > 0)
        // ====================================================================
        // while (true) + break = "ulangi pertanyaan sampai jawabannya valid".
        // Pola ini dipakai di SEMUA input program ini: tanya -> cek -> kalau
        // salah, beri tahu kesalahannya dan tanya ulang. Program tidak akan
        // lanjut sebelum inputnya benar.
        int jumlahMahasiswa = 0;   // int = primitif bilangan bulat.
        while (true) {
            System.out.print("Masukkan jumlah mahasiswa: ");
            try {
                // nextInt() = membaca satu bilangan bulat dari keyboard.
                jumlahMahasiswa = scanner.nextInt();
                // nextLine() di bawah ini WAJIB - ini JEBAKAN KLASIK Scanner:
                // nextInt() hanya mengambil ANGKAnya; tombol Enter (\n) yang
                // ditekan pengguna masih tertinggal di buffer. Kalau Enter
                // itu tidak dibersihkan, nextLine() nama berikutnya akan
                // membaca Enter kosong itu dan namanya jadi terlewat.
                scanner.nextLine();
                if (jumlahMahasiswa <= 0) {
                    System.out.println("Jumlah mahasiswa harus lebih dari 0. Coba lagi.");
                } else {
                    break;   // Input valid -> keluar dari while.
                }
            } catch (InputMismatchException e) {
                // Blok ini jalan kalau pengguna mengetik huruf, bukan angka.
                // e = objek error-nya; pesannya tidak perlu ditampilkan.
                System.out.println("Input harus berupa angka bulat. Coba lagi.");
                scanner.nextLine();   // Buang input yang salah dari buffer.
            }
        }

        // ====================================================================
        //  LANGKAH 2: LOOP INPUT TIAP MAHASISWA (Pertemuan 3: Looping)
        // ====================================================================
        // for (int i = 1; i <= jumlahMahasiswa; i++) artinya:
        //   mulai i dari 1; selama i <= jumlahMahasiswa, jalankan isi loop,
        //   lalu i++ (i bertambah 1) dan ulangi. Jadi loop berjalan TEPAT
        //   sebanyak jumlah mahasiswa.
        for (int i = 1; i <= jumlahMahasiswa; i++) {
            System.out.println("\n--- Mahasiswa ke-" + i + " ---");

            // --- 2a. Input NAMA (String) ---
            // nextLine() = membaca satu baris teks penuh (boleh ada spasi).
            // trim() = buang spasi ujung ("  Budi  " -> "Budi").
            // while (nama.isEmpty()) = kalau pengguna hanya menekan Enter,
            // nama kosong -> minta ulang, jangan lanjut.
            System.out.print("Nama: ");
            String nama = scanner.nextLine().trim();
            while (nama.isEmpty()) {
                System.out.print("Nama tidak boleh kosong. Nama: ");
                nama = scanner.nextLine().trim();
            }

            // --- 2b. Input NIM (String, hanya angka) ---
            // NIM dibaca sebagai STRING (bukan int), karena NIM itu identitas,
            // bukan angka untuk dihitung - lagipula NIM bisa diawali angka 0
            // ("0325016"), yang akan hilang kalau disimpan sebagai int.
            // Validasi dilakukan oleh method isNimValid() di bawah (memakai
            // Character.isDigit). Kalau tidak valid, minta ulang.
            System.out.print("NIM (angka saja): ");
            String nim = scanner.nextLine().trim();
            while (nim.isEmpty() || !isNimValid(nim)) {
                if (nim.isEmpty()) {
                    System.out.print("NIM tidak boleh kosong. NIM (angka saja): ");
                } else {
                    System.out.print("NIM hanya boleh berisi angka (0-9). NIM (angka saja): ");
                }
                nim = scanner.nextLine().trim();
            }

            // --- 2c. Input NILAI + BUAT OBJEK Mahasiswa ---
            // Nilai dibaca sebagai double (primitif desimal, misal 85.5).
            // Objek BARU dibuat di dalam try: new Mahasiswa(nama, nim, nilai).
            // Kalau nilai di luar 0-100, constructor MELEMPAR
            // NilaiInvalidException -> catch di bawah menangkapnya, menampilkan
            // pesannya (e.getMessage()), dan meminta nilai ULANG - nama dan
            // NIM yang sudah benar TIDAK perlu diketik ulang.
            Mahasiswa mhs = null;   // null = "belum menunjuk objek apa pun".
            while (true) {
                System.out.print("Nilai " + nama + " (0-100): ");
                try {
                    double nilai = scanner.nextDouble();
                    scanner.nextLine();   // Bersihkan Enter sisa (lihat 1c).
                    // new Mahasiswa(...) = memanggil CONSTRUCTOR (Mahasiswa.java).
                    // Constructor itu yang menjalankan semua validasi: assert,
                    // Character.isDigit, dan throw NilaiInvalidException.
                    mhs = new Mahasiswa(nama, nim, nilai);
                    break;   // Objek berhasil dibuat -> keluar dari while.
                } catch (InputMismatchException e) {
                    System.out.println("Input harus berupa angka. Coba lagi.");
                    scanner.nextLine();   // Buang input yang salah.
                } catch (NilaiInvalidException e) {
                    // e.getMessage() = pesan yang ditulis di constructor,
                    // contoh: "Nilai harus antara 0 dan 100. ..."
                    System.out.println("Nilai tidak valid: " + e.getMessage());
                    System.out.println("Silakan masukkan ulang nilainya.");
                }
            }

            // --- 2d. SIMPAN OBJEK KE ARRAYLIST ---
            // add(mhs) = menambahkan objek Mahasiswa ke AKHIR list.
            // Mulai sekarang data mahasiswa ini "tinggal" di daftarMahasiswa
            // dan bisa diproses nanti (dihitung, ditampilkan, ditulis ke file).
            daftarMahasiswa.add(mhs);
            // Konfirmasi singkat: grade + status langsung terlihat, diambil
            // dari method objeknya sendiri (mhs.getGrade(), mhs.getStatus()).
            System.out.println("Tersimpan: " + mhs.getNama()
                    + " | Grade: " + mhs.getGrade()
                    + " | Status: " + mhs.getStatus());
        }

        // ====================================================================
        //  LANGKAH 3: HITUNG STATISTIK KELAS (Pertemuan 3: for-each)
        // ====================================================================
        // for (Mahasiswa m : daftarMahasiswa) = FOR-EACH loop:
        //   "untuk SETIAP objek Mahasiswa m di dalam daftarMahasiswa,
        //    jalankan isi loop". Tidak perlu indeks, tidak perlu size() -
        //    loop berhenti sendiri setelah objek terakhir.
        // m di sini adalah REFERENCE: variabel m menunjuk ke objek Mahasiswa
        // yang sama dengan yang ada di list (bukan salinan). Jadi m.getNilai()
        // membaca nilai dari objek aslinya.
        double totalNilai = 0;      // Penampung jumlah semua nilai.
        int jumlahLulus = 0;        // Penghitung yang lulus.
        Mahasiswa nilaiTertinggi = null;  // Reference ke objek nilai tertinggi.
        Mahasiswa nilaiTerendah = null;   // Reference ke objek nilai terendah.
        for (Mahasiswa m : daftarMahasiswa) {
            totalNilai += m.getNilai();   // += artinya: totalNilai = totalNilai + nilai.
            if (m.isLulus()) {           // isLulus() = nilai >= 70 (lihat Mahasiswa.java).
                jumlahLulus++;            // jumlahLulus = jumlahLulus + 1
            }
            // Lacak nilai tertinggi & terendah (perbandingan reference):
            if (nilaiTertinggi == null || m.getNilai() > nilaiTertinggi.getNilai()) {
                nilaiTertinggi = m;       // m ditunjuk sebagai tertinggi sejauh ini.
            }
            if (nilaiTerendah == null || m.getNilai() < nilaiTerendah.getNilai()) {
                nilaiTerendah = m;
            }
        }
        // size() = jumlah objek di list (= jumlah mahasiswa yang diinput).
        int totalMahasiswa = daftarMahasiswa.size();
        int jumlahTidakLulus = totalMahasiswa - jumlahLulus;
        // Rata-rata = total dibagi jumlah. (double) = CASTING: mengubah int
        // menjadi double SUPAYA hasil baginya desimal. Tanpa casting,
        // pembagian dua int di Java menghasilkan int (desimalnya hilang).
        // Pengaman: kalau list kosong (tidak mungkin di alur normal, karena
        // jumlah >= 1), rata-rata dianggap 0 supaya tidak bagi dengan nol.
        double rataRata = (totalMahasiswa == 0)
                ? 0 : totalNilai / (double) totalMahasiswa;

        // --- Tampilkan rekap di konsol (for-each lagi) ---
        System.out.println("\n===== REKAPITULASI NILAI MAHASISWA =====");
        for (Mahasiswa m : daftarMahasiswa) {
            // m.toString() = satu baris rapi (formatnya diatur di Mahasiswa.java).
            // println(m) sama dengan println(m.toString()): Java otomatis
            // memanggil toString() saat objek dicetak.
            System.out.println(m);
        }
        System.out.println("-----------------------------------------");
        System.out.println("Rata-rata nilai kelas : " + String.format("%.2f", rataRata));
        System.out.println("Jumlah mahasiswa      : " + totalMahasiswa);
        System.out.println("Jumlah lulus          : " + jumlahLulus + " mahasiswa");
        System.out.println("Jumlah tidak lulus    : " + jumlahTidakLulus + " mahasiswa");
        if (nilaiTertinggi != null) {
            System.out.println("Nilai tertinggi       : " + nilaiTertinggi.getNama()
                    + " (" + nilaiTertinggi.getNilai() + ")");
            System.out.println("Nilai terendah        : " + nilaiTerendah.getNama()
                    + " (" + nilaiTerendah.getNilai() + ")");
        }

        // ====================================================================
        //  LANGKAH 4a: TULIS LAPORAN KE FILE (File I/O - Pertemuan 6)
        // ====================================================================
        // try-with-resources: tulis ( ... ) setelah kata try.
        // Artinya: objek di dalam kurung OTOMATIS ditutup (close) setelah
        // blok selesai - bahkan kalau terjadi error di tengah jalan.
        // Ini pengganti pola lama try-catch-finally (finally { writer.close(); }):
        // lebih pendek, dan tidak mungkin lupa menutup file.
        //   FileWriter("laporan_nilai.txt") = buat file baru (atau TIMPA file
        //     lama dengan nama yang sama) di folder tempat program dijalankan.
        //   BufferedWriter = pembungkus FileWriter supaya penulisan per baris
        //     lebih cepat (ditampung dulu di buffer, baru ditulis sekaligus).
        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter("laporan_nilai.txt"))) {

            // write() = menulis teks ke file (belum pindah baris).
            // newLine() = pindah ke baris baru (sama seperti menekan Enter).
            writer.write("=================================");
            writer.newLine();
            writer.write("     LAPORAN NILAI MAHASISWA     ");
            writer.newLine();
            writer.write("=================================");
            writer.newLine();

            // For-each: tulis SATU BARIS per mahasiswa, formatnya sama persis
            // dengan yang tampil di konsol (sama-sama memakai toString()).
            for (Mahasiswa m : daftarMahasiswa) {
                writer.write(m.toString());
                writer.newLine();
            }

            // Ringkasan di akhir file (statistik kelas dari Langkah 3).
            // String.format("%.2f", ...) = tampilkan 2 angka di belakang koma.
            writer.write("=================================");
            writer.newLine();
            writer.write("RINGKASAN KELAS");
            writer.newLine();
            writer.write("Rata-rata nilai kelas : " + String.format("%.2f", rataRata));
            writer.newLine();
            writer.write("Jumlah mahasiswa      : " + totalMahasiswa + " mahasiswa");
            writer.newLine();
            writer.write("Jumlah lulus          : " + jumlahLulus + " mahasiswa");
            writer.newLine();
            writer.write("Jumlah tidak lulus    : " + jumlahTidakLulus + " mahasiswa");
            writer.newLine();
            if (nilaiTertinggi != null) {
                writer.write("Nilai tertinggi       : " + nilaiTertinggi.getNama()
                        + " (" + nilaiTertinggi.getNilai() + ")");
                writer.newLine();
                writer.write("Nilai terendah        : " + nilaiTerendah.getNama()
                        + " (" + nilaiTerendah.getNilai() + ")");
                writer.newLine();
            }

            System.out.println("\nData berhasil ditulis ke file laporan_nilai.txt");

        // --- EXCEPTION HANDLING (penulisan file) ---
        // catch (IOException e) = kalau file gagal dibuat/ditulis (misalnya
        // folder tidak bisa ditulisi), program tidak crash: tampilkan pesan
        // yang ramah, lalu hentikan program dengan return (tidak lanjut
        // ke Langkah 4b, karena file yang mau dibaca saja tidak ada).
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis file: " + e.getMessage());
            scanner.close();   // Tutup Scanner sebelum keluar.
            return;            // Berhenti; tidak lanjut ke Langkah 4b.
        }

        // ====================================================================
        //  LANGKAH 4b: BACA KEMBALI FILE & TAMPILKAN (bukti file tersimpan)
        // ====================================================================
        // Polanya sama: try-with-resources supaya reader otomatis tertutup.
        //   FileReader = membuka file yang SUDAH ADA untuk dibaca.
        //   BufferedReader = pembungkusnya, menyediakan readLine().
        System.out.println("\n===== ISI FILE laporan_nilai.txt =====");
        try (BufferedReader reader =
                new BufferedReader(new FileReader("laporan_nilai.txt"))) {

            // String baris = penampung satu baris yang sedang dibaca.
            // readLine() = membaca satu baris penuh dari file.
            // Kalau sudah sampai baris terakhir, readLine() mengembalikan
            // null - itulah tanda berhenti (kondisi while menjadi false).
            // Jadi loop ini berhenti sendiri tepat di akhir file, tanpa
            // perlu tahu dulu ada berapa baris.
            String baris;
            while ((baris = reader.readLine()) != null) {
                // Yang tampil di sini BUKAN data dari memori, melainkan isi
                // FILE yang baru dibaca - bukti penulisan file berhasil.
                System.out.println(baris);
            }
            System.out.println("===== AKHIR FILE =====");

        } catch (IOException e) {
            // Jalan kalau file tidak ditemukan / tidak bisa dibaca.
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }

        // ====================================================================
        //  PENUTUP: TUTUP SCANNER
        // ====================================================================
        // close() = menutup Scanner supaya tidak ada resource yang bocor.
        // (Writer & reader TIDAK perlu ditutup manual - try-with-resources
        // sudah menutupnya otomatis di Langkah 4a dan 4b.)
        scanner.close();
        System.out.println("\nProgram selesai.");
    }

    // ========================================================================
    //  METHOD BANTUAN: isNimValid (Character - Pertemuan 4)
    // ========================================================================
    //  static = bisa dipanggil langsung dari main() tanpa membuat objek.
    //  Tugasnya: memeriksa apakah sebuah NIM hanya berisi angka.
    //    - isEmpty() dulu: NIM kosong langsung tidak valid.
    //    - Loop charAt(i) + Character.isDigit(c): SATU huruf bukan angka
    //      saja sudah cukup untuk menyatakan seluruh NIM tidak valid.
    //  Dipakai DUA kali logikanya: di sini (sebelum input nilai, supaya bisa
    //  minta ulang dengan sopan) dan di constructor Mahasiswa (sebagai
    //  pengaman terakhir - constructor tidak percaya pada pemanggilnya).
    public static boolean isNimValid(String nim) {
        if (nim == null || nim.isEmpty()) {
            return false;
        }
        for (int i = 0; i < nim.length(); i++) {
            char c = nim.charAt(i);
            if (!Character.isDigit(c)) {
                return false;   // Ketemu satu huruf bukan angka -> tidak valid.
            }
        }
        return true;   // Semua huruf angka -> valid.
    }
}
