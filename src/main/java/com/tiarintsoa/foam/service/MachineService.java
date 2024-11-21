package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.MachineDTO;
import com.tiarintsoa.foam.repository.MachineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MachineService {
    private final MachineRepository machineRepository;

    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public List<MachineDTO> getMachineStatistics() {
        List<Object[]> results = machineRepository.findMachineStatistics();
        return convertToMachineDTO(results);
    }

    public List<MachineDTO> getMachineStatisticsByYear(Integer year) {
        List<Object[]> results = machineRepository.findMachineStatisticsByYear(year);
        return convertToMachineDTO(results);
    }

    private List<MachineDTO> convertToMachineDTO(List<Object[]> results) {
        List<MachineDTO> machineStatistics = new ArrayList<>();

        for (Object[] row : results) {
            MachineDTO dto = new MachineDTO(
                    ((Number) row[0]).longValue(),      // idMachine
                    (String) row[1],                   // nomMachine
                    ((Number) row[2]).doubleValue(),   // prixProductionPratique
                    ((Number) row[3]).doubleValue(),   // prixProductionTheorique
                    ((Number) row[4]).doubleValue()    // volumeTotalProduit
            );
            machineStatistics.add(dto);
        }
        return machineStatistics;
    }

    public List<Integer> getMachineStatsYears() {
        List<Integer> years = new ArrayList<>();
        years.add(2024);
        years.add(2023);
        years.add(2022);
        return years;
    }
}
