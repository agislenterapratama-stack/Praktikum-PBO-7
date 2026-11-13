/*
 * ============================================================================
 *  FILE  : model/Mahasiswa.java
 *  PAKET : model  (menampung kelas data - sesuai soal Pertemuan 2)
 *  PERAN : "Satu objek Mahasiswa = satu mahasiswa."
 *          Class ini menyimpan data (nama, NIM, nilai) DAN semua aturan
 *          tentang satu mahasiswa: nilai harus 0-100, NIM harus angka,
 *          grade A-D, dan lulus kalau nilai >= 70.
 *
 *  CARA MENJELASKANNYA SAAT PRESENTASI (30 detik):
 *  "Ini class data-nya. Semua aturan tentang SATU mahasiswa tinggal di
 *   sini, jadi MainApp tidak perlu tahu rumusnya - dia tinggal memanggil
 *   getGrade() atau isLulus(). Kalau aturan berubah, yang diubah hanya
 *   file ini."
 * ============================================================================
 */
package model;   // package = "folder" logis; file ini tinggal di folder src/model/

/**
 * Class data untuk satu mahasiswa.
 * Kata kunci class = cetak biru (blueprint); objek = hasil cetakan dari
 * blueprint itu. Setiap objek Mahasiswa punya salinan nama, NIM, dan
 * nilainya sendiri-sendiri.
 */
public class Mahasiswa {

    // ========================================================================
    //  1. ATRIBUT (data yang disimpan di tiap objek)
    // ========================================================================
    //  - private   = ENKAPSULASI: atribut hanya boleh diubah dari DALAM
    //    class ini. Class lain tidak bisa menulis langsung, misalnya
    //    mhs.nilai = 500;  <-- DITOLAK compiler. Mau membaca? Lewat getter.
    //  - String    = REFERENCE type: variabelnya menyimpan ALAMAT objek teks
    //    di memori (bukan teksnya langsung).
    //  - double    = PRIMITIVE type: variabelnya menyimpan LANGSUNG angkanya.
    //    Jadi: nama & NIM = reference, nilai = primitif.
    private String nama;    // Nama mahasiswa, disimpan huruf kapital semua.
    private String nim;     // NIM mahasiswa, hanya boleh berisi angka.
    private double nilai;   // Nilai akhir, 0 sampai 100.

    // ========================================================================
    //  2. KONSTANTA STANDAR KELULUSAN (Pertemuan 2)
    // ========================================================================
    //  - public = boleh dibaca dari class lain, misal MainApp.
    //  - static = milik CLASS, bukan milik tiap objek. Hanya ada SATU salinan
    //    untuk semua objek Mahasiswa (hemat memori, dan nilainya konsisten).
    //  - final  = nilainya TETAP, tidak bisa diubah setelah baris ini.
    //    Karena itu ambang lulus cukup ditulis di SATU tempat: di sini.
    //    Kalau kampus mengubah ambang jadi 75, ubah angka ini saja.
    //  - Nama konstanta ditulis KAPITAL SEMUA - itu konvensi (kebiasaan) Java.
    //  - Nama FINAL_PASSING_GRADE persis seperti yang diminta soal.
    public static final double FINAL_PASSING_GRADE = 70.0;

