package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

    @Query("""
        SELECT MAX(p.id) FROM Produit p
    """)
    Long findMaxId();

}
