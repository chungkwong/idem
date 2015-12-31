///*
// * Copyright (C) 2015 kwong
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// */
//package com.github.chungkwong.idem.lib.lang.prolog;
//import static com.github.chungkwong.idem.global.Log.LOG;
//import com.github.chungkwong.idem.lib.lang.prolog.predicate.*;
//import java.util.*;
//import java.util.logging.*;
///**
// *
// * @author kwong
// */
//public class StdEvaluator{
//	UndefinedPredicate undefpred=UndefinedPredicate.FAIL;
//	Stack<ExecutionState> stack=new Stack<>();
//	Database db;
//	public StdEvaluator(Predication goal,Database db){
//		this.db=db;
//		stack.push(new ExecutionState());
//		stack.push(new ExecutionState(new ExecutionState.DecoratedSubgoal(goal,stack.peek()),new Substitution()));
//	}
//	boolean isFailed(){
//		return stack.size()<=1;
//	}
//	boolean isSuccessed(){
//		return getDecoratedSubgoals().isEmpty();
//	}
//	public Substitution execute(){
//		int i=0;
//		while(++i<10){//FIXME
//			System.out.println(stack);
//			if(isFailed())
//				return null;
//			if(isSuccessed())
//				return stack.peek().getSubst();
//			selectClause();
//		}
//		throw new RuntimeException();
//	}
//	public Substitution reexecute(){
//		stack.pop();
//		backtrace();
//		return execute();
//	}
//	void selectClause(){
//		Predication curract=stack.peek().getDecsglstk().peek().getActivator();
//		String functor=curract.getPredicate().getFunctor();
//		Packet packet=db.getPacket(functor);
//		int arity=curract.getPredicate().getArity();
//		if(packet==null||packet.getClauses().stream().noneMatch((cl)->cl.getHead().getPredicate().getArity()==arity)){
//			switch(undefpred){
//				case ERROR:
//					throw new NonexistenceException(functor);
//				case WARNING:
//					LOG.log(Level.WARNING,"Functor not found: {0}",functor);
//				case FAIL:
//					//stack.peek().decsglstk.peek().activator=Fail.FAIL;
//					break;
//			}
//		}else{
//			/*stack.peek().BI=BacktraceInfo.CTRL;
//			executeControl();
//			stack.peek().BI=BacktraceInfo.BIP;
//			executeBuiltin();*/
//			stack.peek().setBI(ExecutionState.BacktraceInfo.UP);
//			stack.peek().getCl().addAll(packet.getClauses());
//			executeUser();
//		}
//	}
//	void executeControl(){
//
//	}
//	void executeBuiltin(){
//
//	}
//	void executeUser(){
//		while(!stack.peek().getCl().isEmpty()){
//			Clause c=stack.peek().getCl().get(0);
//			if(c.getHead().getPredicate().getFunctor().equals(getCurrentActivator().getPredicate().getFunctor())){
//				c=c.rename();
//				Substitution context=new Substitution(stack.peek().getSubst());
//				if(c.getHead().getArguments().stream().allMatch((arg)->arg.unities(getCurrentActivator().toTerm(),context))){
//					ExecutionState ccg=new ExecutionState(stack.peek());
//					ccg.setSubst(context);
//					ccg.setDecsglstk(new Stack<>());
//					ccg.getDecsglstk().push(new ExecutionState.DecoratedSubgoal(c.getBodyAsTerm().substitute(context).toBody(),stack.get(stack.size()-2)));
//					ccg.setBI(ExecutionState.BacktraceInfo.NIL);
//					stack.push(ccg);
//					return;
//				}
//			}
//		}
//		stack.peek().setBI(ExecutionState.BacktraceInfo.UP);
//		noMoreClause();
//	}
//	void noMoreClause(){
//		stack.pop();
//		backtrace();
//	}
//	void backtrace(){
//		switch(stack.peek().getBI()){
//			case UP:
//				executeUser();
//				break;
//			case BIP:
//				executeBuiltin();
//				break;
//			case CTRL:
//				executeControl();
//				break;
//			case NIL:
//				//selectClause();
//				break;
//		}
//	}
//	ExecutionState getCurrentState(){
//		return stack.peek();
//	}
//	Predication getCurrentActivator(){
//		return stack.peek().getDecsglstk().peek().getActivator();
//	}
//	Stack<ExecutionState.DecoratedSubgoal> getDecoratedSubgoals(){
//		return stack.peek().getDecsglstk();
//	}
//	ExecutionState.DecoratedSubgoal getCurrentDecoratedSubgoal(){
//		return stack.peek().getDecsglstk().peek();
//	}
//	public Stack<ExecutionState> getStack(){
//		return stack;
//	}
//	public static void main(String[] args){
//		Database db=new Database();
//		db.addClause(new Clause(new Predication("woman",Collections.singletonList(new Constant("mia"))),True.TRUE));
//		db.addClause(new Clause(new Predication("woman",Collections.singletonList(new Constant("jody"))),True.TRUE));
//		db.addClause(new Clause(new Predication("woman",Collections.singletonList(new Constant("yolanda"))),True.TRUE));
//		db.addClause(new Clause(new Predication("playAirGuitar",Collections.singletonList(new Constant("jody"))),True.TRUE));
//		db.addClause(new Clause(new Predication("party",Collections.emptyList()),True.TRUE));
//		System.out.println(new StdEvaluator(new Constant("partycat").toHead(),db).execute());
//		System.out.println(new StdEvaluator(new Predication("woman",Collections.singletonList(new Variable("X"))),db).execute());
//		System.out.println(db);
//	}
//	public enum UndefinedPredicate{
//		ERROR,WARNING,FAIL;
//	}
//}