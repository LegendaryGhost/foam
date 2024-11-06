package com.tiarintsoa.foam.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TransformationConfig {

    @Value("${transformation.margin.percentage}")
    private double marginPercentage;

}

