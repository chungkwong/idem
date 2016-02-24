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
import com.github.chungkwong.idem.lib.*;
import java.util.*;
/**
 * Execution state
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ExecutionState{
	private Stack<DecoratedSubgoal> decsglstk;
	private BacktraceInfo BI;
	private Substitution subst;
	private PeekableIterator<Clause> cl;
	/**
	 * Construct an execution state with empty decorated subgoal stack and substitution
	 */
	public ExecutionState(){
		decsglstk=new Stack<>();
		subst=new Substitution();
		BI=BacktraceInfo.NIL;
		cl=null;
	}
	/**
	 * Construct an execution state with a given decorated subgoal
	 * @param goal
	 */
	public ExecutionState(DecoratedSubgoal goal){
		this();
		decsglstk.push(goal);
	}
	/**
	 * Construct an execution state which is a clone of a given one
	 * @param state prototype
	 */
	public ExecutionState(ExecutionState state){
		decsglstk=new Stack<>();
		decsglstk.ensureCapacity(state.decsglstk.size());
		state.decsglstk.stream().forEach((subgoal)->{
			decsglstk.add(subgoal.clone());
		});
		this.BI=state.BI;
		this.subst=state.subst;
		this.cl=state.cl;
	}
	@Override
	public String toString(){
		StringBuilder buf=new StringBuilder("    ");
		buf.append(getDecsglstk()).append(',').append(getBI()).append(',').append(getSubst());
		return buf.toString();
	}
	/**
	 * @return the stack of decorated subgoals
	 */
	public Stack<DecoratedSubgoal> getDecsglstk(){
		return decsglstk;
	}
	/**
	 * Set the stack of decorated subgoals
	 * @param decsglstk
	 */
	public void setDecsglstk(Stack<DecoratedSubgoal> decsglstk){
		this.decsglstk=decsglstk;
	}
	/**
	 * @return backtrace information
	 */
	public BacktraceInfo getBI(){
		return BI;
	}
	/**
	 * Set backtrace information
	 * @param BI
	 */
	public void setBI(BacktraceInfo BI){
		this.BI=BI;
		if(BI==BacktraceInfo.UP)
			cl=PeekableIterator.EMPTY;
		else
			cl=null;
	}
	/**
	 * @return substitution
	 */
	public Substitution getSubst(){
		return subst;
	}
	/**
	 * Set substitution
	 * @param subst
	 */
	public void setSubst(Substitution subst){
		this.subst=subst;
	}
	/**
	 * @return list of clauses to be executed
	 */
	public PeekableIterator<Clause> getCl(){
		return cl;
	}
	/**
	 * Set list of clauses to be executed
	 * @param cl
	 */
	public void setCl(PeekableIterator<Clause> cl){
		if(BI==BacktraceInfo.UP)
			this.cl=cl;
	}
	/**
	 * Do substitution to all decorated subgoals
	 * @param subst
	 */
	void substitute(Substitution subst){
		decsglstk.stream().forEach((subgoal)->{
			subgoal.setActivator(subgoal.getActivator().substitute(subst));
		});
	}
	/**
	 * Possible value for backtrace information
	 */
	public static enum BacktraceInfo{
		NIL,CTRL,BIP,UP;
	}
}