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
                                 id_article)
    VALUES (NEW.quantite, -- La quantité initiale devient la quantité d'entrée
            0, -- La quantité de sortie est initialisée à 0
            NEW.prix_production,
            NEW.date_heure_insertion,
            NEW.id_article);

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
                                     id_article)
        VALUES (0, -- Aucune entrée
                OLD.quantite - NEW.quantite, -- Quantité de sortie
                OLD.prix_production,
                NOW(), -- Date et heure actuelles
                NEW.id_article -- ID du produit
               );

        -- Cas où la quantité a augmenté
    ELSIF NEW.quantite > OLD.quantite THEN
        INSERT INTO mouvement_stock (quantite_entree,
                                     quantite_sortie,
                                     prix_production,
                                     date_heure_mouvement,
                                     id_article)
        VALUES (NEW.quantite - OLD.quantite, -- Quantité d'entrée
                0, -- Aucune sortie
                OLD.prix_production,
                NOW(), -- Date et heure actuelles
                NEW.id_article -- ID du produit
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
