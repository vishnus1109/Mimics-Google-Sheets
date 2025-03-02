package com.googlesheets.service;

import java.util.List;

import com.googlesheets.entity.Cell;
import com.googlesheets.repository.CellRepository;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FormulaService {
    private final CellRepository cellRepo;

    public FormulaService(CellRepository cellRepo) {
        this.cellRepo = cellRepo;
    }

    // Evaluates a formula and returns computed value
    public String evaluateFormula(Cell cell, List<Cell> allCells) {
        String formula = cell.getValue();
        if (formula == null || !formula.startsWith("=")) {
            return cell.getValue(); // Return raw value if not a formula
        }

        try {
            // Extract function and cell range (e.g., SUM(A1:A5))
            Pattern pattern = Pattern.compile("=([A-Z]+)\\((.*?)\\)");
            Matcher matcher = pattern.matcher(formula);

            if (matcher.find()) {
                String function = matcher.group(1).toUpperCase();
                String range = matcher.group(2);

                // Convert A1:A5 -> List of actual cell values
                List<Double> numbers = getCellValues(range, allCells);

                return switch (function) {
                    case "SUM" -> String.valueOf(numbers.stream().mapToDouble(Double::doubleValue).sum());
                    case "AVERAGE" -> numbers.isEmpty() ? "0" :
                            String.valueOf(numbers.stream().mapToDouble(Double::doubleValue).average().orElse(0));
                    case "MAX" -> numbers.isEmpty() ? "0" :
                            String.valueOf(numbers.stream().mapToDouble(Double::doubleValue).max().orElse(0));
                    case "MIN" -> numbers.isEmpty() ? "0" :
                            String.valueOf(numbers.stream().mapToDouble(Double::doubleValue).min().orElse(0));
                    case "COUNT" -> String.valueOf(numbers.size());
                    default -> "ERROR"; // Unknown function
                };
            }
        } catch (Exception e) {
            return "ERROR";
        }

        return "ERROR";
    }

    // Get list of numeric values from referenced cells
    private List<Double> getCellValues(String range, List<Cell> allCells) {
        String[] parts = range.split(":"); // Handle A1:A5 type ranges

        if (parts.length == 2) {
            int startRow = parseRow(parts[0]);
            int endRow = parseRow(parts[1]);
            int col = parseColumn(parts[0]);

            return allCells.stream()
                    .filter(c -> c.getColumnIndex() == col && c.getRowIndex() >= startRow && c.getRowIndex() <= endRow)
                    .map(Cell::getComputedValue)
                    .map(value -> {
                        try {
                            return Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            return 0.0; // Ignore non-numeric cells
                        }
                    })
                    .toList();
        }
        return List.of();
    }

    private int parseRow(String cell) {
        return Integer.parseInt(cell.replaceAll("[^0-9]", "")) - 1; // Convert A1 -> row index
    }

    private int parseColumn(String cell) {
        return cell.charAt(0) - 'A'; // Convert A1 -> column index
    }
}
