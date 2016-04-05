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
import com.github.chungkwong.idem.global.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class MenuRegistry{
	private final JMenuBar bar;
	public MenuRegistry(JMenuBar bar){
		this.bar=bar;
		SimpleAction open=new SimpleAction(UILanguageManager.getDefaultTranslation("OPEN"));
		SimpleAction edit=new SimpleAction(UILanguageManager.getDefaultTranslation("EDIT"));
		SimpleAction tool=new SimpleAction(UILanguageManager.getDefaultTranslation("TOOL"));
		SimpleAction help=new SimpleAction(UILanguageManager.getDefaultTranslation("HELP"));
		getOrAddMenu(open).addMenuItem(tool);
		getOrAddMenu(open).addSeparator();
		getOrAddMenu(open).getOrAddMenu(edit).addMenuItem(new DefaultEditorKit.CopyAction());
		getOrAddMenu(help);
	}
	public MenuEntry getOrAddMenu(Action action){
		int menuCount=bar.getMenuCount();
		for(int i=0;i<menuCount;i++)
			if(bar.getMenu(i).getAction().equals(action))
				return new MenuEntry(bar.getMenu(i));
		JMenu menu=new JMenu(action);
		bar.add(menu);
		return new MenuEntry(menu);
	}
	public static Action createAction(String name,Icon icon){
		return new SimpleAction(name,icon);
	}
	public static Action createAction(String name){
		return new SimpleAction(name);
	}
	public static class SimpleAction extends AbstractAction{
		public SimpleAction(String name){
			super(name);
		}
		public SimpleAction(String name,Icon icon){
			super(name,icon);
		}
		@Override
		public void actionPerformed(ActionEvent e){

		}
	}
	public static class MenuEntry{
		private final JMenu menu;
		public MenuEntry(JMenu menu){
			this.menu=menu;
		}
		public MenuEntry getOrAddMenu(Action action){
			Component[] menuComponents=menu.getMenuComponents();
			for(Component comp:menuComponents)
				if(comp instanceof JMenu&&((JMenu)comp).getAction().equals(action))
					return new MenuEntry((JMenu)comp);
			JMenu newMenu=new JMenu(action);
			menu.add(newMenu);
			return new MenuEntry(newMenu);
		}
		public void addSeparator(){
			menu.addSeparator();
		}
		public void addMenuItem(Action action){
			menu.add(new JMenuItem(action));
		}
		public void addComponent(Component c){
			menu.add(c);
		}
	}
}