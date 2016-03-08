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
public class BagOf extends ReexecutableBuildinPredicate{
	public static final BagOf INSTANCE=new BagOf();
	public static final Predicate pred=new Predicate("bagof",3);
	private static final Constant EMPTY_LIST=Lists.EMPTY_LIST;
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	@Override
	public void firstActivate(List<Term> arguments,Processor exec,Variable var){
		Set<Variable> freevars=getFreeVariableSet(arguments.get(1),arguments.get(0));
		Term witness=new CompoundTerm("witness",new ArrayList<>(freevars));
		Variable lst=Variable.InternalVariable.newInstance();
		Predication findall=new CompoundTerm("findall",new CompoundTerm("+",witness,arguments.get(0)),
				arguments.get(1).toIteratedTerm(),lst);
		Term result=FindAll.INSTANCE.activate(findall.getArguments(),exec)?exec.getCurrentSubst().findRoot(lst):EMPTY_LIST;
		exec.getCurrentSubst().assign(var,result);
	}
	@Override
	public boolean againActivate(List<Term> arguments,Processor exec,Variable var){
		Substitution subst=exec.getStack().get(exec.getStack().size()-2).getSubst();
		Term s=subst.findRoot(var);
		while(!s.equals(EMPTY_LIST)){
			CompoundTerm wt=(CompoundTerm)Lists.head(s);
			Term tLst=EMPTY_LIST,next=EMPTY_LIST,iter=s;
			while(!iter.equals(EMPTY_LIST)){
				CompoundTerm wwtt=(CompoundTerm)Lists.head(iter);
				if(wwtt.getArguments().get(0).isVariantOf(wt.getArguments().get(0))){
					tLst=new CompoundTerm(".",wwtt.getArguments().get(1),tLst);
				}else{
					next=new CompoundTerm(".",wwtt,next);
				}
				iter=Lists.tail(iter);
			}
			s=Lists.reverse(next);
			if(arguments.get(2).unities(Lists.reverse(tLst),exec.getCurrentSubst())){
				subst.unassign(var);
				subst.assign(var,s);
				return true;
			}
		}
		return false;
	}
	private Set<Variable> getFreeVariableSet(Term term,Term respect){
		Set<Variable> freevars=term.getVariableSet();
		freevars.removeAll(term.getExistentialVariableSet());
		freevars.removeAll(respect.getVariableSet());
		return freevars;
	}
}