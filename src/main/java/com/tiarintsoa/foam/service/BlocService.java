package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.csv.BlocCsvDTO;
import com.tiarintsoa.foam.entity.*;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.repository.*;
import com.tiarintsoa.foam.utils.DateConverter;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BlocService {

    private final TypeProduitRepository typeProduitRepository;
    private final ProduitRepository produitRepository;
    private final BlocRepository blocRepository;
    private final EtatStockRepository etatStockRepository;
    private final ArticleRepository articleRepository;
    private final MachineRepository machineRepository;
    private final FormuleBlocRepository formuleBlocRepository;

    public BlocService(TypeProduitRepository typeProduitRepository, ProduitRepository produitRepository, BlocRepository blocRepository, EtatStockRepository etatStockRepository, ArticleRepository articleRepository, MachineRepository machineRepository, FormuleBlocRepository formuleBlocRepository) {
        this.typeProduitRepository = typeProduitRepository;
        this.produitRepository = produitRepository;
        this.blocRepository = blocRepository;
        this.etatStockRepository = etatStockRepository;
        this.articleRepository = articleRepository;
        this.machineRepository = machineRepository;
        this.formuleBlocRepository = formuleBlocRepository;
    }

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
            etatStock.setQuantite(1.0);
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
        etatStock.setQuantite(1.0);
        etatStock.setOrigine(origne);
        etatStockRepository.save(etatStock);
    }

    public void updateBlocsTheoreticalCostPrice() {
        List<Bloc> blocs = blocRepository.findAllOrderByDate();
        List<FormuleBloc> formules=formuleBlocRepository.findAll();
        HashMap<Long,List<EtatStock>> etatStockMap= new HashMap<>();
        for (FormuleBloc formuleBloc : formules) {
            List<EtatStock> etatStocks = etatStockRepository.findAllByArticleInFormule(formuleBloc.getArticle().getId());
            etatStockMap.put(formuleBloc.getArticle().getId(), etatStocks);
        }

        for (Bloc bloc : blocs) {
            double prixProductionTheorique = 0.0;
            for (FormuleBloc formule : formules) {
                Double quantiteNecessaire = bloc.getProduit().getVolume() * formule.getQuantiteNecessaire();

                for(EtatStock etatStock : etatStockMap.get(formule.getArticle().getId())) {
                    if(quantiteNecessaire == 0) break;

                    if(etatStock.getQuantite() < quantiteNecessaire) {
                        prixProductionTheorique += etatStock.getPrixProduction() * etatStock.getQuantite();
                        quantiteNecessaire -= etatStock.getQuantite();
                        etatStock.setQuantite(0.0);
                    } else {
                        etatStock.setQuantite(etatStock.getQuantite() - quantiteNecessaire);
                        prixProductionTheorique += etatStock.getPrixProduction() * quantiteNecessaire;
                        quantiteNecessaire = 0.0;
                        break;
                    }
                }

                if (quantiteNecessaire > 0) {
                    throw new RuntimeException("Insufficient article quantity");
                }
            }
            bloc.setPrixProductionTheorique(prixProductionTheorique);
        }
        blocRepository.saveAll(blocs);
    }

    public void generateData(int blocCount) {
        Double averageProductionCost = blocRepository.findAverageProductionCost();
        System.out.println(averageProductionCost);
    }
}
