SELECT machine.id_machine,
       machine.nom_machine,
       COALESCE(SUM(bloc.prix_production), 0) AS prix_production_pratique,
       COALESCE(SUM(mouvement_stock.prix_production * mouvement_stock.quantite_sortie), 0) AS prix_production_theorique,
       COALESCE(SUM(produit.longueur*produit.largeur*produit.hauteur), 0) AS volume_total_produit
FROM machine
         LEFT JOIN bloc ON machine.id_machine = bloc.id_machine
         LEFT JOIN mouvement_stock ON bloc.id_bloc = mouvement_stock.id_source
         INNER JOIN produit ON bloc.id_produit = produit.id_produit
GROUP BY machine.id_machine, machine.nom_machine
ORDER BY (COALESCE(SUM(bloc.prix_production), 0) - COALESCE(SUM(mouvement_stock.prix_production * mouvement_stock.quantite_sortie), 0)) / COALESCE(SUM(produit.longueur*produit.largeur*produit.hauteur), 0);



SELECT machine.id_machine,
       machine.nom_machine,
       COALESCE(SUM(bloc.prix_production), 0) AS prix_production_pratique,
       COALESCE(SUM(mouvement_stock.prix_production * mouvement_stock.quantite_sortie), 0) AS prix_production_theorique,
       COALESCE(SUM(produit.longueur*produit.largeur*produit.hauteur), 0) AS volume_total_produit
FROM machine
         LEFT JOIN bloc ON machine.id_machine = bloc.id_machine
         LEFT JOIN mouvement_stock ON bloc.id_bloc = mouvement_stock.id_source
         INNER JOIN produit ON bloc.id_produit = produit.id_produit
WHERE EXTRACT(YEAR FROM bloc.date_heure_insertion) = 2024
GROUP BY machine.id_machine, machine.nom_machine
ORDER BY (COALESCE(SUM(bloc.prix_production), 0) - COALESCE(SUM(mouvement_stock.prix_production * mouvement_stock.quantite_sortie), 0)) / COALESCE(SUM(produit.longueur*produit.largeur*produit.hauteur), 0);