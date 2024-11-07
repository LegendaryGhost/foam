package com.tiarintsoa.foam.dto;

import lombok.Data;

@Data
public class StockProduitPriceAverageDTO {
    protected String nomProduit;
    protected int quantite;
    protected double coutProductionUnitaire;
    protected double prixVenteUnitaire;

    public StockProduitPriceAverageDTO() {}

    public StockProduitPriceAverageDTO(String nomProduit, int quantite, double coutProductionUnitaire, double prixVenteUnitaire) {
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.coutProductionUnitaire = coutProductionUnitaire;
        this.prixVenteUnitaire = prixVenteUnitaire;
    }
}
