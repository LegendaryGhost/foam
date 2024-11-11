INSERT INTO type_produit (nom_type_produit) VALUES
                                                ('bloc'),
                                                ('forme usuelle');

-- Formes usuelles
INSERT INTO produit(nom_produit, longueur, largeur, hauteur, id_type_produit) VALUES
                                                                                  ('U1', 16, 4, 2, 2),
                                                                                  ('U2', 10, 7, 1, 2),
                                                                                  ('U3', 5, 1, 1, 2);
INSERT INTO forme_usuelle(prix_vente, id_produit) VALUES
                                                      (20000, 1),
                                                      (12000, 2),
                                                      (600, 3);

-- Blocs
INSERT INTO produit(nom_produit, longueur, largeur, hauteur, id_type_produit) VALUES
                                                                                  ('Bloc 1',  100, 20, 10, 1),
                                                                                  ('Bloc 2',  100, 40, 10, 1);
INSERT INTO bloc(prix_production, id_produit) VALUES
                                                              (2000000, 4),
                                                              (3000000, 5);
INSERT INTO etat_stock(quantite, prix_production, id_produit) VALUES
                                                                              (1, 2000000, 4),
                                                                              (1, 3000000, 5);
