package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.entity.Bloc;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.service.BlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blocs")
@Controller
public class BlocController {

    @Autowired
    private BlocService blocService;
    @Autowired
    private BlocRepository blocRepository;

    @GetMapping
    public String blocs(Model model) {
        model.addAttribute("blocs", blocRepository.findAll());
        return "bloc";
    }

    @GetMapping("/form")
    public String showBlocForm(@RequestParam(value = "idBloc", required = false) Long idBloc, Model model) {
        Bloc bloc = idBloc == null ? new Bloc() : blocRepository.findById(idBloc).orElse(new Bloc());
        model.addAttribute("blocForm", bloc.toBlocForm());
        model.addAttribute("isModifying", idBloc != null);
        return "blocForm";
    }

    @PostMapping
    public String submitBlocForm(@ModelAttribute BlocForm blocForm) {
        blocService.saveBloc(blocForm);
        return "redirect:/blocs";
    }
}
