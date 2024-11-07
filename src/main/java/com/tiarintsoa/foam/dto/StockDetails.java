package com.tiarintsoa.foam.dto;

import lombok.Data;

import java.util.List;

@Data
public class StockDetails {
    private String nomMethode;
    private String nomProduitUtilise = "Aucun";
    private double coutProductionTotal;
    private double prixVenteTotal;
    private List<StockProduitDTO> stockProduits;
}
