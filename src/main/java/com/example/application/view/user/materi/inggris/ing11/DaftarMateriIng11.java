package com.example.application.view.user.materi.inggris.ing11;

import com.example.application.view.user.materi.inggris.ModelMateriIng;

import java.util.List;

public class DaftarMateriIng11 {

        public static List<ModelMateriIng> getDaftarMateriIng11() {
                return List.of(
                        new ModelMateriIng(
                                25,
                                "Conjunctions",
                                "Conjunctions adalah kata hubung yang digunakan untuk menghubungkan kata, frasa, atau klausa dalam sebuah kalimat.",
                                List.of(
                                        "1. Coordinating Conjunctions (FANBOYS: for, and, nor, but, or, yet, so): menghubungkan dua elemen yang setara.",
                                        "   - I want to go, but I'm tired.",
                                        "2. Subordinating Conjunctions (because, since, although, while, after): menghubungkan klausa dependen dengan klausa independen.",
                                        "   - I will call you after I arrive.",
                                        "3. Correlative Conjunctions (either...or, neither...nor, both...and): berpasangan dan menghubungkan dua elemen setara.",
                                        "   - Both John and Mary are coming."
                                ),
                                List.of(
                                        "Fungsi: Membantu kalimat menjadi lebih kompleks dan mudah dipahami dengan menunjukkan hubungan logis antar ide.",
                                        "Contoh:",
                                        "“I like coffee and she likes tea.” → 'and' menghubungkan dua klausa setara.\n",
                                        "“He studied hard because he wanted to pass.” → 'because' menunjukkan hubungan sebab-akibat."
                                )
                        ),
                        new ModelMateriIng(
                                26,
                                "Comparative and Superlative Adjective",
                                "Comparative dan Superlative Adjective digunakan untuk membandingkan dua atau lebih hal.",
                                List.of(
                                        "1. Comparative Adjective: membandingkan dua hal (lebih... dari).",
                                        "   - Taller, more beautiful.",
                                        "2. Superlative Adjective: membandingkan tiga atau lebih hal (paling...).",
                                        "   - Tallest, most beautiful."
                                ),
                                List.of(
                                        "Untuk satu suku kata: tambahkan '-er' (comparative) dan '-est' (superlative).",
                                        "Contoh: tall → taller → tallest.",
                                        "Untuk dua suku kata atau lebih: gunakan 'more' (comparative) dan 'most' (superlative).",
                                        "Contoh: beautiful → more beautiful → most beautiful."
                                )
                        ),new ModelMateriIng(
                                27,
                                "Analytical Exposition Text",
                                "Analytical Exposition Text adalah teks argumentatif yang memaparkan sudut pandang penulis terhadap suatu isu. Tujuannya adalah meyakinkan pembaca bahwa ide tersebut penting.",
                                List.of(
                                        "1. Thesis: Pernyataan posisi penulis di awal teks.",
                                        "2. Arguments: Serangkaian argumen yang mendukung tesis.",
                                        "3. Reiteration/Conclusion: Penegasan kembali tesis di akhir teks."
                                ),
                                List.of(
                                        "Gunakan Present Tense.",
                                        "Gunakan kata-kata yang menunjukkan pendapat atau sikap (I believe, in my opinion).",
                                        "Gunakan kata hubung logis (because, therefore, in addition)."
                                )
                        )
                );
        }
}