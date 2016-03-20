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
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Abolish extends BuildinPredicate{
	public static final Abolish INSTANCE=new Abolish();
	public static final Predicate pred=new Predicate("abolish",1);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term arg=arguments.get(0);
		if(arg instanceof CompoundTerm){
			CompoundTerm proc=(CompoundTerm)arg;
			if(proc.getFunctor().equals("/")&&proc.getArguments().size()==2){
				Term functor=proc.getArguments().get(0),arity=proc.getArguments().get(1);
				exec.getDatabase().removeProcedure(new Predicate(Helper.getAtomValue(functor)
						,Helper.getIntegerValue(arity).intValueExact()));
				return true;
			}else
				throw new DomainException("predicate_indicator",arg);
		}else if(arg instanceof Variable){
			throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)arg);
		}else
			throw new TypeException("compound",arg);
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}

