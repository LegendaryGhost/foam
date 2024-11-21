package com.tiarintsoa.foam.entity;

import com.tiarintsoa.foam.from.BlocForm;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

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

    @Column(name = "prix_production", nullable = false)
    @ColumnDefault("0")
    private Double prixProductionTheorique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_originel")
    private Bloc originel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_origine")
    private Bloc origine;

    @Column(name = "date_heure_insertion")
    @ColumnDefault("NOW()")
    private LocalDateTime dateHeureInsertion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_produit", nullable = false, unique = true)
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_machine", nullable = false)
    private Machine machine;

    public BlocForm toBlocForm() {
        BlocForm blocForm = new BlocForm();
        blocForm.setId(id);
        blocForm.setNom(produit == null ? null : produit.getArticle().getNomArticle());
        blocForm.setLongueur(produit == null ? null : produit.getLongueur());
        blocForm.setLargeur(produit == null ? null : produit.getLargeur());
        blocForm.setHauteur(produit == null ? null : produit.getHauteur());
        blocForm.setCoutProduction(this.getPrixProduction());
        return blocForm;
    }
}
