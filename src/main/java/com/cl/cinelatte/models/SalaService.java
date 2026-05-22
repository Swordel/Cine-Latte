package com.cl.cinelatte.models;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SalaService {
    
    @Autowired
    AssentoDAO assentoDAO;

    // Roda automaticamente quando a aplicação sobe
    // Gera os assentos das duas salas se ainda não existirem
    @EventListener(ApplicationReadyEvent.class)
    public void inicializarSalas() {
        gerarAssentosSala(1);
        gerarAssentosSala(2);
    }

    public void gerarAssentosSala(int salaId) {
 
        // Só gera se ainda não existirem assentos para essa sala
        if (assentoDAO.existeAssentoNaSala(salaId)) {
            return;
        }
 
        //Me inspirei na sala 8 do Cinemark :D
        Set<String> cadeirantes = Set.of("C5", "C6", "C9", "C10", "C13", "C14");
        Set<String> acompanhantes = Set.of("C4", "C7", "C8", "C11", "C12", "C15","D5", "D18");
        Set<String> mobilidadeReduzida = Set.of("D4", "D19");
 
        for (char fileira = 'A'; fileira <= 'L'; fileira++) {
 
            int quantidade = 22; // padrão: A, B, H, I, J, K
            if (fileira >= 'C' && fileira <= 'G') quantidade = 19;
            if (fileira == 'L') quantidade = 28;
 
            for (int numero = 1; numero <= quantidade; numero++) {
                String codigo = fileira + String.valueOf(numero);
 
                AssentoTipo tipo = AssentoTipo.NORMAL;
                
                if (cadeirantes.contains(codigo)) {
                    tipo = AssentoTipo.CADEIRANTE;
                }
                
                else if (acompanhantes.contains(codigo)) {
                    tipo = AssentoTipo.ACOMPANHANTE;
                }
                
                else if (mobilidadeReduzida.contains(codigo)) {
                    tipo = AssentoTipo.MOBILIDADE_REDUZIDA;
                }
 
                assentoDAO.salvar(new Assento(salaId, String.valueOf(fileira), numero, tipo));
            }
        }
    }

}
