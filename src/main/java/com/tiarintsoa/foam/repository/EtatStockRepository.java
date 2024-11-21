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

    @Query("""
            SELECT es FROM EtatStock es
            JOIN Article a ON es.article.id = a.id
            JOIN Produit p ON p.article.id = a.id
            JOIN Bloc b ON p.id = b.produit.id
            WHERE b.id = :idBloc
    """)
    Optional<EtatStock> findFirstByBlocId(@Param("idBloc") Long idBloc);

    @Query("""
        SELECT new com.tiarintsoa.foam.dto.StockProduitPriceAverageDTO(
            a.nomArticle,
            CAST(SUM(e.quantite) AS int),
            CAST(SUM(e.quantite * e.prixProduction) / SUM(e.quantite) AS double),
            f.prixVente
        )
        FROM EtatStock e
        JOIN e.article a
        JOIN Produit p ON p.article.id = a.id
        JOIN FormeUsuelle f ON f.produit.id = p.id
        JOIN p.typeProduit tp
        WHERE tp.nomTypeProduit = 'forme usuelle'
        GROUP BY a.nomArticle, f.prixVente
        """)
    List<StockProduitPriceAverageDTO> findStockProduitsPriceAverageForFormeUsuelle();

    @Query("""
        SELECT new com.tiarintsoa.foam.dto.StockProduitDTO(
            a.nomArticle,
            e.quantite,
            e.prixProduction,
            f.prixVente,
            oa.nomArticle,
            ola.nomArticle
        )
        FROM EtatStock e
        LEFT JOIN e.article a
        LEFT JOIN Produit p ON p.article.id = a.id
        LEFT JOIN FormeUsuelle f ON f.produit.id = p.id
        LEFT JOIN p.typeProduit tp
        LEFT JOIN e.origine o
        LEFT JOIN o.produit op
        LEFT JOIN op.article oa
        LEFT JOIN e.originel ol
        LEFT JOIN ol.produit olp
        LEFT JOIN olp.article ola
        WHERE tp.nomTypeProduit = 'forme usuelle'
        """)
    List<StockProduitDTO> findStockProduitsForFormeUsuelle();


    @Query("""
        SELECT es FROM EtatStock es
       where es.article.id = :idArticle order by es.dateHeureInsertion
    """)
    List<EtatStock> findAllByArticleInFormule(@Param("idArticle") Long idArticle);
}
