package com.tiarintsoa.foam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineDTO {
    private Long idMachine;               // ID de la machine
    private String nomMachine;            // Nom de la machine
    private Double prixProductionPratique; // Prix de production pratique
    private Double prixProductionTheorique; // Prix de production théorique
    private Double volumeTotalProduit;    // Volume total des produits

    public Double getEcart() {
        return prixProductionPratique - prixProductionTheorique;
    }

    public Double getEcartVolumique() {
        return getEcart() / volumeTotalProduit;
    }
}
