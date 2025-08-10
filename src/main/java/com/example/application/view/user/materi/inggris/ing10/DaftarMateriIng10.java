package com.example.application.view.user.materi.inggris.ing10;

import com.example.application.view.user.materi.inggris.ModelMateriIng;

import java.util.List;

public class DaftarMateriIng10 {

    public static List<ModelMateriIng> getDaftarMateriIng10() {
        return List.of(
                new ModelMateriIng(
                        16,
                        "Present Tense",
                        "Present Tense adalah bentuk waktu yang digunakan untuk menyatakan kejadian yang berlangsung secara umum, kebiasaan, atau fakta.",
                        List.of(
                                "1. She goes to school every day.",
                                "2. The sun rises in the east."
                        ),
                        List.of(
                                "Gunakan bentuk verb 1. Jika subjeknya he/she/it, tambahkan -s/-es pada verb.",
                                "Contoh:",
                                "“I eat” → subjek I (tanpa -s)\n",
                                "“She eats” → subjek she (dengan -s)"
                        )
                ),
                new ModelMateriIng(
                        17,
                        "Past Tense",
                        "Past Tense digunakan untuk menyatakan kejadian yang sudah terjadi di masa lampau.",
                        List.of(
                                "1. I visited my grandmother yesterday.",
                                "2. They played football last week."
                        ),
                        List.of(
                                "Gunakan verb 2 (past form). Untuk regular verbs, tambahkan -ed. Untuk irregular verbs, bentuknya berubah (go → went, eat → ate)."
                        )
                ),new ModelMateriIng(
                        18,
                        "Conditional Sentence",
                        "Conditional Sentence digunakan untuk menyatakan pengandaian atau kondisi yang bisa atau tidak bisa terjadi.",
                        List.of(
                                "1. If I study hard, I will pass the exam. (Type 1)",
                                "2. If I were you, I would apologize. (Type 2)"
                        ),
                        List.of(
                                "Type 1: Kondisi nyata (If + present, will + verb)",
                                "Type 2: Kondisi tidak nyata (If + past, would + verb)"
                        )
                ),new ModelMateriIng(
                        19,
                        "Expressing Opinions",
                        "Ungkapan yang digunakan untuk menyatakan pendapat atau opini.",
                        List.of(
                                "1. I think online learning is helpful.",
                                "2. In my opinion, smoking should be banned."
                        ),
                        List.of(
                                "Gunakan frasa seperti “I think…”, “I believe…”, “In my opinion…”, lalu ikuti dengan pendapatmu."
                        )
                ),new ModelMateriIng(
                        20,
                        "Descriptive Text",
                        "Teks yang mendeskripsikan seseorang, tempat, atau benda secara rinci.",
                        List.of(
                                "1. My cat is very cute. It has white fur, blue eyes, and a long tail.",
                                "2. Your face is very beautiful and you are very cute."
                        ),
                        List.of(
                                "Identification: pengenalan objek.",
                                "Description: rincian ciri-ciriDescription: rincian ciri-ciri"
                        )
                ),new ModelMateriIng(
                        21,
                        "Recount Text",
                        "Teks yang menceritakan pengalaman masa lalu.",
                        List.of(
                                "1. Last holiday, I went to Bali with my family. We visited many places and took a lot of pictures."
                        ),
                        List.of(
                                "Struktur: Orientation → Events → Reorientation",
                                "Ciri: Gunakan past tense dan urutan waktu (first, then, finally)."
                        )
                ),new ModelMateriIng(
                        22,
                        "Procedure Text",
                        "Teks yang berisi langkah-langkah untuk melakukan sesuatu.",
                        List.of(
                                "How To Make Make Fried Rice",
                                "Ingredients: rice, eggs, vegetables",
                                "Steps:",
                                "1. Heat the pan.",
                                "2. Fry the eggs.",
                                "3. Add rice and vegetables.",
                                "4. Stir water and serve"
                        ),
                        List.of(
                                "Struktur: Goal → Materials → Steps"
                        )
                ),new ModelMateriIng(
                        23,
                        "Exposition Text",
                        "Teks yang berisi argumen dan pendapat untuk meyakinkan pembaca.",
                        List.of(
                                "Why We Should Save Electricity",
                                "Electricity is important in our daily life. If we waste it, we will face serious problems. Therefore, we should use it wisely."
                        ),
                        List.of(
                                "Struktur: Thesis → Arguments → Conclusions",
                                "Ciri: Berisi opini dan alasan-alasan logis."
                        )
                ),new ModelMateriIng(
                        24,
                        "Narrative Text",
                        "Teks yang menceritakan suatu kisah atau cerita, bisa nyata atau fiksi.",
                        List.of(
                                "Once upon a time, there was a poor boy named Jack. He lived with his mother in a small village. They were so poor that they had nothing to eat.",
                                "One day, Jack's mother asked him to sell their only cow. On his way to the market, Jack met a strange old man who offered him some magic beans in exchange for the cow. Jack agreed and took the beans home. His mother was angry and threw the beans out of the window.",
                                "The next morning, a giant beanstalk had grown up to the sky. Jack climbed it and found a giant’s castle in the clouds. He entered the castle and saw a goose that laid golden eggs. Jack took the goose and tried to escape.",
                                "The giant chased Jack down the beanstalk. Jack quickly climbed down and cut the beanstalk with an axe. The beanstalk fell and the giant died. Jack and his mother lived happily ever after with the golden eggs."
                        ),
                        List.of(
                                "Struktur: Orientation → Complication → Resolution → Coda(Optional)",
                                "Ciri:  Banyak menggunakan past tense dan kata kerja aksi.."
                        )
                )
        );
    }
}