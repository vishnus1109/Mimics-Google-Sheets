package com.googlesheets.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.googlesheets.entity.Cell;
import com.googlesheets.entity.Spreadsheet;
import com.googlesheets.repository.CellRepository;
import com.googlesheets.repository.SpreadsheetRepository;
import com.googlesheets.service.FormulaService;
import com.googlesheets.service.SpreadsheetService;

@Controller
@RequestMapping("/sheets")
public class SpreadsheetController {
    private final SpreadsheetService sheetService;
    private final SpreadsheetRepository spreadsheetRepo;
    private final CellRepository cellRepo;
    private final FormulaService formulaService;

    public SpreadsheetController(SpreadsheetService sheetService, 
                                 SpreadsheetRepository spreadsheetRepo, 
                                 CellRepository cellRepo, 
                                 FormulaService formulaService) {
        this.sheetService = sheetService;
        this.spreadsheetRepo = spreadsheetRepo;
        this.cellRepo = cellRepo;
        this.formulaService = formulaService;
    }

    @GetMapping("/")
    public String getAllSheets(Model model) {
        List<Spreadsheet> sheets = sheetService.getAllSheets();
        model.addAttribute("sheets", sheets);
        return "index"; // Renders index.html
    }

    @GetMapping("/{id}")
    public String viewSheet(@PathVariable Long id, Model model) {
        Spreadsheet sheet = spreadsheetRepo.findById(id).orElseThrow();

        // Ensure 'cells' is never null
        Map<Integer, Map<Integer, Cell>> cells = new HashMap<>();
        for (Cell cell : sheet.getCells()) {
            cells.computeIfAbsent(cell.getRowIndex(), r -> new HashMap<>())
                 .put(cell.getColumnIndex(), cell);
        }

        model.addAttribute("sheet", sheet);
        model.addAttribute("cells", cells); // Ensure 'cells' is always initialized

        return "sheet"; // Renders sheet.html
    }

    @PostMapping("/create")
    public String createSheet(@RequestParam String name) {
        sheetService.createSheet(name);
        return "redirect:/sheets/";
    }

    @PostMapping("/{id}/update")
    public String updateCell(@PathVariable Long id, 
                             @RequestParam int rowIndex, 
                             @RequestParam int columnIndex, 
                             @RequestParam String value) {
        Spreadsheet sheet = spreadsheetRepo.findById(id).orElseThrow();
        List<Cell> allCells = cellRepo.findAll();

        // Find existing cell or create a new one
        Cell cell = cellRepo.findBySpreadsheetAndRowIndexAndColumnIndex(sheet, rowIndex, columnIndex)
                .orElseGet(Cell::new);

        cell.setSpreadsheet(sheet);
        cell.setRowIndex(rowIndex);
        cell.setColumnIndex(columnIndex);
        cell.setValue(value);
        cell.setComputedValue(formulaService.evaluateFormula(cell, allCells));

        cellRepo.save(cell);
        return "redirect:/sheets/" + id;
    }
}
