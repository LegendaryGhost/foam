package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.StockDetails;
import com.tiarintsoa.foam.dto.StockProduitDTO;
import com.tiarintsoa.foam.dto.StockProduitPriceAverageDTO;
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

    public List<StockProduitDTO> getStockProduits() {
        return etatStockRepository.findStockProduitsForFormeUsuelle();
    }

    public List<StockDetails> getStockDetails() {
        List<StockDetails> stockDetails = new ArrayList<>();
        stockDetails.add(getBasicStockDetails());
        stockDetails.add(getMaxGainStockDetails());
        stockDetails.add(getMinLossStockDetails());
        return stockDetails;
    }

    public StockDetails getBasicStockDetails() {
        List<StockProduitPriceAverageDTO> stockProduits = etatStockRepository.findStockProduitsPriceAverageForFormeUsuelle();

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
        List<StockProduitPriceAverageDTO> stockProduits = etatStockRepository.findStockProduitsPriceAverageForFormeUsuelle();
        double volumeMeilleurFormeUsuelle = transformationFormeUsuelle.getProduit().getVolume();
        List<Bloc> blocsEnStock = blocRepository.findAllByEtatStockQuantiteGreaterThanZero();

        for (Bloc bloc : blocsEnStock) {
            StockProduitPriceAverageDTO stockProduitPriceAverageDTO = new StockProduitPriceAverageDTO();

            stockProduitPriceAverageDTO.setNomProduit(bloc.getProduit().getNomProduit());

            EtatStock etatStock = etatStockRepository.findFirstByBlocId(bloc.getId())
                            .orElseThrow(() -> new RuntimeException("Etat de stock introuvable"));
            stockProduitPriceAverageDTO.setQuantite(etatStock.getQuantite());

            stockProduitPriceAverageDTO.setCoutProductionUnitaire(bloc.getPrixProduction());

            double volumeBloc = bloc.getProduit().getVolume();
            int quantiteFaisable = (int) (volumeBloc / volumeMeilleurFormeUsuelle);
            stockProduitPriceAverageDTO.setPrixVenteUnitaire(quantiteFaisable * transformationFormeUsuelle.getPrixVente());

            stockProduits.add(stockProduitPriceAverageDTO);
        }

        double coutProductionTotal = stockProduits.stream()
                .mapToDouble(sp -> sp.getQuantite() * sp.getCoutProductionUnitaire())
                .sum();

        double prixVenteTotal = stockProduits.stream()
                .mapToDouble(sp -> sp.getQuantite() * sp.getPrixVenteUnitaire())
                .sum();

        StockDetails stockDetails = new StockDetails();
        stockDetails.setNomMethode(nomMethode);
        stockDetails.setNomProduitUtilise(transformationFormeUsuelle.getProduit().getNomProduit());
        stockDetails.setCoutProductionTotal(coutProductionTotal);
        stockDetails.setPrixVenteTotal(prixVenteTotal);
        stockDetails.setStockProduits(stockProduits);

        return stockDetails;
    }

}
