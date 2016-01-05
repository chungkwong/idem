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
public class Cut extends ControlConstruct{
	public static Cut CUT=new Cut();
	private Cut(){}
	private static final Predicate pred=new Predicate("!",0);
	@Override
	public void firstexecute(Processor exec){
		ExecutionState ccs=exec.getCurrentState();
		exec.getCurrentDecoratedSubgoal().setActivator(new Atom("true"));
		exec.getStack().push(ccs);
	}
	@Override
	public void reexecute(Processor exec){
		ExecutionState cut=exec.getCurrentDecoratedSubgoal().getCutparent();
		do{
			exec.getStack().pop();
		}while(exec.getStack().peek()!=cut);
		exec.backtrack();
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
