package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.dto.MachineDTO;
import com.tiarintsoa.foam.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    @Query(value = """
        SELECT 
            machine.id_machine AS idMachine,
            machine.nom_machine AS nomMachine,
            COALESCE(SUM(bloc.prix_production), 0) AS prixProductionPratique,
            COALESCE(SUM(mouvement_stock.prix_production * mouvement_stock.quantite_sortie), 0) AS prixProductionTheorique,
            COALESCE(SUM(produit.longueur * produit.largeur * produit.hauteur), 0) AS volumeTotalProduit
        FROM machine
        LEFT JOIN bloc ON machine.id_machine = bloc.id_machine
        LEFT JOIN mouvement_stock ON bloc.id_bloc = mouvement_stock.id_source
        INNER JOIN produit ON bloc.id_produit = produit.id_produit
        GROUP BY machine.id_machine, machine.nom_machine
    """, nativeQuery = true)
    List<Object[]> findMachineStatisticsNative();

}
