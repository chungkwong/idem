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
package com.github.chungkwong.idem.lib.lang.prolog.buildin;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.io.*;
import java.math.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class NumberChars extends BuildinPredicate{
	public static final NumberChars INSTANCE=new NumberChars();
	public static final Predicate pred=new Predicate("number_chars",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term number=arguments.get(0),list=arguments.get(1);
		if(Lists.isProperList(list)){
			if(!(number instanceof Variable||Helper.isNumber(number)))
				throw new TypeException("number",number);
			String strFromList=Lists.charListToString(list);
			try{
				List<Object> tokens=new PrologLex(strFromList).getRemainingTokens();
				if(tokens.size()==1&&(tokens.get(0) instanceof BigInteger||tokens.get(0) instanceof BigDecimal))
					return new Constant(tokens.get(0)).unities(number,exec.getCurrentSubst());
				else
					throw new ParseException();
			}catch(IOException|LexicalException ex){
				throw new ParseException();
			}
		}else if(list instanceof Variable){
			if(number instanceof Constant){
				Object val=((Constant)number).getValue();
				if(val instanceof BigInteger){
					return Lists.asCharacterList(((BigInteger)val).toString()).unities(list,exec.getCurrentSubst());
				}else if(val instanceof BigDecimal){
					return Lists.asCharacterList(((BigDecimal)val).toString()).unities(list,exec.getCurrentSubst());
				}else{
					throw new TypeException("number",number);
				}
			}else if(number instanceof Variable){
				throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)number);
			}else{
				throw new TypeException("number",number);
			}
		}else{
			throw new DomainException("character_list",list);
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
