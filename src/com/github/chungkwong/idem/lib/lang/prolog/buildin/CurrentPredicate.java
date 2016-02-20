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
public class CurrentPredicate extends ReexecutableBuildinPredicate{
	public static final CurrentPredicate INSTANCE=new CurrentPredicate();
	public static final Predicate pred=new Predicate("current_predicate",1);
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	@Override
	public void firstActivate(List<Term> argments,Processor exec,Variable var){
		Term PI=argments.get(0);
		Term lst=new Atom(Collections.EMPTY_LIST);
		for(Predicate pred:exec.getDatabase().getProcedures().keySet()){
			if(PI.unities(pred.getIndicator(),new Substitution(exec.getCurrentSubst())))
				lst=new CompoundTerm(".",Arrays.asList(pred.getIndicator(),lst));
		}
		exec.getCurrentSubst().assign(var,lst);
	}
	@Override
	public boolean againActivate(List<Term> argments,Processor exec,Variable var){
		Substitution subst=exec.getStack().get(exec.getStack().size()-2).getSubst();
		Term lst=subst.findRoot(var);
		if(lst instanceof CompoundTerm){
			argments.get(0).unities(((CompoundTerm)lst).getArguments().get(0),exec.getCurrentSubst());
			subst.unassign(var);
			subst.assign(var,((CompoundTerm)lst).getArguments().get(1));
			return true;
		}else{
			return false;
		}
	}
}