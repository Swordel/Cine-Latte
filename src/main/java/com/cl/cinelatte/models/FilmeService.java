package com.cl.cinelatte.models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilmeService {

    @Autowired
    FilmeDAO filmeDAO;

    // Insere o filme e seus gêneros em sequência
    public void inserirFilme(Filme filme, List<FilmeGenero> generos) {
        int idFilme = filmeDAO.inserirFilme(filme);
        for (FilmeGenero genero : generos) {
            filmeDAO.inserirFilmeGenero(idFilme, genero);
        }
    }

    // SELECT um filme por id (com gêneros carregados)
    public Filme obterFilme(int id) {
        Filme filme = filmeDAO.obterFilme(id);
        filme.setGeneros(filmeDAO.obterGenerosPorFilme(id));
        return filme;
    }

    //SELECT todos filmes cadastrados
    public List<Filme> obterTodosFilmes() {
        return filmeDAO.obterTodosFilmes();
    }
    
    // SELECT filmes por status
    public List<Filme> obterFilmesPorStatus(FilmeStatus status) {
        return filmeDAO.obterFilmesPorStatus(status);
    }

    public void atualizarFilme(int id, Filme filme, List<FilmeGenero> generos) {
        filmeDAO.atualizarFilme(id, filme);
        
        // Apaga os gêneros antigos e reinsere os novos
        filmeDAO.deletarGeneros(id);
        for (FilmeGenero g : generos) {
            filmeDAO.inserirFilmeGenero(id, g);
        }
    }

    public void deletarFilme(int id) {
        filmeDAO.deletarFilme(id);
    }

}
