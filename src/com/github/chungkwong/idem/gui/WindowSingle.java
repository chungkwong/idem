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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class WindowSingle extends Container implements FocusListener{
	JScrollPane component;
	private DataObject obj;
	public WindowSingle(DataObject obj){
		setLayout(new BorderLayout());
		this.obj=obj;
		JComponent view=obj.createDefaultView();
		view.addFocusListener(this);
		component=new JScrollPane(view);
		add(component,BorderLayout.CENTER);
	}
	public DataObject getDataObject(){
		return obj;
	}
	public void setDataObject(DataObject obj){
		remove(component);
		component.getViewport().getView().removeFocusListener(this);
		this.obj=obj;
		JComponent view=obj.createDefaultView();
		view.addFocusListener(this);
		component=new JScrollPane(view);
		add(component,BorderLayout.CENTER);
		validate();
		view.requestFocusInWindow();
	}
	@Override
	public void focusGained(FocusEvent e){
		MainFrame.MAIN_FRAME.windowRecent=this;
	}
	@Override
	public void focusLost(FocusEvent e){

	}
}