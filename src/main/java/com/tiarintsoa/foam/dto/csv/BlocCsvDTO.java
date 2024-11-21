package com.tiarintsoa.foam.dto.csv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlocCsvDTO {
    private String date;    // Exemple : "10/05/2024"
    private Double longueur;
    private Double largeur;
    private Double hauteur;
    private Double coutRevient;
    private String nomMachine; // Exemple : "M2"
}

