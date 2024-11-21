package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.csv.BlocCsvDTO;
import com.tiarintsoa.foam.entity.*;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.repository.*;
import com.tiarintsoa.foam.utils.DateConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MachineRepository machineRepository;

    public void saveAllCsvDTO(List<BlocCsvDTO> blocCsvDTOS) {
        TypeProduit typeProduit = typeProduitRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Type produit introuvable") );

        List<Machine> machines = machineRepository.findAll();

        for (int i = 0; i < blocCsvDTOS.size(); i++) {
            BlocCsvDTO blocCsvDTO = blocCsvDTOS.get(i);

            Article article = new Article();
            article.setNomArticle("B" + i);
            articleRepository.save(article);

            Produit produit = new Produit();
            produit.setLongueur(blocCsvDTO.getLongueur());
            produit.setLargeur(blocCsvDTO.getLargeur());
            produit.setHauteur(blocCsvDTO.getHauteur());
            produit.setTypeProduit(typeProduit);
            produit.setArticle(article);
            produitRepository.save(produit);

            Bloc bloc = new Bloc();
            bloc.setPrixProduction(blocCsvDTO.getCoutRevient());
            bloc.setDateHeureInsertion(DateConverter.convertToLocalDateTime(blocCsvDTO.getDate()));
            bloc.setProduit(produit);
            bloc.setMachine(findMachineByName(
                    machines,
                    blocCsvDTO.getNomMachine())
            );
            blocRepository.save(bloc);

            EtatStock etatStock = new EtatStock();
            etatStock.setArticle(article);
            etatStock.setPrixProduction(blocCsvDTO.getCoutRevient());
            etatStock.setQuantite(1);
            etatStock.setDateHeureInsertion(DateConverter.convertToLocalDateTime(blocCsvDTO.getDate()));
            etatStockRepository.save(etatStock);
        }
    }

    private Machine findMachineByName(List<Machine> machines, String name) {
        for (Machine machine : machines) {
            if (machine.getNomMachine().equals(name)) {
                return machine;
            }
        }
        return null;
    }

    @Transactional
    public void saveBloc(BlocForm blocForm) {
        saveBloc(blocForm, null, null);
    }

    @Transactional
    public void saveBloc(BlocForm blocForm, Bloc origne, Bloc sourceOriginel) {
        if (blocForm.getId() == null) {
            createBloc(blocForm, origne, sourceOriginel);
        } else {
            updateBloc(blocForm);
        }
    }

    @Transactional
    protected void updateBloc(BlocForm blocForm) {
        Bloc bloc = blocRepository.findById(blocForm.getId())
                .orElseThrow(() -> new RuntimeException("Bloc introuvable"));

        Article article = bloc.getProduit().getArticle();
        article.setNomArticle(blocForm.getNom());
        articleRepository.save(article);

        bloc.setPrixProduction(blocForm.getCoutProduction());
        blocRepository.save(bloc);
    }

    @Transactional
    protected void createBloc(BlocForm blocForm, Bloc origne, Bloc sourceOriginel) {
        TypeProduit typeProduit = typeProduitRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Type produit introuvable") );

        Machine machine = machineRepository.findById(blocForm.getIdMachine())
                .orElseThrow(() -> new RuntimeException("Machine introuvable") );

        Article article = new Article();
        article.setNomArticle(blocForm.getNom());
        articleRepository.save(article);

        Produit produit = new Produit();
        produit.setTypeProduit(typeProduit);
        produit.setHauteur(blocForm.getHauteur());
        produit.setLongueur(blocForm.getLongueur());
        produit.setLargeur(blocForm.getLargeur());
        produit.setArticle(article);
        produitRepository.save(produit);

        Bloc bloc = new Bloc();
        bloc.setPrixProduction(blocForm.getCoutProduction());
        bloc.setProduit(produit);
        bloc.setOrigine(origne);
        bloc.setOriginel(sourceOriginel);
        bloc.setMachine(machine);
        blocRepository.save(bloc);

        EtatStock etatStock = new EtatStock();
        etatStock.setArticle(article);
        etatStock.setPrixProduction(blocForm.getCoutProduction());
        etatStock.setQuantite(1);
        etatStock.setOrigine(origne);
        etatStockRepository.save(etatStock);
    }
}
