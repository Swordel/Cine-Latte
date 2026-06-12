package com.cl.cinelatte.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cl.cinelatte.models.Assento;
import com.cl.cinelatte.models.AssentoService;
import com.cl.cinelatte.models.DataSessao;
import com.cl.cinelatte.models.Filme;
import com.cl.cinelatte.models.FilmeGenero;
import com.cl.cinelatte.models.FilmeService;
import com.cl.cinelatte.models.FilmeStatus;
import com.cl.cinelatte.models.FormaPagamento;
import com.cl.cinelatte.models.ReservaService;
import com.cl.cinelatte.models.SalaService;
import com.cl.cinelatte.models.Sessao;
import com.cl.cinelatte.models.SessaoIdioma;
import com.cl.cinelatte.models.SessaoService;
import com.cl.cinelatte.models.TipoIngresso;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    // Pasta onde os uploads serão salvos 
    private static final String UPLOAD_DIR = "./uploads";

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

    // ========= CADASTRO DE FILMES =========
   @GetMapping("/filme/cadastro")
    public String formFilme(Model model) {
        model.addAttribute("filme", new Filme());
        model.addAttribute("generos", FilmeGenero.values()); // passa todos os gêneros pro HTML
        return "formfilme";
    }

   // Agora recebe MultipartFile para pôster e banner
    @PostMapping("/filme/cadastro")
    public String cadastrarFilme(
            @ModelAttribute Filme filme,
            @RequestParam List<FilmeGenero> generos,
            @RequestParam("poster") MultipartFile poster,
            @RequestParam("bannerFile") MultipartFile bannerFile,
            Model model) throws IOException {

        try{
            // Salva o pôster e guarda o nome no objeto filme
            String nomePoster = salvarArquivo(poster);
            filme.setImagem(nomePoster);
 
            // Salva o banner e guarda o nome no objeto filme
            String nomeBanner = salvarArquivo(bannerFile);
            filme.setBanner(nomeBanner);
 
            FilmeService fs = context.getBean(FilmeService.class);
            fs.inserirFilme(filme, generos);
 
            return "redirect:/admin/filmes";
            
        } catch(DataIntegrityViolationException e){ //Exception ex funciona, mas esse é específico pro erro de DB (violação de integridade do banco)
            model.addAttribute("erro", "ATENÇÃO: Não foi possível cadastrar, pois já existe um filme com este título!");
            model.addAttribute("generos", FilmeGenero.values());
            model.addAttribute("filme", filme); // Devolve o objeto para o admin não perder umas coisas que digitou

            return "formfilme"; // Recarrega a página do formulário exibindo o erro
        }
    }

    /*
    Método auxiliar: salva um MultipartFile na pasta ./uploads/
    e retorna o nome único gerado para o arquivo.
     
    Por que UUID + nome original?
    - UUID evita colisão (dois usuários subindo "poster.jpg" não se sobrescrevem)
    - Manter o nome original facilita identificar o arquivo
    */
   
    private String salvarArquivo(MultipartFile arquivo) throws IOException {

        // Garante que a pasta uploads existe (cria se não existir)
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
 
        // Gera um nome único para o arquivo
        String nomeOriginal = arquivo.getOriginalFilename();
        String nomeUnico = UUID.randomUUID() + "_" + nomeOriginal;
 
        // Copia o conteúdo do upload para o destino
        Path destino = uploadPath.resolve(nomeUnico);
        Files.copy(arquivo.getInputStream(), destino);
 
        return nomeUnico; // isso é o que vai ser salvo no banco
    }

    /*
    Endpoint unificado para servir imagens de filmes.
    
    Funciona assim:
    1. Procura o arquivo em ./uploads/        (filmes novos, enviados pelo formulario)
    2. Se nao achar, procura em static/images/ (filmes antigos, ja existentes no projeto)
    3. Se nao achar em nenhum dos dois, retorna 404
    
    Isso permite apontar TODOS os th:src para /uploads/{nome},
    tanto para filmes antigos quanto novos, sem precisar mudar o banco
    nem ter duas URLs diferentes no HTML.
    
    Uso no Thymeleaf: th:src="@{/uploads/{nome}(nome=${filme.imagem})}"
    */
    @GetMapping("/uploads/{nomeArquivo}")
    public ResponseEntity<Resource> servirImagem(@PathVariable String nomeArquivo) throws IOException {
 
        // 1a tentativa: arquivo enviado pelo formulario, salvo em ./uploads/
        Path caminhoUpload = Paths.get(UPLOAD_DIR).resolve(nomeArquivo);
        
        if (Files.exists(caminhoUpload)) {
            Resource resource = new FileSystemResource(caminhoUpload);
            String contentType = Files.probeContentType(caminhoUpload);

            if (contentType == null)
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        }
 
        // 2a tentativa: imagem antiga dentro de static/images/ (empacotada no .jar)
        // ClassPathResource busca dentro do classpath, que inclui a pasta static/
        Resource resourceStatic = new ClassPathResource("static/images/" + nomeArquivo);

        if (resourceStatic.exists()) {
            String contentType = Files.probeContentType(Paths.get(nomeArquivo));

            if (contentType == null)
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resourceStatic);
        }
 
        // Nao encontrou em nenhum dos dois lugares
        return ResponseEntity.notFound().build();
    }

    // ========= LISTAGEM DE FILMES PARA O ADMIN =========

    // Lista filmes por status. Se não passar ?status=, assume EM_CARTAZ.
    // URL: /admin/filmes  ou  /admin/filmes?status=EM_BREVE
    @GetMapping("/admin/filmes")
    public String listarFilmes(@RequestParam(defaultValue = "EM_CARTAZ") String status, Model model) {
        FilmeService fs = context.getBean(FilmeService.class);
        FilmeStatus filmeStatus = FilmeStatus.valueOf(status);
        model.addAttribute("filmes", fs.obterFilmesPorStatus(filmeStatus));
        model.addAttribute("statusAtivo", status); // usado pelas abas no HTML
        return "listaFilmes";
    }

    // ========= Edição de Filmes / UPDATE =========

    // Formulário de edição: carrega os dados atuais do filme
    // URL: /admin/filme/3/editar
    @GetMapping("/admin/filme/{id}/editar")
    public String formEditarFilme(@PathVariable int id, Model model) {
        FilmeService fs = context.getBean(FilmeService.class);
        Filme filmeAntigo = fs.obterFilme(id);
        model.addAttribute("filme", filmeAntigo);
        model.addAttribute("generos", FilmeGenero.values());
        return "formeditarfilme"; 
    }

    // Recebe o submit do formulário de edição
    // URL: POST /admin/filme/3/editar
    @PostMapping("/admin/filme/{id}/editar")
    public String editarFilme(@PathVariable int id,
                               @ModelAttribute Filme filme,
                               @RequestParam List<FilmeGenero> generos, Model model) {

        try{
            FilmeService fs = context.getBean(FilmeService.class);
            fs.atualizarFilme(id, filme, generos);

            return "redirect:/admin/filmes";

        } catch (DataIntegrityViolationException e){

            // Se cair aqui, o Admin tentou mudar o título para um nome que já existe no banco
            model.addAttribute("erro", "Não foi possível atualizar: já existe outro filme com este título.");

            // Estava com um pouco de dificuldade, optei por voltar o formulário ao que era e perder as edições
            FilmeService fs = context.getBean(FilmeService.class);
            Filme filmeAntigo = fs.obterFilme(id);
            model.addAttribute("filme", filmeAntigo);
            model.addAttribute("generos", FilmeGenero.values());

            return "formeditarfilme"; // Devolve o usuário para a página de edição mostrando o alerta
        }
    }

    // ========= Deletar filme :c =========

    // Deleta o filme e redireciona de volta para a lista
    // URL: POST /admin/filme/3/deletar
    @PostMapping("/admin/filme/{id}/deletar")
    public String deletarFilme(@PathVariable int id) {
        FilmeService fs = context.getBean(FilmeService.class);
        fs.deletarFilme(id);
        return "redirect:/admin/filmes";
    }

    //     ===== CADASTRO DE SESSÕES ===
    // Exibe o formulário de cadastro de sessão
    // Precisa mandar pro model: todos os filmes (para o dropdown)
    // e todas as salas (para o dropdown de sala)
    @GetMapping("/admin/sessao/cadastro")
    public String formSessao(Model model) {
        FilmeService fs = context.getBean(FilmeService.class);
        SalaService sl = context.getBean(SalaService.class);
 
        model.addAttribute("sessao", new Sessao());        // ancora o th:object
        model.addAttribute("filmes", fs.obterTodosFilmes()); 
        model.addAttribute("salas", sl.obterTodasSalas());
        return "formsessao";
    }

    // Recebe o submit do formulário e insere a sessão no banco
    @PostMapping("/admin/sessao/cadastro")
    public String cadastrarSessao(@ModelAttribute Sessao sessao, Model model) {

        try{
            SessaoService ss = context.getBean(SessaoService.class);
            ss.inserirSessao(sessao);

            return "redirect:/admin/filmes";

        } catch(DataIntegrityViolationException e){
            FilmeService fs = context.getBean(FilmeService.class);
            SalaService sl = context.getBean(SalaService.class);

            // Captura o erro caso já exista uma sessão idêntica (mesmo filme, sala, data, hora e idioma) 
            model.addAttribute("erro", "Erro: esta sala já está ocupada nesse mesmo dia e horário!");
            model.addAttribute("filmes", fs.obterTodosFilmes()); // Recarrega listas necessárias para o form
            model.addAttribute("salas", sl.obterTodasSalas());
            model.addAttribute("sessao", sessao);

            return "formsessao"; 
        }
    }

    // ========= PÁGINA DAS SESSÕES =========
    /* Anotações da Gaby:
    - A tag {id} é uma variável dinâmica. Quando alguém acessa /filme/123/sessoes, 
    o @PathVariable int id captura o número 123 e o injeta no parâmetro do método,
    permitindo que o fs.obterFilme(id) busque as sessões exatas daquele filme. */
    @GetMapping("/filme/{id}/sessoes")
    public String sessoes(Model model, @PathVariable int id){ 
        FilmeService fs = context.getBean(FilmeService.class);
        SessaoService ss = context.getBean(SessaoService.class);
        
        Filme filme = fs.obterFilme(id); //o service já está populando os gêneros também
        List<LocalDate> datasBanco = ss.obterDatasDoFilme(id);

        List<DataSessao> datas = new ArrayList<>(); //fiz assim como o aux do DAO 
        for (LocalDate d : datasBanco) {
            datas.add(new DataSessao(d));
        }

         // Seleciona a primeira data disponível por padrão
        LocalDate dataSelecionada = datasBanco.isEmpty() ? null : datasBanco.get(0);

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
            model.addAttribute("sessoesLeg", List.of()); //equivalente de new ArrayList<>() 
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
        List<LocalDate> datasBanco = ss.obterDatasDoFilme(id);

        List<DataSessao> datas = new ArrayList<>(); 
        for (LocalDate d : datasBanco) {
            datas.add(new DataSessao(d));
        }

        carregarSessoes(model, ss, id, data);

        model.addAttribute("filme", filme);
        model.addAttribute("datas", datas);
        model.addAttribute("dataSelecionada", data);
        return "sessoes";
    }

    // ========= PÁGINA DE ASSENTOS ===============

    @GetMapping("/sessao/{id}/assentos")
    public String assentos(@PathVariable int id, Model model){
        SessaoService ss = context.getBean(SessaoService.class);
        FilmeService  fs = context.getBean(FilmeService.class);
        AssentoService as = context.getBean(AssentoService.class);
        ReservaService rs = context.getBean(ReservaService.class);

        Sessao sessao = ss.obterSessao(id); //mando o id da sessão
        Filme filme = fs.obterFilme(sessao.getFilmeId());

        model.addAttribute("sessao", sessao);
        model.addAttribute("filme", filme);

        List<Assento> assentos = as.obterAssentosPorSala(sessao.getSalaId());
        model.addAttribute("assentos", assentos);

        List<Integer> assentosOcupados = rs.obterAssentosOcupadosPorSessao(id);
        model.addAttribute("assentosOcupados", assentosOcupados);

        return "assentos";
    }

    
    // POST: recebe os assentos selecionados, salva na sessão HTTP e redireciona
    @PostMapping("/sessao/{id}/assentos")
    public String selecionarAssentos(@PathVariable int id,
                                      @RequestParam List<Integer> assentoIds,
                                      HttpSession session) {
        session.setAttribute("sessaoId", id);
        session.setAttribute("assentoIds", assentoIds);
        return "redirect:/sessao/" + id + "/ingressos";
    }
    

    // ========= INGRESSOS ======
   
    @GetMapping("/sessao/{id}/ingressos")
    public String ingressos(@PathVariable int id, HttpSession session, Model model) {
        // Recupera assentos salvos na sessão HTTP
        @SuppressWarnings("unchecked") //esse object é realmente um List<Integer> ? -> confia!
        List<Integer> assentoIds = (List<Integer>) session.getAttribute("assentoIds");
        Integer sessaoIdSession = (Integer) session.getAttribute("sessaoId");

        if (sessaoIdSession == null || !sessaoIdSession.equals(id)) {
            return "redirect:/sessao/" + id + "/assentos";
        }

        // "Segurança": se não há assentos na sessão, volta para a seleção
        if (assentoIds == null || assentoIds.isEmpty()) {
            return "redirect:/sessao/" + id + "/assentos";
        }
        
        SessaoService ss = context.getBean(SessaoService.class);
        FilmeService fs = context.getBean(FilmeService.class);
        AssentoService as = context.getBean(AssentoService.class);

        Sessao sessao = ss.obterSessao(id);
        Filme filme = fs.obterFilme(sessao.getFilmeId());
        List<Assento> assentosSelecionados = as.obterAssentosSelecionados(assentoIds);

        model.addAttribute("sessao", sessao);
        model.addAttribute("filme", filme);
        model.addAttribute("totalAssentos", assentoIds.size());
        model.addAttribute("assentos", assentosSelecionados);
        model.addAttribute("precoInteira", TipoIngresso.INTEIRA.getPreco());
        model.addAttribute("precoMeia", TipoIngresso.MEIA.getPreco());
        return "ingressos";
    }

    @PostMapping("/sessao/{id}/ingressos")
    public String confirmarIngressos(@PathVariable int id, @RequestParam int qtdInteira, @RequestParam int qtdMeia, HttpSession session) {
        session.setAttribute("qtdInteira", qtdInteira);
        session.setAttribute("qtdMeia", qtdMeia);

        return "redirect:/sessao/" + id + "/pagamento";
    }

    // == PAGAMENTO ~~~
    @GetMapping("/sessao/{id}/pagamento")
    public String pagamento(@PathVariable int id, HttpSession session, Model model) {

        @SuppressWarnings("unchecked")
        List<Integer> assentoIds = (List<Integer>) session.getAttribute("assentoIds");
        Integer sessaoIdSession = (Integer) session.getAttribute("sessaoId");
        Integer qtdInteira = (Integer) session.getAttribute("qtdInteira");
        Integer qtdMeia = (Integer) session.getAttribute("qtdMeia");

        //detectar que alguém tentou acessar uma sessão diferente da compra em andamento
        if (sessaoIdSession == null || !sessaoIdSession.equals(id)) {
            return "redirect:/sessao/" + id + "/assentos";
        }

        if (assentoIds == null || assentoIds.isEmpty()) {
            return "redirect:/sessao/" + id + "/assentos";
        }

        if (qtdInteira == null || qtdMeia == null) {
            return "redirect:/sessao/" + id + "/ingressos";
        }

        SessaoService ss = context.getBean(SessaoService.class);
        FilmeService fs = context.getBean(FilmeService.class);
        AssentoService as = context.getBean(AssentoService.class);
        ReservaService rs = context.getBean(ReservaService.class);

        Sessao sessao = ss.obterSessao(id);
        Filme filme = fs.obterFilme(sessao.getFilmeId());
        List<Assento> assentosSelecionados = as.obterAssentosSelecionados(assentoIds);

        double valorTotal = rs.calcularTotal(qtdInteira, qtdMeia);

        model.addAttribute("sessao", sessao);
        model.addAttribute("filme", filme);
        model.addAttribute("assentos", assentosSelecionados);
        model.addAttribute("qtdInteira", qtdInteira);
        model.addAttribute("qtdMeia", qtdMeia);
        model.addAttribute("valorTotal", valorTotal);

        return "pagamento";
    }

    @PostMapping("/sessao/{id}/pagamento")
    public String confirmarPagamento(@PathVariable int id,
                                  @RequestParam FormaPagamento formaPagamento,
                                  HttpSession session,
                                  Model model) {

        @SuppressWarnings("unchecked")
        List<Integer> assentoIds = (List<Integer>) session.getAttribute("assentoIds");
        Integer qtdInteira = (Integer) session.getAttribute("qtdInteira");
        Integer qtdMeia = (Integer) session.getAttribute("qtdMeia");
        Integer sessaoIdSession = (Integer) session.getAttribute("sessaoId");

        //valida primeiro se essa compra realmente pertence à sessão da URL antes de continuar
        // alguém pode deixar a aba aberta, abrir outra sessão em outra página e depois tentar enviar na primeira aba
        if (sessaoIdSession == null || !sessaoIdSession.equals(id)) {
            return "redirect:/sessao/" + id + "/assentos";
        }

        if (assentoIds == null || qtdInteira == null || qtdMeia == null) {
            return "redirect:/sessao/" + id + "/assentos";
        }

        ReservaService rs = context.getBean(ReservaService.class);

        // Delega tudo ao Service: controller só passa os dados
        rs.inserirReservaCompleta(id, assentoIds, qtdInteira, qtdMeia, formaPagamento);

        // Limpa a sessão após a compra
        session.removeAttribute("assentoIds");
        session.removeAttribute("sessaoId");
        session.removeAttribute("qtdInteira");
        session.removeAttribute("qtdMeia");

        return "redirect:/sessao/" + id + "/confirmacao";
    }

    @GetMapping("/sessao/{id}/confirmacao")
    public String confirmacao() {
        return "confirmacao";
    }   

}
