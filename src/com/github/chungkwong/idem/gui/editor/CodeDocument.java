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
public class CodeDocument extends AbstractDocument{
	private Element root;
	public CodeDocument(ContextFreeGrammar grammar){
		super(new GapContent());
		root=new NonTerminalInstance((NonTerminal)grammar.getStartSymbol(),null,null);
	}
	@Override
	public Element getDefaultRootElement(){
		return root;
	}
	@Override
	public Element getParagraphElement(int arg0){
		return getDefaultRootElement();
	}
	class TerminalInstance extends AbstractDocument.LeafElement implements SymbolInstance{
		Terminal symbol;
		public TerminalInstance(Terminal symbol,Element arg0,AttributeSet arg1,int arg2,int arg3){
			super(arg0,arg1,arg2,arg3);
			this.symbol=symbol;
		}
		@Override
		public Symbol getSymbol(){
			return symbol;
		}
	}
	class NonTerminalInstance extends AbstractDocument.BranchElement implements SymbolInstance{
		NonTerminal symbol;
		public NonTerminalInstance(NonTerminal symbol,Element arg0,AttributeSet arg1){
			super(arg0,arg1);
			this.symbol=symbol;
		}
		@Override
		public Symbol getSymbol(){
			return symbol;
		}
	}
}