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
public class FilmeDAO {

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbc;

    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    // INSERT: deixou de ser void e agora retorna o id gerado para uso na filme_genero
    // pois um filme pode ter muitos generos e vice-versa, então fiz a tabela assim.
    public int inserirFilme(Filme filme) {
        String sql = "INSERT INTO filme(titulo, sinopse, duracao, classificacao, nota, imagem, status_filme) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] obj = new Object[7];
        obj[0] = (String) filme.getTitulo();
        obj[1] = (String) filme.getSinopse();
        obj[2] = filme.getDuracao();
        obj[3] = (String) filme.getClassificacao();
        obj[4] = filme.getNota();
        obj[5] = (String) filme.getImagem();
        obj[6] = filme.getStatus().name();  // .name() retorna "EM_CARTAZ" ou "EM_BREVE"
        jdbc.update(sql, obj);

        // Busca o id gerado pelo banco
        // tem como fazer com KeyHolder que é próprio pra isso, mas não entendi ainda muito bem
        String sqlId = "SELECT MAX(id) FROM filme";
        return jdbc.queryForObject(sqlId, Integer.class);
    }

     // INSERT na tabela associativa filme_genero
    public void inserirFilmeGenero(int idFilme, FilmeGenero genero) {
        String sql = "INSERT INTO filme_genero(id_filme, genero) VALUES (?, ?)";
        Object[] obj = new Object[2];
        obj[0] = idFilme;
        obj[1] = genero.name();
        jdbc.update(sql, obj);
    }

      // SELECT um filme por id
    public Filme obterFilme(int id) {
        String sql = "SELECT * FROM filme WHERE id = ?";
        Map<String, Object> registro = jdbc.queryForMap(sql, id);
        return Filme.converterRegistros(registro);
    }

     // SELECT todos os filmes de um status (EM_CARTAZ ou EM_BREVE)
    public List<Filme> obterFilmesPorStatus(FilmeStatus status) {
        String sql = "SELECT * FROM filme WHERE status_filme = ? ORDER BY id";
        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, status.name());
        ArrayList<Filme> aux = new ArrayList<>();
        for (Map<String, Object> registro : listaRegistros) {
            aux.add(Filme.converterRegistros(registro));
        }
        return aux;
    }

    // SELECT gêneros de um filme
    public List<FilmeGenero> obterGenerosPorFilme(int idFilme) {
        String sql = "SELECT genero FROM filme_genero WHERE id_filme = ?";
        List<Map<String, Object>> listaRegistros = jdbc.queryForList(sql, idFilme);
        ArrayList<FilmeGenero> aux = new ArrayList<>();
        for (Map<String, Object> registro : listaRegistros) {
            aux.add(FilmeGenero.valueOf((String) registro.get("genero")));
        }
        return aux;
    }



}
