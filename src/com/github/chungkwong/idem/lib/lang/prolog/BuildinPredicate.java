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
 * Buildin Predicate which is not reexecutable
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public abstract class BuildinPredicate implements Procedure{
	/**
	 * Activate a goal
	 * @param arguments the arguments of the goal
	 * @param exec the Prolog processor
	 * @return the goal succeed or not
	 */
	public abstract boolean activate(List<Term> arguments,Processor exec);
	@Override
	public void execute(Processor exec){
		exec.getStack().peek().setBI(ExecutionState.BacktraceInfo.BIP);
		if(activate(exec.getCurrentActivator().getArguments(),exec)){
			exec.getCurrentDecoratedSubgoal().setActivator(new Constant("true"));
		}else{
			exec.getCurrentDecoratedSubgoal().setActivator(new Constant("fail"));
		}
	}
	@Override
	public void reexecute(Processor exec){
		assert false;
	}
	@Override
	public String toString(){
		return getPredicate().toString();
	}
}