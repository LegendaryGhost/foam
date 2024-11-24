-- Fonction pour le trigger qui gère l'insertion dans mouvement_stock
CREATE OR REPLACE FUNCTION insert_mouvement_stock()
    RETURNS TRIGGER AS
$$
BEGIN
    -- Insérer un nouveau mouvement de stock
    INSERT INTO mouvement_stock (quantite_entree,
                                 quantite_sortie,
                                 prix_production,
                                 date_heure_mouvement,
                                 id_article,
                                 id_source)
    VALUES (NEW.quantite, -- La quantité initiale devient la quantité d'entrée
            0, -- La quantité de sortie est initialisée à 0
            NEW.prix_production,
            NEW.date_heure_insertion,
            NEW.id_article,
            NEW.id_source_temp);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Créer le trigger qui s'exécute après l'insertion dans etat_stock
CREATE TRIGGER after_insert_etat_stock
    AFTER INSERT
    ON etat_stock
    FOR EACH ROW
EXECUTE FUNCTION insert_mouvement_stock();



-- Fonction de déclenchement modifiée
CREATE OR REPLACE FUNCTION insert_mouvement_stock_on_update()
    RETURNS TRIGGER AS
$$
BEGIN
    -- Cas où la quantité a diminué
    IF NEW.quantite < OLD.quantite THEN
        INSERT INTO mouvement_stock (quantite_entree,
                                     quantite_sortie,
                                     prix_production,
                                     date_heure_mouvement,
                                     id_article,
                                     id_source)
        VALUES (0, -- Aucune entrée
                OLD.quantite - NEW.quantite, -- Quantité de sortie
                OLD.prix_production,
                NOW(), -- Date et heure actuelles
                NEW.id_article, -- ID du produit
                NEW.id_source_temp
               );

        -- Cas où la quantité a augmenté
    ELSIF NEW.quantite > OLD.quantite THEN
        INSERT INTO mouvement_stock (quantite_entree,
                                     quantite_sortie,
                                     prix_production,
                                     date_heure_mouvement,
                                     id_article,
                                     id_source)
        VALUES (NEW.quantite - OLD.quantite, -- Quantité d'entrée
                0, -- Aucune sortie
                OLD.prix_production,
                NOW(), -- Date et heure actuelles
                NEW.id_article, -- ID du produit
                NEW.id_source_temp
               );
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger pour la table etat_stock
CREATE TRIGGER trigger_update_etat_stock
    AFTER UPDATE
    ON etat_stock
    FOR EACH ROW
    WHEN (NEW.quantite != OLD.quantite) -- Condition pour tout changement de quantité
EXECUTE FUNCTION insert_mouvement_stock_on_update();



CREATE OR REPLACE FUNCTION update_prix_production_dependants()
    RETURNS TRIGGER AS
$$
DECLARE
    ratio NUMERIC;
BEGIN
    -- Calculer le ratio de modification
    ratio := NEW.prix_production / OLD.prix_production;

    -- Mettre à jour les blocs dérivés ayant ce bloc comme origine
    UPDATE bloc
    SET prix_production = prix_production * ratio
    WHERE id_origine = OLD.id_bloc;

    -- Mettre à jour les états de stock liés aux blocs dérivés
    UPDATE etat_stock
    SET prix_production = prix_production * ratio
    WHERE id_origine = OLD.id_bloc;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_prix_production
    AFTER UPDATE OF prix_production
    ON bloc
    FOR EACH ROW
    WHEN (OLD.prix_production IS DISTINCT FROM NEW.prix_production)
EXECUTE FUNCTION update_prix_production_dependants();



CREATE OR REPLACE FUNCTION traiter_blocs()
    RETURNS void AS
$$
DECLARE
    batch_size INTEGER := 1000; -- Taille du lot
    remaining_blocs INTEGER; -- Nombre de blocs restants
BEGIN
    LOOP
        -- Vérifier le nombre de blocs restants
        SELECT COUNT(*) INTO remaining_blocs
        FROM bloc
        WHERE traite IS FALSE;

        -- Sortir si tous les blocs ont été traités
        IF remaining_blocs = 0 THEN
            EXIT;
        END IF;

        -- Appeler la logique pour traiter un lot de blocs
        PERFORM traiter_lot_blocs(batch_size);
    END LOOP;

    RAISE NOTICE 'Tous les blocs ont été traités.';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION traiter_lot_blocs(batch_size INTEGER)
    RETURNS void AS
$$
DECLARE
    bloc_record RECORD;
    article_record RECORD;
    besoin_total NUMERIC;
    besoin_par_bloc NUMERIC;
    stock_record RECORD;
BEGIN
    -- Parcourir les blocs non traités dans un lot
    FOR bloc_record IN
        SELECT * FROM bloc
                          INNER JOIN produit ON bloc.id_produit = produit.id_produit
        WHERE traite IS FALSE
        ORDER BY date_heure_insertion
        LIMIT batch_size
        LOOP
            -- Calcul du volume du bloc
            besoin_total := bloc_record.longueur * bloc_record.largeur * bloc_record.hauteur;

            -- Parcourir les articles nécessaires pour ce type de bloc
            FOR article_record IN
                SELECT fb.id_article, fb.quantite_necessaire
                FROM formule_bloc fb
                         INNER JOIN article a ON fb.id_article = a.id_article
                LOOP
                    -- Calcul de la quantité totale nécessaire
                    besoin_par_bloc := besoin_total * article_record.quantite_necessaire;

                    -- Parcourir les stocks les plus anciens
                    FOR stock_record IN
                        SELECT * FROM etat_stock
                        WHERE id_article = article_record.id_article
                          AND quantite > 0
                        ORDER BY date_heure_insertion
                        LOOP
                            IF stock_record.quantite >= besoin_par_bloc THEN
                                -- Déduire la quantité utilisée
                                UPDATE etat_stock
                                SET quantite = quantite - besoin_par_bloc,
                                    id_source_temp = bloc_record.id_bloc
                                WHERE id_etat_stock = stock_record.id_etat_stock;
                                besoin_par_bloc := 0;
                                EXIT;
                            ELSE
                                -- Consommer tout le stock disponible
                                besoin_par_bloc := besoin_par_bloc - stock_record.quantite;
                                UPDATE etat_stock
                                SET quantite = 0
                                WHERE id_etat_stock = stock_record.id_etat_stock;
                            END IF;
                        END LOOP;

                    -- Si le besoin n'est pas couvert
                    IF besoin_par_bloc > 0 THEN
                        RAISE WARNING 'Stock insuffisant pour l''article %', article_record.id_article;
                        RETURN;
                    END IF;
                END LOOP;

            -- Marquer le bloc comme traité
            UPDATE bloc
            SET traite = TRUE
            WHERE id_bloc = bloc_record.id_bloc;
        END LOOP;
END;
$$ LANGUAGE plpgsql;
