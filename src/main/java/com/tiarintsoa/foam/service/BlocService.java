package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.csv.BlocCsvDTO;
import com.tiarintsoa.foam.entity.*;
import com.tiarintsoa.foam.from.BlocForm;
import com.tiarintsoa.foam.from.GenerationForm;
import com.tiarintsoa.foam.repository.*;
import com.tiarintsoa.foam.utils.DateUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class BlocService {

    private final TypeProduitRepository typeProduitRepository;
    private final ProduitRepository produitRepository;
    private final BlocRepository blocRepository;
    private final EtatStockRepository etatStockRepository;
    private final ArticleRepository articleRepository;
    private final MachineRepository machineRepository;
    private final FormuleBlocRepository formuleBlocRepository;
    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    private final static int BATCH_SIZE = 1000;

    public BlocService(
            TypeProduitRepository typeProduitRepository,
            ProduitRepository produitRepository,
            BlocRepository blocRepository,
            EtatStockRepository etatStockRepository,
            ArticleRepository articleRepository,
            MachineRepository machineRepository,
            FormuleBlocRepository formuleBlocRepository,
            EntityManager entityManager,
            JdbcTemplate jdbcTemplate
    ) {
        this.typeProduitRepository = typeProduitRepository;
        this.produitRepository = produitRepository;
        this.blocRepository = blocRepository;
        this.etatStockRepository = etatStockRepository;
        this.articleRepository = articleRepository;
        this.machineRepository = machineRepository;
        this.formuleBlocRepository = formuleBlocRepository;
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO: remove after new implementation is changed
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
            bloc.setDateHeureInsertion(DateUtils.convertToLocalDateTime(blocCsvDTO.getDate()));
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
            etatStock.setDateHeureInsertion(DateUtils.convertToLocalDateTime(blocCsvDTO.getDate()));
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

    @Transactional
    public void updateBlocsTheoreticalCostPrice() {
        // Fetch data
        List<Bloc> blocs = blocRepository.findAllOrderByDate();
        List<FormuleBloc> formules = formuleBlocRepository.findAll();

        // Create a map for EtatStock grouped by article ID
        HashMap<Long, List<EtatStock>> etatStockMap = new HashMap<>();
        for (FormuleBloc formuleBloc : formules) {
            List<EtatStock> etatStocks = etatStockRepository.findAllByArticleInFormule(formuleBloc.getArticle().getId());
            etatStockMap.put(formuleBloc.getArticle().getId(), etatStocks);
        }

        // Step 2: Prepare updates
        String updateBlocQuery = "UPDATE bloc SET prix_production_theorique = ? WHERE id_bloc = ?";
        List<Object[]> blocUpdates = new ArrayList<>();

        for (Bloc bloc : blocs) {
            double prixProductionTheorique = 0.0;

            for (FormuleBloc formule : formules) {
                double quantiteNecessaire = bloc.getProduit().getVolume() * formule.getQuantiteNecessaire();

                List<EtatStock> etatStocks = etatStockMap.get(formule.getArticle().getId());
                for (EtatStock etatStock : etatStocks) {
                    if (quantiteNecessaire == 0) break;

                    if (etatStock.getQuantite() < quantiteNecessaire) {
                        prixProductionTheorique += etatStock.getPrixProduction() * etatStock.getQuantite();
                        quantiteNecessaire -= etatStock.getQuantite();
                        etatStock.setQuantite(0.0);
                    } else {
                        prixProductionTheorique += etatStock.getPrixProduction() * quantiteNecessaire;
                        etatStock.setQuantite(etatStock.getQuantite() - quantiteNecessaire);
                        quantiteNecessaire = 0.0;
                        break;
                    }
                }

                if (quantiteNecessaire > 0) {
                    throw new RuntimeException("Insufficient article quantity");
                }
            }

            // Add the prepared statement parameters to the list
            blocUpdates.add(new Object[]{prixProductionTheorique, bloc.getId()});

            // Step 3: Execute in batches
            if (blocUpdates.size() >= BATCH_SIZE) {
                jdbcTemplate.batchUpdate(updateBlocQuery, blocUpdates);
                blocUpdates.clear(); // Clear the list after batch execution
            }
        }

        // Step 4: Execute remaining updates
        if (!blocUpdates.isEmpty()) {
            jdbcTemplate.batchUpdate(updateBlocQuery, blocUpdates);
        }
    }

    @Transactional
    public void generateData(GenerationForm generationForm) {
        List<Machine> machines = machineRepository.findAll();
        Double averageVolumicProductionCost = blocRepository.findAverageVolumicProductionCost();
        averageVolumicProductionCost = averageVolumicProductionCost == null ? 6000 : averageVolumicProductionCost;

        Long maxIdArticle = articleRepository.findMaxId();
        maxIdArticle = maxIdArticle == null ? 0 : maxIdArticle;
        Long maxIdProduit = produitRepository.findMaxId();
        maxIdProduit = maxIdProduit == null ? 0 : maxIdProduit;
        Long maxIdBloc = blocRepository.findMaxId();
        maxIdBloc = maxIdBloc == null ? 0 : maxIdBloc;

        TypeProduit typeProduitBloc = entityManager.getReference(TypeProduit.class, 1L);
        LocalDate startDate = LocalDate.of(2022, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 31);

        List<Object[]> articleParams = new ArrayList<>();
        List<Object[]> produitParams = new ArrayList<>();
        List<Object[]> blocParams = new ArrayList<>();

        for (int i = 1; i <= generationForm.getBlocCount(); i++) {
            // Collect article parameters for batch
            articleParams.add(new Object[]{
                    maxIdArticle + i,
                    "Bloc " + (maxIdBloc + i)
            });

            int longueur = ThreadLocalRandom.current().nextInt(generationForm.getMinLongueur(), generationForm.getMaxLongueur()); // 20 - 25 (26)
            int largeur = ThreadLocalRandom.current().nextInt(generationForm.getMinLargeur(), generationForm.getMaxLargeur()); // 5 - 7 (8)
            int hauteur = ThreadLocalRandom.current().nextInt(generationForm.getMinHauteur(), generationForm.getMaxHauteur()); // 10 - 15 (16)
            produitParams.add(new Object[]{
                    maxIdProduit + i,
                    longueur,
                    largeur,
                    hauteur,
                    typeProduitBloc.getIdTypeProduit(),
                    maxIdArticle + i
            });

            Machine randomMachine = machines.get(ThreadLocalRandom.current().nextInt(machines.size()));
            double costVariation = ThreadLocalRandom.current().nextDouble(-10, 10);
            double productionCost = averageVolumicProductionCost * longueur * largeur * hauteur;
            productionCost = productionCost + productionCost * costVariation / 100;
            blocParams.add(new Object[]{
                    maxIdBloc + i,
                    productionCost,
                    DateUtils.generateRandomDate(startDate, endDate),
                    maxIdProduit + i,
                    randomMachine.getId()
            });

            // Execute batch after every 500 records
            if (i % BATCH_SIZE == 0) {
                saveBatch(articleParams, produitParams, blocParams);

                // Clear lists for the next batch
                articleParams.clear();
                produitParams.clear();
                blocParams.clear();
            }
        }

        // Final batch insert if necessary
        if (!articleParams.isEmpty()) {
            saveBatch(articleParams, produitParams, blocParams);
        }

        restartSequence(
                maxIdArticle + generationForm.getBlocCount() + 1,
                maxIdProduit + generationForm.getBlocCount() + 1,
                maxIdBloc + generationForm.getBlocCount() + 1
        );
    }

    protected void saveBatch(List<Object[]> articleParams, List<Object[]> produitParams, List<Object[]> blocParams) {
        String articleInsertQuery = "INSERT INTO article (id_article, nom_article) VALUES (?, ?)";
        String produitInsertQuery = "INSERT INTO produit (id_produit, longueur, largeur, hauteur, id_type_produit, id_article) VALUES (?, ?, ?, ?, ?, ?)";
        String blocInsertQuery = "INSERT INTO bloc (id_bloc, prix_production, date_heure_insertion, id_produit, id_machine) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(articleInsertQuery, articleParams);
        jdbcTemplate.batchUpdate(produitInsertQuery, produitParams);
        jdbcTemplate.batchUpdate(blocInsertQuery, blocParams);
    }

    protected void restartSequence(long idArticle, long idProduit, long idBloc) {
        jdbcTemplate.execute("ALTER SEQUENCE article_id_article_seq START WITH " + idArticle);
        jdbcTemplate.execute("ALTER SEQUENCE produit_id_produit_seq START WITH " + idProduit);
        jdbcTemplate.execute("ALTER SEQUENCE bloc_id_bloc_seq START WITH " + idBloc);
    }

    public void saveCsv(List<Object> csvData) {
        List<Object[]> articleParams = new ArrayList<>();
        List<Object[]> produitParams = new ArrayList<>();
        List<Object[]> blocParams = new ArrayList<>();

        // TODO: continue import
    }
}
