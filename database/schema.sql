CREATE TABLE type_produit(
                             id_type_produit SERIAL,
                             nom_type_produit VARCHAR(50)  NOT NULL,
                             PRIMARY KEY(id_type_produit)
);

CREATE TABLE produit(
                        id_produit SERIAL,
                        nom_produit VARCHAR(255)  NOT NULL,
                        longueur NUMERIC(15,2)   NOT NULL,
                        largeur NUMERIC(15,2)   NOT NULL,
                        hauteur NUMERIC(15,2)   NOT NULL,
                        id_type_produit INTEGER NOT NULL,
                        PRIMARY KEY(id_produit),
                        UNIQUE(nom_produit),
                        FOREIGN KEY(id_type_produit) REFERENCES type_produit(id_type_produit)
);

CREATE TABLE mouvement_stock(
                                id_mouvement_stock SERIAL,
                                quantite_entree INTEGER NOT NULL,
                                quantite_sortie NUMERIC(15,2)   NOT NULL,
                                date_heure_mouvement TIMESTAMP NOT NULL DEFAULT NOW(),
                                id_produit INTEGER NOT NULL,
                                PRIMARY KEY(id_mouvement_stock),
                                FOREIGN KEY(id_produit) REFERENCES produit(id_produit)
);

CREATE TABLE bloc(
                     id_bloc SERIAL,
                     prix_production NUMERIC(15,2)   NOT NULL,
                     id_origine INTEGER,
                     id_produit INTEGER NOT NULL,
                     PRIMARY KEY(id_bloc),
                     UNIQUE(id_produit),
                     FOREIGN KEY(id_origine) REFERENCES bloc(id_bloc),
                     FOREIGN KEY(id_produit) REFERENCES produit(id_produit)
);

CREATE TABLE forme_usuelle(
                              id_forme_usuelle SERIAL,
                              prix_vente NUMERIC(15,2)   NOT NULL,
                              id_produit INTEGER NOT NULL,
                              PRIMARY KEY(id_forme_usuelle),
                              UNIQUE(id_produit),
                              FOREIGN KEY(id_produit) REFERENCES produit(id_produit)
);

CREATE TABLE etat_stock(
                           id_etat_stock SERIAL,
                           quantite INTEGER NOT NULL,
                           prix_production NUMERIC(15,2)   NOT NULL,
                           date_heure_insertion TIMESTAMP NOT NULL DEFAULT NOW(),
                           id_origine INTEGER,
                           id_produit INTEGER NOT NULL,
                           PRIMARY KEY(id_etat_stock),
                           FOREIGN KEY(id_origine) REFERENCES bloc(id_bloc),
                           FOREIGN KEY(id_produit) REFERENCES produit(id_produit)
);
