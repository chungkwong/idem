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
import com.github.chungkwong.idem.loader.*;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class MainFrame extends JFrame{
	private final JMenuBar menuBar=new JMenuBar();
	private final MenuRegistry menu_registry=new MenuRegistry(menuBar);
	private final JLabel statusBar=new JLabel();
	private final WindowGroup windowRoot=WindowGroup.create(new WindowSingle(new TextData()));
	WindowSingle windowRecent;
	public static final MainFrame MAIN_FRAME=new MainFrame();
	private MainFrame(){
		super(UILanguageManager.getDefaultTranslation("IDEM"));
		StartUp.startUp();
		setJMenuBar(menuBar);
		add(windowRoot,BorderLayout.CENTER);
		add(statusBar,BorderLayout.SOUTH);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public WindowSingle getWindowRecent(){
		return windowRecent;
	}
	public MenuRegistry getMenuRegistry(){
		return menu_registry;
	}
	public void setStatus(String msg){
		statusBar.setText(msg);
	}
	public static void main(String[] args){
		SwingUtilities.invokeLater(()->MAIN_FRAME.setVisible(true));
	}
}