package com.cl.cinelatte.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cl.cinelatte.models.Filme;
import com.cl.cinelatte.models.FilmeGenero;
import com.cl.cinelatte.models.FilmeService;
import com.cl.cinelatte.models.FilmeStatus;
import com.cl.cinelatte.models.Sessao;
import com.cl.cinelatte.models.SessaoIdioma;
import com.cl.cinelatte.models.SessaoService;

@Controller
public class MainController {

   @Autowired
   ApplicationContext context;

   // Página inicial -> busca filmes do banco por status
    @GetMapping("/")
    public String index(Model model) {
        FilmeService fs = context.getBean(FilmeService.class); // é só pra chamar com o construtor via autowired
        model.addAttribute("filmesEmCartaz", fs.obterFilmesPorStatus(FilmeStatus.EM_CARTAZ));  //link do view com o model
        model.addAttribute("filmesEmBreve",  fs.obterFilmesPorStatus(FilmeStatus.EM_BREVE));  //oq tá no aspas é o nome no HTML
        return "index";
    }

   @GetMapping("/filme/cadastro")
    public String formFilme(Model model) {
        model.addAttribute("filme", new Filme());
        model.addAttribute("generos", FilmeGenero.values()); // passa todos os gêneros pro HTML
        return "formfilme";
    }

   @PostMapping("/filme/cadastro")
    public String cadastrarFilme(@ModelAttribute Filme filme, @RequestParam List<FilmeGenero> generos, Model model) {
        FilmeService fs = context.getBean(FilmeService.class);
        fs.inserirFilme(filme, generos);
        return "sucesso";
    }

    /* Anotações da Gaby:
    - A tag {id} é uma variável dinâmica. Quando alguém acessa /filme/123/sessoes, 
    o @PathVariable int id captura o número 123 e o injeta no parâmetro do método,
    permitindo que o fs.obterFilme(id) busque as sessões exatas daquele filme. */
    @GetMapping("/filme/{id}/sessoes")
    public String sessoes(Model model, @PathVariable int id){ 
        FilmeService fs = context.getBean(FilmeService.class);
        SessaoService ss = context.getBean(SessaoService.class);
        
        Filme filme = fs.obterFilme(id); //o service já está populando os gêneros também
        List<LocalDate> datas = ss.obterDatasDoFilme(id);

         // Seleciona a primeira data disponível por padrão
        LocalDate dataSelecionada = datas.isEmpty() ? null : datas.get(0);

        carregarSessoes(model, ss, id, dataSelecionada);

        model.addAttribute("filme", filme);
        model.addAttribute("datas", datas);
        model.addAttribute("dataSelecionada", dataSelecionada);

        return "sessoes";
    }

    // Método auxiliar: carrega sessões separadas por idioma para o Model
    // Evita repetição de código entre sessoes() e filtrarSessoes()
    private void carregarSessoes(Model model, SessaoService ss, int filmeId, LocalDate data) {
        if (data == null) {
            model.addAttribute("sessoesLeg", List.of());
            model.addAttribute("sessoesDub", List.of());
            model.addAttribute("temLeg", false);
            model.addAttribute("temDub", false);
            return;
        }
 
        List<Sessao> sessoesLeg = ss.obterSessoesPorFilmeDataIdioma(filmeId, data, SessaoIdioma.LEGENDADO);
        List<Sessao> sessoesDub = ss.obterSessoesPorFilmeDataIdioma(filmeId, data, SessaoIdioma.DUBLADO);
 
        model.addAttribute("sessoesLeg", sessoesLeg);
        model.addAttribute("sessoesDub", sessoesDub);
        
        // Flags para o Thymeleaf saber se deve renderizar cada grupo
        model.addAttribute("temLeg", !sessoesLeg.isEmpty());
        model.addAttribute("temDub", !sessoesDub.isEmpty());
    }

    // Filtra sessões ao clicar em outra data
    /* 
    ~ Reflexão reflexiva ~
    Quando o usuário clica em "28/05", o link gerado é:
    /filme/1/sessoes/filtrar?data=2026-05-28
    O navegador envia isso aí em cima ao Spring como uma requisição HTTP GET. 
    O Spring lê os parâmetros da URL -> ?data=2026-05-28 vira a String "2026-05-28".
    O @RequestParam diz: "pegue o parâmetro chamado  'data' da URL e coloque nessa variável.
    O Spring não sabe converter "2026-05-28" para LocalDate sozinho.
    O @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) diz explicitamente qual formato esperar.
    O ISO.DATE é o padrão internacional YYYY-MM-DD.
    O Spring então usa um conversor interno que chama LocalDate.parse("2026-05-28") por baixo dos panos.
    Sem o @DateTimeFormat, o Spring tentaria converter a String para LocalDate sem saber o formato
        e lançaria MethodArgumentTypeMismatchException.
    */
    @GetMapping("/filme/{id}/sessoes/filtrar")
    public String filtrarSessoes(@PathVariable int id,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                  Model model) {
        FilmeService  fs = context.getBean(FilmeService.class);
        SessaoService ss = context.getBean(SessaoService.class);

        Filme filme = fs.obterFilme(id);
        List<LocalDate> datas = ss.obterDatasDoFilme(id);

        carregarSessoes(model, ss, id, data);

        model.addAttribute("filme", filme);
        model.addAttribute("datas", datas);
        model.addAttribute("dataSelecionada", data);
        return "sessoes";
    }
    
}
