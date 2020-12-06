package com.ribeiro.minefield.view;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.ribeiro.minefield.model.Board;

public class PanelBoard extends JPanel {

	private static final long serialVersionUID = 1L;

	public PanelBoard(Board board) {
		GridLayout layout = new GridLayout(board.getRows(), board.getColumns());
		setLayout(layout);
		
		board.forEachSquare(b -> add(new FieldButton(b)));
		board.calligObservable(e -> {
			SwingUtilities.invokeLater(() -> {
				if (e == true) {
					JOptionPane.showMessageDialog(this, "You won!");
				} else {
					JOptionPane.showMessageDialog(this, "You lost!");
				}
				
				board.reset();
			});
		});	
	}
	
}
