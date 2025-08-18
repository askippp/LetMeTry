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

    // === Tambahan dari tryout_medium ===
    private int idToMedium;
    private int idUsers;
    private String tanggal;
    private String waktuPengerjaan;
    private int nilai;
    private String berhasil;

    // === Tambahan dari detail_tryout_medium ===
    private int idDetailToMedium;
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

    public TryOutMediumModel() {}

    // --- Getter & Setter lama ---
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

    public int getIdJawabanToMedium() {
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

    // --- Getter & Setter tambahan baru ---
    public int getIdToMedium() {
        return idToMedium;
    }

    public void setIdToMedium(int idToMedium) {
        this.idToMedium = idToMedium;
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

    public int getIdDetailToMedium() {
        return idDetailToMedium;
    }

    public void setIdDetailToMedium(int idDetailToMedium) {
        this.idDetailToMedium = idDetailToMedium;
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