    // ========================================================================
    //  3. CONSTRUCTOR (Pertemuan 2)
    // ========================================================================
    //  Constructor = method KHUSUS yang jalan OTOMATIS saat objek dibuat
    //  dengan kata kunci new, contoh: new Mahasiswa("Budi", "0325016", 92).
    //  - Namanya HARUS sama persis dengan nama class (Mahasiswa).
    //  - Tidak punya return type (tidak ada void / String / dsb).
    //  - Tugasnya: mengisi atribut objek yang baru dibuat.
    //  - "throws NilaiInvalidException" = constructor ini BOLEH melempar
    //    exception itu; pemanggilnya WAJIB menangkap dengan try-catch.
    public Mahasiswa(String nama, String nim, double nilai)
            throws NilaiInvalidException {

        // --- 3a. RAPIKAN INPUT: String manipulation (Pertemuan 4) -----------
        //  trim()       = buang spasi di ujung kiri-kanan.
        //                 "  Budi  ".trim()  ->  "Budi"
        //  toUpperCase()= ubah semua huruf jadi kapital.
        //                 "budi".toUpperCase()  ->  "BUDI"
        //  Tujuannya: data yang tersimpan selalu dalam bentuk yang sama,
        //  tidak peduli pengguna mengetik "budi", "Budi", atau "  BUDI  ".
        //  this.nama = "atribut nama MILIK OBJEK INI" (this = objek ini).
        //  Tanpa this, "nama = nama" akan mengisi parameter dengan dirinya
        //  sendiri - atributnya tetap kosong. Itulah gunanya this.
        this.nama = nama.trim().toUpperCase();
        this.nim = nim.trim().toUpperCase();

        // --- 3b. ASSERTION (Pertemuan 4) - baris ini persis seperti soal ---
        //  assert = "saya YAKIN kondisi ini benar; kalau ternyata salah,
        //  berhentikan program dan tampilkan pesan di belakang titik dua".
        //  assert !nim.isEmpty() : "NIM tidak boleh kosong!";
        //  Artinya: "NIM tidak boleh string kosong - kalau kosong, matikan
        //  program dengan pesan ini".
        //  PENTING: assert HANYA AKTIF kalau program dijalankan dengan flag
        //  -ea (enable assertions), contoh: java -ea -cp out app.MainApp
        //  Tanpa -ea, JVM melewati baris ini begitu saja. Karena itu NIM
        //  kosong JUGA dicek di MainApp sebelum sampai ke sini: assert ini
        //  adalah pengaman lapis kedua, khusus saat development/testing.
        assert !this.nim.isEmpty() : "NIM tidak boleh kosong!";

        // --- 3c. VALIDASI NIM: hanya boleh berisi angka (Pertemuan 4) -------
        //  charAt(i)            = mengambil SATU huruf (char) ke-i dari String.
        //                         NIM "0325016", charAt(0) = '0'.
        //  Character.isDigit(c) = true kalau c adalah angka '0' sampai '9'.
        //  Loop ini memeriksa NIM huruf demi huruf; SATU saja bukan angka,
        //  langsung tolak. Ini validasi lapis kedua: MainApp sudah memeriksa
        //  hal yang sama sebelum memanggil constructor - constructor TIDAK
        //  PERCAYA begitu saja pada pemanggilnya (defense in depth).
        for (int i = 0; i < this.nim.length(); i++) {
            char c = this.nim.charAt(i);       // Ambil huruf ke-i dari NIM.
            if (!Character.isDigit(c)) {       // ! = "bukan" / "tidak".
                // throw = LEMPAR error sekarang juga; baris di bawahnya
                // tidak akan dijalankan, objek GAGAL dibuat.
                throw new IllegalArgumentException(
                        "NIM hanya boleh berisi angka (0-9). "
                        + "Ditemukan karakter: '" + c + "'");
            }
        }

        // --- 3d. VALIDASI NILAI dengan CUSTOM EXCEPTION (Pertemuan 4) -------
        //  if (nilai < 0 || nilai > 100)  ->  || artinya "ATAU".
        //  Jadi kondisi ini benar kalau nilai di bawah 0 ATAU di atas 100.
        //  Kalau benar: lempar NilaiInvalidException (class buatan sendiri,
        //  lihat file NilaiInvalidException.java). Pesan di dalamnya akan
        //  dibaca MainApp lewat e.getMessage() dan ditampilkan ke pengguna.
        if (nilai < 0 || nilai > 100) {
            throw new NilaiInvalidException(
                    "Nilai harus antara 0 dan 100. Nilai yang dimasukkan: " + nilai);
        }
        this.nilai = nilai;   // Lolos semua validasi -> baru simpan nilainya.
    }

