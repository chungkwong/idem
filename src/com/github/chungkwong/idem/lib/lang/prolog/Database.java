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
	private final HashMap<Predicate,Procedure> procedures;
	private final HashMap<String,Flag> flags;
	private static final Database base=new Database(new HashMap<>());
	static{
		base.addProcedure(Call.CALL);
		base.addProcedure(Catch.CATCH);
		base.addProcedure(Conjunction.CONJUNCTION);
		base.addProcedure(Cut.CUT);
		base.addProcedure(Disjunction.DISJUNCTION);
		base.addProcedure(Fail.FAIL);
		base.addProcedure(If.IF);
		base.addProcedure(Throw.THROW);
		base.addProcedure(True.TRUE);

		base.addProcedure(Abolish.INSTANCE);
		base.addProcedure(Arg.INSTANCE);
		base.addProcedure(AssertA.INSTANCE);
		base.addProcedure(AssertZ.INSTANCE);
		base.addProcedure(BagOf.INSTANCE);
		base.addProcedure(ClauseOf.INSTANCE);
		base.addProcedure(CopyTerm.INSTANCE);
		base.addProcedure(CurrentPredicate.INSTANCE);
		base.addProcedure(CurrentPrologFlag.INSTANCE);
		base.addProcedure(Equal.INSTANCE);
		base.addProcedure(FailIf.INSTANCE);
		base.addProcedure(FindAll.INSTANCE);
		base.addProcedure(Functor.INSTANCE);
		base.addProcedure(Greater.INSTANCE);
		base.addProcedure(Halt.INSTANCE);
		base.addProcedure(HaltNow.INSTANCE);
		base.addProcedure(Identical.INSTANCE);
		base.addProcedure(Is.INSTANCE);
		base.addProcedure(IsAtom.INSTANCE);
		base.addProcedure(IsAtomic.INSTANCE);
		base.addProcedure(IsCompound.INSTANCE);
		base.addProcedure(IsInteger.INSTANCE);
		base.addProcedure(IsNull.INSTANCE);
		base.addProcedure(IsReal.INSTANCE);
		base.addProcedure(JavaCast.INSTANCE);
		base.addProcedure(JavaField.INSTANCE);
		base.addProcedure(JavaFieldStatic.INSTANCE);
		base.addProcedure(JavaInvoke.INSTANCE);
		base.addProcedure(JavaInvokeStatic.INSTANCE);
		base.addProcedure(JavaNew.INSTANCE);
		base.addProcedure(Less.INSTANCE);
		base.addProcedure(Once.INSTANCE);
		base.addProcedure(Precedes.INSTANCE);
		base.addProcedure(Retract.INSTANCE);
		base.addProcedure(Repeat.INSTANCE);
		base.addProcedure(SetOf.INSTANCE);
		base.addProcedure(SetPrologFlag.INSTANCE);
		base.addProcedure(Succeed.INSTANCE);
		base.addProcedure(UnifyWithOccurCheck.INSTANCE);
		base.addProcedure(Univ.INSTANCE);
		base.addProcedure(Var.INSTANCE);

		InputStream resource=Database.class.getResourceAsStream("StandardProcedures");
		PrologParser parser=new PrologParser(new PrologLex(new InputStreamReader(resource)));
		Predication pred=parser.next();
		while(pred!=null){
			base.addPredication(pred);
			pred=parser.next();
		}
	}
	/**
	 * Construct a prolog database containing the control constructs and buildin predicate
	 */
	public Database(){
		this(base.procedures);
	}
	private Database(HashMap<Predicate,Procedure> procedures){
		this.procedures=new HashMap<>(procedures);
		this.flags=new HashMap<>();
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