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
		Predication disjunction=exec.getCurrentActivator();
		if(isIf(disjunction)){
			pushIfThen(disjunction,exec);
		}else{
			pushCase(disjunction.getArguments().get(1).toBody(),exec);
			pushCase(disjunction.getArguments().get(0).toBody(),exec);
		}
	}
	private boolean isIf(Predication pred){
		Term first=pred.getArguments().get(0);
		return first instanceof Predication&&((Predication)first).getPredicate().equals(If.IF.getPredicate());
	}
	private void pushCase(Predication pred,Processor exec){
		ExecutionState ccs=new ExecutionState(exec.getStack().peek());
		ccs.setBI(ExecutionState.BacktraceInfo.NIL);
		ccs.getDecsglstk().peek().setActivator(pred);
		exec.getStack().push(ccs);
	}
	private void pushIfThen(Predication pred,Processor exec){
		ExecutionState ccs=new ExecutionState(exec.getStack().peek());
		ccs.setBI(ExecutionState.BacktraceInfo.NIL);
		ExecutionState checkpoint=exec.getStack().get(exec.getStack().size()-2);
		pred=((Predication)pred.getArguments().get(0));
		ccs.getDecsglstk().peek().setActivator((Predication)pred.getArguments().get(1));
		ccs.getDecsglstk().push(new DecoratedSubgoal(new Atom("!"),checkpoint));
		ccs.getDecsglstk().push(new DecoratedSubgoal((Predication)pred.getArguments().get(0),exec.getStack().peek()));
		exec.getStack().push(ccs);
	}
	private void pushElse(Processor exec){
		ExecutionState ccs=new ExecutionState(exec.getStack().peek());
		ccs.setBI(ExecutionState.BacktraceInfo.NIL);
		DecoratedSubgoal currdecsgl=ccs.getDecsglstk().pop();
		ExecutionState checkpoint=exec.getStack().get(exec.getStack().size()-2);
		ccs.getDecsglstk().push(new DecoratedSubgoal(
				(Predication)currdecsgl.getActivator().getArguments().get(1),currdecsgl.getCutparent()));
		ccs.getDecsglstk().push(new DecoratedSubgoal(new Atom("!"),checkpoint));
		exec.getStack().push(ccs);
	}
	@Override
	public void reexecute(Processor exec){
		if(isIf(exec.getCurrentActivator())){
			pushElse(exec);
		}else{
			exec.getStack().pop();
			exec.backtrack();
		}
	}

	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
