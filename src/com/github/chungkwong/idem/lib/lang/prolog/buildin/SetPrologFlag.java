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
public class SetPrologFlag extends BuildinPredicate{
	public static final SetPrologFlag INSTANCE=new SetPrologFlag();
	public static final Predicate pred=new Predicate("set_prolog_flag",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term flag=arguments.get(0),val=arguments.get(1);
		if(flag instanceof Variable){
			throw new InstantiationException((Variable)flag);
		}else if(flag instanceof Constant){
			if(val instanceof Variable)
				throw new InstantiationException((Variable)val);
			else{
				Flag toset=exec.getDatabase().getFlag(((Constant)flag).toString());
				if(toset==null)
					throw new DomainException("prolog_flag",flag);
				else{
					toset.setValue(val);
					return true;
				}
			}
		}else{
			throw new TypeException("atom",flag);
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}