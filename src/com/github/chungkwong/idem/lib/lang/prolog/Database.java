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
 * Prolog database
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Database{
	private final HashMap<Predicate,Procedure> procedures=new HashMap<>();
	private final HashMap<String,Flag> flags=new HashMap<>();
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

		addProcedure(Abolish.INSTANCE);
		addProcedure(Arg.INSTANCE);
		addProcedure(AssertA.INSTANCE);
		addProcedure(AssertZ.INSTANCE);
		addProcedure(BagOf.INSTANCE);
		addProcedure(ClauseOf.INSTANCE);
		addProcedure(CopyTerm.INSTANCE);
		addProcedure(CurrentPredicate.INSTANCE);
		addProcedure(CurrentPrologFlag.INSTANCE);
		addProcedure(FailIf.INSTANCE);
		addProcedure(FindAll.INSTANCE);
		addProcedure(Functor.INSTANCE);
		addProcedure(Halt.INSTANCE);
		addProcedure(HaltNow.INSTANCE);
		addProcedure(Identical.INSTANCE);
		addProcedure(Is.INSTANCE);
		addProcedure(IsAtom.INSTANCE);
		addProcedure(IsAtomic.INSTANCE);
		addProcedure(IsCompound.INSTANCE);
		addProcedure(IsInteger.INSTANCE);
		addProcedure(IsNull.INSTANCE);
		addProcedure(IsReal.INSTANCE);
		addProcedure(JavaCast.INSTANCE);
		addProcedure(JavaField.INSTANCE);
		addProcedure(JavaFieldStatic.INSTANCE);
		addProcedure(JavaInvoke.INSTANCE);
		addProcedure(JavaInvokeStatic.INSTANCE);
		addProcedure(JavaNew.INSTANCE);
		addProcedure(Once.INSTANCE);
		addProcedure(Precedes.INSTANCE);
		addProcedure(Retract.INSTANCE);
		addProcedure(Repeat.INSTANCE);
		addProcedure(SetOf.INSTANCE);
		addProcedure(SetPrologFlag.INSTANCE);
		addProcedure(Succeed.INSTANCE);
		addProcedure(UnifyWithOccurCheck.INSTANCE);
		addProcedure(Univ.INSTANCE);
		addProcedure(Var.INSTANCE);

		InputStream resource=Database.class.getResourceAsStream("StandardProcedures");
		PrologParser parser=new PrologParser(new PrologLex(new InputStreamReader(resource)));
		Predication pred=parser.next();
		while(pred!=null){
			addPredication(pred);
			pred=parser.next();
		}

		addFlag(new Flag("bounded",new Constant("false"),false,(o)->true,"character_list"));
		addFlag(new Flag("intger_rounding_function",new Constant("toward_zero"),false,(o)->true,"character_list"));
		Constant on=new Constant("on"),off=new Constant("off"),error=new Constant("error"),fail=new Constant("fail"),warning=new Constant("warning");
		addFlag(new Flag("char_conversion",on,true,(o)->o.equals(on)||o.equals(off),"character_list"));
		addFlag(new Flag("debug",off,true,(o)->o.equals(off)||o.equals(on),"character_list"));
		addFlag(new Flag("undefined_predicate",error,true,(o)->o.equals(error)||o.equals(fail)||o.equals(warning),"character_list"));
	}
	/**
	 * Add a procedure to the database
	 * @param proc to be added
	 */
	public void addProcedure(Procedure proc){
		procedures.put(proc.getPredicate(),proc);
	}
	/**
	 * Remove a procedure form the database
	 * @param pred the predicate of the procedure to be removed
	 */
	public void removeProcedure(Predicate pred){
		procedures.remove(pred);
	}
	/**
	 * Add a clause to a user-defined procedure as the frist one
	 * @param clause to be added
	 */
	public void addClauseToFirst(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)&&procedures.get(predicate) instanceof UserPredicate)
			((UserPredicate)procedures.get(predicate)).getClauses().add(0,clause);
		else{
			procedures.put(predicate,new UserPredicate(clause));
		}
	}
	/**
	 * Add a clause to a user-defined procedure as the last one
	 * @param clause to be added
	 */
	public void addClauseToLast(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)&&procedures.get(predicate) instanceof UserPredicate)
			((UserPredicate)procedures.get(predicate)).getClauses().add(clause);
		else{
			procedures.put(predicate,new UserPredicate(clause));
		}
	}
	/**
	 * Remove a clause from a user-defined procedure
	 * @param clause to be removed
	 */
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
	/**
	 * Add a prolog text to the database as a user-defined procedure
	 * @param pred to be added
	 */
	public void addPredication(Predication pred){
		if(pred.getPredicate().getFunctor().equals(":-"))
			addClauseToLast(new Clause((Predication)pred.getArguments().get(0),(Predication)pred.getArguments().get(1)));
		else
			addClauseToLast(new Clause(pred,new Constant("true")));
	}
	/**
	 * @param predicate
	 * @return a procedure in the database with the given predicate or null
	 * if it does not exists
	 */
	public Procedure getProcedure(Predicate predicate){
		return procedures.get(predicate);
	}
	/**
	 * @return all the procedures in the database
	 */
	public Map<Predicate,Procedure> getProcedures(){
		return Collections.unmodifiableMap(procedures);
	}
	/**
	 * Add a flag to the database
	 * @param flag to be added
	 */
	public void addFlag(Flag flag){
		flags.put(flag.getName(),flag);
	}
	/**
	 * Remove a flag to the database
	 * @param flag to be removed
	 */
	public void removeFlag(Flag flag){
		flags.remove(flag.getName());
	}
	/**
	 * @param name the name of a flag
	 * @return a flag with the given name or null if it does not exists
	 */
	public Flag getFlag(String name){
		return flags.get(name);
	}
	/**
	 * @return all the flags in the database indexed by their name
	 */
	public Map<String,Flag> getFlags(){
		return Collections.unmodifiableMap(flags);
	}
	@Override
	public String toString(){
		return procedures.entrySet().stream().map((entry)->entry.getValue().toString()).collect(Collectors.joining("\n"));
	}
}