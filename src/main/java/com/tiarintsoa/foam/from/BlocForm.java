package com.tiarintsoa.foam.from;

import lombok.Data;

@Data
public class BlocForm {
    private Long id;
    private String nom;
    private Double longueur;
    private Double largeur;
    private Double hauteur;
    private Double coutProduction;
}
