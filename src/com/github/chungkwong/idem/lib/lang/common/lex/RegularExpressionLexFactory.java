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
import com.github.chungkwong.idem.util.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class RegularExpressionLexFactory implements LexFactory{
	private final ArrayList<String> tokenType=new ArrayList<>();
	private final NFA machine=new NFA();
	public RegularExpressionLexFactory(){
	}
	public void addTokenType(String type,String regex){
		tokenType.add(type);
		NFA child=RegularExpression.parseRegularExpression(regex).toNFA();
		machine.getInitState().addLambdaTransition(child.getInitState());
		child.getAcceptState().addLambdaTransition(machine.getAcceptState());
		child.getAcceptState().addLambdaTransition(new NFA.TaggedState(type));
	}
	@Override
	public String[] getAllTokenType(){
		return tokenType.toArray(new String[0]);
	}
	@Override
	public Lex createLex(IntCheckPointIterator src){
		return new RegularExpressionLex(src);
	}
	private class RegularExpressionLex implements Lex{
		private final IntCheckPointIterator src;
		public RegularExpressionLex(IntCheckPointIterator src){
			this.src=src;
		}
		@Override
		public Token get(){
			Pair<NFA.StateSet,String> pair=machine.run(src);
			return new SimpleToken(pair.getSecond(),pair.getSecond(),pair.getFirst().getTag().toString());
		}
	}
}