-- Types produit
INSERT INTO type_produit(nom_type_produit) VALUES
                                                ('bloc'),
                                                ('forme usuelle');



-- Machines
INSERT INTO machine(nom_machine) VALUES
                                     ('M1'),
                                     ('M2'),
                                     ('M3'),
                                     ('M4');



-- Formes usuelles
-- INSERT INTO article(nom_article) VALUES
--                                      ('U1'),
--                                      ('U2'),
--                                      ('U3');
-- INSERT INTO produit(id_article, longueur, largeur, hauteur, id_type_produit) VALUES
--                                                                                   (1, 16, 4, 2, 2),
--                                                                                   (2, 10, 7, 1, 2),
--                                                                                   (3, 5, 1, 1, 2);
-- INSERT INTO forme_usuelle(prix_vente, id_produit) VALUES
--                                                       (20000, 1),
--                                                       (12000, 2),
--                                                       (600, 3);


-- Blocs
-- INSERT INTO article(nom_article) VALUES
--                                      ('Bloc 1'),
--                                      ('Bloc 2');
--
-- INSERT INTO produit(id_article, longueur, largeur, hauteur, id_type_produit) VALUES
--                                                                                   (4,  100, 20, 10, 1),
--                                                                                   (5,  100, 40, 10, 1);
-- INSERT INTO bloc(prix_production, id_machine, id_produit, date_heure_insertion) VALUES
--                                                               (2000000, 1, 4, '2022-05-06'),
--                                                               (3000000, 2, 5, '2024-01-12');
-- INSERT INTO etat_stock(quantite, prix_production, id_article) VALUES
--                                                                               (1, 2000000, 1000001),
--                                                                               (1, 3000000, 1000002);
