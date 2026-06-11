package com.cl.cinelatte.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class ReservaDAO {

    @Autowired
    DataSource dataSource;
 
    JdbcTemplate jdbc;
 
    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    // INSERT da reserva: dt_compra é preenchida pelo banco com NOW()
    // O NOW() retorna a data e hora atual do servidor no momento da execução da query
    // INSERT da reserva retorna o id gerado :9
    public int inserirReserva(Reserva reserva) {
        String sql = "INSERT INTO reserva(sessao_id, dt_compra, valor_total, forma_pagamento, pago) VALUES (?, NOW(), ?, ?, ?)";
        Object[] obj = new Object[4];
        obj[0] = reserva.getSessaoId();
        obj[1] = reserva.getValorTotal();
        obj[2] = reserva.getFormaPagamento().name();
        obj[3] = reserva.isPago();
        jdbc.update(sql, obj);
 
        String sqlId = "SELECT MAX(id) FROM reserva";
        return jdbc.queryForObject(sqlId, Integer.class);
    }
    
     // INSERT de um item da reserva
    public void inserirReservaItem(ReservaItem item) {
        String sql = "INSERT INTO reserva_item(reserva_id, assento_id, tipo_ingresso, valor) " +
                     "VALUES (?, ?, ?, ?)";
        Object[] obj = new Object[4];
        obj[0] = item.getReservaId();
        obj[1] = item.getAssentoId();
        obj[2] = item.getTipoIngresso().name();
        obj[3] = item.getValor();
        jdbc.update(sql, obj);
    }

    // SELECT IDs dos assentos ocupados em uma sessão
    // Busca via reserva_item JOIN reserva
    // Retorna só os IDs para o HTML verificar com contains()
    public List<Integer> obterAssentosOcupadosPorSessao(int sessaoId) {
        String sql = "SELECT ri.assento_id FROM reserva_item ri " +
                     "JOIN reserva r ON r.id = ri.reserva_id " +
                     "WHERE r.sessao_id = ?";

        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, sessaoId);
        ArrayList<Integer> aux = new ArrayList<>();

        for (Map<String, Object> registro : listaRegistros) {
            aux.add((Integer) registro.get("assento_id"));
        }

        return aux;
    }

}
