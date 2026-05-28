package com.cl.cinelatte.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // Converte LocalDate p/ java.sql.Date para o banco
        obj[2] = java.sql.Date.valueOf(sessao.getDt());

        // Converte LocalTime p/ java.sql.Time para o banco
        obj[3] = java.sql.Time.valueOf(sessao.getHorario());

        obj[4] = sessao.getIdioma().name();

        jdbc.update(sql, obj);
    }

    // SELECT uma sessão por id
    public Sessao obterSessao(int id) {
        String sql = "SELECT * FROM sessao WHERE id = ?";
        Map<String, Object> registro = jdbc.queryForMap(sql, id);
        return Sessao.converterRegistros(registro);
    }

    // SELECT datas distintas de um filme, ordenadas
    public List<LocalDate> obterDatasDoFilme(int filmeId) {
        String sql = "SELECT DISTINCT dt_sessao FROM sessao WHERE filme_id = ? ORDER BY dt_sessao";
        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, filmeId);

        ArrayList<LocalDate> aux = new ArrayList<>();
        for (Map<String, Object> registro : listaRegistros) {
            aux.add(((java.sql.Date) registro.get("dt_sessao")).toLocalDate());
        }

        return aux;
    }

    // SELECT sessões de um filme, data e idioma específicos, ordenadas por horário
    // Esse é o método principal que o controller vai usar para separar LEG e DUB
    public List<Sessao> obterSessoesPorFilmeDataIdioma(int filmeId, LocalDate data, SessaoIdioma idioma) {
        String sql = "SELECT * FROM sessao " +
                     "WHERE filme_id = ? AND dt_sessao = ? AND idioma = ? " +
                     "ORDER BY horario";
        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, filmeId, java.sql.Date.valueOf(data), idioma.name());

        ArrayList<Sessao> aux = new ArrayList<>();
        for (Map<String, Object> registro : listaRegistros) {
            aux.add(Sessao.converterRegistros(registro));
        }

        return aux;
    }

    // SELECT todos os idiomas disponíveis para um filme em uma data
    // Usado para saber quais grupos mostrar na tela (só LEG, só DUB, ou os dois)
    public List<SessaoIdioma> obterIdiomasDisponiveis(int filmeId, LocalDate data) {
        String sql = "SELECT DISTINCT idioma FROM sessao WHERE filme_id = ? AND dt_sessao = ? ORDER BY idioma";

        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, filmeId, java.sql.Date.valueOf(data));
        
        ArrayList<SessaoIdioma> aux = new ArrayList<>();
        for (Map<String, Object> registro : listaRegistros) {
            aux.add(SessaoIdioma.valueOf((String) registro.get("idioma")));
        }
        return aux;
    }

}
