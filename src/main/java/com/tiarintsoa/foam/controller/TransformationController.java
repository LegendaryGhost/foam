package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.entity.Bloc;
import com.tiarintsoa.foam.from.TransformationForm;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.service.TransformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transformations")
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
    public String submitTransformationForm(
        @ModelAttribute TransformationForm transformationForm,
        BindingResult bindingResult,
        Model model
    ) {

        // Récupérer le bloc sélectionné
        Bloc bloc = blocRepository.findById(transformationForm.getIdBloc())
            .orElse(null);

        // Vérifications de validation
        if (bloc != null) {
            double volumeBloc = bloc.getProduit().getLongueur() * bloc.getProduit().getLargeur() * bloc.getProduit().getHauteur();
            double volumeReste = transformationForm.getLongueurReste() * transformationForm.getLargeurReste() * transformationForm.getHauteurReste();
            double volumeUsualForms = transformationService.calculateUsualFormsVolume(transformationForm.getUsualFormsQuantities());

            // Vérifie que les dimensions du reste ne sont pas supérieures à celles du bloc d'origine
            /* if (transformationForm.getLongueurReste() > bloc.getProduit().getLongueur()) {
                bindingResult.rejectValue("longueurReste", "error.transformationForm", "Les dimensions du reste ne doivent pas dépasser celles du bloc d'origine.");
            }
            if (transformationForm.getLargeurReste() > bloc.getProduit().getLargeur()) {
                bindingResult.rejectValue("largeurReste", "error.transformationForm", "Les dimensions du reste ne doivent pas dépasser celles du bloc d'origine.");
            }
            if (transformationForm.getHauteurReste() > bloc.getProduit().getHauteur()) {
                bindingResult.rejectValue("hauteurReste", "error.transformationForm", "Les dimensions du reste ne doivent pas dépasser celles du bloc d'origine.");
            } */

            // Vérifie que le volume des produits en sortie n'est pas supérieur au volume du bloc d'origine
            if (volumeUsualForms > volumeBloc) {
                bindingResult.rejectValue("usualFormsQuantities", "error.transformationForm", "Le volume des produits en sortie est supérieur au volume du bloc.");
            }

            // Vérifie que le volume des produits en sortie plus le volume du reste est dans la marge autorisée (volume origine - 2% jusqu'à volume origine)
            if (!transformationService.validateVolume(volumeBloc, volumeReste, volumeUsualForms)) {
                bindingResult.rejectValue(
                        "usualFormsQuantities",
                        "error.transformationForm",
                        "Le volume des produits en sortie plus le volume du reste n'est pas dans la marge autorisée."
                        + " (entre " + transformationService.getMinAcceptableVolume(volumeBloc) + " m^3 et " + volumeBloc + " m^3)"
                        + " (volume du reste + produits actuel : " + (volumeReste + volumeUsualForms) + " m^3)"
                );
            }
        }

        // En cas d'erreurs, retourne au formulaire avec les valeurs remplies
        if (bindingResult.hasErrors()) {
            model.addAttribute("blocs", blocRepository.findAllByEtatStockQuantiteGreaterThanZero());
            return "transformationForm";
        }

        // Si pas d'erreurs, sauvegarde la transformation
        transformationService.saveTransformation(transformationForm);
        return "redirect:/transformations/form";
    }

}
