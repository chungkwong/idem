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
import java.util.*;
/**
 * Operator table for Prolog
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class OperatorTable{
	/** the standard Operator table */
	public static final OperatorTable DEFAULT_OPERATOR_TABLE=new OperatorTable();
	private final Map<String,Operator> infixOperators=new HashMap<>();
	private final Map<String,Operator> prefixOperators=new HashMap<>();
	private final Map<String,Operator> postfixOperators=new HashMap<>();
	static{
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(":-",1200,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("-->",1200,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(":-",1200,Operator.Class.PREFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("?-",1200,Operator.Class.PREFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(";",1100,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("->",1050,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(",",1000,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("|",1000,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("\\=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("==",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("\\==",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("@<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("@=<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("@>",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("@>=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("=..",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("is",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("=:=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("=\\=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("=<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(">",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(">=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("+",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("-",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("/\\",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("\\/",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("*",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("/",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("//",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("rem",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("mod",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("<<",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(">>",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("**",200,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("^",200,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("-",200,Operator.Class.PREFIX,Operator.Associativity.RIGHT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("\\",200,Operator.Class.PREFIX,Operator.Associativity.RIGHT));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator("@",100,Operator.Class.INFIX,Operator.Associativity.NO));
		DEFAULT_OPERATOR_TABLE.addOperator(new Operator(":",50,Operator.Class.INFIX,Operator.Associativity.NO));
	}
	/**
	 * Register a operator
	 * @param operator to be register
	 */
	public void addOperator(Operator operator){
		switch(operator.getCls()){
			case INFIX:
				getInfixOperators().put(operator.getToken(),operator);
				break;
			case PREFIX:
				getPrefixOperators().put(operator.getToken(),operator);
				break;
			case POSTFIX:
				getPostfixOperators().put(operator.getToken(),operator);
				break;
		}
	}
	/**
	 * @return the infix operators indexed by their specifier
	 */
	public Map<String,Operator> getInfixOperators(){
		return infixOperators;
	}
	/**
	 * @return the prefix operators indexed by their specifier
	 */
	public Map<String,Operator> getPrefixOperators(){
		return prefixOperators;
	}
	/**
	 * @return the postfix operators indexed by their specifier
	 */
	public Map<String,Operator> getPostfixOperators(){
		return postfixOperators;
	}
}