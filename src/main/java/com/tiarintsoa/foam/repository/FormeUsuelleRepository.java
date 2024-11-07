package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.FormeUsuelle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FormeUsuelleRepository extends JpaRepository<FormeUsuelle, Long> {

    @Query(value = """
        SELECT f.id_forme_usuelle AS id_forme_usuelle,
               f.prix_vente AS prix_vente,
               f.id_produit AS id_produit
        FROM forme_usuelle f
        JOIN produit p ON f.id_produit = p.id_produit
        ORDER BY (f.prix_vente / (p.longueur * p.largeur * p.hauteur)) DESC
        LIMIT 1
    """, nativeQuery = true)
    FormeUsuelle findTopFormeUsuelleByHighestPrixVolumeRatio();


}
