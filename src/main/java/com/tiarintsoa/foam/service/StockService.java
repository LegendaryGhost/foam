package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.StockDetails;
import com.tiarintsoa.foam.dto.StockProduitDTO;
import com.tiarintsoa.foam.entity.Bloc;
import com.tiarintsoa.foam.entity.EtatStock;
import com.tiarintsoa.foam.entity.FormeUsuelle;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.repository.EtatStockRepository;
import com.tiarintsoa.foam.repository.FormeUsuelleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private EtatStockRepository etatStockRepository;
    @Autowired
    private FormeUsuelleRepository formeUsuelleRepository;
    @Autowired
    private BlocRepository blocRepository;

    public List<StockDetails> getStockDetails() {
        List<StockDetails> stockDetails = new ArrayList<>();
        stockDetails.add(getBasicStockDetails());
        stockDetails.add(getMaxGainStockDetails());
        stockDetails.add(getMinLossStockDetails());
        return stockDetails;
    }

    public StockDetails getBasicStockDetails() {
        List<StockProduitDTO> stockProduits = etatStockRepository.findStockProduitsForFormeUsuelle();

        double coutProductionTotal = stockProduits.stream()
                .mapToDouble(sp -> sp.getQuantite() * sp.getCoutProductionUnitaire())
                .sum();

        double prixVenteTotal = stockProduits.stream()
                .mapToDouble(sp -> sp.getQuantite() * sp.getPrixVenteUnitaire())
                .sum();

        StockDetails stockDetails = new StockDetails();
        stockDetails.setNomMethode("Méthode 1 : Produits finis");
        stockDetails.setCoutProductionTotal(coutProductionTotal);
        stockDetails.setPrixVenteTotal(prixVenteTotal);
        stockDetails.setStockProduits(stockProduits);

        return stockDetails;
    }

    public StockDetails getMaxGainStockDetails() {
        FormeUsuelle meilleurFormeUsuelle = formeUsuelleRepository.findTopFormeUsuelleByHighestPrixVolumeRatio();
        String nomMethode = "Méthode 2 : Maximum de bénéfice";
        return getStockDetailsWithBlocs(nomMethode, meilleurFormeUsuelle);
    }

    public StockDetails getMinLossStockDetails() {
        FormeUsuelle minPerteFormeUsuelle = formeUsuelleRepository.findTopFormeUsuelleByLowestVolume();
        String nomMethode = "Méthode 3 : Minimum de perte";
        return getStockDetailsWithBlocs(nomMethode, minPerteFormeUsuelle);
    }

    public StockDetails getStockDetailsWithBlocs(String nomMethode, FormeUsuelle transformationFormeUsuelle) {
        List<StockProduitDTO> stockProduits = etatStockRepository.findStockProduitsForFormeUsuelle();
        double volumeMeilleurFormeUsuelle = transformationFormeUsuelle.getProduit().getVolume();
        List<Bloc> blocsEnStock = blocRepository.findAllByEtatStockQuantiteGreaterThanZero();

        for (Bloc bloc : blocsEnStock) {
            StockProduitDTO stockProduitDTO = new StockProduitDTO();

            stockProduitDTO.setNomProduit(bloc.getProduit().getNomProduit());

            EtatStock etatStock = etatStockRepository.findFirstByBlocId(bloc.getId())
                            .orElseThrow(() -> new RuntimeException("Etat de stock introuvable"));
            stockProduitDTO.setQuantite(etatStock.getQuantite());

            stockProduitDTO.setCoutProductionUnitaire(bloc.getPrixProduction());

            double volumeBloc = bloc.getProduit().getVolume();
            int quantiteFaisable = (int) (volumeBloc / volumeMeilleurFormeUsuelle);
            stockProduitDTO.setPrixVenteUnitaire(quantiteFaisable * transformationFormeUsuelle.getPrixVente());

            stockProduits.add(stockProduitDTO);
        }

        double coutProductionTotal = stockProduits.stream()
                .mapToDouble(sp -> sp.getQuantite() * sp.getCoutProductionUnitaire())
                .sum();

        double prixVenteTotal = stockProduits.stream()
                .mapToDouble(sp -> sp.getQuantite() * sp.getPrixVenteUnitaire())
                .sum();

        StockDetails stockDetails = new StockDetails();
        stockDetails.setNomMethode(nomMethode);
        stockDetails.setCoutProductionTotal(coutProductionTotal);
        stockDetails.setPrixVenteTotal(prixVenteTotal);
        stockDetails.setStockProduits(stockProduits);

        return stockDetails;
    }

}
