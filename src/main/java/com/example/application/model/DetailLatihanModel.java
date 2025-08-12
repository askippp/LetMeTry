package com.example.application.model;

import java.time.LocalDateTime;

public class DetailLatihanModel {
    private int idDetailLatsol;
    private int idLatsol;
    private int idSoalLatsol;
    private int idJawaban;
    private LocalDateTime startedAt;
    private LocalDateTime endAt;
    private double nilai;

    // Constructors, getters, and setters
    public DetailLatihanModel() {}

    public int getIdDetailLatsol() {
        return idDetailLatsol;
    }

    public void setIdDetailLatsol(int idDetailLatsol) {
        this.idDetailLatsol = idDetailLatsol;
    }

    public int getIdLatsol() {
        return idLatsol;
    }

    public void setIdLatsol(int idLatsol) {
        this.idLatsol = idLatsol;
    }

    public int getIdSoalLatsol() {
        return idSoalLatsol;
    }

    public void setIdSoalLatsol(int idSoalLatsol) {
        this.idSoalLatsol = idSoalLatsol;
    }

    public int getIdJawaban() {
        return idJawaban;
    }

    public void setIdJawaban(int idJawaban) {
        this.idJawaban = idJawaban;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }
}