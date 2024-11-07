package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement_stock")
@Data
public class MouvementStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mouvement_stock")
    private Long id;

    @Column(name = "quantite_entree", nullable = false)
    private Integer quantiteEntree;

    @Column(name = "quantite_sortie", nullable = false)
    private Long quantiteSortie;

    @Column(name = "date_heure_mouvement", nullable = false)
    private LocalDateTime dateHeureMouvement;

    @ManyToOne
    @JoinColumn(name = "id_produit", nullable = false)
    private Produit produit;
}
