package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.entity.Bloc;
import com.tiarintsoa.foam.entity.EtatStock;
import com.tiarintsoa.foam.entity.Produit;
import com.tiarintsoa.foam.entity.TypeProduit;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.repository.BlocRepository;
import com.tiarintsoa.foam.repository.EtatStockRepository;
import com.tiarintsoa.foam.repository.ProduitRepository;
import com.tiarintsoa.foam.repository.TypeProduitRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlocService {

    @Autowired
    private TypeProduitRepository typeProduitRepository;
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private BlocRepository blocRepository;
    @Autowired
    private EtatStockRepository etatStockRepository;

    @Transactional
    public void saveBloc(BlocForm blocForm) {
        TypeProduit typeProduit = typeProduitRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Type produit introuvable") );

        Produit produit = new Produit();
        produit.setTypeProduit(typeProduit);
        produit.setNomProduit(blocForm.getNom());
        produit.setHauteur(blocForm.getHauteur());
        produit.setLongueur(blocForm.getLongueur());
        produit.setLargeur(blocForm.getLargeur());
        produitRepository.save(produit);

        Bloc bloc = new Bloc();
        bloc.setPrixProduction(blocForm.getCoutProduction());
        bloc.setProduit(produit);
        blocRepository.save(bloc);

        EtatStock etatStock = new EtatStock();
        etatStock.setProduit(produit);
        etatStock.setPrixProduction(blocForm.getCoutProduction());
        etatStock.setQuantite(1);
        etatStockRepository.save(etatStock);
    }
}
