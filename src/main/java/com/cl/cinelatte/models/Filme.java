package com.cl.cinelatte.models;

import java.util.List;

// POJO - Plain Old Java Object
public class Filme {
    private int id;
    private String titulo;
    private String sinopse;
    private int duracao; //vou preencher em minutos e depois converto pra horas pra exibir
    private String classificacao;
    private double nota;
    private String imagem;   // ex.:nome do pôster: "cartazPrada.png"
    private FilmeStatus status;   // apenas "EM_CARTAZ" ou "EM_BREVE"
    private List<FilmeGenero> generos; // preenchido separadamente via filme_genero

    // Construtor vazio
    public Filme() {}

    // Construtor para INSERT (sem id e sem o gênero)
    public Filme(String titulo, String sinopse, int duracao,
                 String classificacao, double nota, String imagem, FilmeStatus status) {
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.duracao = duracao;
        this.classificacao = classificacao;
        this.nota = nota;
        this.imagem = imagem;
        this.status = status;
    }

    // Construtor para SELECT (com id, sem generos -> generos são carregados separados)
    public Filme(int id, String titulo, String sinopse, int duracao,
                 String classificacao, double nota, String imagem, FilmeStatus status) {
        this.id = id;
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.duracao = duracao;
        this.classificacao = classificacao;
        this.nota = nota;
        this.imagem = imagem;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public int getDuracao() {
        return duracao;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public double getNota() {
        return nota;
    }

    public String getImagem() {
        return imagem;
    }

    public FilmeStatus getStatus() {
        return status;
    }

    public List<FilmeGenero> getGeneros() {
        return generos;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setStatus(FilmeStatus status) {
        this.status = status;
    }

    public void setGeneros(List<FilmeGenero> generos) {
        this.generos = generos;
    }

}
