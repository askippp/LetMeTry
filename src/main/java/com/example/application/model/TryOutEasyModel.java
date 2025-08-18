package com.example.application.model;

public class TryOutEasyModel {
    private int idSoalToEasy;
    private int noSoal;
    private int idMapel;
    private int idMateri;
    private String pertanyaan;
    private int idJawabanToEasy;
    private String opsi;
    private String textJawaban;
    private String benar;

    // === Tambahan dari tryout_easy ===
    private int idToEasy;
    private int idUsers;
    private String tanggal;
    private String waktuPengerjaan;
    private int nilai;
    private String berhasil;

    // === Tambahan dari detail_tryout_easy ===
    private int idDetailToEasy;
    private int idJawabanDetail;
    private int nilaiDetail;

    // === Tambahan hasil perhitungan ===
    private int totalSoal;
    private int totalTerjawab;
    private int jumlahBenar;
    private int jumlahSalah;
    private int jumlahKosong;

    // === Tambahan dari join users/mapel ===
    private String namaLengkap;
    private String namaMapel;

    public TryOutEasyModel() {}

    // --- Getter & Setter lama (biarkan tetap ada) ---
    public int getIdSoalToEasy() {
        return idSoalToEasy;
    }

    public void setIdSoalToEasy(int idSoalToEasy) {
        this.idSoalToEasy = idSoalToEasy;
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

    public int getIdJawabanToEasy() {
        return idJawabanToEasy;
    }

    public void setIdJawabanToEasy(int idJawabanToEasy) {
        this.idJawabanToEasy = idJawabanToEasy;
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

    // --- Getter & Setter tambahan baru ---
    public int getIdToEasy() {
        return idToEasy;
    }

    public void setIdToEasy(int idToEasy) {
        this.idToEasy = idToEasy;
    }

    public int getIdUsers() {
        return idUsers;
    }

    public void setIdUsers(int idUsers) {
        this.idUsers = idUsers;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktuPengerjaan() {
        return waktuPengerjaan;
    }

    public void setWaktuPengerjaan(String waktuPengerjaan) {
        this.waktuPengerjaan = waktuPengerjaan;
    }

    public int getNilai() {
        return nilai;
    }

    public void setNilai(int nilai) {
        this.nilai = nilai;
    }

    public String getBerhasil() {
        return berhasil;
    }

    public void setBerhasil(String berhasil) {
        this.berhasil = berhasil;
    }

    public int getIdDetailToEasy() {
        return idDetailToEasy;
    }

    public void setIdDetailToEasy(int idDetailToEasy) {
        this.idDetailToEasy = idDetailToEasy;
    }

    public int getIdJawabanDetail() {
        return idJawabanDetail;
    }

    public void setIdJawabanDetail(int idJawabanDetail) {
        this.idJawabanDetail = idJawabanDetail;
    }

    public int getNilaiDetail() {
        return nilaiDetail;
    }

    public void setNilaiDetail(int nilaiDetail) {
        this.nilaiDetail = nilaiDetail;
    }

    public int getTotalSoal() {
        return totalSoal;
    }

    public void setTotalSoal(int totalSoal) {
        this.totalSoal = totalSoal;
    }

    public int getTotalTerjawab() {
        return totalTerjawab;
    }

    public void setTotalTerjawab(int totalTerjawab) {
        this.totalTerjawab = totalTerjawab;
    }

    public int getJumlahBenar() {
        return jumlahBenar;
    }

    public void setJumlahBenar(int jumlahBenar) {
        this.jumlahBenar = jumlahBenar;
    }

    public int getJumlahSalah() {
        return jumlahSalah;
    }

    public void setJumlahSalah(int jumlahSalah) {
        this.jumlahSalah = jumlahSalah;
    }

    public int getJumlahKosong() {
        return jumlahKosong;
    }

    public void setJumlahKosong(int jumlahKosong) {
        this.jumlahKosong = jumlahKosong;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public void setNamaMapel(String namaMapel) {
        this.namaMapel = namaMapel;
    }
}