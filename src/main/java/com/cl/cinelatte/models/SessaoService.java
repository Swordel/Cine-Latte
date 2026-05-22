package com.cl.cinelatte.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessaoService {

    @Autowired
    SessaoDAO sessaoDAO;

      public void inserirSessao(Sessao sessao) {
        sessaoDAO.inserirSessao(sessao);
    }

}
