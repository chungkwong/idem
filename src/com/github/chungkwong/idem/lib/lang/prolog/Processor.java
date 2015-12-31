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
import static com.github.chungkwong.idem.global.Log.LOG;
import java.util.*;
import java.util.logging.*;
/**
 *
 * @author kwong
 */
public class Processor{
	private UndefinedPredicate undefpred=UndefinedPredicate.FAIL;
	private final Stack<ExecutionState> stack=new Stack<>();
	private final Database db;
	public Processor(Predication goal,Database db){
		this.db=db;
		stack.push(new ExecutionState());
		stack.push(new ExecutionState(new ExecutionState.DecoratedSubgoal(goal,stack.peek()),new Substitution()));
		execute();
	}
	public Database getDatabase(){
		return db;
	}
	public UndefinedPredicate getUndefpred(){
		return undefpred;
	}
	public void setUndefpred(UndefinedPredicate undefpred){
		this.undefpred=undefpred;
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
	final int MAX_ITERATOR_COUNT=10;
	private void execute(){
		int i=0;
		while(!isFailed()&&!isSuccessed()&&++i<MAX_ITERATOR_COUNT){//FIXME
			//System.out.println(stack);
			selectClause();
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
			switch(undefpred){
				case ERROR:
					raise(new ExistenceException(Procedure.class,new Atom(currpred.getFunctor())));
					break;
				case WARNING:
					LOG.log(Level.WARNING,"Functor not found: {0}",currpred);
				case FAIL:
					ExecutionState cutparent=stack.peek().getDecsglstk().pop().getCutparent();
					stack.peek().getDecsglstk().push(new ExecutionState.DecoratedSubgoal(new Atom("fail"),cutparent));
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
	public Predication getCurrentActivator(){
		return stack.peek().getDecsglstk().get(0).getActivator();
	}
	public void raise(PrologException ex){
		ExecutionState cutparent=stack.peek().getDecsglstk().pop().getCutparent();
		Predication throwPred=new CompoundTerm("throw",Collections.singletonList(ex.getErrorTerm()));
		stack.peek().getDecsglstk().push(new ExecutionState.DecoratedSubgoal(throwPred,cutparent));
	}
	static List<Substitution> multiquery(Predication goal,Database db){
		List<Substitution> substs=new ArrayList<>();
		Processor processor=new Processor(goal,db);
		while(processor.getSubstitution()!=null){
			substs.add(processor.getSubstitution());
			processor.reexecute();
		}
		return substs;
	}

	public static void main(String[] args){
		Database db=new Database();
		db.addClause(new Clause(new CompoundTerm("woman",Collections.singletonList(new Atom("mia"))),new Atom("true")));
		db.addClause(new Clause(new CompoundTerm("woman",Collections.singletonList(new Atom("jody"))),new Atom("true")));
		db.addClause(new Clause(new CompoundTerm("woman",Collections.singletonList(new Atom("yolanda"))),new Atom("true")));
		db.addClause(new Clause(new CompoundTerm("playAirGuitar",Collections.singletonList(new Atom("jody"))),new Atom("true")));
		db.addClause(new Clause(new CompoundTerm("party",Collections.emptyList()),new Atom("true")));
		//System.out.println(db);
		//System.out.println(new Processor(new Atom("partycat"),db).getSubstitution());
		//System.out.println(new Processor(new Atom("party"),db).getSubstitution());
		//System.out.println(new Processor(new CompoundTerm("woman",Collections.singletonList(new Variable("X"))),db).getSubstitution());
		//System.out.println(new Processor(new CompoundTerm("woman",Collections.singletonList(new Variable("X"))),db).getSubstitution());
		multiquery(new CompoundTerm("woman",Collections.singletonList(new Variable("X"))),db);
	}
	public enum UndefinedPredicate{
		ERROR,WARNING,FAIL;
	}
}
