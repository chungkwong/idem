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
package com.github.chungkwong.idem.lib.lang.common.lex;
import com.github.chungkwong.idem.lib.lang.common.parser.*;
import com.github.chungkwong.idem.util.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class NaiveLex implements LexFactory{
	private final HashMap<Terminal,Pair<Pattern,Function<String,Object>>> types=new HashMap<>();
	public NaiveLex(){

	}
	@Override
	public void addTokenType(Terminal type,String regex,Function<String,Object> converter){
		types.put(type,new Pair<>(Pattern.compile(regex),converter));
	}
	public Iterator<Token> split(String text){
		return new TokenIterator(text);
	}
	@Override
	public Collection<Terminal> getAllTokenType(){
		return types.keySet();
	}
	@Override
	public Lex createLex(IntCheckPointIterator src){
		StringBuilder buf=new StringBuilder();
		while(src.hasNext())
			buf.appendCodePoint(src.nextInt());
		return new TokenIterator(buf.toString());
	}
	private class TokenIterator implements Lex{
		private final String text;
		private int index=0;
		private Token token;
		public TokenIterator(String text){
			this.text=text;
		}
		@Override
		public boolean hasNext(){
			if(token!=null)
				return true;
			for(Map.Entry<Terminal,Pair<Pattern,Function<String,Object>>> entry:types.entrySet()){
				Matcher matcher=entry.getValue().getFirst().matcher(text);
				matcher.region(index,text.length());
				if(matcher.lookingAt()){
					token=new SimpleToken(matcher.group(),entry.getValue().getSecond().apply(matcher.group()),entry.getKey());
					index=matcher.end();
					return true;
				}
			}
			return false;
		}
		@Override
		public Token next(){
			if(hasNext()){
				Token tmp=token;
				token=null;
				return tmp;
			}else{
				throw new NoSuchElementException();
			}
		}
	}
	public static void main(String[] args){
		NaiveLex lex=new NaiveLex();
		lex.addTokenType("A","[a-zA-Z]+");
		lex.addTokenType("B","[0-9]+");
		Iterator<Token> iter=lex.split("catdo54gdog7");
		while(iter.hasNext())
			System.out.println(iter.next());
	}
}