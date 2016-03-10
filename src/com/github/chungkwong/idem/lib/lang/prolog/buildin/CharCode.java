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
import java.math.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CharCode extends BuildinPredicate{
	public static final CharCode INSTANCE=new CharCode();
	public static final Predicate pred=new Predicate("char_code",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term character=arguments.get(0),code=arguments.get(1);
		if(character instanceof Variable){
			if(code instanceof Variable)
				throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)character);
			else
				return character.unities(getCharacterCode(code),exec.getCurrentSubst());
		}else{
			if(code instanceof Variable)
				return getCharacter(character).unities(code,exec.getCurrentSubst());
			else
				return getCharacter(character).unities(getCharacterCode(code),exec.getCurrentSubst());
		}
	}
	private Term getCharacter(Term ch){
		try{
			String val=(String)((Constant)ch).getValue();
			if(val.codePointCount(0,val.length())==1)
				return ch;
			else
				throw new RuntimeException();
		}catch(RuntimeException ex){
			throw new RepresentationException("character_code",ch);
		}
	}
	private Term getCharacterCode(Term code){
		try{
			int val=((BigInteger)((Constant)code).getValue()).intValueExact();
			if(Character.isValidCodePoint(val))
				return new Constant(new String(new int[]{val},0,1));
			else
				throw new RuntimeException();
		}catch(RuntimeException ex){
			throw new RepresentationException("character_code",code);
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}