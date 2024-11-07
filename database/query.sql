select * from etat_stock inner join produit on etat_stock.id_produit = produit.id_produit;

select * from bloc inner join produit on bloc.id_produit = produit.id_produit;

select * from forme_usuelle inner join produit on forme_usuelle.id_produit = produit.id_produit;

select * from bloc
    inner join produit on bloc.id_produit = produit.id_produit
    inner join etat_stock on produit.id_produit = etat_stock.id_produit
where etat_stock.quantite > 0;

select * from etat_stock
    inner join produit on etat_stock.id_produit = produit.id_produit
    inner join bloc on produit.id_produit = bloc.id_produit
where bloc.id_bloc = 4;

select produit.nom_produit, etat_stock.quantite, etat_stock.prix_production from etat_stock
    inner join produit on etat_stock.id_produit = produit.id_produit;

select *, forme_usuelle.prix_vente/(produit.longueur * produit.largeur * produit.hauteur) as rapport_prix_volume from produit
    inner join forme_usuelle on produit.id_produit = forme_usuelle.id_produit
    order by rapport_prix_volume desc ;
