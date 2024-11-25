CREATE TEMP TABLE temp_table (
    id_matelas serial,
    Long numeric(15,2),
    Larg numeric(15,2),
    Haut numeric(15,2),
    Revient numeric(15,2),
    Date date,
    Machine varchar(50)
);

\COPY temp_table(Date,Long,Larg,Haut,Revient,Machine) FROM 'D:\ITU\S5\architecture-logiciel\foam\database\import\data_1m.csv' WITH (FORMAT CSV, DELIMITER ',', HEADER);

INSERT INTO article(nom_article)
    SELECT 'B' || id_matelas FROM temp_table;

INSERT INTO produit(longueur, largeur, hauteur, id_type_produit, id_article)
    SELECT Long, Larg, Haut, 1, id_matelas FROM temp_table;

INSERT INTO bloc(prix_production, date_heure_insertion, id_machine, id_produit)
    SELECT Revient, Date, CAST(REPLACE(Machine, 'M', '') AS INT), id_matelas FROM temp_table;

INSERT INTO etat_stock(quantite, prix_production, date_heure_insertion, id_article)
    SELECT 1, Revient, Date, id_matelas FROM temp_table;

DROP TABLE temp_table;