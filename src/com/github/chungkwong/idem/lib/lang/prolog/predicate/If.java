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
public class If  extends ControlConstruct{
	public static final If IF=new If();
	private If(){}
	private static final Predicate pred=new Predicate("->",2);
	@Override
	public void firstexecute(Processor exec){
		ExecutionState ccs2=new ExecutionState(exec.getStack().peek());
		ccs2.setBI(ExecutionState.BacktraceInfo.NIL);
		ccs2.getDecsglstk().peek().setActivator(new Atom("or"));
		exec.getStack().push(ccs2);
		ExecutionState ccs1=new ExecutionState(exec.getStack().peek());
		ccs1.setBI(ExecutionState.BacktraceInfo.NIL);
		ccs1.getDecsglstk().peek().setActivator(new Atom("either"));
		exec.getStack().push(ccs1);
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