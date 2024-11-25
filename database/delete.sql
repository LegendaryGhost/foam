DELETE FROM etat_stock CASCADE;
DELETE FROM formule_bloc CASCADE;
DELETE FROM bloc CASCADE;
DELETE FROM produit CASCADE;
DELETE FROM article CASCADE;

ALTER SEQUENCE formule_bloc_id_formule_bloc_seq RESTART WITH 1;
ALTER SEQUENCE etat_stock_id_etat_stock_seq RESTART WITH 1;
ALTER SEQUENCE bloc_id_bloc_seq RESTART WITH 1;
ALTER SEQUENCE produit_id_produit_seq RESTART WITH 1;
ALTER SEQUENCE article_id_article_seq RESTART WITH 1;
