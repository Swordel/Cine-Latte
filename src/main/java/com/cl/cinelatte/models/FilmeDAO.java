package com.cl.cinelatte.models;

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
        String sql = "INSERT INTO filme(titulo, sinopse, duracao, classificacao, nota, imagem, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] obj = new Object[7];
        obj[0] = (String) filme.getTitulo();
        obj[1] = (String) filme.getSinopse();
        obj[2] = (int) filme.getDuracao();
        obj[3] = (String) filme.getClassificacao();
        obj[4] = (double) filme.getNota();
        obj[5] = (String) filme.getImagem();
        obj[6] = (String) filme.getStatus().name();  // .name() retorna "EM_CARTAZ" ou "EM_BREVE"
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
}
