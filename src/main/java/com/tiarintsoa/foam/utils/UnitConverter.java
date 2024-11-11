package com.tiarintsoa.foam.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnitConverter {

    public static double convertStringToDouble(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        // Pattern to match numbers with optional units (m or cm)
        Pattern pattern = Pattern.compile("^(\\d+)(m|cm)?$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            // Extract the numeric part
            int value = Integer.parseInt(matcher.group(1));

            // Check for unit and convert accordingly
            String unit = matcher.group(2);
            if (unit == null) {
                return value; // No unit, return as integer
            } else if (unit.equals("m")) {
                return value; // Unit is meters, return integer
            } else if (unit.equals("cm")) {
                return value / 100.0; // Unit is centimeters, convert to meters
            }
        }

        // If input doesn't match the pattern, throw an exception
        throw new IllegalArgumentException("Invalid input format: " + input);
    }
}

