package com.example.application.model;

public class HistoryModel {
    private String level;
    private int idTryout;
    private String tanggal;
    private String waktuPengerjaan;
    private int nilai;
    private String berhasil;
    private String namaMapel;
    private int totalTerjawab;
    private int jumlahBenar;
    private int jumlahSalah;
    private int totalSoal;
    private int persentase;

    public HistoryModel() {}

    // Getters
    public String getLevel() { return level; }
    public int getIdTryout() { return idTryout; }
    public String getTanggal() { return tanggal; }
    public String getWaktuPengerjaan() { return waktuPengerjaan; }
    public int getNilai() { return nilai; }
    public String getBerhasil() { return berhasil; }
    public String getNamaMapel() { return namaMapel; }
    public int getTotalTerjawab() { return totalTerjawab; }
    public int getJumlahBenar() { return jumlahBenar; }
    public int getJumlahSalah() { return jumlahSalah; }
    public int getTotalSoal() { return totalSoal; }
    public int getPersentase() { return persentase; }

    // Setters
    public void setLevel(String level) { this.level = level; }
    public void setIdTryout(int idTryout) { this.idTryout = idTryout; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setWaktuPengerjaan(String waktuPengerjaan) { this.waktuPengerjaan = waktuPengerjaan; }
    public void setNilai(int nilai) { this.nilai = nilai; }
    public void setBerhasil(String berhasil) { this.berhasil = berhasil; }
    public void setNamaMapel(String namaMapel) { this.namaMapel = namaMapel; }
    public void setTotalTerjawab(int totalTerjawab) { this.totalTerjawab = totalTerjawab; }
    public void setJumlahBenar(int jumlahBenar) { this.jumlahBenar = jumlahBenar; }
    public void setJumlahSalah(int jumlahSalah) { this.jumlahSalah = jumlahSalah; }
    public void setTotalSoal(int totalSoal) { this.totalSoal = totalSoal; }
    public void setPersentase(int persentase) { this.persentase = persentase; }
}