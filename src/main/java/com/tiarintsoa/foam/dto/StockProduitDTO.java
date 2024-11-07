package com.tiarintsoa.foam.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StockProduitDTO extends StockProduitPriceAverageDTO {

    private String nomOrigine;
    private String nomOriginel;

    public StockProduitDTO(String nomProduit, int quantite, double coutProductionUnitaire, double prixVenteUnitaire, String nomOrigine, String nomOriginel) {
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.coutProductionUnitaire = coutProductionUnitaire;
        this.prixVenteUnitaire = prixVenteUnitaire;
        this.nomOrigine = nomOrigine;
        this.nomOriginel = nomOriginel;
    }
}
