package com.cl.cinelatte.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssentoService {

    @Autowired
    AssentoDAO assentoDAO;

    public List<Assento> obterAssentosPorSala(int salaId){
        return assentoDAO.obterAssentosPorSala(salaId);
    }


    public List<Assento> obterAssentosSelecionados(List<Integer> ids) {
        List<Assento> assentos = new ArrayList<>();

        for(Integer id : ids) {
         assentos.add(assentoDAO.obterAssento(id));
        }

        return assentos;
    }

}
