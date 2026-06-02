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

    // INSERT de uma reserva
    public void inserirReserva(Reserva reserva) {
        String sql = "INSERT INTO reserva(assento_id, sessao_id, status_assento) VALUES (?, ?, ?)";
        Object[] obj = new Object[3];
        obj[0] = reserva.getAssentoId();
        obj[1] = reserva.getSessaoId();
        obj[2] = reserva.getStatus().name();
        jdbc.update(sql,obj);
    }

    
    /* SELECT IDs dos assentos ocupados em uma sessão
       Retorna só os IDs para o HTML verificar com contains() */
    public List<Integer> obterAssentosOcupadosPorSessao(int sessaoId) {
        String sql = "SELECT assento_id FROM reserva WHERE sessao_id = ? AND status_assento = 'OCUPADO'";
        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, sessaoId);
        ArrayList<Integer> aux = new ArrayList<>();
        for (Map<String, Object> registro : listaRegistros) {
            aux.add((Integer) registro.get("assento_id"));
        }
        return aux;
    }

}
