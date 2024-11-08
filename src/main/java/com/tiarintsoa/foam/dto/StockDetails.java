package com.tiarintsoa.foam.dto;

import lombok.Data;

import java.util.List;

@Data
public class StockDetails {
    private String nomMethode;
    private String nomProduitUtilise;
    private double coutProductionTotal;
    private double prixVenteTotal;
    private List<StockProduitPriceAverageDTO> stockProduits;
}
