package com.example.application.model;

import java.time.LocalDateTime;

public class LatihanModel {
    private int idLatsol;
    private int idUser;
    private int idMateri;
    private double nilai;
    private LocalDateTime createdAt;

    // Constructors, getters, and setters
    public LatihanModel() {}

    public int getIdLatsol() {
        return idLatsol;
    }

    public void setIdLatsol(int idLatsol) {
        this.idLatsol = idLatsol;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public void setIdMateri(int idMateri) {
        this.idMateri = idMateri;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}