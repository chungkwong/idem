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
import com.github.chungkwong.idem.util.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class RegularExpressionLexFactory implements LexFactory{
	private final ArrayList<String> tokenType=new ArrayList<>();
	private final NFA machine=new NFA();
	private boolean changed=true;
	public RegularExpressionLexFactory(){
	}
	public void addTokenType(String type,String regex){
		changed=true;
		tokenType.add(type);
		NFA child=RegularExpression.parseRegularExpression(regex).toNFA();
		machine.getInitState().addLambdaTransition(child.getInitState());
		child.getAcceptState().addLambdaTransition(machine.getAcceptState());
		child.getAcceptState().addLambdaTransition(new NFA.TaggedState(new Terminal(type)));
	}
	@Override
	public String[] getAllTokenType(){
		return tokenType.toArray(new String[0]);
	}
	@Override
	public Lex createLex(IntCheckPointIterator src){
		if(changed){
			machine.prepareForRun();
			changed=false;
		}
		return new RegularExpressionLex(src);
	}
	private class RegularExpressionLex implements Lex{
		private final IntCheckPointIterator src;
		public RegularExpressionLex(IntCheckPointIterator src){
			this.src=src;
		}
		@Override
		public Token next(){
			Pair<NFA.StateSet,String> pair=machine.run(src);
			if(pair.getFirst()!=null)
				return new SimpleToken(pair.getSecond(),pair.getSecond(),pair.getFirst().getTag());
			else
				return null;
		}
		@Override
		public boolean hasNext(){
			return src.hasNext();
		}
	}
	public static void main(String[] args){
		RegularExpressionLexFactory factory=new RegularExpressionLexFactory();
		factory.addTokenType("NUMBER","[0-9]+");
		factory.addTokenType("WORD","[a-zA-Z]+");
		factory.addTokenType("OTHER","[^0-9a-zA-Z]");
		Lex lex=factory.createLex(new IntCheckPointIterator("fe2672j-=".codePoints().iterator()));
		Token t;
		while(lex.hasNext()){
			System.out.println(lex.next());
		}
	}
}