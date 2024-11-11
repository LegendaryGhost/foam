package com.tiarintsoa.foam.from;

import com.tiarintsoa.foam.utils.UnitConverter;
import lombok.Data;

import java.util.List;

@Data
public class TransformationForm {
    private Long idBloc;
    private String longueurResteString;
    private String largeurResteString;
    private String hauteurResteString;
    private List<QuantiteUsuelleForm> usualFormsQuantities;

    public double getLongueurResteAsDouble() {
        return UnitConverter.convertStringToDouble(longueurResteString);
    }

    public double getLargeurResteAsDouble() {
        return UnitConverter.convertStringToDouble(largeurResteString);
    }

    public double getHauteurResteAsDouble() {
        return UnitConverter.convertStringToDouble(hauteurResteString);
    }

    public double getVolumeReste() {
        return getLongueurResteAsDouble() * getLargeurResteAsDouble() * getHauteurResteAsDouble();
    }
}
