package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "etat_stock")
@Data
public class EtatStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etat_stock")
    private Long id;

    @Column(name = "quantite", nullable = false)
    private Double quantite;

    @Column(name = "prix_production", nullable = false)
    private Double prixProduction;

    @Column(name = "date_heure_insertion", nullable = false, insertable = false, updatable = false)
    private LocalDateTime dateHeureInsertion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_originel")
    private Bloc originel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_origine")
    private Bloc origine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;
}
