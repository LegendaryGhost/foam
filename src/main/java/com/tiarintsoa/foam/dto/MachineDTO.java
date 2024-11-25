package com.tiarintsoa.foam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineDTO {
    private Long idMachine;
    private String nomMachine;
    private Double prixProductionPratique;
    private Double prixProductionTheorique;
    private Double volumeTotalProduit;

    public Double getEcart() {
        return prixProductionPratique - prixProductionTheorique;
    }

    public Double getEcartVolumique() {
        return getEcart() / volumeTotalProduit;
    }
}
