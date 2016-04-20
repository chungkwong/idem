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
package com.github.chungkwong.idem.gui.widget;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class HexEditor extends JPanel{
	private final JTextArea area;
	public HexEditor(Document doc){
		setLayout(new BorderLayout());
		area=new JTextArea(doc);
		area.setWrapStyleWord(true);
		add(area,BorderLayout.CENTER);
	}
	public byte[] getContent(){
		char[] str=area.getText().toCharArray();
		int count=str.length*2/3;
		byte[] buf=new byte[count];
		for(int i=0,j=0;j<count;i+=3,j++){
			buf[j]=(byte)((Character.getNumericValue(str[i])<<4)|Character.getNumericValue(str[i+1]));
		}
		return buf;
	}
}
