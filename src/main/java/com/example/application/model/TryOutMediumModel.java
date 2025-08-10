package com.example.application.model;

public class TryOutMediumModel {
    private int idSoalToMedium;
    private int noSoal;
    private int idMapel;
    private int idMateri;
    private String pertanyaan;
    private int idJawabanToMedium;
    private String opsi;
    private String textJawaban;
    private String benar;

    public TryOutMediumModel() {}

    public int getIdSoalToMedium() {
        return idSoalToMedium;
    }

    public void setIdSoalToMedium(int idSoalToMedium) {
        this.idSoalToMedium = idSoalToMedium;
    }

    public int getNoSoal() {
        return noSoal;
    }

    public void setNoSoal(int noSoal) {
        this.noSoal = noSoal;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public void setIdMapel(int idMapel) {
        this.idMapel = idMapel;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public int setIdJawabanToMedium() {
        return idJawabanToMedium;
    }

    public void setIdJawabanToMedium(int idJawabanToMedium) {
        this.idJawabanToMedium = idJawabanToMedium;
    }

    public String getOpsi() {
        return opsi;
    }

    public void setOpsi(String opsi) {
        this.opsi = opsi;
    }

    public String getTextJawaban() {
        return textJawaban;
    }

    public void setTextJawaban(String textJawaban) {
        this.textJawaban = textJawaban;
    }

    public String getBenar() {
        return benar;
    }

    public void setBenar(String benar) {
        this.benar = benar;
    }
}
