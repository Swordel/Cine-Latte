package com.cl.cinelatte.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// POJO - Plain Old Java Object
public class Filme {
    private int id;
    private String titulo;
    private String sinopse;
    private Integer duracao; //vou preencher em minutos e depois converto pra horas pra exibir
    private String classificacao;
    private String imagem;   // ex.:nome do pôster: "cartazPrada.png"
    private String banner;  // nome do arquivo do banner, ex.: "bannerPrada.webp"
    private FilmeStatus status;   // apenas "EM_CARTAZ" ou "EM_BREVE"
    private LocalDate dataEstreia;
    private List<FilmeGenero> generos; // preenchido separadamente via filme_genero

    // Construtor vazio
    public Filme() {}

    // Construtor para INSERT (sem id e sem o gênero)
    public Filme(String titulo, String sinopse, Integer duracao,
                 String classificacao, String imagem, String banner, FilmeStatus status, LocalDate dataEstreia) {
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.duracao = duracao;
        this.classificacao = classificacao;
        this.imagem = imagem;
        this.banner = banner;
        this.status = status;
        this.dataEstreia = dataEstreia;
    }

    // Construtor para SELECT (com id, sem generos -> generos são carregados separados)
    public Filme(int id, String titulo, String sinopse, Integer duracao,
                 String classificacao, String imagem, String banner, FilmeStatus status, LocalDate dataEstreia) {
        this.id = id;
        this.titulo = titulo;
        this.sinopse = sinopse;
        this.duracao = duracao;
        this.classificacao = classificacao;
        this.imagem = imagem;
        this.banner = banner;
        this.status = status;
        this.dataEstreia = dataEstreia;
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

    public Integer getDuracao() {
        return duracao;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public String getImagem() {
        return imagem;
    }

    public String getBanner() {
        return banner;
    }

    public FilmeStatus getStatus() {
        return status;
    }

    public LocalDate getDataEstreia() {
        return dataEstreia;
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

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setStatus(FilmeStatus status) {
        this.status = status;
    }

    public void setDataEstreia(LocalDate dataEstreia) {
        this.dataEstreia = dataEstreia;
    }

    public void setGeneros(List<FilmeGenero> generos) {
        this.generos = generos;
    }

     // Converte um registro do banco (Map) para um objeto Filme
    public static Filme converterRegistros(Map<String, Object> registro) {
        int id               = (int) registro.get("id");
        String titulo        = (String) registro.get("titulo");
        String sinopse       = (String) registro.get("sinopse");
        Integer duracao      = (Integer) registro.get("duracao");
        String classificacao = (String) registro.get("classificacao");
        String imagem        = (String) registro.get("imagem");
        String banner        = (String) registro.get("banner");
        FilmeStatus status   = FilmeStatus.valueOf((String) registro.get("status_filme"));
        LocalDate dataEstreia = registro.get("data_estreia") != null ? 
                                                 ((java.sql.Date) registro.get("data_estreia")).toLocalDate() : null;
        return new Filme(id, titulo, sinopse, duracao, classificacao, imagem, banner, status, dataEstreia);
    }

    // Retorna a duração formatada: 130 -> "2h 10min"
    public String getDuracaoFormatada() {
        if (duracao == null) 
            return "Duração a definir";
            
        int horas = duracao / 60;
        int minutos = duracao % 60;
        
        if (minutos == 0)
            return horas + "h";

        return horas + "h " + minutos + "min";
    }

}
