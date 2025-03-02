package com.googlesheets.entity;

import jakarta.persistence.*;
@Entity
public class Cell {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private int rowIndex;
	 private int columnIndex;
	 private String value;
	 private String computedValue;

	 @ManyToOne
	 @JoinColumn(name = "spreadsheet_id")
	 private Spreadsheet spreadsheet;

	public Cell() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Cell(Long id, int rowIndex, int columnIndex, String value, String computedValue, Spreadsheet spreadsheet) {
		super();
		this.id = id;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.value = value;
		this.computedValue = computedValue;
		this.spreadsheet = spreadsheet;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getComputedValue() {
		return computedValue;
	}

	public void setComputedValue(String computedValue) {
		this.computedValue = computedValue;
	}

	public Spreadsheet getSpreadsheet() {
		return spreadsheet;
	}

	public void setSpreadsheet(Spreadsheet spreadsheet) {
		this.spreadsheet = spreadsheet;
	}

	@Override
	public String toString() {
		return "Cell [id=" + id + ", rowIndex=" + rowIndex + ", columnIndex=" + columnIndex + ", value=" + value
				+ ", computedValue=" + computedValue + ", spreadsheet=" + spreadsheet + "]";
	}

 
}
