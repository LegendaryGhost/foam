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

-- Blocs
insert into article(nom_article) values
    ('Bloc 1'),
    ('Bloc 2'),
    ('Bloc 3'),
    ('Bloc 4'),
    ('Bloc 5'),
    ('Bloc 6'),
    ('Bloc 7'),
    ('Bloc 8'),
    ('Bloc 9'),
    ('Bloc 10');
insert into produit(longueur, largeur, hauteur, id_type_produit, id_article) VALUES
    (25,	5,	12, 1, 1),
    (25,	5,	11, 1, 2),
    (21,	7,	10, 1, 3),
    (20,	7,	11, 1, 4),
    (25,	7,	14, 1, 5),
    (23,	7,	12, 1, 6),
    (22,	6,	15, 1, 7),
    (22,	7,	11, 1, 8),
    (20,	5,	10, 1, 9),
    (21,	5,	15, 1, 10);
insert into bloc(prix_production, id_machine, date_heure_insertion, id_produit) VALUES
    (9740849.84,	2,	'2023-04-04 18:06:19.000000', 1),
    (10077142.1,	1,	'2024-09-20 03:02:57.000000', 2),
    (5816964.47,	3,	'2023-08-29 19:38:14.000000', 3),
    (9673093.64, 2,	'2023-05-29 03:25:50.000000', 4),
    (11101237.29,	2,	'2022-12-10 10:11:54.000000', 5),
    (12699671.13,	1, '2024-01-20 15:25:39.000000', 6),
    (15749421.55,	4,	'2022-11-04 22:06:47.000000', 7),
    (8554114.05,	1,	'2023-12-07 17:55:58.000000', 8),
    (9538700.07,	3,	'2023-05-28 09:58:57.000000', 9),
    (7817950.53,	3,	'2024-05-09 08:52:24.000000', 10);





-- Articles + Formule de bloc
INSERT INTO article(nom_article) VALUES
                                     ('essence'),
                                     ('papier'),
                                     ('durcissant');
INSERT INTO formule_bloc(quantite_necessaire, id_article, unite) VALUES
                                                                     (1, 11, 'litre'),
                                                                     (1, 12, 'kilogramme'),
                                                                     (0.5, 13, 'boite');
-- Achat d'articles
INSERT INTO etat_stock(quantite, prix_production, date_heure_insertion, id_article) VALUES
                                                                                        (4430, 5900, '2022-01-01', 11),
                                                                                        (4430, 400, '2022-01-01', 12),
                                                                                        (2215, 450, '2022-01-01', 13),
                                                                                        (7204, 5950, '2023-01-01', 11),
                                                                                        (7204, 500, '2023-01-01', 12),
                                                                                        (3602, 500, '2023-01-01', 13),
                                                                                        (4882, 6000, '2024-01-01', 11),
                                                                                        (4882, 600, '2024-01-01', 12),
                                                                                        (2441, 550, '2024-01-01', 13);
