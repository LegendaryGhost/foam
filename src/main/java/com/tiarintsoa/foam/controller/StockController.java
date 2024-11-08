package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.repository.MouvementStockRepository;
import com.tiarintsoa.foam.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;
    @Autowired
    private MouvementStockRepository mouvementStockRepository;

    @GetMapping
    public String showStockDetails(Model model) {
        model.addAttribute("mouvementsStock", mouvementStockRepository.findAll());
        model.addAttribute("stockProduits", stockService.getStockProduits());
        model.addAttribute("stockDetails", stockService.getStockDetails());
        return "stock";
    }

}
