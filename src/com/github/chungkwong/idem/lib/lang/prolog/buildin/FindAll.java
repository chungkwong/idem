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
public class FindAll extends BuildinPredicate{
	public static final FindAll INSTANCE=new FindAll();
	public static final Predicate pred=new Predicate("findall",3);
	private static final Atom EMPTY_LIST=new Atom(Collections.EMPTY_LIST);
	@Override
	public boolean activate(List<Term> argments,Processor exec){
		Term result=EMPTY_LIST;
		Predication goal=new CompoundTerm("call",Collections.singletonList(argments.get(1)));
		Processor processor=new Processor(goal,exec.getDatabase());
		while(processor.isSuccessed()){
			result=new CompoundTerm(".",Arrays.asList(argments.get(0).substitute(processor.getSubstitution()),result));
			processor.reexecute();
		}
		return argments.get(2).unities(result,exec.getStack().peek().getSubst());
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
