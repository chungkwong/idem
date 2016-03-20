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
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class True extends ControlConstruct{
	public static final True TRUE=new True();
	private True(){}
	private static final Predicate pred=new Predicate("true",0);
	@Override
	public void firstexecute(Processor exec){
		exec.getStack().peek().getDecsglstk().pop();
		exec.getStack().peek().setBI(ExecutionState.BacktraceInfo.NIL);
	}
	@Override
	public void reexecute(Processor exec){}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}
