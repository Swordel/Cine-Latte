package com.cl.cinelatte.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

// POJO - Plain Old Java Object
// Editado: Reserva representa uma compra -> uma reserva pode ter vários assentos/itens (ReservaItem)
public class Reserva {

    private int id;
    private int sessaoId;
    private LocalDateTime dtCompra;
    private double valorTotal;
    private FormaPagamento formaPagamento;
    private boolean pago;
    private String nomeCliente;

    // Construtor vazio
    public Reserva() {}

    // Construtor para INSERT (sem id e sem dtCompra, pq o banco preenche com NOW())
    public Reserva(int sessaoId, double valorTotal, FormaPagamento formaPagamento, String nomeCliente) {
        this.sessaoId   = sessaoId;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.nomeCliente = nomeCliente;
        this.pago = true; // pagamento fake: sempre aprovado
    }

    // Construtor para SELECT (com id e dtCompra)
    public Reserva(int id, int sessaoId, LocalDateTime dtCompra, double valorTotal, FormaPagamento formaPagamento, boolean pago, String nomeCliente) {
        this.id = id;
        this.sessaoId = sessaoId;
        this.dtCompra = dtCompra;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.pago = pago;
        this.nomeCliente = nomeCliente;
    }

    public static Reserva converterRegistros(Map<String, Object> registro) {
        int id = (int) registro.get("id");
        int sessaoId = (int) registro.get("sessao_id");
        LocalDateTime dt = ((java.sql.Timestamp) registro.get("dt_compra")).toLocalDateTime();
        double valorTotal = ((Number) registro.get("valor_total")).doubleValue();
        FormaPagamento formaPagamento = FormaPagamento.valueOf((String) registro.get("forma_pagamento"));
        boolean pago = (boolean) registro.get("pago");
        String nomeCliente = (String) registro.get("nome_cliente");

        return new Reserva(id, sessaoId, dt, valorTotal, formaPagamento, pago, nomeCliente);
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

    public String getNomeCliente() {
        return nomeCliente;
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

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getDtCompraFormatada() {
        return dtCompra.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

}