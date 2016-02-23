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
import com.github.chungkwong.idem.lib.lang.prolog.InstantiationException;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class AtomConcat extends BuildinPredicate{
	public static final Arg INSTANCE=new Arg();
	public static final Predicate pred=new Predicate("arg",3);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term atom1=arguments.get(0),atom2=arguments.get(1),atom12=arguments.get(2);
		if(atom12 instanceof Variable){
			if(atom1 instanceof Variable||atom2 instanceof Variable)
				throw new InstantiationException((Variable)atom12);
			else if(!isAtom(atom1)){
				throw new TypeException("atom",atom1);
			}else if(!isAtom(atom2)){
				throw new TypeException("atom",atom2);
			}else{
				return atom12.unities(new Constant(((Constant)atom1).getValue().toString()+((Constant)atom2).getValue().toString()),exec.getCurrentSubst());
			}
		}else if(isAtom(atom12)){
			if(isAtom(atom1)){
				if(isAtom(atom2))
					return atom12.toString().equals(atom1.toString()+atom2.toString());
				else if(atom2 instanceof Variable){
					return atom12.toString().startsWith(atom1.toString())
							&&atom2.unities(new Constant(atom12.toString().substring(atom1.toString().length())),exec.getCurrentSubst());
				}else
					throw new TypeException("atom",atom2);
			}else if(atom1 instanceof Variable){
				if(isAtom(atom2))
					return atom12.toString().endsWith(atom2.toString())
							&&atom1.unities(new Constant(atom12.toString().substring(0,atom1.toString().length())),exec.getCurrentSubst());
				else if(atom2 instanceof Variable){
					//FIXME
					return atom1.unities(atom12,exec.getCurrentSubst())&&atom2.unities(new Constant(""),exec.getCurrentSubst());
				}else
					throw new TypeException("atom",atom2);
			}else{
				throw new TypeException("atom",atom1);
			}
		}else{
			throw new TypeException("atom",atom12);
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	private static boolean isAtom(Term t){
		return t instanceof Constant&&((Constant)t).getValue()instanceof String;
	}
}