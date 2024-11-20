package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.config.TransformationConfig;
import com.tiarintsoa.foam.entity.Bloc;
import com.tiarintsoa.foam.entity.EtatStock;
import com.tiarintsoa.foam.entity.FormeUsuelle;
import com.tiarintsoa.foam.entity.Produit;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.from.QuantiteUsuelleForm;
import com.tiarintsoa.foam.from.TransformationForm;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.repository.EtatStockRepository;
import com.tiarintsoa.foam.repository.FormeUsuelleRepository;
import com.tiarintsoa.foam.repository.ProduitRepository;
import jakarta.transaction.Transactional;
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
    @Autowired
    private EtatStockRepository etatStockRepository;
    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private BlocService blocService;

    public TransformationForm getEmptyForm() {
        TransformationForm transformationForm = new TransformationForm();
        List<FormeUsuelle> formeUsuelle = formeUsuelleRepository.findAll();
        List<QuantiteUsuelleForm> quantiteUsuelleForms = new ArrayList<>();
        for(FormeUsuelle forme : formeUsuelle) {
            QuantiteUsuelleForm quantiteForm = new QuantiteUsuelleForm();
            quantiteForm.setFormName(forme.getProduit().getArticle().getNomArticle());
            quantiteForm.setIdFormeUsuelle(forme.getId());
            quantiteUsuelleForms.add(quantiteForm);
        }
        transformationForm.setUsualFormsQuantities(quantiteUsuelleForms);
        return transformationForm;
    }

    public double calculateUsualFormsVolume(List<QuantiteUsuelleForm> usualFormsQuantities) {
        double totalVolume = 0.0;
        for (QuantiteUsuelleForm form : usualFormsQuantities) {
            Produit produit = produitRepository.findById(form.getIdFormeUsuelle()).orElse(null);
            if (produit != null) {
                double volumeProduit = produit.getLongueur() * produit.getLargeur() * produit.getHauteur();
                totalVolume += form.getQuantity() * volumeProduit;
            }
        }
        return totalVolume;
    }

    public boolean validateVolume(double volumeBloc, double volumeReste, double volumeUsualForms) {
        double minAcceptableVolume = getMinAcceptableVolume(volumeBloc);

        return (volumeUsualForms + volumeReste >= minAcceptableVolume &&
                volumeUsualForms + volumeReste <= volumeBloc);
    }

    public double getMinAcceptableVolume(double volumeBloc) {
        return volumeBloc * (1 - transformationConfig.getMarginPercentage() / 100.0);
    }

    @Transactional
    public void saveTransformation(TransformationForm transformationForm) {
        // Mise à jour de l'état du stock et mouvement de sortie
        EtatStock etatStockOrigine = etatStockRepository.findFirstByBlocId(transformationForm.getIdBloc())
                .orElseThrow(() -> new RuntimeException("Etat de stock introuvable pour l'id bloc : " + transformationForm.getIdBloc()));
        etatStockOrigine.setQuantite(0);
        etatStockRepository.save(etatStockOrigine);

        // Insertion du bloc reste
        Bloc origine = blocRepository.findById(transformationForm.getIdBloc())
                .orElseThrow(() -> new RuntimeException("Bloc d'origine introuvable"));
        Bloc sourceOriginel = origine.getOriginel() == null ? origine : origine.getOriginel();
        double volumeOrigine = origine.getProduit().getVolume();
        BlocForm blocFormReste = getBlocForm(transformationForm, origine, volumeOrigine);
        blocService.saveBloc(blocFormReste, origine, sourceOriginel);

        for(QuantiteUsuelleForm quantiteUsuelleForm: transformationForm.getUsualFormsQuantities()) {
            if (quantiteUsuelleForm.getQuantity() > 0) {
                FormeUsuelle formeUsuelle = formeUsuelleRepository.findById(quantiteUsuelleForm.getIdFormeUsuelle())
                        .orElseThrow(() -> new RuntimeException("Forme usuelle introuvable"));
                Produit produit = formeUsuelle.getProduit();
                EtatStock etatStockFormeUsuelle = new EtatStock();
                etatStockFormeUsuelle.setQuantite(quantiteUsuelleForm.getQuantity());
                double volumeProduit = produit.getLongueur() * produit.getLargeur() * produit.getHauteur();
                double coutProduction = volumeProduit * origine.getPrixProduction() / volumeOrigine;
                etatStockFormeUsuelle.setPrixProduction(coutProduction);
                etatStockFormeUsuelle.setOrigine(origine);
                etatStockFormeUsuelle.setOriginel(sourceOriginel);
                etatStockFormeUsuelle.setArticle(produit.getArticle());
                etatStockRepository.save(etatStockFormeUsuelle);
            }
        }
    }

    private static BlocForm getBlocForm(TransformationForm transformationForm, Bloc origine, double volumeOrigine) {
        BlocForm blocForm = new BlocForm();
        blocForm.setNom(origine.getProduit().getArticle().getNomArticle() + "-R");
        blocForm.setLongueur(transformationForm.getLongueurResteAsDouble());
        blocForm.setLargeur(transformationForm.getLargeurResteAsDouble());
        blocForm.setHauteur(transformationForm.getHauteurResteAsDouble());
        double volumeReste = transformationForm.getVolumeReste();
        Double coutProduction = volumeReste * origine.getPrixProduction() / volumeOrigine;
        blocForm.setCoutProduction(coutProduction);
        return blocForm;
    }

}
