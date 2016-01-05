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
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class OperatorTable{
	private static final OperatorTable DEFAULT_OPERATOR_TABLE=new OperatorTable();
	public static OperatorTable getDEFAULT_OPERATOR_TABLE(){
		return DEFAULT_OPERATOR_TABLE;
	}
	private final TreeMap<String,Operator> infixOperators=new TreeMap<>();
	private final TreeMap<String,Operator> prefixOperators=new TreeMap<>();
	private final TreeMap<String,Operator> postfixOperators=new TreeMap<>();
	static{
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(":-",1200,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("-->",1200,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(":-",1200,Operator.Class.PREFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("?-",1200,Operator.Class.PREFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(";",1100,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("->",1050,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(",",1000,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("\\=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("==",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("\\==",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("@<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("@=<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("@>",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("@>=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("=..",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("is",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("=:=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("=\\=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("=<",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(">",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(">=",700,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("+",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("-",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("/\\",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("\\/",500,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("*",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("/",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("//",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("rem",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("mod",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("<<",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(">>",400,Operator.Class.INFIX,Operator.Associativity.LEFT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("**",200,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("^",200,Operator.Class.INFIX,Operator.Associativity.RIGHT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("-",200,Operator.Class.PREFIX,Operator.Associativity.RIGHT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("\\",200,Operator.Class.PREFIX,Operator.Associativity.RIGHT));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator("@",100,Operator.Class.INFIX,Operator.Associativity.NO));
		getDEFAULT_OPERATOR_TABLE().addOperator(new Operator(":",50,Operator.Class.INFIX,Operator.Associativity.NO));
	}
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
	public TreeMap<String,Operator> getInfixOperators(){
		return infixOperators;
	}
	public TreeMap<String,Operator> getPrefixOperators(){
		return prefixOperators;
	}
	public TreeMap<String,Operator> getPostfixOperators(){
		return postfixOperators;
	}


}