package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.StockDetails;
import com.tiarintsoa.foam.dto.StockProduitDTO;
import com.tiarintsoa.foam.repository.EtatStockRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private EtatStockRepository etatStockRepository;

    public List<StockDetails> getStockDetails() {
        List<StockDetails> stockDetails = new ArrayList<>();
        stockDetails.add(getBasicStockDetails());
        stockDetails.add(getMaxGainStockDetails());
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

}
