package com.cl.cinelatte.models;

public enum FilmeGenero {
    ACAO("Ação"),
    ANIMACAO("Animação"),
    AVENTURA("Aventura"),
    COMEDIA("Comédia"),
    DRAMA("Drama"),
    FANTASIA("Fantasia"),
    FICCAO_CIENTIFICA("Ficção Científica"),
    MUSICAL("Musical"),
    ROMANCE("Romance"),
    SUSPENSE("Suspense"),
    TERROR("Terror");

    private final String descricao;

    FilmeGenero(String descricao){  //construtor privado implicito
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}
