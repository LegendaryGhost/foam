package com.tiarintsoa.foam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "machine")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_machine", nullable = false)
    private Long id;

    @Column(name = "nom_machine", nullable = false, length = 50)
    private String nomMachine;

}