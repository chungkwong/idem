/*
 * Copyright (C) 2017 Chan Chung Kwong <1m02math@126.com>
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
package com.github.chungkwong.idem.gui.editor;
import com.github.chungkwong.idem.lib.lang.common.lex.*;
import java.util.*;
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class StyleScheme{
	private final HashMap<String,AttributeSet> style=new HashMap<>();
	private final NaiveLex lex=new NaiveLex();
	public void addTokenType(String type,String regex,AttributeSet set){
		lex.addType(type,regex);
		style.put(type,set);
	}
	public void updateStyle(StyledDocument doc){
		String text=null;
		try{
			text=doc.getText(0,doc.getLength());
		}catch(BadLocationException ex){

		}
		Iterator<Token> iter=lex.split(text);
		int index=0;
		while(iter.hasNext()){
			Token token=iter.next();
			doc.setCharacterAttributes(index,token.getText().length(),style.get(token.getType()),true);
			index+=token.getText().length();
		}
	}
}
