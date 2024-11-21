-- Désactiver les contraintes de clés étrangères
ALTER TABLE formule_bloc DROP CONSTRAINT formule_bloc_id_article_fkey;
ALTER TABLE produit DROP CONSTRAINT produit_id_type_produit_fkey;
ALTER TABLE produit DROP CONSTRAINT produit_id_article_fkey;
ALTER TABLE bloc DROP CONSTRAINT bloc_id_machine_fkey;
ALTER TABLE bloc DROP CONSTRAINT bloc_id_originel_fkey;
ALTER TABLE bloc DROP CONSTRAINT bloc_id_origine_fkey;
ALTER TABLE bloc DROP CONSTRAINT bloc_id_produit_fkey;
ALTER TABLE forme_usuelle DROP CONSTRAINT forme_usuelle_id_produit_fkey;
ALTER TABLE etat_stock DROP CONSTRAINT etat_stock_id_originel_fkey;
ALTER TABLE etat_stock DROP CONSTRAINT etat_stock_id_origine_fkey;
ALTER TABLE etat_stock DROP CONSTRAINT etat_stock_id_article_fkey;
ALTER TABLE mouvement_stock DROP CONSTRAINT mouvement_stock_id_source_fkey;
ALTER TABLE mouvement_stock DROP CONSTRAINT mouvement_stock_id_article_fkey;

-- Supprimer toutes les données des tables
DELETE FROM mouvement_stock;
DELETE FROM etat_stock;
DELETE FROM forme_usuelle;
DELETE FROM bloc;
DELETE FROM produit;
DELETE FROM formule_bloc;
DELETE FROM article;
DELETE FROM machine;
DELETE FROM type_produit;

-- Remettre les séquences à zéro
DO $$
    DECLARE
        seq_name TEXT;
    BEGIN
        FOR seq_name IN
            SELECT pg_get_serial_sequence('public.' || tablename, column_name)
            FROM information_schema.columns
            WHERE table_schema = 'public' AND column_default LIKE 'nextval%'
            LOOP
                EXECUTE 'ALTER SEQUENCE ' || seq_name || ' RESTART WITH 1;';
            END LOOP;
    END $$;

-- Réactiver les contraintes de clés étrangères
ALTER TABLE formule_bloc ADD CONSTRAINT formule_bloc_id_article_fkey FOREIGN KEY (id_article) REFERENCES article(id_article);
ALTER TABLE produit ADD CONSTRAINT produit_id_type_produit_fkey FOREIGN KEY (id_type_produit) REFERENCES type_produit(id_type_produit);
ALTER TABLE produit ADD CONSTRAINT produit_id_article_fkey FOREIGN KEY (id_article) REFERENCES article(id_article);
ALTER TABLE bloc ADD CONSTRAINT bloc_id_machine_fkey FOREIGN KEY (id_machine) REFERENCES machine(id_machine);
ALTER TABLE bloc ADD CONSTRAINT bloc_id_originel_fkey FOREIGN KEY (id_originel) REFERENCES bloc(id_bloc);
ALTER TABLE bloc ADD CONSTRAINT bloc_id_origine_fkey FOREIGN KEY (id_origine) REFERENCES bloc(id_bloc);
ALTER TABLE bloc ADD CONSTRAINT bloc_id_produit_fkey FOREIGN KEY (id_produit) REFERENCES produit(id_produit);
ALTER TABLE forme_usuelle ADD CONSTRAINT forme_usuelle_id_produit_fkey FOREIGN KEY (id_produit) REFERENCES produit(id_produit);
ALTER TABLE etat_stock ADD CONSTRAINT etat_stock_id_originel_fkey FOREIGN KEY (id_originel) REFERENCES bloc(id_bloc);
ALTER TABLE etat_stock ADD CONSTRAINT etat_stock_id_origine_fkey FOREIGN KEY (id_origine) REFERENCES bloc(id_bloc);
ALTER TABLE etat_stock ADD CONSTRAINT etat_stock_id_article_fkey FOREIGN KEY (id_article) REFERENCES article(id_article);
ALTER TABLE mouvement_stock ADD CONSTRAINT mouvement_stock_id_source_fkey FOREIGN KEY (id_source) REFERENCES bloc(id_bloc);
ALTER TABLE mouvement_stock ADD CONSTRAINT mouvement_stock_id_article_fkey FOREIGN KEY (id_article) REFERENCES article(id_article);
