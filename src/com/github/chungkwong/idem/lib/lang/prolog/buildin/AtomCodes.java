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
public class AtomCodes extends BuildinPredicate{
	public static final AtomCodes INSTANCE=new AtomCodes();
	public static final Predicate pred=new Predicate("atom_codes",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term atom=arguments.get(0),list=arguments.get(1);
		if(Lists.isProperList(list)){
			if(!(atom instanceof Variable||Helper.isAtom(atom)))
				throw new TypeException("atom",atom);
			return new Constant(Lists.codeListToString(list)).unities(atom,exec.getCurrentSubst());
		}else if(list instanceof Variable){
			if(atom instanceof Constant&&((Constant)atom).getValue()instanceof String){
				return Lists.asCodeList((String)((Constant)atom).getValue()).unities(list,exec.getCurrentSubst());
			}else if(atom instanceof Variable){
				throw new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)atom);
			}else{
				throw new TypeException("atom",atom);
			}
		}else{
			throw new DomainException("character_code_list",list);
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}