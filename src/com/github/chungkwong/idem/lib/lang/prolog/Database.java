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
import com.github.chungkwong.idem.lib.lang.prolog.buildin.*;
import com.github.chungkwong.idem.lib.lang.prolog.constructs.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;
/**
 *
 * @author kwong
 */
public class Database{
	HashMap<Predicate,Procedure> procedures=new HashMap<>();
	{
		addProcedure(Call.CALL);
		addProcedure(Catch.CATCH);
		addProcedure(Conjunction.CONJUNCTION);
		addProcedure(Cut.CUT);
		addProcedure(Disjunction.DISJUNCTION);
		addProcedure(Fail.FAIL);
		addProcedure(If.IF);
		addProcedure(Throw.THROW);
		addProcedure(True.TRUE);
		addProcedure(Var.VAR);
		InputStream resource=Database.class.getResourceAsStream("StandardProcedures");
		PrologParser parser=new PrologParser(new PrologLex(new InputStreamReader(resource)));
		Predication pred=parser.next();
		while(pred!=null){
			addPredication(pred);
			pred=parser.next();
		}
	}
	public Database(){

	}
	public void addProcedure(Procedure clause){
		procedures.put(clause.getPredicate(),clause);
	}
	public void removeProcedure(Procedure clause){
		procedures.put(clause.getPredicate(),clause);
	}
	public void addClause(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)&&procedures.get(predicate) instanceof UserPredicate)
			((UserPredicate)procedures.get(predicate)).addClause(clause);
		else{
			procedures.put(predicate,new UserPredicate(clause));
		}
	}
	public void removeClause(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)){
			Procedure proc=procedures.get(predicate);
			if(proc instanceof UserPredicate){
				((UserPredicate)proc).removeClause(clause);
				if(((UserPredicate)proc).getClauses().isEmpty())
					procedures.remove(predicate);
			}
		}
	}
	public void addPredication(Predication pred){
		if(pred.getPredicate().getFunctor().equals(":-"))
			addClause(new Clause((Predication)pred.getArguments().get(0),(Predication)pred.getArguments().get(1)));
		else
			addClause(new Clause(pred,new Atom("true")));
	}
	Procedure getProcedure(Predicate predicate){
		return procedures.get(predicate);
	}
	@Override
	public String toString(){
		return procedures.entrySet().stream().map((entry)->entry.getValue().toString()).collect(Collectors.joining("\n"));
	}
}