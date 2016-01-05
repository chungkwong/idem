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
package com.github.chungkwong.idem.lib.lang.prolog;
import com.github.chungkwong.swingconsole.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author kwong
 */
public class PrologConsole implements Shell{
	Database db=new Database();
	String result;
	public boolean acceptLine(String line){
		throw new UnsupportedOperationException();
	}
	public String evaluate(){
		return result;
	}
	public java.util.List<String> getHints(String prefix){
		ArrayList<String> lst=new ArrayList<String>();

		return lst;
	}
	public static void main(String[] args) throws Exception{
		JFrame f=new JFrame("Console");
		f.add(new SwingConsole(new PrologConsole()),BorderLayout.CENTER);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}