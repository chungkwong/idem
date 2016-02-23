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
import java.util.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class HintedTextField extends JTextField implements FocusListener{
	private TreeMap<String,String> hints=new TreeMap<>();
	public HintedTextField(TreeMap<String,String> hints){
		this.hints=hints;
		addFocusListener(this);
		
	}
	public static void main(String[] args){
		JFrame f=new JFrame("Console");
		TreeMap<String,String> hints=new TreeMap<>();
		hints.put("ls","list file");
		hints.put("ps","list jobs");
		hints.put("pwd","working directory");
		f.add(new HintedTextField(hints),BorderLayout.CENTER);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	@Override
	public void focusGained(FocusEvent e){

	}
	@Override
	public void focusLost(FocusEvent e){

	}
}
