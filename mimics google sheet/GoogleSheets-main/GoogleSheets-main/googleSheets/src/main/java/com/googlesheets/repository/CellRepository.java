package com.googlesheets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.googlesheets.entity.Cell;
import com.googlesheets.entity.Spreadsheet;

public interface CellRepository extends JpaRepository<Cell, Long> {
	 // Custom query to find a cell by spreadsheet, row index, and column index
    @Query("SELECT c FROM Cell c WHERE c.spreadsheet = :spreadsheet AND c.rowIndex = :rowIndex AND c.columnIndex = :columnIndex")
    Optional<Cell> findBySpreadsheetAndRowIndexAndColumnIndex(@Param("spreadsheet") Spreadsheet spreadsheet,
                                                              @Param("rowIndex") int rowIndex,
                                                              @Param("columnIndex") int columnIndex);
    List<Cell> findBySpreadsheet(Spreadsheet spreadsheet);
}
