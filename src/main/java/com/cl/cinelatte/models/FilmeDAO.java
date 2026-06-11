package com.cl.cinelatte.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        String sql = "INSERT INTO filme(titulo, sinopse, duracao, classificacao, imagem, banner, status_filme, data_estreia) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] obj = new Object[8];
        obj[0] = filme.getTitulo();
        obj[1] = filme.getSinopse();
        obj[2] = filme.getDuracao();
        obj[3] = filme.getClassificacao();
        obj[4] = filme.getImagem();
        obj[5] = filme.getBanner();
        obj[6] = filme.getStatus().name();  // .name() retorna "EM_CARTAZ" ou "EM_BREVE"
        obj[7] = filme.getDataEstreia() != null ? java.sql.Date.valueOf(filme.getDataEstreia()) : null;
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

    // UPDATE: atualiza todos os campos de texto do filme (sem imagem)
    public void atualizarFilme(int id, Filme novo) {
        String sql = "UPDATE filme SET titulo = ?, sinopse = ?, duracao = ?, classificacao = ?, status_filme = ?, data_estreia = ? WHERE id = ?";
        Object[] obj = new Object[7];
        obj[0] = novo.getTitulo();
        obj[1] = novo.getSinopse();
        obj[2] = novo.getDuracao();
        obj[3] = novo.getClassificacao();
        obj[4] = novo.getStatus().name();
        obj[5] = novo.getDataEstreia() != null ? java.sql.Date.valueOf(novo.getDataEstreia()) : null;
        obj[6] = id;
        jdbc.update(sql, obj);
    }
     
    // Necessário para o atualizarFilme no Service poder recriar os gêneros
    // Apago só os gêneros e não o filme inteiro
    public void deletarGeneros(int idFilme) {
        jdbc.update("DELETE FROM filme_genero WHERE id_filme = ?", idFilme);
    }



    // DELETE: ordem importa por causa da FK >:D
    //Essa anotação abaixo -> se qualquer etapa falhar, tudo é desfeito:
    @Transactional
    public void deletarFilme(int id) {

    // 1.  Seleciona todos os IDs de reservas feitas para o filme definido
    // e apago da tabela de itens todos os registros que pertencem às reservas encontradas
    // As três aspas (""") são usadas para criar uma String de Múltiplas Linhas e facilitar a escrita do SQL
    jdbc.update("""
        DELETE FROM reserva_item
        WHERE reserva_id IN (
            SELECT r.id FROM reserva r
            JOIN sessao s ON r.sessao_id = s.id
            WHERE s.filme_id = ?
        )""", id);

    // 2. reservas com sessões desse filme
    jdbc.update("""
        DELETE FROM reserva
        WHERE sessao_id IN (
            SELECT id FROM sessao WHERE filme_id = ?
        )""", id);

    // 3. sessões do filme
    jdbc.update("DELETE FROM sessao WHERE filme_id = ?", id);

    // 4. gêneros do filme
    jdbc.update("DELETE FROM filme_genero WHERE id_filme = ?", id);

    // 5. o filme
    jdbc.update("DELETE FROM filme WHERE id = ?", id);
    }

}
