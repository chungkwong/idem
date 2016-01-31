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
public class Functor extends BuildinPredicate{
	public static final Functor INSTANCE=new Functor();
	public static final Predicate pred=new Predicate("functor",3);
	@Override
	public boolean activate(List<Term> argments,Processor exec){
		Term term=argments.get(0);
		Substitution subst=exec.getStack().peek().getSubst();
		if(term instanceof Atom){
			return argments.get(1).unities(term,subst)&&argments.get(2).unities(new Atom(BigInteger.ZERO),subst);
		}else if(term instanceof CompoundTerm){
			return argments.get(1).unities(new Atom(((CompoundTerm)term).getFunctor()),subst)&&
					argments.get(2).unities(new Atom(BigInteger.valueOf(((CompoundTerm)term).getArguments().size())),subst);
		}else{
			if(argments.get(2))
				return term.unities(argments.get(1),subst);
			else{

			}
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}