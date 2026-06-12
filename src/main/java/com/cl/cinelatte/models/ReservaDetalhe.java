package com.cl.cinelatte.models;

import java.util.List;

public record ReservaDetalhe(
    Reserva reserva,
    Sessao sessao,
    Filme filme,
    Sala sala,
    List<Assento> assentos,
    List<ReservaItem> itens) {

    // Contadores de tipo de ingresso pra exibir no HTML das reservas
    public int getQtdInteira() {
        int contador = 0;

        for (ReservaItem item : itens) {
            if (item.getTipoIngresso() == TipoIngresso.INTEIRA)
                contador++;
        }

        return contador;
    }

    public int getQtdMeia() {
        int contador = 0;
        
        for (ReservaItem item : itens) {
            if (item.getTipoIngresso() == TipoIngresso.MEIA)
                contador++;
        }

        return contador;
    }
}
