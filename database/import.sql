CREATE TEMP TABLE temp_table (
    id Varchar(255),
    longueur numeric(15,2),
    largeur numeric(15,2),
    epaisseur numeric(15,2),
    daty date,
    id_machine INTEGER,
    etat INTEGER default 2,
    id_type_matelas INTEGER default 1
);

\COPY temp_table(id,longueur,largeur,epaisseur,prix_unitaire,id_machine,date_mvt_stock) FROM 'C:\Users\ryrab\Desktop\Ryan\Etudes\S5\ArchitectureLogiciel\Matelas\matelas\Matelas\sql\data.csv' WITH (FORMAT CSV, DELIMITER ';', HEADER);

INSERT INTO matelas(matelas,longueur,largeur,epaisseur,prix_unitaire,etat,id_type_matelas)
 SELECT matelas,longueur,largeur,epaisseur,prix_unitaire,etat,id_type_matelas FROM temp_table;

INSERT INTO mvt_stock(entree,date_mvt_stock,prix_revient,id_machine,id_matelas)
 select 1 as entree,date_mvt_stock,matelas.prix_unitaire,id_machine,id_matelas from temp_table join matelas on temp_table.matelas=matelas.matelas;

DROP TABLE temp_table;