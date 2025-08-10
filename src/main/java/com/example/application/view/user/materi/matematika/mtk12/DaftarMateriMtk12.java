package com.example.application.view.user.materi.matematika.mtk12;

import com.example.application.view.user.materi.matematika.ModelMateriMtk;

import java.util.List;

public class DaftarMateriMtk12 {

        public static List<ModelMateriMtk> getDaftarMateriMtk12() {
                return List.of(
                        new ModelMateriMtk(
                                15,
                                "Aturan Pencacahan dan Peluang",
                                "Aturan pencacahan adalah metode untuk menghitung banyaknya kemungkinan yang dapat terjadi. Peluang adalah kemungkinan terjadinya suatu kejadian.",
                                List.of(
                                        "Aturan Perkalian: n₁ × n₂ × ... × nₖ",
                                        "Faktorial: n! = n × (n-1) × ... × 1",
                                        "Permutasi: P(n,r) = n! / (n-r)!",
                                        "Kombinasi: C(n,r) = n! / (r! × (n-r)!)",
                                        "Peluang: P(A) = n(A) / n(S)"
                                ),
                                List.of(
                                        "Dari 5 calon, dipilih 3 posisi (ketua, wakil, sekretaris):",
                                        "Permutasi: P(5,3) = 5! / (5-3)! = 60 cara.",
                                        "Dari 5 siswa, dipilih 3 orang untuk lomba:",
                                        "Kombinasi: C(5,3) = 5! / (3! × (5-3)!) = 10 cara."
                                ),
                                List.of(
                                        "n = jumlah total objek",
                                        "r = jumlah objek yang dipilih",
                                        "n(A) = banyaknya anggota kejadian A",
                                        "n(S) = banyaknya anggota ruang sampel"
                                )
                        )
                );
        }
}