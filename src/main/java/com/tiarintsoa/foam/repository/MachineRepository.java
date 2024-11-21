package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {

    @Query(value = """
        SELECT * FROM statistiques_machine
            ORDER BY (prix_production_pratique - prix_production_théorique) / volume_total_produit
    """, nativeQuery = true)
    List<Object[]> findMachineStatisticsNative();

}
