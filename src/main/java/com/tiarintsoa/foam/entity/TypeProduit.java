package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "type_produit")
@Data
public class TypeProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_produit")
    private Long idTypeProduit;

    @Column(name = "nom_type_produit", nullable = false, length = 50)
    private String nomTypeProduit;

}
