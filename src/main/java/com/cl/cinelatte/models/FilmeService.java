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

}
