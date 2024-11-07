package com.tiarintsoa.foam.entity;

import com.tiarintsoa.foam.from.BlocForm;
import jakarta.persistence.*;
import lombok.Data;

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
    @JoinColumn(name = "id_originel")
    private Bloc originel;

    @ManyToOne
    @JoinColumn(name = "id_origine")
    private Bloc origine;

    @OneToOne
    @JoinColumn(name = "id_produit", nullable = false, unique = true)
    private Produit produit;

    public BlocForm toBlocForm() {
        BlocForm blocForm = new BlocForm();
        blocForm.setId(id);
        blocForm.setNom(produit == null ? null : produit.getNomProduit());
        blocForm.setLongueur(produit == null ? null : produit.getLongueur());
        blocForm.setLargeur(produit == null ? null : produit.getLargeur());
        blocForm.setHauteur(produit == null ? null : produit.getHauteur());
        blocForm.setCoutProduction(this.getPrixProduction());
        return blocForm;
    }
}
