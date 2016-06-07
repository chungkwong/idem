/*
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
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PopupHint2 extends JPanel implements KeyListener,MouseListener,
		UndoableEditListener,WindowFocusListener,ListSelectionListener{
	private Hint[] choices;
	private DefaultListModel<Hint> vec=new DefaultListModel<>();
	private JTextComponent editor;
	private JTextField input=new JTextField();
	private JEditorPane note=new JEditorPane();
	private JList<Hint> loc=new JList<Hint>(vec);
	private Document doc;
	private int pos;
	private Popup popup;
	/**
	 * Construct a CompletePrompt
	 * @param area the JTextArea where this component is shown
	 * @param doc the Document that the final choice is to be inserted
	 */
	public PopupHint2(JTextComponent editor,Document doc){
		this.doc=doc;
		this.editor=editor;
		setLayout(new BorderLayout());
		//setUndecorated(true);
		setSize(400,300);
		loc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loc.setSelectedIndex(0);
		input.getDocument().addUndoableEditListener(this);
		input.addKeyListener(this);
		loc.addMouseListener(this);
		loc.addListSelectionListener(this);
		loc.setCellRenderer(new DefaultListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList arg0,Object arg1,int arg2,boolean arg3,boolean arg4){
				Component c=super.getListCellRendererComponent(arg0,arg1,arg2,arg3,arg4);
				((JLabel)c).setText(((Hint)arg1).getDisplayText());
				((JLabel)c).setIcon(((Hint)arg1).getIcon());
				((JLabel)c).setHorizontalAlignment(SwingConstants.LEFT);
				return c;
			}
		});
		loc.setOpaque(false);
		//addWindowFocusListener(this);
		add(input,BorderLayout.NORTH);
		add(new JScrollPane(loc),BorderLayout.WEST);
		note.setContentType("text/html");
		note.setEditable(false);
		add(new JScrollPane(note),BorderLayout.EAST);
		//pack();
	}
	public void show(Hint[] choices){
		this.pos=editor.getCaretPosition();
		this.choices=choices;
		vec.ensureCapacity(choices.length);
		for(int i=0;i<choices.length;i++)
			vec.add(i,choices[i]);
		loc.setSelectedIndex(0);
		try{
			Point loc=editor.modelToView(pos).getLocation();
			loc.translate((int)editor.getLocationOnScreen().getX(),(int)editor.getLocationOnScreen().getY());
			popup=PopupFactory.getSharedInstance().getPopup(editor,this,(int)loc.getX(),(int)loc.getY());
			popup.show();
			input.requestFocusInWindow();
			//popup.show(this,(int)rect.getX(),(int)rect.getY());
			//popup.requestFocusInWindow();
		}catch(BadLocationException|NullPointerException ex){

		}
		//pack();
	}
	public void hide(){
		if(popup!=null){
			popup.hide();
			popup=null;
			choices=null;
			vec.removeAllElements();
			String text=input.getText();
			if(!text.isEmpty()){
				try{doc.insertString(pos,text,null);}catch(BadLocationException ex){}
			}
			input.setText("");
		}
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
		for(int i=0,j=0;i<choices.length;i++)
			if(choices[i].getInputText().startsWith(text))
				vec.add(j++,choices[i]);
		if(vec.isEmpty()){
			choose(input.getText());
		}else{
			loc.setSelectedIndex(0);
			//pack();
			//loc.repaint();
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
		}catch(Exception ex){
			LOG.log(Level.WARNING,null,ex);
		}
		input.setText("");
		//editor.requestFocus();
		setVisible(false);
	}
	@Override
	public void windowGainedFocus(WindowEvent e){
		input.requestFocusInWindow();
	}
	@Override
	public void windowLostFocus(WindowEvent e){

		setVisible(false);
	}
	@Override
	public void valueChanged(ListSelectionEvent e){
		try{
			if(loc.getSelectedValue()!=null){
				note.read(loc.getSelectedValue().getDocument(),null);

				/*note.setPreferredSize(new Dimension(
						Math.max((int)loc.getPreferredSize().getWidth(),(int)note.getPreferredSize().getWidth())
						,(int)loc.getHeight()));*/
			}
		}catch(IOException ex){
			note.setText(UILanguageManager.getDefaultTranslation("NO_DOCUMENT"));
		}
	}
}
