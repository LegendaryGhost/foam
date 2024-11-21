CREATE TABLE type_produit(
                             id_type_produit SERIAL,
                             nom_type_produit VARCHAR(50)  NOT NULL,
                             PRIMARY KEY(id_type_produit)
);

CREATE TABLE machine(
                        id_machine SERIAL,
                        nom_machine VARCHAR(50)  NOT NULL,
                        PRIMARY KEY(id_machine),
                        UNIQUE(nom_machine)
);

CREATE TABLE article(
                        id_article SERIAL,
                        nom_article VARCHAR(255)  NOT NULL,
                        PRIMARY KEY(id_article),
                        UNIQUE(nom_article)
);

CREATE TABLE formule_bloc(
                             id_formule_bloc SERIAL,
                             quantite_necessaire NUMERIC(15,2)   NOT NULL,
                             unite VARCHAR(50)  NOT NULL,
                             id_article INTEGER NOT NULL,
                             PRIMARY KEY(id_formule_bloc),
                             UNIQUE(id_article),
                             FOREIGN KEY(id_article) REFERENCES article(id_article)
);

CREATE TABLE produit(
                        id_produit SERIAL,
                        longueur NUMERIC(15,2)   NOT NULL,
                        largeur NUMERIC(15,2)   NOT NULL,
                        hauteur NUMERIC(15,2)   NOT NULL,
                        id_type_produit INTEGER NOT NULL,
                        id_article INTEGER NOT NULL,
                        PRIMARY KEY(id_produit),
                        UNIQUE(id_article),
                        FOREIGN KEY(id_type_produit) REFERENCES type_produit(id_type_produit),
                        FOREIGN KEY(id_article) REFERENCES article(id_article)
);

CREATE TABLE bloc(
                     id_bloc SERIAL,
                     prix_production NUMERIC(15,2)   NOT NULL,
                     traite BOOLEAN NOT NULL DEFAULT FALSE,
                     date_heure_insertion TIMESTAMP NOT NULL DEFAULT NOW(),
                     id_machine INTEGER NOT NULL,
                     id_originel INTEGER,
                     id_origine INTEGER,
                     id_produit INTEGER NOT NULL,
                     PRIMARY KEY(id_bloc),
                     UNIQUE(id_produit),
                     FOREIGN KEY(id_machine) REFERENCES machine(id_machine),
                     FOREIGN KEY(id_originel) REFERENCES bloc(id_bloc),
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
                           id_source_temp INTEGER,
                           id_originel INTEGER,
                           id_origine INTEGER,
                           id_article INTEGER NOT NULL,
                           PRIMARY KEY(id_etat_stock),
                           FOREIGN KEY(id_source_temp) REFERENCES bloc(id_bloc),
                           FOREIGN KEY(id_originel) REFERENCES bloc(id_bloc),
                           FOREIGN KEY(id_origine) REFERENCES bloc(id_bloc),
                           FOREIGN KEY(id_article) REFERENCES article(id_article)
);

CREATE TABLE mouvement_stock(
                                id_mouvement_stock SERIAL,
                                quantite_entree INTEGER NOT NULL,
                                quantite_sortie INTEGER NOT NULL,
                                prix_production NUMERIC(15,2)  ,
                                date_heure_mouvement TIMESTAMP NOT NULL DEFAULT NOW(),
                                id_source INTEGER,
                                id_article INTEGER NOT NULL,
                                PRIMARY KEY(id_mouvement_stock),
                                FOREIGN KEY(id_source) REFERENCES bloc(id_bloc),
                                FOREIGN KEY(id_article) REFERENCES article(id_article)
);

CREATE INDEX idx_bloc_traite ON bloc (traite);
CREATE INDEX idx_etat_stock_id_article ON etat_stock (id_article);
CREATE INDEX idx_etat_stock_date_heure_insertion ON etat_stock (date_heure_insertion);
CREATE INDEX idx_formule_bloc_id_article ON formule_bloc (id_article);
CREATE INDEX idx_bloc_date_heure_insertion ON bloc (date_heure_insertion);

