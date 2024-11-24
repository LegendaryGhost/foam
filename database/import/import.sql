CREATE TEMP TABLE temp_table (
    id int,
    nom varchar(255),
    longueur numeric(15, 2),
    largeur numeric(15, 2),
    epaisseur numeric(15, 2),
    cout_production numeric(15, 2),
    cout_tehorique numeric(15, 2),
    volume numeric(15, 2),
    machine_id int,
    block_mere int,
    date_production date
);

\COPY temp_table(id,nom,longueur,largeur,epaisseur,cout_production,cout_tehorique,volume,machine_id,block_mere,date_production) FROM 'D:\ITU\S5\architecture-logiciel\foam\database\import\blocs.csv' WITH (FORMAT CSV, DELIMITER ',', HEADER);

UPDATE bloc
SET prix_production = temp_table.cout_production,
    prix_production_theorique = temp_table.cout_tehorique
FROM temp_table
WHERE bloc.id_bloc = temp_table.id;


DROP TABLE temp_table;