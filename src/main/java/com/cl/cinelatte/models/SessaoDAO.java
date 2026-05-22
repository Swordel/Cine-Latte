package com.cl.cinelatte.models;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class SessaoDAO {
    
    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    // INSERT
    public void inserirSessao(Sessao sessao) {
        String sql = "INSERT INTO sessao(filme_id, sala_id, dt_sessao, horario, idioma) VALUES (?, ?, ?, ?, ?)";
        Object[] obj = new Object[5];
        obj[0] = sessao.getFilmeId();
        obj[1] = sessao.getSalaId();
        obj[2] = (String) sessao.getData();
        obj[3] = (String) sessao.getHorario();
        obj[4] = sessao.getIdioma().name();
        jdbc.update(sql, obj);
    }

}
