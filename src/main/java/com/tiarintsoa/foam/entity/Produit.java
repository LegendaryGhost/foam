package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "produit")
@Data
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produit")
    private Long id;

    @Column(name = "longueur", nullable = false)
    private Double longueur;

    @Column(name = "largeur", nullable = false)
    private Double largeur;

    @Column(name = "hauteur", nullable = false)
    private Double hauteur;

    @ManyToOne
    @JoinColumn(name = "id_type_produit", nullable = false)
    private TypeProduit typeProduit;

    @OneToOne
    @JoinColumn(name = "id_article", nullable = false, unique = true)
    private Article article;

    public double getVolume() {
        return longueur * largeur * hauteur;
    }
}
