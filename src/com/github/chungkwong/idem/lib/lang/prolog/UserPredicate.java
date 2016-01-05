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
import java.util.stream.*;
/**
 *
 * @author kwong
 */
public class UserPredicate implements Procedure{
	private final List<Clause> clauses=new LinkedList<>();
	public UserPredicate(Clause clause){
		clauses.add(clause);
	}
	@Override
	public void execute(Processor exec){
		exec.getStack().peek().setBI(ExecutionState.BacktraceInfo.UP);
		exec.getStack().peek().setCl(new PeekableIterator<>(clauses.listIterator()));
		executeUser(exec);
	}
	@Override
	public void reexecute(Processor exec){
		exec.getStack().peek().getCl().next();
		executeUser(exec);
	}
	private void executeUser(Processor exec){
		while(exec.getStack().peek().getCl().hasNext()){
			Clause c=exec.getStack().peek().getCl().peek();
			c=c.rename();
			Substitution context=new Substitution(exec.getStack().peek().getSubst());
			if(c.getHead().unities(exec.getCurrentActivator(),context)){
				ExecutionState ccs=new ExecutionState(exec.getStack().peek());
				ccs.setSubst(context);
				ccs.getDecsglstk().pop();
				ccs.getDecsglstk().push(new DecoratedSubgoal(c.getBody().substitute(context),exec.getStack().get(exec.getStack().size()-2)));
				ccs.setBI(ExecutionState.BacktraceInfo.NIL);
				exec.getStack().push(ccs);
				return;
			}else
				exec.getStack().peek().getCl().next();
		}
		exec.getStack().peek().setBI(ExecutionState.BacktraceInfo.UP);
		exec.noMoreClause();
	}
	@Override
	public Predicate getPredicate(){
		return clauses.get(0).getHead().getPredicate();
	}
	public void addClause(Clause clause){
		clauses.add(clause);
	}
	public void removeClause(Clause clause){
		clauses.remove(clause);
	}
	public List<Clause> getClauses(){
		return Collections.unmodifiableList(clauses);
	}
	public String toString(){
		return clauses.stream().map(Clause::toString).collect(Collectors.joining("\n"));
	}
}