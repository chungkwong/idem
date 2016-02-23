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
public class ClauseOf extends ReexecutableBuildinPredicate{
	public static final ClauseOf INSTANCE=new ClauseOf();
	public static final Predicate pred=new Predicate("clause",2);
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	@Override
	public void firstActivate(List<Term> arguments,Processor exec,Variable var){
		if(!(arguments.get(0)instanceof Predication))
			throw new TypeException("callable",arguments.get(0));
		Procedure proc=exec.getDatabase().getProcedure(((Predication)arguments.get(0)).getPredicate());
		if(proc!=null&&!(proc instanceof UserPredicate))
			throw new PermissionException(new Constant("access_clause"),new Constant("static_procedure"),exec.getCurrentActivator());
		Term lst=Lists.EMPTY_LIST;
		if(proc!=null){
			for(Clause clause:((UserPredicate)proc).getClauses()){
				Substitution subst=new Substitution(exec.getCurrentSubst());
				if(clause.getHeadAsTerm().unities(arguments.get(0),subst)&&clause.getBodyAsTerm().unities(arguments.get(1),subst))
					lst=new CompoundTerm(".",new CompoundTerm("clause",clause.getHeadAsTerm(),clause.getBodyAsTerm()),lst);
			}
		}
		exec.getCurrentSubst().assign(var,lst);
	}
	@Override
	public boolean againActivate(List<Term> arguments,Processor exec,Variable var){
		Substitution subst=exec.getStack().get(exec.getStack().size()-2).getSubst();
		Term lst=subst.findRoot(var);
		if(lst instanceof CompoundTerm){
			CompoundTerm clause=(CompoundTerm)((CompoundTerm)lst).getArguments().get(0);
			arguments.get(0).unities(clause.getArguments().get(0),exec.getCurrentSubst());
			arguments.get(1).unities(clause.getArguments().get(1),exec.getCurrentSubst());
			subst.unassign(var);
			subst.assign(var,((CompoundTerm)lst).getArguments().get(1));
			return true;
		}else{
			return false;
		}
	}
}