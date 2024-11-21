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


-- Articles + Formule de bloc
INSERT INTO article(nom_article) VALUES
                                     ('essence'),
                                     ('papier'),
                                     ('durcissant');
INSERT INTO formule_bloc(quantite_necessaire, id_article, unite) VALUES
                                                                     (1, 1000001, 'litre'),
                                                                     (1, 1000002, 'kilogramme'),
                                                                     (0.5, 1000003, 'boite');
-- Achat d'articles
INSERT INTO etat_stock(quantite, prix_production, date_heure_insertion, id_article) VALUES
                                                                                        (224727345, 6000, '2024-01-01', 1000001),
                                                                                        (224727345, 600, '2024-01-01', 1000002),
                                                                                        (112363672.5, 550, '2024-01-01', 1000003),
                                                                                        (225233313, 5950, '2023-01-01', 1000001),
                                                                                        (225233313, 500, '2023-01-01', 1000002),
                                                                                        (112616656.5, 500, '2023-01-01', 1000003),
                                                                                        (224974632, 5900, '2022-01-01', 1000001),
                                                                                        (224974632, 400, '2022-01-01', 1000002),
                                                                                        (112487316, 450, '2022-01-01', 1000003);
