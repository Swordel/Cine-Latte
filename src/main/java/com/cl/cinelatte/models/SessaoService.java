package com.cl.cinelatte.models;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessaoService {

    @Autowired
    SessaoDAO sessaoDAO;

      public void inserirSessao(Sessao sessao) {
        sessaoDAO.inserirSessao(sessao);
    }

    public Sessao obterSessao(int id) {
        return sessaoDAO.obterSessao(id);
    }
 
    public List<LocalDate> obterDatasDoFilme(int filmeId) {
        return sessaoDAO.obterDatasDoFilme(filmeId);
    }
 
     public List<Sessao> obterSessoesPorFilmeDataIdioma(int filmeId, LocalDate data, SessaoIdioma idioma) {
        return sessaoDAO.obterSessoesPorFilmeDataIdioma(filmeId, data, idioma);
    }
 
    public List<SessaoIdioma> obterIdiomasDisponiveis(int filmeId, LocalDate data) {
        return sessaoDAO.obterIdiomasDisponiveis(filmeId, data);
    }

}
