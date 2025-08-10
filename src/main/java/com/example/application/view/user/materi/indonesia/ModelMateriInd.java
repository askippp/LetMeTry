package com.example.application.view.user.materi.indonesia;

import java.util.List;

public class ModelMateriInd {
    private int idMateri;
    private String judul;
    private String pengertian;
    private List<String> contoh;
    private List<String> penjelasan;

    public ModelMateriInd(int idMateri, String judul, String pengertian, List<String> contoh, List<String> penjelasan) {
        this.idMateri = idMateri;
        this.judul = judul;
        this.pengertian = pengertian;
        this.contoh = contoh;
        this.penjelasan = penjelasan;
    }

    public int getIdMateri() { return idMateri; }
    public String getJudul() { return judul; }
    public String getPengertian() { return pengertian; }
    public List<String> getContoh() { return contoh; }
    public List<String> getPenjelasan() { return penjelasan; }
}
