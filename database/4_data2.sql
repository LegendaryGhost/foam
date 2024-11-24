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
