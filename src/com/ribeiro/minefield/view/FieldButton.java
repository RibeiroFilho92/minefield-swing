package com.ribeiro.minefield.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.ribeiro.minefield.model.EventField;
import com.ribeiro.minefield.model.Field;

public class FieldButton extends JButton implements BiConsumer<Field, EventField>, MouseListener  {

	private static final long serialVersionUID = 1L;

	private Color BG_COMMON = new Color(184, 184, 184);
	private Color BG_MARKED = new Color(8, 179, 247);
	private Color BG_EXPLOSION = new Color(189, 66, 68);
	private Color TEXT = new Color(0, 100, 0);
	
	private Field field;
	
	public FieldButton(Field field) {
		this.field = field;
		
		setBackground(BG_COMMON);
		setOpaque(true);
		setBorder(BorderFactory.createBevelBorder(0));
		
		addMouseListener(this);
		
		field.calligObservable(this);
	}

	@Override
	public void accept(Field field, EventField event) {
		switch(event) {
		case OPEN: apllyOpen();
				   break;
		case MARK_UP: applyMark();
				   break;
		case EXPLOSION: applyExplosion();
		                break;	
		default: applyNormalStyle();
		}
	}

	private void applyNormalStyle() {
		setBackground(BG_COMMON);
		setText("");
		setBorder(BorderFactory.createBevelBorder(0));
	}

	private void applyExplosion() {
		setBackground(BG_EXPLOSION);
		setForeground(Color.WHITE);
		setText("X");
		
	}

	private void applyMark() {
		setBackground(BG_MARKED);
		setForeground(Color.BLACK);
		setText("B");
	}

	private void apllyOpen() {
		
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if (field.getMine()) {
			setBackground(BG_EXPLOSION);
			return;
		}
		
		setBackground(BG_COMMON);

		switch(field.landMineOnNeighborhod()) {
			case 1: setForeground(TEXT);
					break;
			case 2: setForeground(Color.BLUE);
					break;
			case 3: setForeground(Color.YELLOW);
					break;
			case 4: 
			case 5: 
			case 6: 
					setForeground(Color.YELLOW);
					break;	
			default: 
					setForeground(Color.PINK);
		}
		
		String value = !field.safeNeighborhood() ? field.landMineOnNeighborhod() + "" : "";
		setText(value);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			field.openUpField();
		} else {
			field.asMarkedOrNot();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
