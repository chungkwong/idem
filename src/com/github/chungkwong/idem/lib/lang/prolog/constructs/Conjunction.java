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
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Conjunction extends ControlConstruct{
	public static final Conjunction CONJUNCTION=new Conjunction();
	private Conjunction(){}
	private static final Predicate pred=new Predicate(",",2);
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	@Override
	public void firstexecute(Processor exec){
		List<Term> arguments=exec.getCurrentActivator().getArguments();
		ExecutionState ccs=new ExecutionState(exec.getStack().peek());
		ExecutionState cutparent=ccs.getDecsglstk().pop().getCutparent();
		ccs.getDecsglstk().push(new DecoratedSubgoal(arguments.get(1).toBody(),cutparent));
		ccs.getDecsglstk().push(new DecoratedSubgoal(arguments.get(0).toBody(),cutparent));
		ccs.setBI(ExecutionState.BacktraceInfo.NIL);
		exec.getStack().push(ccs);
		exec.selectClause();
	}
	@Override
	public void reexecute(Processor exec){
		exec.getStack().pop();
		exec.backtrack();
	}
}
