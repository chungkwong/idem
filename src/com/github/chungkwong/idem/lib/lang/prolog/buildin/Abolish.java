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
public class Abolish extends BuildinPredicate{
	public static final Abolish INSTANCE=new Abolish();
	public static final Predicate pred=new Predicate("abolish",1);
	@Override
	public boolean activate(List<Term> argments,Processor exec){
		Term arg=argments.get(0);
		if(arg instanceof CompoundTerm){
			CompoundTerm proc=(CompoundTerm)arg;
			if(proc.getFunctor().equals("/")&&proc.getArguments().size()==2){
				Term functor=proc.getArguments().get(0),arity=proc.getArguments().get(1);
				if(functor instanceof Atom){
					if(arity instanceof Atom&&((Atom)arity).getValue()instanceof BigInteger){
						exec.getDatabase().removeProcedure(new Predicate(((Atom)functor).getValue(),((BigInteger)((Atom)arity).getValue()).intValueExact()));
						return true;
					}else if(arity instanceof Variable)
						throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)arity);
					else
						throw new TypeException("integer",arity);
				}else if(functor instanceof Variable)
					throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)functor);
				else
					throw new TypeException("atom",arity);
			}else
				throw new DomainException(null,arg);
		}else if(arg instanceof Variable){
			throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)arg);
		}else
			return true;
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}

