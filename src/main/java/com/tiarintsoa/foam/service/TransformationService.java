package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.entity.FormeUsuelle;
import com.tiarintsoa.foam.from.QuantiteUsuelleForm;
import com.tiarintsoa.foam.from.TransformationForm;
import com.tiarintsoa.foam.repository.FormeUsuelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransformationService {

    @Autowired
    private FormeUsuelleRepository formeUsuelleRepository;

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

    public void saveTransformation(TransformationForm transformationForm) {
        System.out.println(transformationForm);
    }

}
