package com.ribeiro.minefield.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Board implements BiConsumer<Field, EventField> {

	private Integer rows;
	private Integer columns;
	private Integer landMines;
	
	private List<Field> fields = new ArrayList<>();
	private List<Consumer<Boolean>> observables = new ArrayList<>();
	
	public Board(Integer rows, Integer columns, Integer landMines) {
		this.rows = rows;
		this.columns = columns;
		this.landMines = landMines;
		
		createFields();
		neighborsAssociation();
		landMineRandomizing();
	}
	
	
	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getColumns() {
		return columns;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	
	public void forEachSquare(Consumer<Field> function) {
		this.fields.forEach(function);
	}

	public void calligObservable(Consumer<Boolean> observer) {
		observables.add(observer);
	}
	
	public void msgByObservable(Boolean result) {
		observables.stream().forEach(o -> o.accept(result));
	}

	public void createFields() {
		for (int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				Field field = new Field(i, j);
				field.calligObservable(this);
				fields.add(field);
			}
		}
	}
	
	public void neighborsAssociation() {
		for (Field f : fields) {
			for (Field fi : fields) {
				f.addNeighbor(fi);
			}
		}
	}

	private void landMineRandomizing() {
		long mineLandsActivates = 0;
		do {
			int randomizing = (int) (Math.random() * fields.size());
			fields.get(randomizing).putLandMine();
			mineLandsActivates = fields.stream().filter(f -> f.getMine()).count();
		} while (mineLandsActivates < this.landMines);
	}
	
	public boolean hasBeenUsed() {
		return fields.stream().allMatch(f -> f.hasBeenUsed());
	}

	public void reset() {
		fields.stream().forEach(f -> f.reset());
		this.landMineRandomizing();
	}
	
	public void openUp(int row, int column) {
		try {
			fields.parallelStream()
			.filter(f -> f.getRow() == row && f.getColumn() == column)
			.findFirst().ifPresent(f -> f.openUpField());
		} catch (Exception e) { // FIXME
			fields.forEach(f -> f.setOpen(true));
			throw e;
		}
	}
	
	public void showMineLands() {
		fields.stream().filter(f -> f.getMine()).filter(f -> !f.getMarked()).forEach(f -> f.setOpen(true));
	}
	
	public void puttingASymbol(int row, int column) {
		fields.parallelStream().filter(f -> f.getRow() == row && f.getColumn() == column)
		.findFirst().ifPresent(f -> f.asMarkedOrNot());
	}

	@Override
	public void accept(Field field, EventField event) {
		if(event == EventField.EXPLOSION) {
			showMineLands();
			msgByObservable(false);
		} else if (hasBeenUsed()) {
			System.out.println("Won");
			msgByObservable(true);
		}
		
	}


	
}
