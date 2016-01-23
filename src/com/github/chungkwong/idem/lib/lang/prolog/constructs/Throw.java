/*
 * Copyright (C) 2015 Chan Chung Kwong <1m02math@126.com>
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
package com.github.chungkwong.idem.lib.lang.prolog.constructs;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.util.*;
/**
 *
 * @author kwong
 */
public class Throw extends ControlConstruct{
	public static Throw THROW=new Throw();
	private Throw(){}
	private static final Predicate pred=new Predicate("throw",1);
	@Override
	public void firstexecute(Processor exec){
		Predication throwPred=exec.getCurrentActivator();
		ExecutionState cutparent=exec.getCurrentDecoratedSubgoal().getCutparent();
		Substitution subst=null;
		while(true){
			exec.getStack().pop();
			if(exec.getStack().size()<=1)
				throw new SystemException("Uncaught prolog error");
			if((subst=matchCatch(throwPred,cutparent,exec))!=null)
				break;
			cutparent=exec.getStack().peek().getDecsglstk().peek().getCutparent();
		}
		DecoratedSubgoal currdec=exec.getStack().peek().getDecsglstk().pop();
		Predication recover=new CompoundTerm("call",Collections.singletonList(currdec.getActivator().getArguments().get(2).substitute(subst)));
		exec.getStack().peek().getDecsglstk().push(new DecoratedSubgoal(recover,currdec.getCutparent()));
		exec.getStack().peek().setBI(ExecutionState.BacktraceInfo.NIL);
	}
	private Substitution matchCatch(Predication throwPred,ExecutionState cutparent,Processor exec){
		Predication curract=exec.getCurrentActivator();
		if(!curract.getPredicate().equals(Catch.CATCH.getPredicate()))
			return null;
		Substitution subst=new Substitution();
		if(!curract.getArguments().get(1).unities(throwPred.getArguments().get(0),subst))
			return null;
		if(exec.getStack().search(cutparent)>=exec.getStack().search(exec.getCurrentDecoratedSubgoal().getCutparent()))
			return null;
		return subst;
	}
	@Override
	public void reexecute(Processor exec){}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
