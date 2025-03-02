package com.googlesheets.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.googlesheets.entity.Cell;
import com.googlesheets.entity.Spreadsheet;
import com.googlesheets.repository.CellRepository;
import com.googlesheets.repository.SpreadsheetRepository;

@Service
public class SpreadsheetService {

	SpreadsheetRepository ssr;
	CellRepository cr;
	FormulaService formulaService;
	
	public SpreadsheetService(SpreadsheetRepository ssr, CellRepository cr, FormulaService formulaService) {
        this.ssr = ssr;
        this.cr = cr;
        this.formulaService = formulaService;
    }
    
	
	public List<Spreadsheet> getAllSheets(){
		return ssr.findAll();	
	}
	
	public Optional<Spreadsheet> getSheetById(Long id){
		return ssr.findById(id);
	}
	
	public Spreadsheet createSheet(String name) {
		Spreadsheet sheet=new Spreadsheet();
		sheet.setName(name);
		return ssr.save(sheet);
	}
	
    public Cell updateCell(Long sheetId, int rowIndex, int columnIndex, String value) {
        Spreadsheet sheet = ssr.findById(sheetId).orElseThrow();
        List<Cell> allCells = cr.findAll(); // Get all cells for dependency calculations

        // Find existing cell or create new one
        Cell cell = cr.findBySpreadsheetAndRowIndexAndColumnIndex(sheet, rowIndex, columnIndex)
                .orElseGet(() -> new Cell());

        cell.setSpreadsheet(sheet);
        cell.setRowIndex(rowIndex);
        cell.setColumnIndex(columnIndex);
        cell.setValue(value);
        
        // Compute value if it's a formula
        cell.setComputedValue(formulaService.evaluateFormula(cell, allCells));

        return cr.save(cell);
    }


	
}
