package com.cl.cinelatte.models;

// POJO - Plain Old Java Object
// Liga um assento a uma sessão com um status
public class Reserva {

    private int id;
    private int assentoId;
    private int sessaoId;
    private ReservaStatus status;

    // Construtor vazio
    public Reserva() {}

    // Construtor para INSERT (sem id)
    public Reserva(int assentoId, int sessaoId, ReservaStatus status) {
        this.assentoId = assentoId;
        this.sessaoId = sessaoId;
        this.status = status;
    }

    // Construtor para SELECT (com id)
    public Reserva(int id, int assentoId, int sessaoId, ReservaStatus status) {
        this.id = id;
        this.assentoId = assentoId;
        this.sessaoId = sessaoId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getAssentoId() {
        return assentoId;
    }

    public int getSessaoId() {
        return sessaoId;
    }

    public ReservaStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAssentoId(int assentoId) {
        this.assentoId = assentoId;
    }

    public void setSessaoId(int sessaoId) {
        this.sessaoId = sessaoId;
    }

    public void setStatus(ReservaStatus status) {
        this.status = status;
    }

}