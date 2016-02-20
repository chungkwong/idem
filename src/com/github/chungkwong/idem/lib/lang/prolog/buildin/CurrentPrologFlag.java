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
public class CurrentPrologFlag extends ReexecutableBuildinPredicate{
	public static final CurrentPrologFlag INSTANCE=new CurrentPrologFlag();
	public static final Predicate pred=new Predicate("current_prolog_flag",2);
	@Override
	public void firstActivate(List<Term> argments,Processor exec,Variable var){
		Term flag=argments.get(0),val=argments.get(1);
		Term lst=new Atom(Collections.EMPTY_LIST);
		if(flag instanceof Variable){
			for(Flag f:exec.getDatabase().getFlags().values()){
				if(f.getValue().unities(val,new Substitution(exec.getCurrentSubst())))
					lst=new CompoundTerm(".",Arrays.asList(new CompoundTerm("flag",Arrays.asList(new Atom(f.getName()),f.getValue())),lst));
			}
		}else if(flag instanceof Atom){
			Flag f=exec.getDatabase().getFlag(((Atom)flag).toString());
			if(f.getValue().unities(val,new Substitution(exec.getCurrentSubst())))
				lst=new CompoundTerm(".",Arrays.asList(new CompoundTerm("flag",Arrays.asList(flag,f.getValue())),lst));
		}else{
			throw new TypeException("atom",flag);
		}
		exec.getCurrentSubst().assign(var,lst);
	}
	@Override
	public boolean againActivate(List<Term> argments,Processor exec,Variable var){
		Term flag=argments.get(0),val=argments.get(1);
		Substitution subst=exec.getStack().get(exec.getStack().size()-2).getSubst();
		Term lst=subst.findRoot(var);
		if(lst instanceof CompoundTerm){
			CompoundTerm entry=(CompoundTerm)((CompoundTerm)lst).getArguments().get(0);
			argments.get(0).unities(entry.getArguments().get(0),exec.getCurrentSubst());
			argments.get(1).unities(entry.getArguments().get(1),exec.getCurrentSubst());
			subst.unassign(var);
			subst.assign(var,((CompoundTerm)lst).getArguments().get(1));
			return true;
		}else{
			return false;
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}