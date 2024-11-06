package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlocRepository extends JpaRepository<Bloc, Long> {

    @Query("SELECT b FROM Bloc b " +
            "JOIN b.produit p " +
            "JOIN EtatStock e ON p.id = e.produit.id " +
            "WHERE e.quantite > 0")
    List<Bloc> findAllByEtatStockQuantiteGreaterThanZero();

}
