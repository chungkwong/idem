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
import com.github.chungkwong.idem.lib.lang.prolog.directive.*;
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
	private final HashSet<File> ensureLoad=new HashSet<>();
	private List<Predication> initialization=new LinkedList<>();
	private final Map<Character,Character> conversion=new HashMap<>();
	private final OperatorTable operaterTable=new OperatorTable(OperatorTable.DEFAULT_OPERATOR_TABLE);
	private final HashSet<Predicate> DYNAMIC_PREDICATES=new HashSet<>();
	private static final HashMap<Predicate,Directive> directives=new HashMap<>();
	private static final Database base=new Database(new HashMap<>());
	static{
		addDirective(CharConversion.INSTANCE);
		addDirective(Discontiguous.INSTANCE);
		addDirective(Dynamic.INSTANCE);
		addDirective(EnsureLoaded.INSTANCE);
		addDirective(Include.INSTANCE);
		addDirective(Initialization.INSTANCE);
		addDirective(MultiFile.INSTANCE);
		addDirective(Op.INSTANCE);

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
		base.addProcedure(AtomConcat.INSTANCE);
		base.addProcedure(AtomLength.INSTANCE);
		base.addProcedure(AtomChars.INSTANCE);
		base.addProcedure(AtomCodes.INSTANCE);
		base.addProcedure(BagOf.INSTANCE);
		base.addProcedure(ClauseOf.INSTANCE);
		base.addProcedure(CharCode.INSTANCE);
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
		base.addProcedure(NumberChars.INSTANCE);
		base.addProcedure(NumberCodes.INSTANCE);
		base.addProcedure(Once.INSTANCE);
		base.addProcedure(Precedes.INSTANCE);
		base.addProcedure(Retract.INSTANCE);
		base.addProcedure(Repeat.INSTANCE);
		base.addProcedure(SetOf.INSTANCE);
		base.addProcedure(SetPrologFlag.INSTANCE);
		base.addProcedure(SubAtom.INSTANCE);
		base.addProcedure(Succeed.INSTANCE);
		base.addProcedure(UnifyWithOccurCheck.INSTANCE);
		base.addProcedure(Univ.INSTANCE);
		base.addProcedure(Var.INSTANCE);

		base.loadPrologText(new InputStreamReader(Database.class.getResourceAsStream("StandardProcedures")));
	}
	/**
	 * Construct a prolog database containing the control constructs and buildin predicate
	 */
	public Database(){
		this((FileReader)null);
	}
	/**
	 * Construct a prolog database containing the control constructs and buildin predicate
	 */
	public Database(Reader startup){
		this(base.procedures);
		if(startup!=null)
			loadPrologText(startup);
		for(Predication goal:initialization)
			new Processor(goal,this);
		initialization=null;
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
		if(procedures.containsKey(pred)){
			if(isDynamic(pred))
				procedures.remove(pred);
			else
				throw new PermissionException(new Constant("modify")
						,new Constant("static_procedure"),pred.getIndicator());
		}
	}
	/**
	 * Add a clause to a user-defined procedure as the frist one
	 * @param clause to be added
	 */
	public void addClauseToFirst(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)&&procedures.get(predicate) instanceof UserPredicate)
				((UserPredicate)procedures.get(predicate)).getClauses().add(0,clause);
		else
			procedures.put(predicate,new UserPredicate(clause));
	}
	/**
	 * Add a clause to a user-defined procedure as the last one
	 * @param clause to be added
	 */
	public void addClauseToLast(Clause clause){
		Predicate predicate=clause.getHead().getPredicate();
		if(procedures.containsKey(predicate)&&procedures.get(predicate) instanceof UserPredicate)
				((UserPredicate)procedures.get(predicate)).getClauses().add(clause);
		else
			procedures.put(predicate,new UserPredicate(clause));
	}
	/**
	 * Add a initialization goal
	 * @param goal
	 */
	public void addInitialization(Predication goal){
		if(initialization!=null)
			initialization.add(goal);
		else
			new Processor(goal,this);
	}
	/**
	 * Include a file here
	 * @param file
	 */
	public void include(File file){
		try{
			loadPrologText(new FileReader(file));
		}catch(FileNotFoundException ex){
			throw new SystemException("Fail to load file",ex);
		}
	}
	/**
	 * Include a file if it is not included
	 * @param file
	 */
	public void ensureLoaded(File file){
		if(!ensureLoad.contains(file)){
			ensureLoad.add(file);
			include(file);
		}
	}
	/**
	 * Prepare prolog text for execution
	 * @param in source
	 */
	public void loadPrologText(Reader in){
		PrologParser parser=getParser(in);
		Predication pred=parser.next();
		while(pred!=null){
			addPredication(pred);
			pred=parser.next();
		}
	}
	/**
	 * Add a prolog text to the database as a user-defined procedure
	 * @param pred to be added
	 */
	public void addPredication(Predication pred){
		Predicate predicate=pred.getPredicate();
		if(directives.containsKey(predicate))
			directives.get(predicate).process(pred.getArguments(),this);
		else if(predicate.getFunctor().equals(":-"))
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
	private static void addDirective(Directive directive){
		directives.put(directive.getPredicate(),directive);
	}
	/**
	 * Add a flag to the database
	 * @param flag to be added
	 */
	private void addFlag(Flag flag){
		flags.put(flag.getName(),flag);
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
	/**
	 * @param predicate indicate a procedure to be set dynamic
	 */
	public void makeDynamic(Predicate predicate){
		DYNAMIC_PREDICATES.add(predicate);
	}
	/**
	 * @param predicate
	 * @return if predicate is dynamic
	 */
	public boolean isDynamic(Predicate predicate){
		return DYNAMIC_PREDICATES.contains(predicate);
	}
	/**
	 * @return the operator table used by a parser
	 */
	public OperatorTable getOperatorTable(){
		return operaterTable;
	}
	/**
	 * @return the table for character conversion
	 */
	public Map<Character,Character> getConversionMap(){
		return conversion;
	}
	/**
	 * @param in Prolog source
	 * @return a parser that can parse source
	 */
	public PrologParser getParser(Reader in){
		return new PrologParser(getTokenizer(in),operaterTable);
	}
	/**
	 * @param source Prolog source
	 * @return a parser that can parse source
	 */
	public PrologParser getParser(String source){
		return getParser(new StringReader(source));
	}
	/**
	 * @param in Prolog source
	 * @return a Tokenizer that can tokenize source
	 */
	public PrologLex getTokenizer(Reader in){
		if(flags.get("char_conversion").getValue().toString().equals("on")){
			return new PrologLex(in,conversion);
		}else
			return new PrologLex(in);
	}
	/**
	 * @param source Prolog source
	 * @return a Tokenizer that can tokenize source
	 */
	public PrologLex getTokenizer(String source){
		return getTokenizer(new StringReader(source));
	}
	@Override
	public String toString(){
		return procedures.entrySet().stream().map((entry)->entry.getValue().toString()).collect(Collectors.joining("\n"));
	}
}