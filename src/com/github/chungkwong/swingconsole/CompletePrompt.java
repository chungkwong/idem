/* CompletePrompt.java
 * =========================================================================
 * This file is originally part of the SwingConsole Project
 *
 * Copyright (C) 2015 Chan Chung Kwong
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 */
package com.github.chungkwong.swingconsole;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
/**
 * A swing component that show auto-complete choice
 */
public final class CompletePrompt extends JPopupMenu implements KeyListener,UndoableEditListener,MouseListener,ComponentListener{
	List<String> options=new ArrayList<String>();
	DefaultListModel<String> vec=new DefaultListModel<String>();
	JTextField input=new JTextField();
	JList<String> loc=new JList<String>(vec);
	Document doc;
	int pos;
	/**
	 * Construct a CompletePrompt
	 * @param options all possible choices
	 * @param area the JTextArea where this component is shown
	 * @param doc the Document that the final choice is to be inserted
	 * @param pos offset that the final choice is to be inserted
	 */
	public CompletePrompt(List<String> options,JTextArea area,Document doc,int pos){
		this.options=options;
		this.doc=doc;
		this.pos=pos;
		Collections.sort(options);
		for(String option:options)
			vec.addElement(option);
		loc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loc.setSelectedIndex(0);
		input.getDocument().addUndoableEditListener(this);
		input.addKeyListener(this);
		loc.addMouseListener(this);
		this.addComponentListener(this);
		add(input);
		add(loc);
		pack();
		try{
			Rectangle rect=area.modelToView(pos);
			show(area,(int)rect.getX(),(int)rect.getY());
		}catch(Exception ex){
			Logger.getGlobal().log(Level.WARNING,null,ex);
		}
	}
	public void keyPressed(KeyEvent e){
		//System.out.println(e.getKeyChar());
		int code=e.getKeyCode();
		if(code==KeyEvent.VK_UP){
			loc.setSelectedIndex((loc.getSelectedIndex()+vec.size()-1)%vec.size());
		}else if(code==KeyEvent.VK_DOWN){
			loc.setSelectedIndex((loc.getSelectedIndex()+1)%vec.size());
		}else if(code==KeyEvent.VK_ENTER){
			choose(input.getText());
		}else if(code==KeyEvent.VK_SPACE){
			choose(loc.getSelectedValue());
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	/**
	 * Update the choices in the list according to current input
	 */
	public void undoableEditHappened(UndoableEditEvent e){
		vec.clear();
		String text=input.getText();
		for(String option:options)
			if(option.startsWith(text))
				vec.addElement(option);
		if(vec.isEmpty())
			choose(input.getText());
		else{
			pack();
			loc.setSelectedIndex(0);
			input.requestFocus();
			loc.repaint();
		}
	}
	/**
	 * Make final choice if a list item is double-clicked
	 */
	public void mouseClicked(MouseEvent e){
		if(e.getClickCount()==2){
			choose(vec.get(loc.locationToIndex(e.getPoint())));
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void componentHidden(ComponentEvent e){}
	public void componentMoved(ComponentEvent e){
		input.requestFocus();
	}
	public void componentResized(ComponentEvent e){
		input.requestFocus();
	}
	public void componentShown(ComponentEvent e){}
	/**
	 * Make the final choice
	 * @param choice final choice
	 */
	public void choose(String choice){
		try{
			doc.insertString(pos,choice,null);
			setVisible(false);
		}catch(Exception ex){
			Logger.getGlobal().log(Level.WARNING,null,ex);
		}
	}
}