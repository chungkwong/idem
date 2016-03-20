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
import static com.github.chungkwong.idem.global.Log.LOG;
import java.util.*;
import java.util.logging.*;
/**
 * A Prolog processor
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Processor{
	private final Stack<ExecutionState> stack=new Stack<>();
	private final Database db;
	/**
	 * Construct a Prolog processor and execute a goal
	 * @param goal the goal to be executed
	 * @param db the Prolog database
	 */
	public Processor(Predication goal,Database db){
		this.db=db;
		stack.push(new ExecutionState());
		stack.push(new ExecutionState(new DecoratedSubgoal(goal,stack.peek())));
		stack.peek().getDecsglstk();
		execute();
	}
	/**
	 * @return the PrologDatabase
	 */
	public Database getDatabase(){
		return db;
	}
	/**
	 * @return execution stack
	 */
	public Stack<ExecutionState> getStack(){
		return stack;
	}
	/**
	 * @return substitution if the goal succeed or null if the goal failed
	 */
	public Substitution getSubstitution(){
		if(isFailed())
			return null;
		if(isSucceed())
			return stack.peek().getSubst();
		throw new RuntimeException("Illegal status");
	}
	/**
	 * @return if the goal is already failed
	 */
	public boolean isFailed(){
		return stack.size()<=1;
	}
	/**
	 * @return if the goal is already succeed
	 */
	public boolean isSucceed(){
		return stack.size()>=2&&stack.peek().getDecsglstk().isEmpty();
	}
	//private final int MAX_ITERATOR_COUNT=100;//FIXME
	private void execute(){
		//int i=0;
		//System.out.println();
		while(!isFailed()&&!isSucceed()){//&&++i<MAX_ITERATOR_COUNT){
			//System.out.println(stack);
			try{
				selectClause();
			}catch(PrologException ex){
				raise(ex);
			}
		}
		//if(i==MAX_ITERATOR_COUNT)
		//	throw new RuntimeException("Iteration reach limit");
	}
	/**
	 * Reexecute the goal after a success
	 */
	public void reexecute(){
		stack.pop();
		backtrack();
		execute();
	}
	/**
	 * Select a clause for execution
	 */
	public void selectClause(){
		Predicate currpred=getCurrentActivator().getPredicate();
		Procedure proc=db.getProcedure(currpred);
		if(proc==null){
			switch(db.getFlag("undefined_predicate").getValue().toString()){
				case "error":
					throw new ExistenceException(ExistenceException.PROCEDURE,new Constant(currpred.getFunctor()));
				case "warning":
					LOG.log(Level.WARNING,"Functor not found: {0}",currpred);
				case "fail":
					ExecutionState cutparent=stack.peek().getDecsglstk().pop().getCutparent();
					stack.peek().getDecsglstk().push(new DecoratedSubgoal(new Constant("fail"),cutparent));
					break;
				default:
					assert false;
			}
		}else{
			proc.execute(this);
		}
	}
	/**
	 * Do backtracking
	 */
	public void backtrack(){
		if(isFailed()||isSucceed())
			return;
		Procedure proc=db.getProcedure(getCurrentActivator().getPredicate());
		if(stack.peek().getBI()!=ExecutionState.BacktraceInfo.NIL){
			proc.reexecute(this);
		}else
			proc.execute(this);
	}
	/**
	 * Execute a user-defined procedure with no more clause
	 */
	public void noMoreClause(){
		stack.pop();
		backtrack();
	}
	public ExecutionState getCurrentState(){
		return stack.peek();
	}
	public DecoratedSubgoal getCurrentDecoratedSubgoal(){
		return stack.peek().getDecsglstk().peek();
	}
	public Predication getCurrentActivator(){
		return stack.peek().getDecsglstk().peek().getActivator();
	}
	public Substitution getCurrentSubst(){
		return getStack().peek().getSubst();
	}
	/**
	 * Raise a prolog error
	 * @param ex
	 */
	public void raise(PrologException ex){
		ExecutionState cutparent=stack.peek().getDecsglstk().pop().getCutparent();
		Predication throwPred=new CompoundTerm("throw",ex.getErrorTerm());
		stack.peek().getDecsglstk().push(new DecoratedSubgoal(throwPred,cutparent));
	}
}