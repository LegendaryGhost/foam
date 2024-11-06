package com.tiarintsoa.foam.from;

import lombok.Data;

import java.util.List;

@Data
public class TransformationForm {
    private Long idBloc;
    private double longueurReste;
    private double largeurReste;
    private double hauteurReste;
    private List<QuantiteUsuelleForm> usualFormsQuantities;
}
