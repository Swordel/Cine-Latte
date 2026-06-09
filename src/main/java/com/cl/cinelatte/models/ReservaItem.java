package com.cl.cinelatte.models;

// POJO :D
// Representa um assento dentro de uma compra (Reserva)
public class ReservaItem {
    private int id;
    private int reservaId;
    private int assentoId;
    private TipoIngresso tipoIngresso;
    private double valor;

    // Construtor vazio
    public ReservaItem() {}

    // Construtor para INSERT (sem id)
    public ReservaItem(int reservaId, int assentoId, TipoIngresso tipoIngresso, double valor) {
        this.reservaId = reservaId;
        this.assentoId = assentoId;
        this.tipoIngresso = tipoIngresso;
        this.valor = valor;
    }

    // Construtor para SELECT (com id)
    public ReservaItem(int id, int reservaId, int assentoId, TipoIngresso tipoIngresso, double valor) {
        this.id = id;
        this.reservaId = reservaId;
        this.assentoId = assentoId;
        this.tipoIngresso = tipoIngresso;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public int getReservaId() {
        return reservaId;
    }

    public int getAssentoId() {
        return assentoId;
    }

    public TipoIngresso getTipoIngresso() {
        return tipoIngresso;
    }

    public double getValor() {
        return valor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReservaId(int reservaId) {
        this.reservaId = reservaId;
    }

    public void setAssentoId(int assentoId) {
        this.assentoId = assentoId;
    }

    public void setTipoIngresso(TipoIngresso tipoIngresso) {
        this.tipoIngresso = tipoIngresso;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }



}
