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
	public boolean activate(List<Term> arguments,Processor exec){
		Term term=arguments.get(0),name=arguments.get(1),arity=arguments.get(2);
		Substitution subst=exec.getCurrentSubst();
		if(term instanceof Constant){
			return name.unities(term,subst)&&arity.unities(new Constant(BigInteger.ZERO),subst);
		}else if(term instanceof CompoundTerm){
			return name.unities(new Constant(((CompoundTerm)term).getFunctor()),subst)&&
					arity.unities(new Constant(BigInteger.valueOf(((CompoundTerm)term).getArguments().size())),subst);
		}else{
			int n=Helper.getIntegerValue(arity).intValueExact();
			if(n>0){
				List<Term> args=new ArrayList(n);
				for(int i=0;i<n;i++)
					args.add(Variable.InternalVariable.newInstance());
				return term.unities(new CompoundTerm(Helper.getAtomValue(name),args),subst);
			}else if(n==0){
				return term.unities(name,subst);
			}else{
				throw new DomainException("not_less_than_zero",arity);
			}
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}