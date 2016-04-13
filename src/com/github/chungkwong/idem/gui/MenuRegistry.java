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
import com.github.chungkwong.idem.actions.*;
import com.github.chungkwong.idem.global.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class MenuRegistry{
	private final JMenuBar bar;
	private final HashMap<Action,MenuEntry> mapping=new HashMap<>();
	public static final Action FILE=new SimpleAction(UILanguageManager.getDefaultTranslation("FILE"));
	public static final Action EDIT=new SimpleAction(UILanguageManager.getDefaultTranslation("EDIT"));
	public static final Action VIEW=new SimpleAction(UILanguageManager.getDefaultTranslation("VIEW"));
	public static final Action TOOL=new SimpleAction(UILanguageManager.getDefaultTranslation("TOOL"));
	public static final Action HELP=new SimpleAction(UILanguageManager.getDefaultTranslation("HELP"));
	/**
	 * No action can appear more than once
	 * @param bar MenuBar
	 */
	public MenuRegistry(JMenuBar bar){
		this.bar=bar;
		getOrAddMenu(FILE).addMenuItem(new OpenAction());
		getOrAddMenu(FILE).addMenuItem(new NewAction());
		getOrAddMenu(EDIT);
		getOrAddMenu(VIEW);
		getOrAddMenu(TOOL);
		getOrAddMenu(HELP);

	}
	public MenuEntry getOrAddMenu(Action action){
		if(mapping.containsKey(action))
			return mapping.get(action);
		else{
			JMenu menu=new JMenu(action);
			bar.add(menu);
			MenuEntry entry=new MenuEntry(menu);
			mapping.put(action,entry);
			return entry;
		}
	}
	public static Action createSimpleAction(String name,Icon icon){
		return new SimpleAction(name,icon);
	}
	public static Action createSimpleAction(String name){
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
	public class MenuEntry{
		private final JMenu menu;
		public MenuEntry(JMenu menu){
			this.menu=menu;
		}
		public MenuEntry getOrAddMenu(Action action){
			if(mapping.containsKey(action))
				return mapping.get(action);
			else{
				JMenu submenu=new JMenu(action);
				menu.add(submenu);
				MenuEntry entry=new MenuEntry(submenu);
				mapping.put(action,entry);
				return entry;
			}
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