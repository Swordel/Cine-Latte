package com.cl.cinelatte.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cl.cinelatte.models.Filme;
import com.cl.cinelatte.models.FilmeGenero;
import com.cl.cinelatte.models.FilmeService;

@Controller
public class MainController {

   @Autowired
   ApplicationContext context;

   @GetMapping("/")
    public String index(){
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
    
}
