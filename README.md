# Pengelola Data Nilai Mahasiswa — Program Java OOP

> **Satu kalimat untuk pembuka presentasi:** program ini membaca data nilai mahasiswa dari keyboard, menyimpannya sebagai objek di `ArrayList`, menghitung rata-rata dan jumlah lulus, lalu menulis rekapnya ke `laporan_nilai.txt` — dan membuktikan file itu tersimpan dengan membacanya kembali.

| | |
|---|---|
| **Nama** | Agis Lentera Pratama |
| **NIM** | L0325016 |
| **Program Studi** | Informatika, Fakultas Teknologi Informasi dan Sains Data, Universitas Sebelas Maret |
| **Mata kuliah** | Praktikum Pemrograman Berorientasi Objek — Pengganti Pertemuan 7 |
| **Bahasa** | Java (OOP: package, class, constructor, custom exception, assertion, collection, file I/O) |
| **Aturan kelulusan** | Nilai ≥ 70 (`FINAL_PASSING_GRADE`) → **Lulus** |
| **Grade** | A = 85–100, B = 70–84, C = 55–69, D = di bawah 55 |
| **Status** | Selesai dan terverifikasi: compile bersih, output sesuai perhitungan manual (lihat [§6](#6-contoh-input--output-terverifikasi)) |

## Daftar Isi

- [1. Tujuan Program](#1-tujuan-program)
- [2. Alur Program (untuk dijelaskan di presentasi)](#2-alur-program-untuk-dijelaskan-di-presentasi)
- [3. Struktur Project](#3-struktur-project)
- [4. Pemetaan Soal ke Kode (checklist Pertemuan 2–6)](#4-pemetaan-soal-ke-kode-checklist-pertemuan-2-6)
- [5. Penjelasan Kode per File](#5-penjelasan-kode-per-file)
- [6. Contoh Input & Output (terverifikasi)](#6-contoh-input--output-terverifikasi)
- [7. Tabel Pengujian](#7-tabel-pengujian)
- [8. Pertanyaan yang Sering Ditanyakan Saat Presentasi](#8-pertanyaan-yang-sering-ditanyakan-saat-presentasi)
- [9. Cara Menjalankan](#9-cara-menjalankan)
- [10. Keterbatasan & Pengembangan Lanjutan](#10-keterbatasan--pengembangan-lanjutan)

## 1. Tujuan Program

1. Membaca data mahasiswa (nama + NIM + nilai) dari keyboard dengan `Scanner`.
2. Menyimpan tiap mahasiswa sebagai **objek** `Mahasiswa` di dalam `ArrayList<Mahasiswa>`.
3. Memvalidasi data dengan **assertion** (NIM tidak boleh kosong) dan **custom exception** `NilaiInvalidException` (nilai harus 0–100), serta `Character.isDigit` (NIM hanya angka).
4. Menentukan **grade huruf (A–D)** dan **status kelulusan** untuk tiap mahasiswa.
5. Menghitung statistik kelas: rata-rata, jumlah lulus / tidak lulus, nilai tertinggi & terendah.
6. Menulis seluruh rekap ke `laporan_nilai.txt` (`FileWriter` + `BufferedWriter`, `try-with-resources`), lalu membacanya kembali sebagai bukti.

## 2. Alur Program (untuk dijelaskan di presentasi)

```
Keyboard (Scanner)
      │
      ▼
┌──────────────────────────────────────────┐
│ 1. Minta jumlah mahasiswa (n)            │  ← divalidasi: angka bulat > 0
│ 2. Loop i = 1 … n:                       │
│    • input nama  → trim, tidak boleh     │
│      kosong                              │
│    • input NIM   → trim + toUpperCase,   │
│      hanya angka (Character.isDigit)     │
│    • input nilai → new Mahasiswa(...)    │
│      constructor memvalidasi: assert     │
│      NIM tidak kosong, nilai 0–100      │
│      (kalau salah → NilaiInvalidException│
│       → minta nilai ulang)               │
│    • daftarMahasiswa.add(mhs)            │  ← ArrayList
│ 3. Hitung statistik (for-each):          │
│    total, rata-rata, jumlah lulus,       │
│    nilai tertinggi & terendah            │
└──────────────────────────────────────────┘
      │  FileWriter + BufferedWriter (try-with-resources)
      ▼
  laporan_nilai.txt  ──▶  FileReader + BufferedReader
      │                       │
      │                       ▼
      │               readLine() sampai null
      │                       │
      └──────────▶  tampilkan isi file di konsol
```

**Kalimat pembuka presentasi (30 detik):**
> "Program ini mengelola nilai mahasiswa dalam empat langkah. Pertama, data dibaca dari keyboard dengan `Scanner`. Kedua, tiap mahasiswa dijadikan objek `Mahasiswa` dan disimpan di `ArrayList` — di sinilah constructor memvalidasi NIM dan nilai. Ketiga, statistik kelas dihitung dengan for-each: rata-rata dan jumlah lulus. Keempat, semuanya ditulis ke `laporan_nilai.txt` memakai `FileWriter` dan `BufferedWriter`, lalu file itu dibaca kembali — jadi yang tampil di konsol terakhir adalah isi file yang sebenarnya, bukan data dari memori."

**Kalimat penutup presentasi (15 detik):**
> "Jadi pembagian kerjanya jelas: `Mahasiswa` mengurus aturan tentang satu mahasiswa, `NilaiInvalidException` memberi nama pada satu jenis kesalahan, dan `MainApp` hanya mengurus alur — input, simpan, hitung, tulis file. Kalau aturan nilai berubah, yang diubah hanya satu file: `Mahasiswa.java`."

## 3. Struktur Project

```
nilai-mahasiswa-java/
├── README.md                  ← file ini (penjelasan siap presentasi)
├── CARA_MENJALANKAN.txt       ← langkah menjalankan di NetBeans & terminal
├── Hasil_Run.txt              ← output lengkap hasil run yang terverifikasi (4 mahasiswa)
├── Hasil_Run_Demo_Validasi.txt ← output demo semua jalur validasi (input salah)
├── Hasil_Uji_Assert.txt       ← output uji assert (-ea vs tanpa -ea) + batas grade
├── laporan_nilai.txt          ← file laporan hasil run terakhir (contoh)
└── src/
    ├── model/
    │   ├── Mahasiswa.java            ← class data: nama, NIM, nilai + grade + lulus
    │   └── NilaiInvalidException.java ← custom exception untuk nilai 0–100
    └── app/
        └── MainApp.java              ← kelas utama: input, ArrayList, statistik, file I/O
```

Tidak ada library luar — hanya library standar Java (`java.util`, `java.io`).

## 4. Pemetaan Soal ke Kode (checklist Pertemuan 2–6)

| Materi (soal) | Implementasi | File |
|---|---|---|
| **Package** `model` & `app` (P2) | `package model;` untuk kelas data, `package app;` untuk kelas utama | Semua file |
| **Class `Mahasiswa`**: nama, NIM, nilai (P2) | Atribut `private String nama, nim; private double nilai;` | `model/Mahasiswa.java` |
| **Constructor** (P2) | `public Mahasiswa(String nama, String nim, double nilai) throws NilaiInvalidException` | `model/Mahasiswa.java` |
| **Konstanta** `FINAL_PASSING_GRADE = 70.0` (P2) | `public static final double FINAL_PASSING_GRADE = 70.0;` | `model/Mahasiswa.java` |
| **Grade A–D** (P3) | `getGrade()`: if-else berantai (A ≥ 85, B ≥ 70, C ≥ 55, D < 55) | `model/Mahasiswa.java` |
| **Status kelulusan** (P3) | `isLulus()` (`nilai >= FINAL_PASSING_GRADE`) + `getStatus()` (ternary) | `model/Mahasiswa.java` |
| **Looping + rata-rata** (P3) | `for` untuk input, `for-each` untuk total & statistik | `app/MainApp.java` |
| **Primitif vs reference** (P3) | `double`/`int`/`boolean` = primitif; `String`/`Mahasiswa` = reference (dijelaskan di komentar) | `model/Mahasiswa.java`, `app/MainApp.java` |
| **Custom exception** (P4) | `NilaiInvalidException extends Exception`, dilempar (`throw`) jika nilai < 0 atau > 100 | `model/NilaiInvalidException.java`, `model/Mahasiswa.java` |
| **Assertion** (P4) | `assert !this.nim.isEmpty() : "NIM tidak boleh kosong!";` — persis seperti soal; aktif dengan flag `-ea` | `model/Mahasiswa.java` |
| **String: `trim()`, `toUpperCase()`** (P4) | Nama & NIM dirapikan di constructor | `model/Mahasiswa.java` |
| **`Character.isDigit()`** (P4) | Validasi NIM hanya angka, di constructor dan di `isNimValid()` | `model/Mahasiswa.java`, `app/MainApp.java` |
| **`ArrayList<Mahasiswa>`** (P5) | `add()`, `size()`, for-each | `app/MainApp.java` |
| **Console I/O: `Scanner`** (P6) | Input jumlah, nama, NIM, nilai | `app/MainApp.java` |
| **File I/O: `BufferedWriter` & `FileWriter`** (P6) | Tulis rekap + statistik ke `laporan_nilai.txt` | `app/MainApp.java` |
| **`Try-Catch-Finally` / `Try-with-Resources`** (P6) | `try-with-resources` (pengganti `finally` untuk menutup file) + `catch (IOException)` | `app/MainApp.java` |

## 5. Penjelasan Kode per File

### 5.1 `model/Mahasiswa.java` — "aturan tentang SATU mahasiswa"

Satu objek = satu mahasiswa. File ini berisi:

- **Atribut `private`** (enkapsulasi): `nama`, `nim` (`String` = reference), `nilai` (`double` = primitif). Class lain tidak bisa mengubahnya langsung — hanya lewat constructor dan getter.
- **Konstanta** `FINAL_PASSING_GRADE = 70.0` (`public static final`): ambang lulus hanya ditulis di satu tempat.
- **Constructor** yang melakukan 4 hal berurutan: (a) rapikan input (`trim()`, `toUpperCase()`); (b) `assert` NIM tidak kosong; (c) cek NIM hanya angka (`Character.isDigit` huruf demi huruf); (d) cek nilai 0–100, kalau tidak → `throw new NilaiInvalidException(...)`.
- **`getGrade()`**: if-else berantai. Dipakai if-else (bukan switch) karena grade memakai **rentang** (`>= 85`, `>= 70`), sedangkan switch hanya bisa membandingkan nilai yang persis sama.
- **`isLulus()` / `getStatus()`**: `nilai >= FINAL_PASSING_GRADE` → `"Lulus"` / `"Tidak Lulus"` (ternary).
- **`toString()`** (`@Override`): satu baris rapi (`String.format`) yang dipakai di konsol DAN di file — jadi tampilannya selalu sama persis.

### 5.2 `model/NilaiInvalidException.java` — "nama untuk satu jenis kesalahan"

```java
public class NilaiInvalidException extends Exception {
    public NilaiInvalidException(String pesan) {
        super(pesan);
    }
}
```

- `extends Exception` → exception ini **CHECKED**: compiler memaksa pemanggil untuk menangkapnya (`try-catch`) atau meneruskannya (`throws`). Kalau `MainApp` lupa menulis `catch`, program gagal dikompilasi. Itulah gunanya: nilai tidak valid tidak mungkin lolos tanpa ditangani.
- Bedanya dengan `IllegalArgumentException` (dipakai untuk NIM berisi huruf): yang itu **UNCHECKED** (bawaan Java, compiler tidak memaksa). Program ini sengaja memakai keduanya supaya perbedaannya terlihat.

### 5.3 `app/MainApp.java` — "otak program: input → simpan → hitung → tulis file"

| Bagian | Isi | Yang perlu dihafal |
|---|---|---|
| Import | `model.Mahasiswa`, `model.NilaiInvalidException`, `java.io.*`, `java.util.*` | `import` = meminjam class supaya namanya bisa ditulis pendek |
| `main()` | Titik awal program; semua langkah dipanggil dari sini berurutan | JVM selalu mencari `main()` pertama kali |
| Langkah 1 | `Scanner` + `Locale.US` (desimal bertitik `85.5`), `ArrayList<Mahasiswa>`, minta jumlah mahasiswa | `while (true)` + `break`: ulangi sampai input valid |
| Langkah 2 | Loop `for` input nama → NIM → nilai; `new Mahasiswa(...)` di dalam `try`; `daftarMahasiswa.add(mhs)` | Objek dibuat di `try` supaya `NilaiInvalidException` bisa ditangkap dan nilai diminta ulang tanpa mengulang nama/NIM |
| Langkah 3 | For-each: total, rata-rata (`(double)` casting supaya hasilnya desimal), jumlah lulus, tertinggi/terendah | `for (Mahasiswa m : daftarMahasiswa)` = "untuk setiap objek m di list" |
| Langkah 4a | `try-with-resources` + `BufferedWriter`/`FileWriter` → `laporan_nilai.txt` | Resource otomatis `close()` — pengganti `finally` |
| Langkah 4b | `try-with-resources` + `BufferedReader`/`FileReader`, `readLine()` sampai `null` | Yang tampil di konsol adalah **isi file**, bukti penulisan berhasil |
| Bantuan | `isNimValid(String)` — loop `charAt(i)` + `Character.isDigit(c)` | Validasi NIM lapis pertama; constructor memeriksa ulang sebagai lapis kedua |

> **Jebakan klasik yang wajib dijelaskan saat presentasi:** setelah `nextInt()` / `nextDouble()`, tombol Enter (`\n`) masih tertinggal di buffer. Karena itu selalu ada `scanner.nextLine()` sesudahnya untuk "membersihkan" Enter itu. Kalau baris ini dihapus, `nextLine()` nama berikutnya akan membaca Enter kosong dan namanya jadi terlewat. Ini bug paling umum di program `Scanner` — dan paling sering ditanyakan.

## 6. Contoh Input & Output (terverifikasi)

**Input yang diketik (4 mahasiswa — sengaja satu untuk tiap grade):**

```
Masukkan jumlah mahasiswa: 4
Nama: Budi Santoso     | NIM: 0325016 | Nilai: 92
Nama: Siti Aminah      | NIM: 0325017 | Nilai: 78
Nama: Andi Pratama     | NIM: 0325018 | Nilai: 62
Nama: Dewi Lestari     | NIM: 0325019 | Nilai: 45
```

**Output di konsol (salinan persis dari `Hasil_Run.txt`):**

```text
Masukkan jumlah mahasiswa: 4

--- Mahasiswa ke-1 ---
Nama: Budi Santoso
NIM (angka saja): 0325016
Nilai Budi Santoso (0-100): 92
Tersimpan: BUDI SANTOSO | Grade: A | Status: Lulus

--- Mahasiswa ke-2 ---
Nama: Siti Aminah
NIM (angka saja): 0325017
Nilai Siti Aminah (0-100): 78
Tersimpan: SITI AMINAH | Grade: B | Status: Lulus

--- Mahasiswa ke-3 ---
Nama: Andi Pratama
NIM (angka saja): 0325018
Nilai Andi Pratama (0-100): 62
Tersimpan: ANDI PRATAMA | Grade: C | Status: Tidak Lulus

--- Mahasiswa ke-4 ---
Nama: Dewi Lestari
NIM (angka saja): 0325019
Nilai Dewi Lestari (0-100): 45
Tersimpan: DEWI LESTARI | Grade: D | Status: Tidak Lulus

===== REKAPITULASI NILAI MAHASISWA =====
BUDI SANTOSO    | NIM: 0325016 | Nilai:  92.00 | Grade: A | Status: Lulus
SITI AMINAH     | NIM: 0325017 | Nilai:  78.00 | Grade: B | Status: Lulus
ANDI PRATAMA    | NIM: 0325018 | Nilai:  62.00 | Grade: C | Status: Tidak Lulus
DEWI LESTARI    | NIM: 0325019 | Nilai:  45.00 | Grade: D | Status: Tidak Lulus
-----------------------------------------
Rata-rata nilai kelas : 69.25
Jumlah mahasiswa      : 4
Jumlah lulus          : 2 mahasiswa
Jumlah tidak lulus    : 2 mahasiswa
Nilai tertinggi       : BUDI SANTOSO (92.0)
Nilai terendah        : DEWI LESTARI (45.0)

Data berhasil ditulis ke file laporan_nilai.txt

===== ISI FILE laporan_nilai.txt =====
=================================
     LAPORAN NILAI MAHASISWA     
=================================
BUDI SANTOSO    | NIM: 0325016 | Nilai:  92.00 | Grade: A | Status: Lulus
SITI AMINAH     | NIM: 0325017 | Nilai:  78.00 | Grade: B | Status: Lulus
ANDI PRATAMA    | NIM: 0325018 | Nilai:  62.00 | Grade: C | Status: Tidak Lulus
DEWI LESTARI    | NIM: 0325019 | Nilai:  45.00 | Grade: D | Status: Tidak Lulus
=================================
RINGKASAN KELAS
Rata-rata nilai kelas : 69.25
Jumlah mahasiswa      : 4 mahasiswa
Jumlah lulus          : 2 mahasiswa
Jumlah tidak lulus    : 2 mahasiswa
Nilai tertinggi       : BUDI SANTOSO (92.0)
Nilai terendah        : DEWI LESTARI (45.0)
===== AKHIR FILE =====

Program selesai.
```

**Perhitungan manual (untuk dicocokkan saat presentasi):** (92 + 78 + 62 + 45) / 4 = 277 / 4 = **69.25**; lulus 2 (Budi 92 ≥ 70, Siti 78 ≥ 70), tidak lulus 2 (Andi 62, Dewi 45). Grade: 92 → A (≥ 85), 78 → B (≥ 70), 62 → C (≥ 55), 45 → D (< 55). Isi `laporan_nilai.txt` sama persis dengan blok di antara `===== ISI FILE =====` dan `===== AKHIR FILE =====` di atas.

**Bukti validasi (siap didemokan saat presentasi):** `Hasil_Run_Demo_Validasi.txt` merekam satu sesi yang sengaja salah input — jumlah `tiga`, nama kosong, NIM `12A4`, nilai `105`, nilai `delapan` — dan semuanya ditolak dengan pesan yang ramah lalu diminta ulang, sampai akhirnya tersimpan Budi 85 (A, Lulus) dan Siti 65 (C, Tidak Lulus), rata-rata 75.00. `Hasil_Uji_Assert.txt` merekam uji constructor langsung: dengan `-ea`, NIM kosong → `AssertionError: NIM tidak boleh kosong!`; tanpa `-ea`, assert dilewati JVM (itulah kenapa `MainApp` juga memeriksa NIM kosong secara eksplisit); batas grade teruji: 70 → B/lulus, 85 → A, 69.99 → C/tidak lulus, 55 → C, 54.99 → D.

## 7. Tabel Pengujian

| No | Skenario | Input | Hasil yang diharapkan | Status |
|---|---|---|---|---|
| 1 | Kasus normal, 4 grade | Budi 92, Siti 78, Andi 62, Dewi 45 | Rata-rata 69.25; lulus 2, tidak lulus 2; grade A/B/C/D | ✓ Terverifikasi |
| 2 | Batas lulus (tepat 70) | 1 mahasiswa, nilai 70 | Grade **B**, status **Lulus** (karena `>= 70`) | ✓ Sesuai aturan |
| 3 | Batas grade A (tepat 85) | 1 mahasiswa, nilai 85 | Grade **A** | ✓ Sesuai aturan |
| 4 | Di bawah batas lulus | 1 mahasiswa, nilai 69.99 | Grade **C**, status **Tidak Lulus** | ✓ Sesuai aturan |
| 5 | Nilai di luar rentang | Nilai 105, lalu 85 | `NilaiInvalidException` → pesan "Nilai harus antara 0 dan 100", diminta ulang, lalu diterima | ✓ Validasi aktif |
| 6 | Nilai negatif | Nilai -5, lalu 60 | Sama seperti No. 5 (ditolak, diminta ulang) | ✓ Sesuai aturan |
| 7 | Input nilai bukan angka | Nilai: `delapan`, lalu `80` | `InputMismatchException` → "Input harus berupa angka", diminta ulang | ✓ Validasi aktif |
| 8 | NIM berisi huruf | NIM: `12A4`, lalu `0325016` | "NIM hanya boleh berisi angka (0-9)", diminta ulang | ✓ Validasi aktif |
| 9 | NIM kosong | NIM: (hanya Enter) | "NIM tidak boleh kosong", diminta ulang (plus `assert` di constructor saat `-ea` aktif) | ✓ Validasi aktif |
| 10 | Nama kosong | Nama: (hanya Enter), lalu `Budi` | "Nama tidak boleh kosong", diminta ulang | ✓ Validasi aktif |
| 11 | Jumlah mahasiswa 0 / negatif | Jumlah: `0`, lalu `2` | "Jumlah mahasiswa harus lebih dari 0", diminta ulang | ✓ Validasi aktif |
| 12 | File dibaca kembali | Jalankan program sampai selesai | Isi file tampil lengkap di konsol setelah `===== ISI FILE =====` | ✓ Terverifikasi |

## 8. Pertanyaan yang Sering Ditanyakan Saat Presentasi

**Q: Kenapa grade memakai if-else, bukan switch-case?**
A: Karena switch hanya bisa membandingkan nilai yang persis sama (`==`), sedangkan grade memakai rentang (`>= 85`, `>= 70`, `>= 55`). Untuk rentang, if-else berantai yang tepat. Switch-case cocok kalau pilihannya nilai tunggal, misalnya menu 1/2/3.

**Q: Apa beda `NilaiInvalidException` dengan `IllegalArgumentException`?**
A: `NilaiInvalidException` adalah **checked exception** buatan sendiri (`extends Exception`) — compiler memaksa `MainApp` menangkapnya, jadi nilai tidak valid tidak mungkin lolos tanpa ditangani. `IllegalArgumentException` adalah **unchecked exception** bawaan Java (`extends RuntimeException`) — compiler tidak memaksa. Program ini sengaja memakai keduanya supaya perbedaannya terlihat: nilai → checked, NIM huruf → unchecked.

**Q: Kapan `assert` bekerja?**
A: Hanya kalau program dijalankan dengan flag `-ea` (`java -ea ...`, atau VM Options `-ea` di NetBeans). Tanpa `-ea`, JVM melewati `assert` begitu saja. Karena itu NIM kosong juga dicek secara eksplisit di `MainApp` — `assert` di sini adalah pengaman lapis kedua saat development/testing, bukan satu-satunya pertahanan.

**Q: Kenapa NIM disimpan sebagai `String`, bukan `int`?**
A: Karena NIM itu identitas, bukan angka untuk dihitung — lagipula NIM bisa diawali angka 0 (`0325016`), yang akan hilang kalau disimpan sebagai `int`. Sebagai `String`, NIM juga bisa diperiksa huruf demi huruf dengan `Character.isDigit`.

**Q: Kenapa nama tersimpan huruf KAPITAL semua?**
A: Sesuai perintah soal: `toUpperCase()` dipakai untuk merapikan nama/NIM, supaya `budi`, `Budi`, dan `BUDI` tercatat sama. NIM juga di-`trim()` + `toUpperCase()`.

**Q: Kenapa pakai `BufferedWriter`, bukan `FileWriter` saja?**
A: `FileWriter` menulis langsung ke disk tiap kali dipanggil — lambat kalau sering. `BufferedWriter` menampung dulu di memori (buffer) lalu menulis sekaligus. Fungsinya sama, `BufferedWriter` hanya lebih efisien. Hal yang sama berlaku untuk `BufferedReader` vs `FileReader`.

**Q: Kenapa tidak `close()` manual saja, pakai `finally`?**
A: Karena kalau error terjadi sebelum baris `close()` tercapai, file tetap terbuka. `try-with-resources` menutupnya otomatis dalam semua kondisi — itu alasan soal memintanya (soal membolehkan `try-catch-finally` ATAU `try-with-resources`; program ini memakai yang kedua, yang lebih modern).

**Q: Kenapa `readLine()` berhenti di `null`?**
A: `readLine()` mengembalikan `null` kalau sudah tidak ada baris lagi (akhir file / EOF). Jadi `while ((baris = reader.readLine()) != null)` artinya "baca terus sampai habis" — tanpa perlu menghitung jumlah baris dulu.

**Q: Apa yang terjadi kalau `laporan_nilai.txt` sudah ada?**
A: Ditimpa seluruhnya dengan data baru (perilaku default `new FileWriter(namaFile)`). Kalau ingin menambah tanpa menghapus isi lama, pakai `new FileWriter(namaFile, true)` — parameter kedua `true` berarti mode *append*.

**Q: Kenapa tidak ada setter (`setNilai` dsb.) di `Mahasiswa`?**
A: Sengaja: data mahasiswa dibuat tidak bisa diubah setelah objek jadi (immutable). Kalau ada yang salah, buat objek baru yang benar. Ini mencegah data yang sudah tervalidasi diubah diam-diam menjadi tidak valid sesudahnya.

**Q: NIM saya `L0325016` mengandung huruf L — kenapa ditolak program?**
A: Karena soal Pertemuan 4 memerintahkan NIM hanya berisi karakter angka (`Character.isDigit`). Jadi NIM UNS yang mengandung huruf tidak bisa dimasukkan apa adanya ke program ini — itu batasan yang memang diminta soal, bukan bug. Kalau dosen bertanya, jawab itu.

## 9. Cara Menjalankan

Lihat `CARA_MENJALANKAN.txt`. Ringkasnya:

**NetBeans:** buat project → buat package `model` dan `app` → salin file → VM Options `-ea` → Main Class `app.MainApp` → Run (F6).

**Terminal / CMD** (dari folder project, satu perintah satu baris):

```bash
javac -d out src/model/*.java src/app/*.java
java -ea -cp out app.MainApp
```

## 10. Keterbatasan & Pengembangan Lanjutan

- **File ditimpa tiap kali dijalankan** — data lama hilang. Pengembangan: mode *append* atau nama file berisi tanggal.
- **Data hanya tersimpan di file teks** — tidak bisa dicari/diurutkan dengan mudah. Pengembangan: simpan sebagai CSV, atau baca kembali ke `ArrayList` saat program dimulai.
- **Tidak ada fitur edit/hapus mahasiswa** — program sekali jalan. Pengembangan: menu (tambah / lihat / hapus / keluar).
- **Grade memakai batas tetap 85/70/55** — pengembangan: baca batas grade dari file konfigurasi supaya bisa diubah tanpa mengubah kode.
- **Nama disimpan huruf kapital semua** — sesuai perintah soal (`toUpperCase`); kalau ingin tampilan nama asli, simpan dua versi (asli + kapital).

---

*Kode lengkap dengan komentar penjelasan tiap sintaks ada di `src/model/Mahasiswa.java`, `src/model/NilaiInvalidException.java`, dan `src/app/MainApp.java` — komentarnya ditulis supaya bisa dibacakan langsung saat presentasi.*
