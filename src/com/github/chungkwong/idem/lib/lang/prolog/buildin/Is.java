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
import com.github.chungkwong.idem.lib.lang.prolog.eval.*;
import java.util.*;
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Is extends BuildinPredicate{
	public static final Is INSTANCE=new Is();
	public static final Predicate pred=new Predicate("is",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		return arguments.get(0).unities(evaluate(arguments.get(1)),exec.getCurrentSubst());
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	private Term evaluate(Term t){
		if(t instanceof Constant){
			return t;
		}else if(t instanceof CompoundTerm){
			CompoundTerm ct=(CompoundTerm)t;
			Evaluable evaluable=EvaluableFunctorTable.getEvaluableFunctor(ct.getFunctor().toString(),ct.getArguments().size());
			if(evaluable==null)
				throw new SystemException("Evaluable functor not found");
			else
				return evaluable.evaluate(ct.getArguments().stream().map((e)->evaluate(e)).collect(Collectors.toList()));
		}else{
			throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)t);
		}
	}
}