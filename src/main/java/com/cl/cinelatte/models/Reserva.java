package com.cl.cinelatte.models;

import java.time.LocalDateTime;

// POJO - Plain Old Java Object
// Editado: Reserva representa uma compra -> uma reserva pode ter vários assentos/itens (ReservaItem)
public class Reserva {

    private int id;
    private int sessaoId;
    private LocalDateTime dtCompra;
    private double valorTotal;
    private FormaPagamento formaPagamento;
    private boolean pago;

    // Construtor vazio
    public Reserva() {}

    // Construtor para INSERT (sem id e sem dtCompra, pq o banco preenche com NOW())
    public Reserva(int sessaoId, double valorTotal, FormaPagamento formaPagamento) {
        this.sessaoId   = sessaoId;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.pago = true; // pagamento fake: sempre aprovado
    }

    // Construtor para SELECT (com id e dtCompra)
    public Reserva(int id, int sessaoId, LocalDateTime dtCompra, double valorTotal, FormaPagamento formaPagamento, boolean pago) {
        this.id = id;
        this.sessaoId = sessaoId;
        this.dtCompra = dtCompra;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.pago = pago;
    }

    public int getId() {
        return id;
    }

    public int getSessaoId() {
        return sessaoId;
    }

    public LocalDateTime getDtCompra() {
        return dtCompra;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public boolean isPago() {
        return pago;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSessaoId(int sessaoId) {
        this.sessaoId = sessaoId;
    }

    public void setDtCompra(LocalDateTime dtCompra) {
        this.dtCompra = dtCompra;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }



}