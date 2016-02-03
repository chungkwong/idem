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
public class Retract extends BuildinPredicate{
	public static final AssertA INSTANCE=new AssertA();
	public static final Predicate pred=new Predicate("asserta",1);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term clause=arguments.get(0);
		Term head=null,body=null;
		if(clause instanceof CompoundTerm&&((CompoundTerm)clause).getFunctor().equals(":-")
				&&((CompoundTerm)clause).getArguments().size()==2){
			head=((CompoundTerm)clause).getArguments().get(0);
			body=((CompoundTerm)clause).getArguments().get(1);
		}else{
			head=clause;
			body=new Atom("true");
		}
		Procedure proc=exec.getDatabase().getProcedure(((Predication)head).getPredicate());
		if(proc!=null&&proc instanceof UserPredicate){
			Iterator<Clause> iter=((UserPredicate)proc).getClauses().iterator();
			while(iter.hasNext()){
				Clause cand=iter.next();
				Substitution subst=new Substitution(exec.getStack().peek().getSubst());
				if(head.unities(cand.getHeadAsTerm(),subst)&&body.unities(cand.getBodyAsTerm(),subst)){
					iter.remove();
					return true;
				}
			}
			return false;
		}else
			return false;
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}