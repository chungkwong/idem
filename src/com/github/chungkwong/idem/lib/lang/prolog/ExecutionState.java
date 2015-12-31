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
package com.github.chungkwong.idem.lib.lang.prolog;
import com.github.chungkwong.idem.lib.*;
import java.util.*;
/**
 *
 * @author kwong
 */
public class ExecutionState{
	private Stack<DecoratedSubgoal> decsglstk;
	private BacktraceInfo BI;
	private Substitution subst;
	private PeekableIterator<Clause> cl;
	public ExecutionState(){
		decsglstk=new Stack<>();
		subst=new Substitution();
		BI=BacktraceInfo.NIL;
		cl=null;
	}
	public ExecutionState(DecoratedSubgoal goal,Substitution subst){
		decsglstk=new Stack<>();
		decsglstk.push(goal);
		this.subst=subst;
		BI=BacktraceInfo.NIL;
		cl=null;
	}
	public ExecutionState(ExecutionState state){
		this.decsglstk=(Stack)state.decsglstk.clone();
		this.BI=state.BI;
		this.subst=state.subst;
		this.cl=state.cl;
	}
	public String toString(){
		StringBuilder buf=new StringBuilder("    ");
		buf.append(getDecsglstk()).append(',').append(getBI()).append(',').append(getSubst());
		return buf.toString();
	}
	public Stack<DecoratedSubgoal> getDecsglstk(){
		return decsglstk;
	}
	public void setDecsglstk(Stack<DecoratedSubgoal> decsglstk){
		this.decsglstk=decsglstk;
	}
	public BacktraceInfo getBI(){
		return BI;
	}
	public void setBI(BacktraceInfo BI){
		this.BI=BI;
		if(BI==BacktraceInfo.UP)
			cl=PeekableIterator.EMPTY;
		else
			cl=null;
	}
	public Substitution getSubst(){
		return subst;
	}
	public void setSubst(Substitution subst){
		this.subst=subst;
	}
	public PeekableIterator<Clause> getCl(){
		return cl;
	}
	public void setCl(PeekableIterator<Clause> cl){
		if(BI==BacktraceInfo.UP)
			this.cl=cl;
	}
	public static enum BacktraceInfo{
		NIL,CTRL,BIP,UP;
	}
	public static class DecoratedSubgoal{
		private final Predication activator;
		private final ExecutionState cutparent;
		public DecoratedSubgoal(Predication activator,ExecutionState cutparent){
			this.activator=activator;
			this.cutparent=cutparent;
		}
		@Override
		public String toString(){
			return getActivator().toString();
		}
		public Predication getActivator(){
			return activator;
		}
		public ExecutionState getCutparent(){
			return cutparent;
		}
	}
}