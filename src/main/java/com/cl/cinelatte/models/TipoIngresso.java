package com.cl.cinelatte.models;

public enum TipoIngresso {
    INTEIRA(30.00),
    MEIA(15.00);

    private final double preco;

    TipoIngresso(double preco) {
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }
}
