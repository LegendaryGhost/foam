package com.tiarintsoa.foam.repository;

import com.tiarintsoa.foam.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
