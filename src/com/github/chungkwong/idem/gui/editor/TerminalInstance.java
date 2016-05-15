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
package com.github.chungkwong.idem.gui.editor;
import javax.swing.text.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class TerminalInstance implements SymbolInstance{
	private Terminal symbol;
	private NonTerminalInstance parent;
	private String token;

	public TerminalInstance(Terminal symbol,String token){
		this.symbol=symbol;
		this.token=token;
	}
	@Override
	public Symbol getSymbol(){
		return symbol;
	}
	@Override
	public Document getDocument(){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	@Override
	public Element getParentElement(){
		return parent;
	}
	@Override
	public String getName(){
		return symbol.getName();
	}
	@Override
	public AttributeSet getAttributes(){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	@Override
	public int getStartOffset(){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	@Override
	public int getEndOffset(){
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	@Override
	public int getElementIndex(int offset){
		return 0;
	}
	@Override
	public int getElementCount(){
		return 0;
	}
	@Override
	public Element getElement(int index){
		return null;
	}
	@Override
	public boolean isLeaf(){
		return true;
	}

}
