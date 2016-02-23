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
package com.github.chungkwong.idem;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Emacs extends JFrame{
	private JMenuBar menubar=new JMenuBar();
	private JMenu file=new JMenu("File"),help=new JMenu("Help");
	private JTextField search=new JTextField();
	private JToolBar toolbar=new JToolBar(JToolBar.VERTICAL);
	private JTextPane pane=new JTextPane();
	private JLabel status=new JLabel("status");
	public Emacs(){
		super("EMACS Clone");
		add(new SumViewer(),BorderLayout.CENTER);
		setJMenuBar(menubar);
		file.add(new JMenuItem("New"));
		menubar.add(file);
		menubar.add(help);
		menubar.add(new JTextField());
		toolbar.add(new JButton("Ex"));
		add(toolbar,BorderLayout.WEST);
		add(pane,BorderLayout.CENTER);
		add(status,BorderLayout.SOUTH);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	public static void main(String[] args){
		new Emacs();
	}
}
