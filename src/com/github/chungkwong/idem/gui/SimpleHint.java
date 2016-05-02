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
import java.io.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class SimpleHint implements Hint{
	private final Icon icon;
	private final String display;
	private final String input;
	private final String document;
	public SimpleHint(String display,String input,Icon icon,String document){
		this.icon=icon;
		this.display=display;
		this.input=input;
		this.document=document;
	}
	public SimpleHint(String text,Icon icon,String document){
		this(text,text,icon,document);
	}
	@Override
	public String getDisplayText(){
		return display;
	}
	@Override
	public String getInputText(){
		return input;
	}
	@Override
	public Reader getDocument(){
		return new StringReader(document);
	}
	@Override
	public Icon getIcon(){
		return icon;
	}
}