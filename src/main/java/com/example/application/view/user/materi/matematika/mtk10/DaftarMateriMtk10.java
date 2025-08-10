package com.example.application.view.user.materi.matematika.mtk10;

import com.example.application.view.user.materi.matematika.ModelMateriMtk;

import java.util.List;

public class DaftarMateriMtk10 {

        public static List<ModelMateriMtk> getDaftarMateriMtk10() {
                return List.of(
                        new ModelMateriMtk(
                                1,
                                "Statistika",
                                "Statistika adalah ilmu yang mempelajari cara mengumpulkan, mengolah, menyajikan, dan menganalisis data untuk menarik kesimpulan.",
                                List.of(
                                        "1. Rata-rata (mean): x̄ = (Σx) / n",
                                        "2. Median: nilai tengah dari data yang telah diurutkan",
                                        "3. Modus: nilai yang paling sering muncul",
                                        "4. Jangkauan: data terbesar - data terkecil",
                                        "5. Kuartil (Q1, Q2, Q3): nilai yang membagi data menjadi empat bagian sama banyak"
                                ),
                                List.of(
                                        "Data: 60, 70, 80, 90, 90",
                                        "x̄ = (60+70+80+90+90)/5 = 78",
                                        "Data diurutkan: 60, 70, 80, 90, 90",
                                        "Median = 80, Modus = 90",
                                        "Jangkauan = 90 - 60 = 30"
                                ),
                                List.of(
                                        "x̄ = rata-rata",
                                        "Σx = jumlah seluruh data",
                                        "n = jumlah data",
                                        "Digunakan untuk mengetahui nilai umum, sebaran, dan variasi dari suatu data."
                                )
                        ),
                        new ModelMateriMtk(
                                2,
                                "Persamaan Kuadrat",
                                "Persamaan kuadrat adalah bentuk aljabar dengan pangkat tertinggi dua (ax² + bx + c = 0). Fungsi kuadrat adalah fungsi yang grafiknya berbentuk parabola (f(x) = ax² + bx + c).",
                                List.of(
                                        "ax² + bx + c = 0",
                                        "Rumus kuadrat (Rumus ABC): x = (-b ± √(b² - 4ac)) / 2a",
                                        "Diskriminan: D = b² - 4ac",
                                        "Titik Puncak Fungsi: (-b/2a, f(-b/2a))"
                                ),
                                List.of(
                                        "x² - 5x + 6 = 0",
                                        "→ (x-2)(x-3) = 0",
                                        "→ x = 2 atau x = 3",
                                        "Untuk f(x) = x² - 4x + 3, titik puncaknya berada di x = -(-4)/(2*1) = 2. f(2) = 2² - 4(2) + 3 = -1. Jadi, titik puncaknya (2, -1)."
                                ),
                                List.of(
                                        "a, b, c = koefisien konstanta dalam persamaan",
                                        "D digunakan untuk menentukan jenis akar: D > 0 (dua akar nyata), D = 0 (satu akar nyata/kembar), D < 0 (tidak ada akar nyata).",
                                        "Digunakan untuk menentukan akar-akar (nilai x) dari persamaan kuadrat dan menggambarkan grafik parabola."
                                )
                        ),
                        new ModelMateriMtk(
                                3,
                                "Peluang",
                                "Peluang adalah kemungkinan terjadinya suatu peristiwa dari suatu percobaan acak.",
                                List.of(
                                        "P(A) = n(A) / n(S)",
                                        "P(A) + P(A') = 1"
                                ),
                                List.of(
                                        "Peluang muncul angka genap dari pelemparan sebuah dadu: n(A) = 3 (angka 2, 4, 6) dan n(S) = 6. Maka, P(A) = 3/6 = 1/2."
                                ),
                                List.of(
                                        "P(A) = peluang kejadian A",
                                        "n(A) = jumlah kejadian yang diinginkan",
                                        "n(S) = jumlah seluruh kejadian (ruang sampel)",
                                        "P(A') = peluang bukan kejadian A"
                                )
                        ),
                        new ModelMateriMtk(
                                4,
                                "Trigonometri",
                                "Trigonometri adalah cabang matematika yang mempelajari hubungan antara sisi dan sudut dalam segitiga siku-siku.",
                                List.of(
                                        "sin(θ) = depan / miring (sinus)",
                                        "cos(θ) = samping / miring (kosinus)",
                                        "tan(θ) = depan / samping (tangen)",
                                        "Identitas dasar: sin²(θ) + cos²(θ) = 1"
                                ),
                                List.of(
                                        "Jika sudut 30° pada segitiga siku-siku, maka sin(30°) = 1/2, cos(30°) = √3/2, tan(30°) = 1/√3."
                                ),
                                List.of(
                                        "θ = sudut dalam segitiga",
                                        "Digunakan untuk menghitung panjang sisi atau besar sudut segitiga, serta dalam fisika dan rekayasa."
                                )
                        ),
                        new ModelMateriMtk(
                                5,
                                "Barisan dan Deret",
                                "Barisan adalah urutan bilangan, sedangkan deret adalah jumlah dari suku-suku barisan. Ada dua jenis utama: Aritmatika (beda konstan) dan Geometri (rasio konstan).",
                                List.of(
                                        "Aritmatika: Un = a + (n-1)b, Sn = (n/2)(a + Un)",
                                        "Geometri: Un = a × r^(n-1), Sn = a(r^n - 1) / (r - 1) untuk r ≠ 1"
                                ),
                                List.of(
                                        "Barisan Aritmatika: 2, 4, 6, 8,...",
                                        "→ Un = 2 + (n-1)×2. Sn untuk 4 suku: (4/2)(2+8) = 20.",
                                        "Barisan Geometri: 3, 6, 12, 24,...",
                                        "→ Un = 3 × 2^(n-1). Sn untuk 4 suku: 3(2^4 - 1)/(2-1) = 3(15) = 45."
                                ),
                                List.of(
                                        "a = suku pertama",
                                        "b = beda (aritmatika)",
                                        "r = rasio (geometri)",
                                        "n = banyaknya suku",
                                        "Un = suku ke-n",
                                        "Sn = jumlah n suku"
                                )
                        ),
                        new ModelMateriMtk(
                                6,
                                "Program Linear",
                                "Program linear digunakan untuk menentukan nilai maksimum atau minimum dari suatu fungsi objektif dengan batasan-batasan (kendala) tertentu yang berbentuk pertidaksamaan linear.",
                                List.of(
                                        "Fungsi objektif: f(x, y) = ax + by",
                                        "Batasan: pertidaksamaan linear seperti x + y ≤ 10, x ≥ 0, y ≥ 0",
                                        "Metode: uji titik pojok pada grafik daerah himpunan penyelesaian."
                                ),
                                List.of(
                                        "Maksimalkan f(x, y) = 3x + 2y dengan syarat x + y ≤ 10, x ≥ 0, y ≥ 0.",
                                        "Titik pojoknya adalah (0,0), (10,0), dan (0,10).",
                                        "Nilai maksimum diperoleh di (10,0) atau (0,10)."
                                ),
                                List.of(
                                        "a dan b = koefisien dari variabel x dan y",
                                        "Digunakan untuk masalah optimasi di berbagai bidang, seperti ekonomi dan manajemen."
                                )
                        ),
                        new ModelMateriMtk(
                                7,
                                "Sistem Persamaan Linear dan Pertidaksamaan Linear",
                                "Sistem persamaan atau pertidaksamaan yang terdiri dari dua variabel atau lebih dan pangkat tertinggi tiap variabel adalah satu.",
                                List.of(
                                        "Metode penyelesaian: substitusi, eliminasi, atau grafik",
                                        "Contoh: x + y = 5 dan 2x - y = 1"
                                ),
                                List.of(
                                        "Dari x + y = 5 dan 2x - y = 1, dengan metode eliminasi, kita dapatkan 3x = 6, sehingga x = 2. Dengan substitusi, y = 5 - 2 = 3. Jadi, solusinya (2, 3)."
                                ),
                                List.of(
                                        "Sistem = gabungan dari beberapa persamaan",
                                        "Solusi dari sistem adalah nilai variabel yang memenuhi semua persamaan atau pertidaksamaan."
                                )
                        ),
                        new ModelMateriMtk(
                                8,
                                "Vektor",
                                "Vektor adalah besaran yang memiliki arah dan besar (panjang).",
                                List.of(
                                        "Vektor dalam komponen 2D: v = ⟨x, y⟩",
                                        "Vektor dalam komponen 3D: v = ⟨x, y, z⟩",
                                        "Panjang vektor: |v| = √(x² + y²)"
                                ),
                                List.of(
                                        "Vektor v = ⟨3, 4⟩ memiliki panjang |v| = √(3² + 4²) = √25 = 5.",
                                        "Vektor u = ⟨1, 2, 2⟩ memiliki panjang |u| = √(1² + 2² + 2²) = √9 = 3."
                                ),
                                List.of(
                                        "v = vektor dalam komponen x, y, dan z",
                                        "|v| = panjang atau magnitudo vektor",
                                        "Digunakan untuk merepresentasikan besaran fisika seperti gaya, kecepatan, dan perpindahan."
                                )
                        ),
                        new ModelMateriMtk(
                                9,
                                "Persamaan dan Pertidaksamaan Logaritma",
                                "Logaritma adalah invers dari eksponen. Persamaan atau pertidaksamaan logaritma adalah persamaan yang mengandung bentuk logaritma.",
                                List.of(
                                        "logₐ b = c ↔ aᶜ = b",
                                        "Sifat-sifat: log a + log b = log(ab), log a - log b = log(a/b)"
                                ),
                                List.of(
                                        "log x + log(x - 3) = log 10",
                                        "→ log(x(x-3)) = log 10",
                                        "→ x² - 3x - 10 = 0",
                                        "→ (x-5)(x+2) = 0.",
                                        "Dengan syarat x > 3, maka solusi yang memenuhi adalah x = 5."
                                ),
                                List.of(
                                        "a = basis logaritma",
                                        "b = bilangan logaritma (numerus), b > 0",
                                        "c = hasil logaritma",
                                        "Penyelesaian harus selalu memperhatikan syarat numerus (>0) dan basis (a > 0, a ≠ 1)."
                                )
                        ),
                        new ModelMateriMtk(
                                11,
                                "Fungsi Kuadrat",
                                "Fungsi atau persamaan dengan variabel di pangkat (eksponen).",
                                List.of(
                                        "Jika aˣ = aʸ, maka x = y (dengan a > 0 dan a ≠ 1)",
                                        "Jika a^(f(x)) > a^(g(x)), maka f(x) > g(x) untuk a > 1"
                                ),
                                List.of(
                                        "3^(x+1) = 27",
                                        "→ 3^(x+1) = 3³",
                                        "→ x + 1 = 3",
                                        "→ x = 2."
                                ),
                                List.of(
                                        "a = basis, x = pangkat (eksponen)",
                                        "Digunakan untuk memodelkan pertumbuhan dan peluruhan, seperti pertumbuhan populasi atau peluruhan radioaktif."
                                )
                        ),
                        new ModelMateriMtk(
                                12,
                                "Sistem Persamaan dan Pertidaksamaan Tidak Linear",
                                "Sistem persamaan yang salah satu atau keduanya berbentuk tidak linear (misalnya kuadrat atau lingkaran).",
                                List.of(
                                        "Contoh: x² + y² = 25 (persamaan lingkaran) dan x + y = 7 (persamaan linear)",
                                        "Metode penyelesaian utama adalah substitusi."
                                ),
                                List.of(
                                        "Untuk menyelesaikan x² + y² = 25 dan x + y = 7, substitusikan y = 7 - x ke persamaan pertama: x² + (7 - x)² = 25. Selesaikan persamaan kuadrat yang dihasilkan."
                                ),
                                List.of(
                                        "Grafik dari sistem ini bisa berupa perpotongan antara garis dan parabola, garis dan lingkaran, atau dua parabola."
                                )
                        )
                );
        }
}