package com.cl.cinelatte.models;

// POJO - Plain Old Java Object

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Sessao {

    private int id;
    private int filmeId;
    private int salaId;
    private LocalDate dt;
    private LocalTime horario;
    private SessaoIdioma idioma; //legendado ou dublado :D

    // Construtor vazio
    public Sessao() {}

    // Construtor para INSERT (sem id)
    public Sessao(int filmeId, int salaId, LocalDate dt, LocalTime horario, SessaoIdioma idioma) {
        this.filmeId = filmeId;
        this.salaId = salaId;
        this.dt = dt;
        this.horario = horario;
        this.idioma = idioma;
    }

    // Construtor para SELECT (com id)
    public Sessao(int id, int filmeId, int salaId, LocalDate dt, LocalTime horario, SessaoIdioma idioma) {
        this.id = id;
        this.filmeId = filmeId;
        this.salaId = salaId;
        this.dt = dt;
        this.horario = horario;
        this.idioma = idioma;
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

    public LocalDate getDt() {
        return dt;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public SessaoIdioma getIdioma() {
        return idioma;
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

   public void setDt(LocalDate dt) {
        this.dt = dt;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public void setIdioma(SessaoIdioma idioma) {
        this.idioma = idioma;
    }

    // Converte um registro do banco (Map) para um objeto Sessao
    public static Sessao converterRegistros(Map<String, Object> registro) {
        int id = (int) registro.get("id");
        int filmeId = (int) registro.get("filme_id");
        int salaId = (int) registro.get("sala_id");
 
        // O PostgreSQL retorna DATE como java.sql.Date — converte para LocalDate
        LocalDate dt = ((java.sql.Date) registro.get("dt_sessao")).toLocalDate(); // Recupera o objeto java.sql.Date primeiro e depois converte
 
        // O PostgreSQL retorna TIME como java.sql.Time — converte para LocalTime
        LocalTime horario = ((java.sql.Time) registro.get("horario")).toLocalTime();
 
        SessaoIdioma idioma = SessaoIdioma.valueOf((String) registro.get("idioma"));
 
        return new Sessao(id, filmeId, salaId, dt, horario, idioma);
    }

    // Retorna o dia da semana abreviado: "SEG", "TER", etc.
    // avaliando se vou usar mesmo, mas já deixei pronto ~
    public String getDiaSemana() {
        return switch (dt.getDayOfWeek()) {
            case MONDAY    -> "SEG";
            case TUESDAY   -> "TER";
            case WEDNESDAY -> "QUA";
            case THURSDAY  -> "QUI";
            case FRIDAY    -> "SEX";
            case SATURDAY  -> "SÁB";
            case SUNDAY    -> "DOM";
        };
    }

    // Retorna a data formatada para exibição nas sessões: "26/05" >:D
    public String getDataFormatada() {
        return dt.format(DateTimeFormatter.ofPattern("dd/MM"));
    }

    // Retorna o horário formatado, ex.: "15:40"
    public String getHorarioFormatado() {
        return horario.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
}