package com.cl.cinelatte.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    @Autowired
    ReservaDAO reservaDAO;

        //AINDA vou repassar isso!!
     // Calcula o valor total: usado pelo controller na página de pagamento (GET)
    public double calcularTotal(int qtdInteira, int qtdMeia) {
        return (qtdInteira * TipoIngresso.INTEIRA.getPreco())
             + (qtdMeia * TipoIngresso.MEIA.getPreco());
    }

    // Valida, calcula e insere a reserva completa -> retorna a Reserva criada
    public Reserva inserirReservaCompleta(int sessaoId, List<Integer> assentoIds, int qtdInteira, int qtdMeia, FormaPagamento formaPagamento) {

         // == VALIDAÇÕES == :D

        // 1. Soma dos tipos deve bater com a quantidade de assentos
        if (qtdInteira + qtdMeia != assentoIds.size()) {
            throw new IllegalArgumentException(
                "Quantidade de ingressos não corresponde aos assentos selecionados."
            );
        }

        // 2. Não pode haver assentos duplicados
        Set<Integer> semDuplicatas = new HashSet<>(assentoIds);
        if (semDuplicatas.size() != assentoIds.size()) {
            throw new IllegalArgumentException(
                "Existem assentos duplicados na seleção."
            );
        }

        // 3. Nenhum assento pode já estar ocupado nessa sessão
        List<Integer> ocupados = reservaDAO.obterAssentosOcupadosPorSessao(sessaoId);
        for (int assentoId : assentoIds) {
            if (ocupados.contains(assentoId)) {
                throw new IllegalArgumentException(
                    "Um ou mais assentos já estão ocupados."
                );
            }
        }

        // == CÁLCULO DO VALOR TOTAL == ~
        double valorTotal = calcularTotal(qtdInteira, qtdMeia);

        // == INSERÇÃO == ~
        Reserva reserva = new Reserva(sessaoId, valorTotal, formaPagamento);
        int reservaId = reservaDAO.inserirReserva(reserva);
        reserva.setId(reservaId);
 
        // Monta e insere os itens
        // Primeiros qtdInteira assentos = INTEIRA, restantes = MEIA
        List<ReservaItem> itens = new ArrayList<>();
        for (int i = 0; i < assentoIds.size(); i++) {
            TipoIngresso tipo = (i < qtdInteira) ? TipoIngresso.INTEIRA : TipoIngresso.MEIA;
            itens.add(new ReservaItem(reservaId, assentoIds.get(i), tipo, tipo.getPreco()));
        }
 
        for (ReservaItem item : itens) {
            reservaDAO.inserirReservaItem(item);
        }
 
        return reserva;

    }

    public List<Integer> obterAssentosOcupadosPorSessao(int sessaoId) {
        return reservaDAO.obterAssentosOcupadosPorSessao(sessaoId);
    }

}
