package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.service.BlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/blocs")
@Controller
public class BlocController {

    @Autowired
    private BlocService blocService;

    @GetMapping("/form")
    public String showBlocForm(Model model) {
        model.addAttribute("blocForm", new BlocForm());
        return "blocForm"; // Nom du template Thymeleaf
    }

    @PostMapping
    public String submitBlocForm(@ModelAttribute BlocForm blocForm) {
        // Logique d'insertion (enregistrement en base de données)
        blocService.saveBloc(blocForm);

        return "redirect:/blocs/form"; // Redirection après l'insertion
    }
}
