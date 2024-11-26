package com.tiarintsoa.foam.service;

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
    private final RepositoryService repositoryService;

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
            JdbcTemplate jdbcTemplate,
            RepositoryService repositoryService
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
        this.repositoryService = repositoryService;
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

        Long maxIdArticle = repositoryService.getMaxIdOrDefault(articleRepository::findMaxId, 0L);
        Long maxIdProduit = repositoryService.getMaxIdOrDefault(produitRepository::findMaxId, 0L);
        Long maxIdBloc = repositoryService.getMaxIdOrDefault(blocRepository::findMaxId, 0L);

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
                    "Bloc " + (maxIdArticle + i)
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

    public void saveCsv(List<String[]> csvData) {
        Long maxIdArticle = repositoryService.getMaxIdOrDefault(articleRepository::findMaxId, 0L);
        Long maxIdProduit = repositoryService.getMaxIdOrDefault(produitRepository::findMaxId, 0L);
        Long maxIdBloc = repositoryService.getMaxIdOrDefault(blocRepository::findMaxId, 0L);

        List<Object[]> articleParams = new ArrayList<>();
        List<Object[]> produitParams = new ArrayList<>();
        List<Object[]> blocParams = new ArrayList<>();

        // Starts with the index 1 to ignore the headers
        for (int i = 1; i < csvData.size(); i++) {
            String[] row = csvData.get(i);

            articleParams.add(new Object[]{
                    maxIdArticle + i,
                    "Bloc " + (maxIdArticle + i)
            });

            produitParams.add(new Object[]{
                    maxIdProduit + i,
                    Double.parseDouble(row[1]),
                    Double.parseDouble(row[2]),
                    Double.parseDouble(row[3]),
                    1,
                    maxIdArticle + i
            });

            blocParams.add(new Object[]{
                    maxIdBloc + i,
                    Double.parseDouble(row[4]),
                    DateUtils.convertToLocalDateTime(row[0]),
                    maxIdProduit + i,
                    Integer.parseInt(row[5].replace("M", "")),
            });

            if (i % BATCH_SIZE == 0) {
                saveBatch(articleParams, produitParams, blocParams);

                // Clear lists for the next batch
                articleParams.clear();
                produitParams.clear();
                blocParams.clear();
            }
        }

        if (!articleParams.isEmpty()) {
            saveBatch(articleParams, produitParams, blocParams);
        }

        restartSequence(
                maxIdArticle + csvData.size(),
                maxIdProduit + csvData.size(),
                maxIdBloc + csvData.size()
        );
    }
}
