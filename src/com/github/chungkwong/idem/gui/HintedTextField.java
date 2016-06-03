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
import com.github.chungkwong.idem.util.*;
import java.awt.*;
import java.util.logging.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class HintedTextField extends JTextField implements CaretListener,AncestorListener{
	private HintProvider hintProvider;
	private RealtimeTask hintDaemon;
	//private PopupHint popup;
	private PopupHint2 popup;
	public HintedTextField(HintProvider hintProvider){
		this.hintProvider=hintProvider;
		this.popup=new PopupHint2(this,getDocument());
		addCaretListener(this);
		addAncestorListener(this);
	}
	public static void main(String[] args){
		JFrame f=new JFrame("Test");
		f.add(new HintedTextField(new HintProvider(){
			@Override
			public Hint[] getHints(Document doc,int pos){
				ImageIcon icon=null;
				try{
					icon=new ImageIcon(ImageIO.read(HintedTextField.class.getResourceAsStream("/com/github/chungkwong/idem/resources/icons/jedit.png")));
				}catch(Exception ex){
					ex.printStackTrace();
				}
				return new Hint[]{
					new SimpleHint("<html><i>ls</i></html>","ls",icon,"list file"),
					new SimpleHint("ln",null,"<h1>link file</h1>"),
					new SimpleHint("pwd",null,"show working directory"),
					new SimpleHint(Integer.toHexString(pos),null,"position")
				};
			}
		}),BorderLayout.CENTER);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	public void updateHint(){

	}
	public void showHint(Hint[] hints){
		if(hints.length>0){
			popup.prepare(hints,getCaretPosition());
			try{
				Point loc=modelToView(getCaretPosition()).getLocation();
				loc.translate((int)getLocationOnScreen().getX(),(int)getLocationOnScreen().getY());
				popup.setLocation(loc);
				popup.setVisible(true);

				//popup.show(this,(int)rect.getX(),(int)rect.getY());
				//popup.requestFocusInWindow();
			}catch(BadLocationException ex){
				LOG.log(Level.FINE.WARNING,"",ex);
			}
		}
	}
	@Override
	public void caretUpdate(CaretEvent e){
		hintDaemon.invoke();
	}
	@Override
	public synchronized void ancestorAdded(AncestorEvent event){
		if(hintDaemon==null){
			hintDaemon=new RealtimeTask(
					()->SwingUtilities.invokeLater(()->showHint(hintProvider.getHints(getDocument(),getCaretPosition()))));
			hintDaemon.invoke();
		}
	}
	@Override
	public synchronized void ancestorRemoved(AncestorEvent event){
		hintDaemon.stop();
		hintDaemon=null;
	}
	@Override
	public void ancestorMoved(AncestorEvent event){

	}
}