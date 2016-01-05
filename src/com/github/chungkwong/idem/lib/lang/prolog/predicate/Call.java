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
package com.github.chungkwong.idem.lib.lang.prolog.predicate;
import com.github.chungkwong.idem.lib.lang.prolog.*;
/**
 *
 * @author kwong
 */
public class Call extends ControlConstruct{
	public static Call CALL=new Call();
	private Call(){}
	private static final Predicate pred=new Predicate("call",1);
	@Override
	public void firstexecute(Processor exec){
		ExecutionState ccs=exec.getStack().peek();
		ccs.setBI(ExecutionState.BacktraceInfo.NIL);
		Term goal=ccs.getDecsglstk().pop().getActivator().getArguments().get(0);
		if(goal instanceof Variable){
			exec.raise(new com.github.chungkwong.idem.lib.lang.prolog.InstantiationException((Variable)goal));
		}
		ExecutionState cutparent=exec.getStack().get(exec.getStack().size()-2);
		ccs.getDecsglstk().push(new DecoratedSubgoal(goal.toBody(),cutparent));
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