package com.ribeiro.minefield.view;

import javax.swing.JFrame;

import com.ribeiro.minefield.model.Board;

public class MainScreen extends JFrame{

	private static final long serialVersionUID = 1L;

	public MainScreen() {
		Board board = new Board(16, 30, 50);
		PanelBoard panel = new PanelBoard(board);
		
		add(panel);
		setTitle("Minefield");
		setSize(690, 438);
		setLocale(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		
		new MainScreen();
	}
}
