package com.googlesheets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.googlesheets.entity.Cell;
import com.googlesheets.entity.Spreadsheet;
import com.googlesheets.repository.CellRepository;
import com.googlesheets.repository.SpreadsheetRepository;

@Service
public class CellService {
    private final CellRepository cellRepository;
    private final SpreadsheetRepository spreadsheetRepository;
    private final FormulaService formulaService;

    public CellService(CellRepository cellRepository, SpreadsheetRepository spreadsheetRepository, FormulaService formulaService) {
        this.cellRepository = cellRepository;
        this.spreadsheetRepository = spreadsheetRepository;
        this.formulaService = formulaService;
    }

    /**
     * Find a cell by spreadsheet, row, and column index.
     */
    public Optional<Cell> getCell(Long sheetId, int rowIndex, int columnIndex) {
        Spreadsheet sheet = spreadsheetRepository.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("Spreadsheet not found"));
        return cellRepository.findBySpreadsheetAndRowIndexAndColumnIndex(sheet, rowIndex, columnIndex);
    }

    /**
     * Create or update a cell value.
     */
    public Cell updateCell(Long sheetId, int rowIndex, int columnIndex, String value) {
        Spreadsheet sheet = spreadsheetRepository.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("Spreadsheet not found"));
        List<Cell> allCells = cellRepository.findAll(); // Fetch all cells for dependency evaluation

        // Find existing cell or create a new one
        Cell cell = cellRepository.findBySpreadsheetAndRowIndexAndColumnIndex(sheet, rowIndex, columnIndex)
                .orElseGet(Cell::new);

        cell.setSpreadsheet(sheet);
        cell.setRowIndex(rowIndex);
        cell.setColumnIndex(columnIndex);
        cell.setValue(value);

        // If it's a formula, evaluate and store computed value
        cell.setComputedValue(formulaService.evaluateFormula(cell, allCells));

        return cellRepository.save(cell);
    }

    /**
     * Delete a cell by spreadsheet ID, row index, and column index.
     */
    public void deleteCell(Long sheetId, int rowIndex, int columnIndex) {
        Spreadsheet sheet = spreadsheetRepository.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("Spreadsheet not found"));

        cellRepository.findBySpreadsheetAndRowIndexAndColumnIndex(sheet, rowIndex, columnIndex)
                .ifPresent(cellRepository::delete);
    }

    /**
     * Get all cells in a spreadsheet.
     */
    public List<Cell> getAllCells(Long sheetId) {
        Spreadsheet sheet = spreadsheetRepository.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("Spreadsheet not found"));
        return cellRepository.findBySpreadsheet(sheet);
    }
}
