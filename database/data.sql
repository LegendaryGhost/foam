INSERT INTO type_produit (nom_type_produit) VALUES
                                                ('bloc'),
                                                ('forme usuelle');

-- Formes usuelles
INSERT INTO produit(nom_produit, longueur, largeur, hauteur, id_type_produit) VALUES
                                                                                  ('U1', 2, 1.6, 0.3, 2),
                                                                                  ('U2', 2, 1.4, 0.25, 2),
                                                                                  ('U3', 1.9, 0.9, 0.2, 2),
                                                                                  ('U4', 0.1, 0.1, 0.1, 2);
INSERT INTO forme_usuelle(prix_vente, id_produit) VALUES
                                                      (125000, 1),
                                                      (100000, 2),
                                                      (60000, 3),
                                                      (100, 4);

-- Blocs
INSERT INTO produit(nom_produit, longueur, largeur, hauteur, id_type_produit) VALUES
                                                                                  ('Bloc 1',  10.60, 8.2, 5, 1),
                                                                                  ('Bloc 2',  10.60, 8.2, 5, 1);
INSERT INTO bloc(prix_production, id_produit) VALUES
                                                              (38250000.00, 1),
                                                              (38250000.00, 2);
INSERT INTO etat_stock(quantite, prix_production, id_produit) VALUES
                                                                              (1, 38250000.00, 5),
                                                                              (1, 38250000.00, 6);
