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
