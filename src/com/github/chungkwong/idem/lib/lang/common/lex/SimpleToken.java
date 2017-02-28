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
package com.github.chungkwong.idem.lib.lang.common.lex;
import com.github.chungkwong.idem.lib.lang.common.parser.*;

public class SimpleToken implements Token{
	private final String text;
	private final Object val;
	private final Terminal type;
	public SimpleToken(String text,Object val,Terminal type){
		this.text=text;
		this.val=val;
		this.type=type;
	}
	@Override
	public String getText(){
		return text;
	}
	@Override
	public Object getValue(){
		return val;
	}
	@Override
	public Terminal getType(){
		return type;
	}
	@Override
	public String toString(){
		return type+":"+val+"("+text+")"; //To change body of generated methods, choose Tools | Templates.
	}

}
