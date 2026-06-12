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
public class SalaDAO {

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

     public List<Sala> obterTodasSalas() {
        String sql = "SELECT * FROM sala ORDER BY id";
        List<Map<String, Object>> registros = jdbc.queryForList(sql);
 
        List<Sala> salas = new ArrayList<>();
        
        for (Map<String, Object> registro : registros) {
            int id = (int) registro.get("id");
            String nome = (String) registro.get("nome");
            salas.add(new Sala(id, nome));
        }
        return salas;
    }

    //select de sala por id pra mostrar na página de reservas
    public Sala obterSala(int id) {
        String sql = "SELECT * FROM sala WHERE id = ?";
        Map<String, Object> r = jdbc.queryForMap(sql, id);
        return new Sala((int) r.get("id"), (String) r.get("nome")); //converter registro já mandando o objeto (só tem 2 atributos mesmo u.u)
    }

}
