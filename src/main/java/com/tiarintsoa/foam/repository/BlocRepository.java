package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.Bloc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlocRepository extends JpaRepository<Bloc, Long> {

    @Query("""
            SELECT b FROM Bloc b
            JOIN b.produit p
            JOIN p.article a
            JOIN EtatStock e ON a.id = e.article.id
            WHERE e.quantite > 0
    """)
    List<Bloc> findAllByEtatStockQuantiteGreaterThanZero();

    @Query("""
        SELECT b FROM Bloc b
        ORDER by b.dateHeureInsertion, b.id
    """)
    List<Bloc> findAllOrderByDate();

    @Query("SELECT AVG(b.prixProduction / (p.longueur * p.largeur * p.hauteur)) " +
            "FROM Bloc b INNER JOIN b.produit p")
    Double findAverageVolumicProductionCost();

    @Query("""
        SELECT MAX(b.id) FROM Bloc b
    """)
    Long findMaxId();

}
