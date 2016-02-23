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
public class Univ extends BuildinPredicate{
	public static final Univ INSTANCE=new Univ();
	public static final Predicate pred=new Predicate("=..",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term term=arguments.get(0),list=arguments.get(1);
		if(term instanceof Constant){
			return list.unities(new CompoundTerm(".",term,Lists.EMPTY_LIST),exec.getCurrentSubst());
		}else if(term instanceof CompoundTerm){
			return list.unities(term,exec.getCurrentSubst());
		}else if(term instanceof Variable){
			if(Lists.isList(list)){
				if(Lists.length(list)==1){
					return ((CompoundTerm)list).getArguments().get(0).unities(term,exec.getCurrentSubst());
				}else{
					Term hd=Lists.head(list);
					if(hd instanceof Constant){
						return new CompoundTerm(((Constant)hd).getValue(),Lists.tail(list)).unities(term,exec.getCurrentSubst());
					}else if(hd instanceof CompoundTerm){
						throw new TypeException("atom",list);
					}else if(hd instanceof Variable){
						throw new InstantiationException((Variable)hd);
					}else{
						assert false;
						return false;
					}
				}
			}else if(list instanceof Variable){
				throw new InstantiationException((Variable)list);
			}else{
				return false;
			}
		}else{
			assert false;
			return false;
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}