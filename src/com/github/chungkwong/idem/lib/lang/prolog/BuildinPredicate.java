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
package com.github.chungkwong.idem.lib.lang.prolog;
import java.util.*;
/**
 *
 * @author kwong
 */
public abstract class BuildinPredicate implements Procedure{
	public abstract boolean activate(List<Term> argments,Processor exec);
	@Override
	public void execute(Processor exec){
		exec.getStack().peek().setBI(ExecutionState.BacktraceInfo.BIP);
		ExecutionState ccs=new ExecutionState(exec.getCurrentState());
		exec.getStack().push(ccs);

		if(activate(exec.getCurrentActivator().getArguments(),exec)){
			ccs.getDecsglstk().peek().setActivator(new Atom("true"));
		}else{
			ccs.getDecsglstk().peek().setActivator(new Atom("fail"));
		}
	}
	@Override
	public void reexecute(Processor exec){

	}
	@Override
	public String toString(){
		return getPredicate().toString();
	}
}