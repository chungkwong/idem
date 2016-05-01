/*/*
 * Copyright (C) 2016 Chan Chung Kwong <1m02math@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.chungkwong.idem.gui;

import static com.github.chungkwong.idem.global.Log.LOG;
import com.github.chungkwong.idem.global.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
/**
 * A swing component that show auto-complete choice
 */
public final class PopupHint extends JPopupMenu implements KeyListener,
		UndoableEditListener,MouseListener,FocusListener,PopupMenuListener,ListSelectionListener{
	private Hint[] hints;
	private DefaultListModel<Hint> vec=new DefaultListModel<>();
	private JTextField input=new JTextField();
	private JEditorPane note=new JEditorPane();
	private JList<Hint> loc=new JList<Hint>(vec);
	private Document doc;
	private int pos;
	/**
	 * Construct a CompletePrompt
	 * @param area the JTextArea where this component is shown
	 * @param doc the Document that the final choice is to be inserted
	 */
	public PopupHint(JTextComponent area,Document doc){
		this.doc=doc;
		this.pos=pos;
		setOpaque(false);
		loc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loc.setSelectedIndex(0);
		input.getDocument().addUndoableEditListener(this);
		input.addKeyListener(this);
		loc.addMouseListener(this);
		loc.addListSelectionListener(this);
		loc.setCellRenderer(new DefaultListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList arg0,Object arg1,int arg2,boolean arg3,boolean arg4){
				Component c=super.getListCellRendererComponent(loc,arg1,arg2,arg4,arg4);
				
				return c;
			}

		});
		loc.setOpaque(false);
		addFocusListener(this);
		addPopupMenuListener(this);
		add(input);
		add(new JScrollPane(loc));
		note.setContentType("text/html");
		note.setEditable(false);
		add(new JScrollPane(note));
		pack();
	}
	public void prepare(Hint[] choices,int pos){
		this.hints=choices;
		vec.removeAllElements();
		vec.ensureCapacity(choices.length);
		for(int i=0;i<choices.length;i++)
			vec.add(i,choices[i]);
		this.pos=pos;
		loc.setSelectedIndex(0);
	}
	@Override
	public void keyPressed(KeyEvent e){
		//System.out.println(e.getKeyChar());
		int code=e.getKeyCode();
		if(code==KeyEvent.VK_UP){
			loc.setSelectedIndex((loc.getSelectedIndex()+vec.size()-1)%vec.size());
		}else if(code==KeyEvent.VK_DOWN){
			loc.setSelectedIndex((loc.getSelectedIndex()+1)%vec.size());
		}else if(code==KeyEvent.VK_SPACE){
			choose(input.getText());
		}else if(code==KeyEvent.VK_ENTER){
			choose(loc.getSelectedValue().getInputText());
		}
	}
	@Override
	public void keyReleased(KeyEvent e){}
	@Override
	public void keyTyped(KeyEvent e){}
	/**
	 * Update the choices in the list according to current input
	 */
	@Override
	public void undoableEditHappened(UndoableEditEvent e){
		vec.clear();
		String text=input.getText();
		for(int i=0,j=0;i<hints.length;i++)
			if(hints[i].getDisplayText().startsWith(text))
				vec.add(j++,hints[i]);
		if(vec.isEmpty()){
			choose(input.getText());
		}else{
			pack();
			loc.setSelectedIndex(0);
			loc.repaint();
			input.requestFocusInWindow();
		}
	}
	/**
	 * Make final choice if a list item is double-clicked
	 */
	@Override
	public void mouseClicked(MouseEvent e){
		if(e.getClickCount()==2){
			choose(vec.get(loc.locationToIndex(e.getPoint())).getInputText());
		}
	}
	@Override
	public void mouseEntered(MouseEvent e){}
	@Override
	public void mouseExited(MouseEvent e){}
	@Override
	public void mousePressed(MouseEvent e){}
	@Override
	public void mouseReleased(MouseEvent e){}
	/**
	 * Make the final choice
	 * @param choice final choice
	 */
	public void choose(String choice){
		try{
			doc.insertString(pos,choice,null);
			input.setText("");
			setVisible(false);
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
	}
	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e){
		input.requestFocusInWindow();
	}
	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e){
		String text=input.getText();
		if(!text.isEmpty()){
			try{
				doc.insertString(pos,text,null);
			}catch(BadLocationException ex){

			}
			input.setText("");
		}
		vec.clear();
		getInvoker().requestFocusInWindow();
	}
	@Override
	public void popupMenuCanceled(PopupMenuEvent e){

	}
	@Override
	public void focusGained(FocusEvent e){
		input.requestFocusInWindow();
	}
	@Override
	public void focusLost(FocusEvent e){

	}
	@Override
	public void valueChanged(ListSelectionEvent e){
		try{
			if(loc.getSelectedValue()!=null){
				note.read(loc.getSelectedValue().getDocument(),null);
				note.setPreferredSize(new Dimension(
						Math.max((int)loc.getPreferredSize().getWidth(),(int)note.getPreferredSize().getWidth())
						,(int)loc.getHeight()));
			}
		}catch(IOException ex){
			note.setText(UILanguageManager.getDefaultTranslation("NO_DOCUMENT"));
		}
	}
}