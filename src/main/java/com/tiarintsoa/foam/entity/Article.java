package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article", nullable = false)
    private Long id;

    @Column(name = "nom_article", nullable = false, unique = true)
    private String nomArticle;

}