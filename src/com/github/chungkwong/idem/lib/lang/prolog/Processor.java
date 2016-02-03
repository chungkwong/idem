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
 *
 * @author kwong
 */
public class Processor{
	private final Stack<ExecutionState> stack=new Stack<>();
	private final Database db;
	public Processor(Predication goal,Database db){
		this.db=db;
		stack.push(new ExecutionState());
		stack.push(new ExecutionState(new DecoratedSubgoal(goal,stack.peek()),new Substitution()));
		execute();
	}
	public Database getDatabase(){
		return db;
	}
	public Stack<ExecutionState> getStack(){
		return stack;
	}
	public Substitution getSubstitution(){
		if(isFailed())
			return null;
		if(isSuccessed())
			return stack.peek().getSubst();
		throw new RuntimeException("Illegal status");
	}
	public boolean isFailed(){
		return stack.size()<=1;
	}
	public boolean isSuccessed(){
		return stack.peek().getDecsglstk().isEmpty();
	}
	final int MAX_ITERATOR_COUNT=100;
	private void execute(){
		int i=0;
		//System.out.println();
		while(!isFailed()&&!isSuccessed()&&++i<MAX_ITERATOR_COUNT){//FIXME
			//System.out.println(stack);
			try{
				selectClause();
			}catch(PrologException ex){
				raise(ex);
			}
		}
		if(i==MAX_ITERATOR_COUNT)
			throw new RuntimeException("Iteration reach limit");
	}
	public void reexecute(){
		stack.pop();
		backtrack();
		execute();
	}
	public void selectClause(){
		Predicate currpred=getCurrentActivator().getPredicate();
		Procedure proc=db.getProcedure(currpred);
		if(proc==null){
			switch(db.getFlag("undefined_predicate").getName()){
				case "error":
					throw new ExistenceException(Procedure.class,new Atom(currpred.getFunctor()));
				case "warning":
					LOG.log(Level.WARNING,"Functor not found: {0}",currpred);
				case "fail":
					ExecutionState cutparent=stack.peek().getDecsglstk().pop().getCutparent();
					stack.peek().getDecsglstk().push(new DecoratedSubgoal(new Atom("fail"),cutparent));
					break;
			}
		}else{
			proc.execute(this);
		}
	}
	public void backtrack(){
		if(isFailed()||isSuccessed())
			return;
		Procedure proc=db.getProcedure(getCurrentActivator().getPredicate());
		if(stack.peek().getBI()!=ExecutionState.BacktraceInfo.NIL){
			proc.reexecute(this);
		}else
			proc.execute(this);
	}
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
	public void raise(PrologException ex){
		ExecutionState cutparent=stack.peek().getDecsglstk().pop().getCutparent();
		Predication throwPred=new CompoundTerm("throw",Collections.singletonList(ex.getErrorTerm()));
		stack.peek().getDecsglstk().push(new DecoratedSubgoal(throwPred,cutparent));
	}
}