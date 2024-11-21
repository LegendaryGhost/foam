select * from etat_stock inner join article on etat_stock.id_article = article.id_article;

select * from bloc inner join produit on bloc.id_produit = produit.id_produit;

select * from forme_usuelle inner join produit on forme_usuelle.id_produit = produit.id_produit;

select * from bloc
    inner join produit on bloc.id_produit = produit.id_produit
    inner join article on article.id_article = produit.id_article
    inner join etat_stock on article.id_article = etat_stock.id_article
where etat_stock.quantite > 0;

select * from etat_stock
    inner join article on etat_stock.id_article = article.id_article
    inner join produit on article.id_article = produit.id_article
    inner join bloc on produit.id_produit = bloc.id_produit
where bloc.id_bloc = 4;

select article.nom_article, etat_stock.quantite, etat_stock.prix_production from etat_stock
    inner join article on etat_stock.id_article = article.id_article;

select *, forme_usuelle.prix_vente/(produit.longueur * produit.largeur * produit.hauteur) as rapport_prix_volume from produit
    inner join forme_usuelle on produit.id_produit = forme_usuelle.id_produit
    order by rapport_prix_volume desc ;

-- practical and theoretical production cost and gap
select machine.id_machine,
       machine.nom_machine,
       coalesce(sum(bloc.prix_production), 0) as prix_production_pratique,
       coalesce(sum(mouvement_stock.prix_production * mouvement_stock.quantite_sortie), 0) as prix_production_théorique,
       coalesce(sum(produit.longueur*produit.largeur*produit.hauteur), 0) as volume_total_produit
from machine
    left join bloc on machine.id_machine = bloc.id_machine
    left join mouvement_stock on bloc.id_bloc = mouvement_stock.id_source
    inner join produit on bloc.id_produit = produit.id_produit
    group by machine.id_machine, machine.nom_machine;

select traiter_blocs();

select article.nom_article, mouvement_stock.date_heure_mouvement, mouvement_stock.quantite_entree, mouvement_stock.quantite_sortie, mouvement_stock.id_source
from mouvement_stock
    inner join article on mouvement_stock.id_article = article.id_article
    inner join formule_bloc on article.id_article = formule_bloc.id_article;
