package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.from.TransformationForm;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.service.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transformation")
public class TransformationController {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private TransformationService transformationService;

    @GetMapping("/form")
    public String showTransformationForm(Model model) {
        TransformationForm transformationForm = transformationService.getEmptyForm();
        model.addAttribute("transformationForm", transformationForm);
        model.addAttribute("blocs", blocRepository.findAllByEtatStockQuantiteGreaterThanZero());
        return "transformationForm";
    }

    @PostMapping
    public String submitTransformationForm(TransformationForm transformationForm) {
        transformationService.saveTransformation(transformationForm);
        return "redirect:/transformation/form";
    }

}
