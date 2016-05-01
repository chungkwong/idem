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
	private final String text;
	private final String document;
	public SimpleHint(String text,Icon icon,String document){
		this.icon=icon;
		this.text=text;
		this.document=document;
	}
	@Override
	public String getDisplayText(){
		return text;
	}
	@Override
	public String getInputText(){
		return text;
	}
	@Override
	public Reader getDocument(){
		return new StringReader(document);
	}
	@Override
	public Icon getIcon(){
		return icon;
	}
	@Override
	public String toString(){
		return text;
	}
}
