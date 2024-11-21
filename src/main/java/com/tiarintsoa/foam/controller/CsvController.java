package com.tiarintsoa.foam.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/csv")
public class CsvController {

    @GetMapping
    public String uploadPage() {
        return "csv";
    }

    @PostMapping
    public String uploadCsv(MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Veuillez sélectionner un fichier.");
            return "csv";
        }

        List<String[]> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // Ajustez le séparateur si nécessaire
                data.add(values);
                System.out.println(line);
            }
        } catch (Exception e) {
            model.addAttribute("message", "Erreur lors de la lecture du fichier : " + e.getMessage());
            return "csv";
        }

        // Traitez les données ici (sauvegarde en base de données, validation, etc.)
        model.addAttribute("message", "Fichier importé avec succès !");
        model.addAttribute("data", data); // Facultatif : Pour afficher les données

        return "csv";
    }
}