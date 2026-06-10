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

     // Verifica se já existem assentos para uma sala
    public boolean existeAssentoNaSala(int salaId) {
        String sql = "SELECT COUNT(*) FROM assento WHERE sala_id = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, salaId);
        return count != null && count > 0;
    }
 
    // SELECT todos os assentos de uma sala, ordenados por fileira e número
    public List<Assento> obterAssentosPorSala(int salaId) {
        String sql = "SELECT * FROM assento WHERE sala_id = ? ORDER BY fileira, numero";
        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, salaId);
        ArrayList<Assento> aux = new ArrayList<>();
        for (Map<String, Object> registro : listaRegistros) {
            aux.add(Assento.converterRegistros(registro));
        }
        return aux;
    }

    //SELECT de assentos por IDs selecionados, pois na página de ingressos preciso obter o código deles!!
    // Eu ía fazer assim: public List<Assento> obterAssentos(List<Integer> ids){ ...}
    // para fazer apenas 1 consulta no banco, mas precisa de placeholders que ainda não entendi bem, então evitei.
        public Assento obterAssento(int id){
        String sql = "SELECT * FROM assento WHERE id = ?";
        Map<String, Object> registro = jdbc.queryForMap(sql, id);
        return Assento.converterRegistros(registro);
    }

}
