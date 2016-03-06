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
public class Retract extends ReexecutableBuildinPredicate{
	public static final Retract INSTANCE=new Retract();
	public static final Predicate pred=new Predicate("retract",1);
	@Override
	public void firstActivate(List<Term> arguments,Processor exec,Variable var){

	}
	@Override
	public boolean againActivate(List<Term> arguments,Processor exec,Variable var){
		Term clause=arguments.get(0);
		Term head=null,body=null;
		if(clause instanceof CompoundTerm&&((CompoundTerm)clause).getFunctor().equals(":-")
				&&((CompoundTerm)clause).getArguments().size()==2){
			head=((CompoundTerm)clause).getArguments().get(0);
			body=((CompoundTerm)clause).getArguments().get(1);
		}else{
			head=clause;
			body=new Constant("true");
		}
		if(head instanceof Variable)
			throw new InstantiationException((Variable)head);
		else if(head instanceof Constant&&!(((Constant)head).getValue()instanceof String))
			throw new TypeException("callable",head);
		Predicate predicate=((Predication)head).getPredicate();
		Procedure proc=exec.getDatabase().getProcedure(predicate);
		if(proc!=null){
			if(proc.isDynamic()){
				Iterator<Clause> iter=((UserPredicate)proc).getClauses().iterator();
				while(iter.hasNext()){
					Clause cand=iter.next();
					Substitution subst=new Substitution(exec.getCurrentSubst());
					if(head.unities(cand.getHeadAsTerm(),subst)&&body.unities(cand.getBodyAsTerm(),subst)){
						iter.remove();
						return true;
					}
				}
			}else{
				throw new PermissionException(new Constant("modify_clause")
						,new Constant("static_procedure"),head);
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