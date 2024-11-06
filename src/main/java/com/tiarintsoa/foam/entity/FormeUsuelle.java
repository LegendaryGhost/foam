package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "forme_usuelle")
@Data
public class FormeUsuelle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_forme_usuelle")
    private Long id;

    @Column(name = "prix_vente", nullable = false)
    private Double prixVente;

    @OneToOne
    @JoinColumn(name = "id_produit", nullable = false, unique = true)
    private Produit produit;
}
