package com.cl.cinelatte.models;

// POJO - Plain Old Java Object
public class Sala {

    private int id;
    private String nome;

    // Construtor vazio
    public Sala() {}

    // Construtor para INSERT (sem id)
    public Sala(String nome) {
        this.nome = nome;
    }

    // Construtor para SELECT (com id)
    public Sala(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}