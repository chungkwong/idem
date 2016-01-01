/*
 * Copyright (C) 2015 kwong
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
package com.github.chungkwong.idem.lib.lang.prolog.predicate;
import com.github.chungkwong.idem.lib.lang.prolog.*;
/**
 *
 * @author kwong
 */
public class Disjunction extends ControlConstruct{
	public static Disjunction DISJUNCTION=new Disjunction();
	private Disjunction(){}
	private static final Predicate pred=new Predicate(";",2);
	@Override
	public void firstexecute(Processor exec){
		ExecutionState ccs=new ExecutionState(exec.getCurrentState());
		ccs.setBI(ExecutionState.BacktraceInfo.NIL);
		ExecutionState.DecoratedSubgoal currdecsgl=ccs.getDecsglstk().pop();
		ExecutionState checkpoint=exec.getStack().get(exec.getStack().size()-2);
		ccs.getDecsglstk().push(new ExecutionState.DecoratedSubgoal(
				(Predication)currdecsgl.getActivator().getArguments().get(1),currdecsgl.getCutparent()));
		ccs.getDecsglstk().push(new ExecutionState.DecoratedSubgoal(new Atom("!"),checkpoint));
		ccs.getDecsglstk().push(new ExecutionState.DecoratedSubgoal(
				(Predication)currdecsgl.getActivator().getArguments().get(0),checkpoint));
		exec.getStack().push(ccs);
	}
	@Override
	public void reexecute(Processor exec){
		exec.getStack().pop();
		exec.backtrack();
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
