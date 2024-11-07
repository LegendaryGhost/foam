package com.tiarintsoa.foam.dto;

import lombok.Data;

@Data
public class StockProduitDTO {
    private String nomProduit;
    private int quantite;
    private double coutProductionUnitaire;
    private double prixVenteUnitaire;

    public StockProduitDTO(String nomProduit, int quantite, double coutProductionUnitaire, double prixVenteUnitaire) {
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.coutProductionUnitaire = coutProductionUnitaire;
        this.prixVenteUnitaire = prixVenteUnitaire;
    }
}
