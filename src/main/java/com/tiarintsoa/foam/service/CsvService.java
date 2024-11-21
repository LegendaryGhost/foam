package com.tiarintsoa.foam.service;

import com.tiarintsoa.foam.dto.csv.BlocCsvDTO;

import java.util.List;

public class CsvService {

    private final BlocService blocService;

    public CsvService(BlocService blocService) {
        this.blocService = blocService;
    }

    public void saveCsvData(List<String[]> csvData) {
        List<BlocCsvDTO> csvDTOS = csvData.stream()
                .map(data -> new BlocCsvDTO(
                        data[0],
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3]),
                        Double.parseDouble(data[4]),
                        data[5]
                ))
                .toList();
        blocService.saveAllCsvDTO(csvDTOS);
    }

}
