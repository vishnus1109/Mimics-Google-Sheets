package com.googlesheets.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Spreadsheet {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private String name;

	 @OneToMany(mappedBy = "spreadsheet", cascade = CascadeType.ALL)
	 private List<Cell> cells;

	public Spreadsheet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Spreadsheet(Long id, String name, List<Cell> cells) {
		super();
		this.id = id;
		this.name = name;
		this.cells = cells;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Cell> getCells() {
		return cells;
	}

	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}

	@Override
	public String toString() {
		return "Spreadsheet [id=" + id + ", name=" + name + "]";
	}
	 
	 

}
