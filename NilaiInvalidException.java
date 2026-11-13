/*
 * ============================================================================
 *  FILE  : model/NilaiInvalidException.java
 *  PAKET : model
 *  PERAN : "Jenis error buatan sendiri, khusus untuk nilai yang tidak valid."
 *
 *  CARA MENJELASKANNYA SAAT PRESENTASI (20 detik):
 *  "Java sudah punya banyak exception bawaan, tapi tidak ada yang artinya
 *   'nilai mahasiswa di luar 0 sampai 100'. Jadi saya buat sendiri:
 *   NilaiInvalidException. Dengan nama ini, pesan errornya langsung bisa
 *   dibaca maksudnya - dan karena dia CHECKED, MainApp dipaksa
 *   menanganinya, tidak bisa lupa."
 * ============================================================================
 */
package model;   // Satu paket dengan Mahasiswa, supaya mudah di-import bersama.

/**
 * CUSTOM EXCEPTION (Pertemuan 4).
 *
 * extends Exception = class ini MEWARISI semua kemampuan class Exception.
 * Karena induknya Exception (bukan RuntimeException), exception ini
 * bersifat CHECKED: compiler MEMAKSA setiap pemanggil untuk memilih satu:
 *   (a) menangkapnya dengan try-catch, ATAU
 *   (b) meneruskannya dengan kata kunci throws di methodnya.
 * Kalau pemanggil tidak melakukan keduanya, program GAGAL dikompilasi.
 * Itulah gunanya checked: nilai yang tidak valid TIDAK MUNGKIN lolos
 * tanpa ditangani - MainApp harus menulis catch-nya.
 *
 * Beda dengan RuntimeException (misal IllegalArgumentException): yang itu
 * UNCHECKED - compiler tidak memaksa, jadi pemanggil bisa lupa.
 * Di program ini keduanya dipakai, sengaja dibedakan:
 *   - NilaiInvalidException (checked)  -> untuk NILAI di luar 0-100.
 *   - IllegalArgumentException (unchecked, bawaan Java) -> untuk NIM
 *     yang berisi huruf (dipakai di constructor Mahasiswa).
 */
public class NilaiInvalidException extends Exception {

    // Constructor: satu-satunya tugasnya adalah meneruskan pesan error
    // ke constructor induknya lewat super(pesan).
    // Nanti pesan ini bisa dibaca kembali dengan e.getMessage().
    // Contoh pemakaian (di Mahasiswa.java):
    //   throw new NilaiInvalidException("Nilai harus antara 0 dan 100.");
    public NilaiInvalidException(String pesan) {
        super(pesan);   // super(...) = "panggil constructor class induk (Exception)".
    }
}
