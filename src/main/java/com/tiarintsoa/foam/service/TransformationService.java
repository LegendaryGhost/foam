package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.config.TransformationConfig;
import com.tiarintsoa.foam.entity.FormeUsuelle;
import com.tiarintsoa.foam.entity.Produit;
import com.tiarintsoa.foam.from.QuantiteUsuelleForm;
import com.tiarintsoa.foam.from.TransformationForm;
import com.tiarintsoa.foam.repository.FormeUsuelleRepository;
import com.tiarintsoa.foam.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransformationService {

    @Autowired
    private TransformationConfig transformationConfig;

    @Autowired
    private FormeUsuelleRepository formeUsuelleRepository;
    @Autowired
    private ProduitRepository produitRepository;

    public TransformationForm getEmptyForm() {
        TransformationForm transformationForm = new TransformationForm();
        List<FormeUsuelle> formeUsuelle = formeUsuelleRepository.findAll();
        List<QuantiteUsuelleForm> quantiteUsuelleForms = new ArrayList<>();
        for(FormeUsuelle forme : formeUsuelle) {
            QuantiteUsuelleForm quantiteForm = new QuantiteUsuelleForm();
            quantiteForm.setFormName(forme.getProduit().getNomProduit());
            quantiteForm.setIdProduit(forme.getProduit().getId());
            quantiteUsuelleForms.add(quantiteForm);
        }
        transformationForm.setUsualFormsQuantities(quantiteUsuelleForms);
        return transformationForm;
    }

    public double calculateUsualFormsVolume(List<QuantiteUsuelleForm> usualFormsQuantities) {
        double totalVolume = 0.0;
        for (QuantiteUsuelleForm form : usualFormsQuantities) {
            Produit produit = produitRepository.findById(form.getIdProduit()).orElse(null);
            if (produit != null) {
                double volumeProduit = produit.getLongueur() * produit.getLargeur() * produit.getHauteur();
                totalVolume += form.getQuantity() * volumeProduit;
            }
        }
        return totalVolume;
    }

    public boolean validateVolume(double volumeBloc, double volumeReste, double volumeUsualForms) {
        double margin = transformationConfig.getMarginPercentage() / 100.0;
        double minAcceptableVolume = volumeBloc * (1 - margin);

        System.out.println("Minimum acceptable: " + minAcceptableVolume);
        System.out.println("Maximum acceptable: " + volumeBloc);
        System.out.println("Current: " + (volumeUsualForms + volumeReste));

        return (volumeUsualForms + volumeReste >= minAcceptableVolume &&
                volumeUsualForms + volumeReste <= volumeBloc);
    }

    public void saveTransformation(TransformationForm transformationForm) {
        System.out.println(transformationForm);
    }

}
