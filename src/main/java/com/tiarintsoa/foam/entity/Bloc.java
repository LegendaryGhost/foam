package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "bloc")
@Data
public class Bloc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bloc")
    private Long id;

    @Column(name = "prix_production", nullable = false)
    private Double prixProduction;

    @ManyToOne
    @JoinColumn(name = "id_origine")
    private Bloc origine;

    @OneToOne
    @JoinColumn(name = "id_produit", nullable = false, unique = true)
    private Produit produit;
}
