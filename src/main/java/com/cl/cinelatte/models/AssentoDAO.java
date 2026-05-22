package com.cl.cinelatte.models;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class AssentoDAO {

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

     // INSERT de um assento
    public void salvar(Assento assento) {
        String sql = "INSERT INTO assento(sala_id, fileira, numero, tipo) VALUES (?, ?, ?, ?)";
        Object[] obj = new Object[4];
        obj[0] = assento.getSalaId();
        obj[1] = (String) assento.getFileira();
        obj[2] = assento.getNumero();
        obj[3] = assento.getTipo().name();
        jdbc.update(sql, obj);
    }

    //VERIFICAR OS 2 DEBAIXO

     // Verifica se já existem assentos para uma sala
    public boolean existeAssentoNaSala(int salaId) {
        String sql = "SELECT COUNT(*) FROM assento WHERE sala_id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, salaId);
        return count != null && count > 0;
    }
 
 /*
    // SELECT todos os assentos de uma sala
    public List<Assento> listarPorSala(int salaId) {
        String sql = "SELECT * FROM assento WHERE sala_id = ? ORDER BY fileira, numero";
        return jdbc.query(sql, new Object[]{salaId}, (rs, rowNum) -> new Assento(
            rs.getInt("id"),
            rs.getInt("sala_id"),
            rs.getString("fileira"),
            rs.getInt("numero"),
            AssentoTipo.valueOf(rs.getString("tipo"))
        ));
    }
*/
}
