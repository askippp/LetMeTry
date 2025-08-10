package com.example.application.view.user.materi.inggris.ing12;

import com.example.application.view.user.materi.inggris.ModelMateriIng;

import java.util.List;

public class DaftarMateriIng12 {

        public static List<ModelMateriIng> getDaftarMateriIng12() {
                return List.of(
                        new ModelMateriIng(
                                28,
                                "Active and Passive Voice",
                                "Active Voice adalah kalimat di mana subjek melakukan aksi. Passive Voice adalah kalimat di mana subjek menerima aksi.",
                                List.of(
                                        "1. Active: “The cat eats the fish.” (Kucing memakan ikan)",
                                        "2. Passive: “The fish is eaten by the cat.” (Ikan dimakan oleh kucing)"
                                ),
                                List.of(
                                        "Rumus Passive Voice: Subject + be (sesuai tense) + verb 3 (past participle) + by + object.",
                                        "Perubahan dari Active ke Passive:",
                                        "Objek di kalimat aktif menjadi subjek di kalimat pasif.",
                                        "Subjek di kalimat aktif menjadi objek di kalimat pasif, diawali kata 'by'."
                                )
                        )
                );
        }
}