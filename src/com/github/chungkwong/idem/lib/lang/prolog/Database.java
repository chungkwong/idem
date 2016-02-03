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
	HashMap<String,Flag> flags=new HashMap<>();
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
		addProcedure(UnifyWithOccurCheck.INSTANCE);
		InputStream resource=Database.class.getResourceAsStream("StandardProcedures");
		PrologParser parser=new PrologParser(new PrologLex(new InputStreamReader(resource)));
		Predication pred=parser.next();
		while(pred!=null){
			addPredication(pred);
			pred=parser.next();
		}
		addFlag(new Flag("bounded","false",false,(o)->true,"character_list"));
		addFlag(new Flag("intger_rounding_function","toward_zero",false,(o)->true,"character_list"));
		addFlag(new Flag("char_conversion","on",true,(o)->o.equals("on")||o.equals("off"),"character_list"));
		addFlag(new Flag("debug","off",true,(o)->o.equals("off")||o.equals("on"),"character_list"));
		addFlag(new Flag("undefined_predicate","error",true,(o)->o.equals("error")||o.equals("fail")||o.equals("warning"),"character_list"));
	}
	public Database(){

	}
	public void addProcedure(Procedure clause){
		procedures.put(clause.getPredicate(),clause);
	}
	public void removeProcedure(Predicate pred){
		procedures.remove(pred);
	}
	public void addClauseToFirst(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)&&procedures.get(predicate) instanceof UserPredicate)
			((UserPredicate)procedures.get(predicate)).getClauses().add(0,clause);
		else{
			procedures.put(predicate,new UserPredicate(clause));
		}
	}
	public void addClauseToLast(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)&&procedures.get(predicate) instanceof UserPredicate)
			((UserPredicate)procedures.get(predicate)).getClauses().add(clause);
		else{
			procedures.put(predicate,new UserPredicate(clause));
		}
	}
	public void removeClause(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)){
			Procedure proc=procedures.get(predicate);
			if(proc instanceof UserPredicate){
				((UserPredicate)proc).getClauses().remove(clause);
				if(((UserPredicate)proc).getClauses().isEmpty())
					procedures.remove(predicate);
			}
		}
	}
	public void addPredication(Predication pred){
		if(pred.getPredicate().getFunctor().equals(":-"))
			addClauseToLast(new Clause((Predication)pred.getArguments().get(0),(Predication)pred.getArguments().get(1)));
		else
			addClauseToLast(new Clause(pred,new Atom("true")));
	}
	public Procedure getProcedure(Predicate predicate){
		return procedures.get(predicate);
	}
	public void addFlag(Flag flag){
		flags.put(flag.getName(),flag);
	}
	public void removeFlag(Flag flag){
		flags.remove(flag.getName());
	}
	public Flag getFlag(String name){
		return flags.get(name);
	}
	@Override
	public String toString(){
		return procedures.entrySet().stream().map((entry)->entry.getValue().toString()).collect(Collectors.joining("\n"));
	}
}