package com.example.application.view.user.materi.matematika.mtk11;

import com.example.application.view.user.materi.matematika.ModelMateriMtk;

import java.util.List;

public class DaftarMateriMtk11 {

        public static List<ModelMateriMtk> getDaftarMateriMtk11() {
                return List.of(
                        new ModelMateriMtk(
                                13,
                                "Fungsi",
                                "Fungsi kuadrat adalah fungsi polinomial dengan pangkat tertinggi dua. Grafiknya dikenal sebagai parabola.",
                                List.of(
                                        "Bentuk umum: f(x) = ax² + bx + c",
                                        "Diskriminan: D = b² - 4ac",
                                        "Rumus Sumbu Simetri: x = -b / 2a",
                                        "Titik Puncak: (-b / 2a, f(-b / 2a))"
                                ),
                                List.of(
                                        "Untuk f(x) = x² - 4x + 3:",
                                        "Sumbu simetri: x = -(-4) / (2*1) = 2",
                                        "Titik puncak: f(2) = 2² - 4(2) + 3 = -1",
                                        "Titik puncaknya adalah (2, -1)"
                                ),
                                List.of(
                                        "a, b, c = koefisien",
                                        "Jika a > 0, parabola terbuka ke atas",
                                        "Jika a < 0, parabola terbuka ke bawah",
                                        "D digunakan untuk menentukan posisi grafik terhadap sumbu x."
                                )
                        ),
                        new ModelMateriMtk(
                                14,
                                "Lingkaran",
                                "Lingkaran adalah himpunan semua titik yang memiliki jarak (r) yang sama dari sebuah titik pusat.",
                                List.of(
                                        "Pusat (0,0): x² + y² = r²",
                                        "Pusat (a,b): (x-a)² + (y-b)² = r²",
                                        "Bentuk umum: x² + y² + Ax + By + C = 0"
                                ),
                                List.of(
                                        "Tentukan persamaan lingkaran dengan pusat (2,3) dan jari-jari 5.",
                                        "Menggunakan rumus (x-a)² + (y-b)² = r²",
                                        "→ (x-2)² + (y-3)² = 5²",
                                        "→ (x-2)² + (y-3)² = 25"
                                ),
                                List.of(
                                        "r = jari-jari",
                                        "Pusat lingkaran = (a, b)",
                                        "Digunakan untuk menghitung jari-jari dan posisi pusat lingkaran."
                                )
                        )
                );
        }
}