package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.dto.StockProduitDTO;
import com.tiarintsoa.foam.dto.StockProduitPriceAverageDTO;
import com.tiarintsoa.foam.entity.EtatStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EtatStockRepository extends JpaRepository<EtatStock, Long> {

    @Query("SELECT es FROM EtatStock es " +
            "JOIN Produit p ON es.produit.id = p.id " +
            "JOIN Bloc b ON p.id = b.produit.id " +
            "WHERE b.id = :idBloc")
    Optional<EtatStock> findFirstByBlocId(@Param("idBloc") Long idBloc);

    @Query("""
        SELECT new com.tiarintsoa.foam.dto.StockProduitPriceAverageDTO(
            p.nomProduit,
            CAST(SUM(e.quantite) AS int),
            CAST(SUM(e.quantite * e.prixProduction) / SUM(e.quantite) AS double),
            f.prixVente
        )
        FROM EtatStock e
        JOIN e.produit p
        JOIN FormeUsuelle f ON f.produit.id = p.id
        JOIN p.typeProduit tp
        WHERE tp.nomTypeProduit = 'forme usuelle'
        GROUP BY p.nomProduit, f.prixVente
        """)
    List<StockProduitPriceAverageDTO> findStockProduitsPriceAverageForFormeUsuelle();

    @Query("""
        SELECT new com.tiarintsoa.foam.dto.StockProduitDTO(
            p.nomProduit,
            e.quantite,
            e.prixProduction,
            f.prixVente,
            op.nomProduit,
            olp.nomProduit
        )
        FROM EtatStock e
        LEFT JOIN e.produit p
        LEFT JOIN FormeUsuelle f ON f.produit.id = p.id
        LEFT JOIN p.typeProduit tp
        LEFT JOIN e.origine o
        LEFT JOIN o.produit op
        LEFT JOIN e.originel ol
        LEFT JOIN ol.produit olp
        WHERE tp.nomTypeProduit = 'forme usuelle'
        """)
    List<StockProduitDTO> findStockProduitsForFormeUsuelle();

}
