CREATE OR REPLACE VIEW statistiques_machine AS
SELECT machine.id_machine,
       machine.nom_machine,
       COALESCE(SUM(bloc.prix_production), 0) as prix_production_pratique,
       COALESCE(SUM(mouvement_stock.prix_production * mouvement_stock.quantite_sortie), 0) as prix_production_théorique,
       COALESCE(SUM(produit.longueur*produit.largeur*produit.hauteur), 0) as volume_total_produit
from machine
         left join bloc on machine.id_machine = bloc.id_machine
         left join mouvement_stock on bloc.id_bloc = mouvement_stock.id_source
         inner join produit on bloc.id_produit = produit.id_produit
group by machine.id_machine, machine.nom_machine;