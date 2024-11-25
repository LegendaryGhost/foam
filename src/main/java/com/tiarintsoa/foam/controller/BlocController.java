package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.entity.Bloc;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.from.GenerationForm;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.repository.MachineRepository;
import com.tiarintsoa.foam.service.BlocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String blocs(Model model,
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size) {

        page = page < 1 ? 1 : page;

        Page<Bloc> pageBlocs = blocRepository.findAll(PageRequest.of(page - 1, size));
        model.addAttribute("blocs", pageBlocs.getContent());
        model.addAttribute("totalPages", pageBlocs.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", pageBlocs.getTotalElements());
        model.addAttribute("pageSize", size);

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

    @GetMapping("/generate-data")
    public String showDataGenerationForm(Model model) {
        model.addAttribute("generationForm", new GenerationForm());
        return "generation";
    }

    @PostMapping("/generate-data")
    public String generateData(@ModelAttribute GenerationForm generationForm) {
        blocService.generateData(generationForm);
        return "redirect:/machines";
    }
}
