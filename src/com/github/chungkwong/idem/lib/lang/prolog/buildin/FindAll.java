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
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term result=Lists.EMPTY_LIST;
		Processor processor=new Processor(new CompoundTerm("call",arguments.get(1)),exec.getDatabase());
		while(processor.isSucceed()){
			result=new CompoundTerm(".",arguments.get(0).substitute(processor.getSubstitution()),result);
			processor.reexecute();
		}
		return arguments.get(2).unities(Lists.reverse(result),exec.getCurrentSubst());
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
