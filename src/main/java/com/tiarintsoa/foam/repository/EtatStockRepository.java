package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.EtatStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EtatStockRepository extends JpaRepository<EtatStock, Long> {

    @Query("SELECT es FROM EtatStock es " +
            "JOIN Produit p ON es.produit.id = p.id " +
            "JOIN Bloc b ON p.id = b.produit.id " +
            "WHERE b.id = :idBloc")
    Optional<EtatStock> findFirstByBlocId(@Param("idBloc") Long idBloc);

}
