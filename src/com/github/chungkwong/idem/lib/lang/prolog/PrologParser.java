/*
 * Copyright (C) 2016 Chan Chung Kwong <1m02math@126.com>
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
import static com.github.chungkwong.idem.lib.lang.prolog.OperatorTable.DEFAULT_OPERATOR_TABLE;
import java.math.*;
import java.util.*;
/**
 * A parser for Prolog
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PrologParser implements SimpleIterator<Predication>{
	private static final Operator curly=new Operator("{}",0,Operator.Class.PREFIX,Operator.Associativity.RIGHT);
	private static final Operator openParen=new Operator("(",1202,Operator.Class.INFIX,Operator.Associativity.RIGHT);
	private final PushBackIterator lex;
	/**
	 * Construct a PrologParser
	 * @param lex The provider of tokens
	 */
	public PrologParser(PrologLex lex){
		this.lex=new PushBackIterator<>(new SimpleIteratorWraper<>(lex));
	}
	/**
	 * Parse the remaining code
	 * @return the remaining prolog text
	 */
	public List<Predication> getRemaining(){
		ArrayList<Predication> lst=new ArrayList<>();
		Predication pred=next();
		while(pred!=null){
			lst.add(pred);
			pred=next();
		}
		return lst;
	}
	/**
	 * @return the next prolog text
	 */
	@Override
	public Predication next(){
		ParseState state=new ParseState();
		out:while(lex.hasNext()){
			Object token=lex.next();
			if(token instanceof Number){
				state.pushOperand(new Constant(token));
			}else if(token instanceof Term){
				state.pushOperand((Term)token);
			}else if(token instanceof String){
				String name=(String)token;
				switch(name){
					case "(":state.pushOpenParen(lex);break;
					case ")":state.pushCloseParen();break;
					case "[":state.pushOpenBracket(lex);break;
					case "]":state.pushCloseBracket();break;
					case "{":state.pushOpenCurly(lex);break;
					case "}":state.pushCloseCurly();break;
					default:
						if(state.pushOperator(name,lex))
							break out;
				}
			}else{
				assert false;
			}
			//System.err.println(state);
		}
		state.end();
		if(state.operands.isEmpty())
			return null;
		if(state.operands.size()!=1)
			throw new ParseException();
		return (Predication)state.operands.pop();
	}
	/**
	 * Entry of a debug tool which print out prolog text generated from
	 * each line of input
	 * @param args unused
	 */
	public static void main(String[] args){
		Scanner in=new Scanner(System.in);
		while(in.hasNextLine()){
			PrologParser parse=new PrologParser(new PrologLex(in.nextLine()));
			System.out.println(parse.getRemaining());
		}
	}
}
enum ExpectClass{
	PREFIX,INFIX_OR_POSTFIX
}
class ParseState{
	private static final Operator OPEN_PAREN=new Operator("(",1202,Operator.Class.INFIX,Operator.Associativity.RIGHT);
	private static final Operator OPEN_BRACKET=new Operator("[",1202,Operator.Class.INFIX,Operator.Associativity.RIGHT);
	private static final Operator OPEN_CURLY=new Operator("{",1202,Operator.Class.INFIX,Operator.Associativity.RIGHT);
	private static final Constant EMPTY_LIST=Lists.EMPTY_LIST;
	Stack<Term> operands=new Stack<>();
	Stack<Operator> operators=new Stack<>();
	ExpectClass expected=ExpectClass.PREFIX;
	ParseState(){
		operators.push(new Operator("#",1202,Operator.Class.INFIX,Operator.Associativity.NO));
	}
	void pushOperand(Term term){
		operands.push(term);
		expected=ExpectClass.INFIX_OR_POSTFIX;
	}
	boolean pushOperator(String name,PushBackIterator lex){
		if(expected==ExpectClass.INFIX_OR_POSTFIX){
			Operator infix=DEFAULT_OPERATOR_TABLE.getInfixOperators().get(name);
			Operator postfix=DEFAULT_OPERATOR_TABLE.getPostfixOperators().get(name);
			if(infix==null){
				if(postfix==null)
					if(name.equals("."))return true;
					else throw new ParseException();
				else
					pushOperator(postfix);
			}else if(postfix==null){
				pushOperator(infix);
			}else{
				Object peek=lex.peek();
				if(peek instanceof String&&(DEFAULT_OPERATOR_TABLE.getInfixOperators().containsKey((String)peek)
						||DEFAULT_OPERATOR_TABLE.getPostfixOperators().containsKey((String)peek))){
					pushOperator(postfix);
				}else{
					pushOperator(infix);
				}
			}
		}else{
			Object peek=lex.peek();
			if(peek.equals("(")){
				pushOperator(new Operator(name,0,Operator.Class.PREFIX,Operator.Associativity.RIGHT));
			}else{
				Operator prefix=DEFAULT_OPERATOR_TABLE.getPrefixOperators().get(name);
				if(prefix==null){
					pushOperand(new Constant(name));
				}else{
					pushOperator(prefix);
				}
			}
		}
		return false;
	}
	void pushOperator(Operator next){
		if(next.getCls()==Operator.Class.POSTFIX)
			expected=ExpectClass.INFIX_OR_POSTFIX;
		else
			expected=ExpectClass.PREFIX;
		while(true){
			Operator top=operators.peek();
			if(top.getPriority()<next.getPriority()){
				reduceTopOperator();
			}else if(top.getPriority()>next.getPriority()){
				break;
			}else if(top.getAssociativity()==Operator.Associativity.LEFT){//top must be infix
				reduceTopOperator();
			}else if(next.getAssociativity()==Operator.Associativity.RIGHT){
				break;
			}else{
				throw new ParseException();
			}
		}
		if(next.getCls()==Operator.Class.POSTFIX)
			operands.push(new CompoundTerm(next,operands.pop()));
		else
			operators.push(next);
	}
	void pushOpenBracket(PushBackIterator lex){
		if(lex.peek().equals("]")){
			lex.next();
			pushOperand(EMPTY_LIST);
		}else{
			operators.push(OPEN_BRACKET);
			expected=ExpectClass.PREFIX;
		}
	}
	void pushOpenParen(PushBackIterator lex){
		if(lex.peek().equals(")")){
			lex.next();
			pushOperand(new CompoundTerm(operators.pop().getToken(),Collections.EMPTY_LIST));
		}else{
			operators.push(OPEN_PAREN);
			expected=ExpectClass.PREFIX;
		}
	}
	void pushOpenCurly(PushBackIterator lex){
		if(lex.peek().equals("}")){
			lex.next();
			pushOperator(new Operator("{}",0,Operator.Class.PREFIX,Operator.Associativity.RIGHT));
		}else{
			operators.push(OPEN_CURLY);
			expected=ExpectClass.PREFIX;
		}
	}
	void pushCloseCurly(){
		LinkedList<Term> lst=new LinkedList<>();
		Operator top=operators.peek();
		while(top!=OPEN_CURLY){
			reduceTopOperator();
			top=operators.peek();
		}
		operators.pop();
		operands.push(new CompoundTerm("{}",operands.pop()));
	}
	void pushCloseBracket(){
		Operator top=operators.peek();
		Term lst=EMPTY_LIST;
		while(top!=OPEN_BRACKET){
			if(top.getToken().equals(",")){
				operators.pop();
				lst=new CompoundTerm(".",operands.pop(),lst);
			}else if(top.getToken().equals("|")){
				operators.pop();
				lst=operands.pop();
			}else
				reduceTopOperator();
			top=operators.peek();
		}
		operators.pop();
		operands.push(new CompoundTerm(".",operands.pop(),lst));
	}
	private static List<Term> convertTermtoList(Term t,boolean multi){
		if(multi){
			List<Term> lst=new ArrayList<>();
			while(t instanceof CompoundTerm&&((CompoundTerm)t).getFunctor().equals(",")){
				lst.add(((CompoundTerm)t).getArguments().get(0));
				t=((CompoundTerm)t).getArguments().get(1);
			}
			lst.add(t);
			return lst;
		}else{
			return Collections.singletonList(t);
		}
	}
	void pushCloseParen(){
		Operator top=operators.peek();
		int multi=0;
		while(top!=OPEN_PAREN){
			if(top.getToken().equals(",")&&multi!=-1)
				multi=1;
			else if(top.getPriority()>1000)//1000 is the priority of ','
				multi=-1;
			reduceTopOperator();
			top=operators.peek();
		}
		operators.pop();
		if(operators.peek().getCls()==Operator.Class.PREFIX){
			List<Term> arg=convertTermtoList(operands.pop(),multi==1);
			operands.push(new CompoundTerm(operators.pop().getToken(),arg));
		}
	}
	void reduceTopOperator(){
		Operator top=operators.pop();
		if(top.getCls()==Operator.Class.INFIX){
			Term right=operands.pop();
			operands.push(new CompoundTerm(top.getToken(),operands.pop(),right));
		}else{
			Term operand=operands.pop();
			if(top.getToken().equals("-")&&operand instanceof Constant){
				Object val=((Constant)operand).getValue();
				if(val instanceof BigInteger){
					operands.push(new Constant(((BigInteger)val).negate()));
					return;
				}else if(val instanceof BigDecimal){
					operands.push(new Constant(((BigDecimal)val).negate()));
					return;
				}
			}
			operands.push(new CompoundTerm(top.getToken(),operand));
		}
	}
	void end(){
		while(operators.size()>1)
			reduceTopOperator();
		expected=ExpectClass.PREFIX;
	}
	@Override
	public String toString(){
		return "Operands="+operands+"\nOperators="+operators+"\nExpected="+expected;
	}
}