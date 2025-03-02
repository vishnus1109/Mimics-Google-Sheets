package com.googlesheets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.googlesheets.entity.Spreadsheet;

public interface SpreadsheetRepository extends JpaRepository<Spreadsheet, Long> {

}
