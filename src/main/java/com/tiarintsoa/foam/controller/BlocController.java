package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.entity.Bloc;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.repository.MachineRepository;
import com.tiarintsoa.foam.service.BlocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blocs")
@Controller
public class BlocController {

    private final BlocService blocService;
    private final BlocRepository blocRepository;
    private final MachineRepository machineRepository;

    public BlocController(BlocService blocService, BlocRepository blocRepository, MachineRepository machineRepository) {
        this.blocService = blocService;
        this.blocRepository = blocRepository;
        this.machineRepository = machineRepository;
    }

    @GetMapping
    public String blocs(Model model) {
        model.addAttribute("blocs", blocRepository.findAll());
        return "bloc";
    }

    @GetMapping("/form")
    public String showBlocForm(@RequestParam(value = "idBloc", required = false) Long idBloc, Model model) {
        Bloc bloc = idBloc == null ? new Bloc() : blocRepository.findById(idBloc).orElse(new Bloc());
        model.addAttribute("blocForm", bloc.toBlocForm());
        model.addAttribute("machines", machineRepository.findAll());
        model.addAttribute("isModifying", idBloc != null);
        return "blocForm";
    }

    @PostMapping
    public String submitBlocForm(@ModelAttribute BlocForm blocForm) {
        blocService.saveBloc(blocForm);
        return "redirect:/blocs";
    }

    @GetMapping("/update-theoretical-price")
    public String updateTheoreticalPrice() {
        blocService.updateBlocsTheoreticalCostPrice();
        return "redirect:/machines";
    }
}
