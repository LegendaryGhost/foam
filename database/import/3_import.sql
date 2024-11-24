CREATE TEMP TABLE temp_table (
    id_matelas serial,
    longueur numeric(15,2),
    largeur numeric(15,2),
    epaisseur numeric(15,2),
    prix_unitaire numeric(15,2),
    date_mvt_stock date,
    id_machine INTEGER,
    etat INTEGER default 2,
    id_type_matelas INTEGER default 1
);

\COPY temp_table(date_mvt_stock,longueur,largeur,epaisseur,prix_unitaire,id_machine) FROM 'D:\ITU\S5\architecture-logiciel\foam\database\import\donnee.csv' WITH (FORMAT CSV, DELIMITER ',', HEADER);

INSERT INTO article(nom_article)
    SELECT 'B' || id_matelas FROM temp_table;

INSERT INTO produit(longueur, largeur, hauteur, id_type_produit, id_article)
    SELECT longueur, largeur, epaisseur, 1, id_matelas FROM temp_table;

INSERT INTO bloc(prix_production, date_heure_insertion, id_machine, id_produit)
    SELECT prix_unitaire, date_mvt_stock, id_machine, id_matelas FROM temp_table;

INSERT INTO etat_stock(quantite, prix_production, date_heure_insertion, id_article)
    SELECT 1, prix_unitaire, date_mvt_stock, id_matelas FROM temp_table;

DROP TABLE temp_table;