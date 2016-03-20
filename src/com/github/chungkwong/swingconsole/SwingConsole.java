/* SwingConsole.java
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
import static com.github.chungkwong.idem.global.Log.LOG;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;
/**
 * A swing component intended to provide user interface to command-line style program(somewhat shell)
 */
public final class SwingConsole extends JPanel implements CaretListener,KeyListener{
	static String lineSeparator=System.getProperty("line.separator");
	PlainDocument doc=new PlainDocument();
	JTextArea area=new JTextArea(doc);
	final Shell shell;
	int start=0,caretPosition=0,count=0,historyLimit=10;
	String PS1="$ ",currentEdit=null;
	LinkedList<String> history=new LinkedList<String>();
	ListIterator<String> iter=null;
	JFileChooser jfc=new JFileChooser();
	final UndoManager undoManager=new UndoManager();
	/**
	 * Construct a SwingConsole
	 * @param sh the shell
	 * @param initPrompt the first prompt text
	 */
	public SwingConsole(Shell sh,String initPrompt){
		this.shell=sh;
		area.setBackground(Color.BLACK);
		area.setForeground(Color.GREEN);
		area.setCaretColor(Color.GREEN);
		area.setFont(new Font("MONOSPACED",Font.PLAIN,20));
		area.setLineWrap(true);
		area.addCaretListener(this);
		area.addKeyListener(this);
		doc.setDocumentFilter(new DocumentFilter(){
			public void insertString(DocumentFilter.FilterBypass fb,int offset,String string,AttributeSet attr)throws BadLocationException{
				String command=null;
				if(string.equals(lineSeparator)&&shell.acceptLine(command=area.getText().substring(start))){
					fb.insertString(doc.getLength(),"\n",null);
					fb.insertString(doc.getLength(),shell.evaluate(),null);
					fb.insertString(doc.getLength(),PS1,null);
					start=doc.getLength();
					area.setCaretPosition(start);
					addHistoryItem(command);
					currentEdit=null;
					iter=null;
					undoManager.discardAllEdits();
				}else if(string.equals("\t")){
					java.util.List<String> options=shell.getHints(doc.getText(start,offset-start));
					if(options!=null&&!options.isEmpty())
						new CompletePrompt(options,area,doc,offset);
				}else
					fb.insertString(Math.max(offset,start),string,attr);
			}
			public void remove(DocumentFilter.FilterBypass fb,int offset,int length)throws BadLocationException{
				fb.remove(Math.max(offset,start),length);
				iter=null;
			}
			public void replace(DocumentFilter.FilterBypass fb,int offset,int length,String text,AttributeSet attrs)throws BadLocationException{
				String command=null;
				if(text.equals(lineSeparator)&&shell.acceptLine(command=area.getText().substring(start))){
					fb.insertString(doc.getLength(),"\n",null);
					fb.insertString(doc.getLength(),shell.evaluate(),null);
					fb.insertString(doc.getLength(),PS1,null);
					start=doc.getLength();
					area.setCaretPosition(start);
					addHistoryItem(command);
					currentEdit=null;
					iter=null;
					undoManager.discardAllEdits();
				}else if(text.equals("\t")){
					fb.remove(offset,length);
					java.util.List<String> options=shell.getHints(doc.getText(start,offset-start));
					if(options!=null&&!options.isEmpty())
						new CompletePrompt(options,area,doc,offset);
				}else
					fb.replace(Math.max(offset,start),length,text,attrs);
			}
		});
		doc.addUndoableEditListener((UndoableEditEvent e)->{
			undoManager.addEdit(e.getEdit());
		});
		InputMap im=area.getInputMap();
		ActionMap am=area.getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK),"Save");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_DOWN_MASK),"Undo");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_DOWN_MASK|InputEvent.SHIFT_DOWN_MASK),"Redo");
		am.put("Save",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				try{
					if(jfc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
						File file=jfc.getSelectedFile();
						try (Writer out=new OutputStreamWriter(new FileOutputStream(file),"UTF-8")){
							out.write(area.getText());
						}
					}
				}catch(Exception ex){
					LOG.log(Level.INFO,null,ex);
				}
			}
		});
		am.put("Undo",new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				try{
					if(undoManager.canUndo())
						undoManager.undo();
				}catch(CannotUndoException ex){
					LOG.log(Level.INFO,null,ex);
				}
			}
		});
		am.put("Redo", new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				try{
					if(undoManager.canRedo())
						undoManager.redo();
				}catch(CannotUndoException ex){
					LOG.log(Level.INFO,null,ex);
				}
			}
		});
		setLayout(new BorderLayout());
		add(new JScrollPane(area),BorderLayout.CENTER);
		area.append(initPrompt);
		start=initPrompt.length();
		area.setCaretPosition(PS1.length());
	}
	/**
	 * Construct a SwingConsole
	 * @param sh the shell
	 */
	public SwingConsole(Shell sh){
		this(sh,"$ ");
	}
	/**
	 * Get background color of the console
	 * @return background color currently used
	 */
	public Color getBackgroundColor(){
		return area.getBackground();
	}
	/**
	 * Get foreground color of the console
	 * @return foreground color currently used
	 */
	public Color getForegroundColor(){
		return area.getForeground();
	}
	/**
	 * Set background color of the console
	 * @param bg new background color
	 */
	public void setBackgroundColor(Color bg){
		area.setBackground(bg);
	}
	/**
	 * Set foreground color of the console
	 * @param fg new foreground color
	 */
	public void setForegroundColor(Color fg){
		area.setForeground(fg);
	}
	/**
	 * Prevent caret from moving to inactive region
	 * @param e a event notify move of caret
	 */
	public void caretUpdate(CaretEvent e){
		if(e.getDot()<start){
			if(e.getDot()!=e.getMark()){
				area.copy();
			}
			area.setCaretPosition(caretPosition);
		}else
			caretPosition=e.getDot();
	}
	/**
	 * Scroll in the history if needed
	 */
	public void keyPressed(KeyEvent e){
		if(e.isActionKey()){
			try{
				if(e.getKeyCode()==KeyEvent.VK_UP&&area.getLineOfOffset(start)==area.getLineOfOffset(area.getCaretPosition())){
					doc.replace(start,doc.getLength()-start,getPreviousRecord(),null);
				}else if(e.getKeyCode()==KeyEvent.VK_DOWN&&area.getLineOfOffset(doc.getLength())==area.getLineOfOffset(area.getCaretPosition())){
					doc.replace(start,doc.getLength()-start,getNextRecord(),null);
				}
			}catch(Exception ex){
				LOG.log(Level.INFO,null,ex);
			}
		}else
			iter=null;
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	/**
	 * Get prompt text of the console
	 * @return prompt text currently used
	 */
	public String getPromptText(){
		return PS1;
	}
	/**
	 * Set prompt of the console
	 * @param PS1 new prompt
	 */
	public void setPromptText(String PS1){
		this.PS1=PS1;
	}
	/**
	 * Get the maximum number of history records allowed
	 * @return the upper limit
	 */
	public int getHistoryLimit(){
		return historyLimit;
	}
	/**
	 * Set the maximum number of history records allowed
	 * @param historyLimit the upper limit
	 */
	public void setHistoryLimit(int historyLimit){
		this.historyLimit=historyLimit;
	}
	/**
	 * Get the maximum number of undo/redo records allowed
	 * @return the upper limit
	 */
	public int getUndoLimit(){
		return undoManager.getLimit();
	}
	/**
	 * Set the maximum number of undo/redo records allowed
	 * @param undoLimit the upper limit
	 */
	public void setUndoLimit(int undoLimit){
		undoManager.setLimit(undoLimit);
	}
	/**
	 * Add a new history item to the list of history
	 * @param command the item
	 */
	public void addHistoryItem(String command){
		if(historyLimit>0){
			history.add(command);
			if(history.size()>historyLimit){
				history.poll();
			}
			iter=null;
		}
	}
	/**
	 * Get the previous history record
	 * @return record
	 */
	String getPreviousRecord(){
		if(iter==null){
			currentEdit=area.getText().substring(start);
			iter=history.listIterator(history.size());
		}
		if(iter.hasPrevious()){
			return iter.previous();
		}else{
			iter=null;
			return currentEdit;
		}
	}
	/**
	 * Get the next history record
	 * @return record
	 */
	String getNextRecord(){
		if(iter==null){
			currentEdit=area.getText().substring(start);
			if(!history.isEmpty()){
				iter=history.listIterator();
				return history.get(0);
			}else
				return currentEdit;
		}
		if(iter.hasNext())
			iter.next();
		if(iter.hasNext()){
			return iter.next();
		}else{
			iter=null;
			return currentEdit;
		}
	}
	/**
	 * Get the font currently used
	 * @return the font
	 */
	public Font getTextFont(){
		return area.getFont();
	}
	/**
	 * Set the font to be used
	 * @param font the font
	 */
	public void setTextFont(Font font){
		area.setFont(font);
	}
}