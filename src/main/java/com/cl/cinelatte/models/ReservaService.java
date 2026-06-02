package com.cl.cinelatte.models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    ReservaDAO reservaDAO;

    public void inserirReserva(Reserva reserva){
        reservaDAO.inserirReserva(reserva);
    }

    public List<Integer> obterAssentosOcupadosPorSessao(int sessaoId){
        return reservaDAO.obterAssentosOcupadosPorSessao(sessaoId);
    }

}