    // ========================================================================
    //  4. GETTER (cara class lain MEMBACA atribut private)
    // ========================================================================
    //  Getter = method "get..." yang mengembalikan nilai satu atribut.
    //  Karena atributnya private, inilah SATU-SATUNYA jalan membaca data
    //  dari luar class. Tidak ada setter di sini (tidak ada setNama dsb):
    //  data mahasiswa sengaja dibuat TIDAK BISA diubah setelah objek jadi
    //  (immutable) - kalau ada yang salah, buat objek baru yang benar.
    public String getNama() {
        return nama;
    }

    public String getNim() {
        return nim;
    }

    public double getNilai() {
        return nilai;
    }

    // ========================================================================
    //  5. GRADE HURUF (Pertemuan 3: If-Else)
    // ========================================================================
    //  If-else BERANTAI: dicek dari atas ke bawah, cabang PERTAMA yang
    //  kondisinya benar langsung menang dan method selesai (return).
    //  Contoh: nilai 92 -> 92 >= 85 benar -> return "A", berhenti.
    //          nilai 78 -> 78 >= 85 salah, 78 >= 70 benar -> return "B".
    //          nilai 62 -> lolos ke 62 >= 55 -> return "C".
    //          nilai 45 -> semua salah -> else terakhir -> return "D".
    //  Kenapa if-else, bukan switch-case? Karena switch hanya bisa
    //  membandingkan nilai yang PERSIS sama (==), sedangkan grade memakai
    //  RENTANG (>= 85, >= 70, ...). Untuk rentang, if-else yang tepat.
    public String getGrade() {
        if (nilai >= 85) {          // 85 - 100
            return "A";
        } else if (nilai >= 70) {   // 70 - 84
            return "B";
        } else if (nilai >= 55) {   // 55 - 69
            return "C";
        } else {                    // di bawah 55
            return "D";
        }
    }

    // ========================================================================
    //  6. STATUS KELULUSAN (Pertemuan 3: kondisional + konstanta)
    // ========================================================================
    //  boolean = tipe primitif yang nilainya hanya true atau false.
    //  Method ini mengembalikan (return) HASIL perbandingan:
    //  nilai >= FINAL_PASSING_GRADE  ->  true kalau nilai >= 70.
    //  Karena memakai konstanta FINAL_PASSING_GRADE (bukan angka 70 yang
    //  ditulis langsung), ambang lulus tetap konsisten di seluruh program.
    public boolean isLulus() {
        return nilai >= FINAL_PASSING_GRADE;
    }

    //  getStatus() memakai OPERATOR TERNARY - if-else versi satu baris:
    //  kondisi ? nilaiJikaBenar : nilaiJikaSalah
    //  Jadi: kalau isLulus() true -> "Lulus", kalau false -> "Tidak Lulus".
    public String getStatus() {
        return isLulus() ? "Lulus" : "Tidak Lulus";
    }

    // ========================================================================
    //  7. toString() - tampilan satu baris untuk konsol & file laporan
    // ========================================================================
    //  @Override = "method ini MENULIS ULANG method toString() bawaan Java".
    //  Tanpa override, mencetak objek akan menampilkan alamat memori yang
    //  tidak bisa dibaca (misal model.Mahasiswa@6d06d69c).
    //  String.format = menyusun teks dengan format rapi:
    //    %-15s  = String (nama) rata kiri, lebar kolom 15 karakter.
    //    %s     = String biasa (NIM).
    //    %6.2f  = angka desimal, lebar 6, DUA angka di belakang koma (92.00).
    //  Method ini dipakai DUA tempat: tampil di konsol DAN tulis ke file,
    //  jadi tampilannya selalu sama persis.
    @Override
    public String toString() {
        return String.format("%-15s | NIM: %s | Nilai: %6.2f | Grade: %s | Status: %s",
                nama, nim, nilai, getGrade(), getStatus());
    }
}
