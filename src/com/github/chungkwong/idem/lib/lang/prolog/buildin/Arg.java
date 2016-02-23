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
import java.math.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Arg extends BuildinPredicate{
	public static final Arg INSTANCE=new Arg();
	public static final Predicate pred=new Predicate("arg",3);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term n=arguments.get(0),term=arguments.get(1),arg=arguments.get(2);
		if(term instanceof CompoundTerm){
			if(n instanceof Constant&&((Constant)n).getValue()instanceof BigInteger){
				int i=((BigInteger)((Constant)n).getValue()).intValueExact()-1;
				return i>=0&&i<((CompoundTerm)term).getArguments().size()
						&&((CompoundTerm)term).getArguments().get(i).unities(arg,exec.getCurrentSubst());
			}else if(n instanceof Variable){
				throw new InstantiationException((Variable)n);
			}else{
				throw new TypeException("integer",n);
			}
		}else if(term instanceof Variable){
			throw new InstantiationException((Variable)term);
		}else{
			throw new TypeException("compound",term);
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
