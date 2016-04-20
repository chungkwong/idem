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
package com.github.chungkwong.idem.loader;
import com.github.chungkwong.idem.global.*;
import com.github.chungkwong.idem.gui.*;
import com.github.chungkwong.idem.gui.widget.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class HexData implements DataObject{
	private Document doc;
	private Object src;
	public HexData(){
		doc=new PlainDocument();
	}
	public HexData(InputStream in,Object src){
		doc=new PlainDocument();
		this.src=src;
		byte[] buf=new byte[4096];
		int count;
		try{
			count=in.read(buf);
			while(count!=-1){
				doc.insertString(doc.getLength(),toString(buf,0,count),null);
				count=in.read(buf);
			}
		}catch(IOException|BadLocationException ex){
			Log.LOG.throwing("HexData","<init>",ex);
		}
	}
	@Override
	public String getDescription(){
		return "Hex";
	}
	@Override
	public JComponent createDefaultView(){
		return new HexEditor(doc);
	}
	public static String toString(byte[] data){
		return toString(data,0,data.length);
	}
	public static String toString(byte[] data,int offset,int size){
		StringBuilder buf=new StringBuilder();
		for(int i=offset;i<size;i++){
			byte b=data[i];
			buf.append(NUMBER2HEX[b>>>4]);
			buf.append(NUMBER2HEX[b&0x0F]);
			buf.append(' ');
		}
		return buf.toString();
	}
	private static final char[] NUMBER2HEX=new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
}
