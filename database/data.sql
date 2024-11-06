INSERT INTO type_produit (nom_type_produit) VALUES
                                                ('bloc'),
                                                ('forme usuelle');

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
