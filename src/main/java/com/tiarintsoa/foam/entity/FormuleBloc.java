package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "formule_bloc")
public class FormuleBloc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formule_bloc", nullable = false)
    private Long id;

    @Column(name = "quantite_necessaire", nullable = false)
    private Double quantiteNecessaire;

    @Column(name = "unite", nullable = false)
    private String unite;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_article", nullable = false)
    private Article article;

}