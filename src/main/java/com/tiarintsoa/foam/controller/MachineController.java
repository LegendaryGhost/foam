package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.service.MachineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Controller
@RequestMapping("/machines")
public class MachineController {

    private final MachineService machineService;

    @GetMapping
    public String statistiquesMachines(@RequestParam(value = "year", required = false) Integer year, Model model) {
        model.addAttribute("years", machineService.getMachineStatsYears());
        model.addAttribute("selectedYear", year);
        if (year == null) {
            model.addAttribute("machines", machineService.getMachineStatistics());
        } else {
            model.addAttribute("machines", machineService.getMachineStatisticsByYear(year));
        }
        return "machine";
    }

}
