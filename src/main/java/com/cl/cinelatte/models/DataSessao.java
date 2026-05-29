package com.cl.cinelatte.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataSessao {
    private LocalDate dtSessao;

    public DataSessao(LocalDate dtSessao) {
        this.dtSessao = dtSessao;
    }

    public LocalDate getDtSessao() {
        return dtSessao;
    }

    // Retorna a data formatada para exibição nas sessões: "26/05" >:D
    public String getDataFormatada() {
        return dtSessao.format(DateTimeFormatter.ofPattern("dd/MM"));
    }

    // Retorna o dia da semana abreviado: "SEG", "TER", etc.
    // aprendendo a usar o switch novo~
    public String getDiaSemana() {
        return switch (dtSessao.getDayOfWeek()) {
            case MONDAY    -> "SEG";
            case TUESDAY   -> "TER";
            case WEDNESDAY -> "QUA";
            case THURSDAY  -> "QUI";
            case FRIDAY    -> "SEX";
            case SATURDAY  -> "SÁB";
            case SUNDAY    -> "DOM";
        };
    }

}
