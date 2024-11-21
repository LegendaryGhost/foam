package com.tiarintsoa.foam.controller;

import com.tiarintsoa.foam.service.MachineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public String statistiquesMachines(Model model) {
        model.addAttribute("machines", machineService.getMachineStatistics());
        return "machine";
    }

}
