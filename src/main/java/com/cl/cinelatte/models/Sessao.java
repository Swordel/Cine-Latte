package com.cl.cinelatte.models;

// POJO - Plain Old Java Object
public class Sessao {

    private int id;
    private int filmeId;
    private int salaId;
    private String data;     // formato "DD/MM/YYYY"
    private String horario;  // formato "HH:MM"

    // Construtor vazio
    public Sessao() {}

    // Construtor para INSERT (sem id)
    public Sessao(int filmeId, int salaId, String data, String horario) {
        this.filmeId = filmeId;
        this.salaId = salaId;
        this.data = data;
        this.horario = horario;
    }

    // Construtor para SELECT (com id)
    public Sessao(int id, int filmeId, int salaId, String data, String horario) {
        this.id = id;
        this.filmeId = filmeId;
        this.salaId = salaId;
        this.data = data;
        this.horario = horario;
    }

    public int getId() {
        return id;
    }

    public int getFilmeId() {
        return filmeId;
    }

    public int getSalaId() {
        return salaId;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFilmeId(int filmeId) {
        this.filmeId = filmeId;
    }

    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    
}