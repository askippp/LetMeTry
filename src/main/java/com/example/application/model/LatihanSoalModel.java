package com.example.application.model;

public class LatihanSoalModel {
    private int idSoalLatsol;
    private int noSoal;
    private int idMapel;
    private int idMateri;
    private String pertanyaan;
    private int idJawaban;
    private String opsi;
    private String textJawaban;
    private String benar;

    public LatihanSoalModel(){}

    public int getIdSoalLatsol() {
        return idSoalLatsol;
    }

    public void setIdSoalLatsol(int idSoalLatsol) {
        this.idSoalLatsol = idSoalLatsol;
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

    public int getIdJawaban() {
        return idJawaban;
    }

    public void setIdJawaban(int idJawaban) {
        this.idJawaban = idJawaban;
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
