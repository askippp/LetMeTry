package com.example.application.view.user.materi.matematika;

import com.example.application.view.user.materi.matematika.mtk10.DaftarMateriMtk10;
import java.util.List;

public class ModelMateriMtk {
    private int idMateri;
    private String judul;
    private String pengertian;
    private List<String> rumus;
    private List<String> contoh;
    private List<String> penjelasan;

    public ModelMateriMtk(int idMateri, String judul, String pengertian, List<String> rumus, List<String> contoh, List<String> penjelasan) {
        this.idMateri = idMateri;
        this.judul = judul;
        this.pengertian = pengertian;
        this.rumus = rumus;
        this.contoh = contoh;
        this.penjelasan = penjelasan;
    }

    // Getter methods
    public int getIdMateri() { return idMateri; }
    public String getJudul() { return judul; }
    public String getPengertian() { return pengertian; }
    public List<String> getRumus() { return rumus; }
    public List<String> getContoh() { return contoh; }
    public List<String> getPenjelasan() { return penjelasan; }

    public String getDeskripsi() {
        return pengertian != null ? pengertian : "Deskripsi tidak tersedia";
    }

    public static List<ModelMateriMtk> getList() {
        return DaftarMateriMtk10.getDaftarMateriMtk10();
    }
}