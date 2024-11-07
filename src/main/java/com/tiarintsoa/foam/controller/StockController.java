package com.tiarintsoa.foam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock")
public class StockController {

    public String showStockDetails(Model model) {
        return "stock";
    }

}
