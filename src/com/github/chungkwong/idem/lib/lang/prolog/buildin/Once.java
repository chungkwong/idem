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
public class Once extends BuildinPredicate{
	public static final Once INSTANCE=new Once();
	public static final Predicate pred=new Predicate("once",1);
	@Override
	public boolean activate(List<Term> argments,Processor exec){
		Term term=argments.get(0);
		if(term instanceof CompoundTerm||(term instanceof Atom&&((Atom)term).getValue()instanceof String)){
			return (new Processor((Predication)term,exec.getDatabase()).isSuccessed());
		}else if(term instanceof Variable){
			throw new InstantiationException((Variable)term);
		}else{
			throw new TypeException("callable",term);
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}