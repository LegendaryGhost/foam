package com.tiarintsoa.foam.from;

import lombok.Data;

@Data
public class GenerationForm {
    private int blocCount;
    private int minLongueur = 20;
    private int maxLongueur = 26;
    private int minLargeur = 5;
    private int maxLargeur = 8;
    private int minHauteur = 10;
    private int maxHauteur = 16;
}
