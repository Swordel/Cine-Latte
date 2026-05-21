package com.cl.cinelatte.models;

// POJO - Plain Old Java Object
public class Assento {

    private int id;
    private int salaId;
    private String fileira;  // "A", "B", ..., "L"
    private int numero;      // 1 a 22, 19 ou 28 dependendo da fileira
    private AssentoTipo tipo;

    // Construtor vazio
    public Assento() {}

    // Construtor para INSERT (sem id)
    public Assento(int salaId, String fileira, int numero, AssentoTipo tipo) {
        this.salaId = salaId;
        this.fileira = fileira;
        this.numero = numero;
        this.tipo = tipo;
    }

    // Construtor para SELECT (com id)
    public Assento(int id, int salaId, String fileira, int numero, AssentoTipo tipo) {
        this.id = id;
        this.salaId = salaId;
        this.fileira = fileira;
        this.numero = numero;
        this.tipo = tipo;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getSalaId() {
        return salaId;
    }

    public String getFileira() {
        return fileira;
    }

    public int getNumero() {
        return numero;
    }

    public AssentoTipo getTipo() {
        return tipo;
    }
    
    // Retorna o código alfanumérico do assento: "A1", "C5", "L28", etc.
    public String getCodigo() {
        return fileira + numero;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSalaId(int salaId) {
        this.salaId = salaId;
    }

    public void setFileira(String fileira) {
        this.fileira = fileira;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setTipo(AssentoTipo tipo) {
        this.tipo = tipo;
    }    
}