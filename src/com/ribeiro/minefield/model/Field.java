package com.ribeiro.minefield.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class Field {
	
	private Boolean open = false;
	private Boolean landMine = false;
	private Boolean marked = false;
	
	private List<Field> neighbors = new ArrayList<>();
	private List<BiConsumer<Field, EventField>> observables = new ArrayList<>();
	
	private final Integer row;
	private final Integer column;
	
	public Field(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean openOrNot) {
		this.open = openOrNot;
		
		if (open) {
			msgByObservable(EventField.OPEN);
		}
	}

	public Boolean getMine() {
		return landMine;
	}

	public void setMine(Boolean landMine) {
		this.landMine = landMine;
	}

	public Boolean getMarked() {
		return marked;
	}

	public void setMarked(Boolean marked) {
		this.marked = marked;
	}

	public List<Field> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Field> neighbors) {
		this.neighbors = neighbors;
	}

	public Integer getRow() {
		return row;
	}

	public Integer getColumn() {
		return column;
	}
	
	public void calligObservable(BiConsumer<Field, EventField> consumer) {
		observables.add(consumer);
	}
	
	public void msgByObservable(EventField event) {
		observables.stream().forEach(o -> o.accept(this, event));
	}
	
	public boolean addNeighbor(Field neighbor) {
		boolean differentRow = this.row != neighbor.getRow();
		boolean differentColumn = this.column != neighbor.getColumn();
		boolean diagonal = differentRow && differentColumn;
		
		int rowDifferential = Math.abs(this.row - neighbor.getRow());
		int columnDifferential = Math.abs(this.column - neighbor.getColumn());
		int sumDifferential = rowDifferential + columnDifferential;
		
		if (sumDifferential == 1 && !diagonal) {
			this.neighbors.add(neighbor);
			return true;
		} else if (sumDifferential == 2 && diagonal) {
			this.neighbors.add(neighbor);
			return true;
		} else {
			return false;
		}
	}
	
	public void asMarkedOrNot() {
		if (!open) {
			marked = !marked;
			
			if (marked) {
				msgByObservable(EventField.MARK_UP);
			} else {
				msgByObservable(EventField.UNMARK);
			}
		}
	}
	
	public boolean openUpField() {
		if (!open && !marked) {

			if (landMine) {
				msgByObservable(EventField.EXPLOSION);
				return true;
			}
			
			setOpen(true);
			
			if (safeNeighborhood()) {
				this.neighbors.forEach(n -> n.openUpField());
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	public boolean safeNeighborhood() {
		return this.neighbors.stream().noneMatch(n -> n.getMine());
	}
	
	public void putLandMine() {
		landMine = true;
	}
	
	public boolean hasBeenUsed() {
		boolean safe = !landMine && open;
		boolean protectedSquare = landMine && marked;
		return safe || protectedSquare;
	}
	
	public int landMineOnNeighborhod() {
		return (int) neighbors.stream().filter(n -> n.getMine()).count();
	}
	
	public void reset() {
		open = false;
		landMine = false;
		marked = false;
		msgByObservable(EventField.RESTART);
	}
	
	public boolean isMined() {
		if (landMine) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
