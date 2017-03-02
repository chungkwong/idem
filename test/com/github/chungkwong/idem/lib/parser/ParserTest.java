/*
 * Copyright (C) 2017 Chan Chung Kwong <1m02math@126.com>
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
package com.github.chungkwong.idem.lib.parser;

import com.github.chungkwong.idem.lib.lang.common.lex.*;
import com.github.chungkwong.idem.lib.lang.common.parser.*;
import com.github.chungkwong.idem.util.*;
import java.util.*;
import org.junit.*;

/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ParserTest{
	public ParserTest(){
	}
	@Test
	public void testAddition(){
		Terminal number=new Terminal("NUMBER"),operator=new Terminal("OP");
		LexFactory lex=new RegularExpressionLexFactory();
		lex.addTokenType(number,"[0-9]+",Integer::parseInt);
		lex.addTokenType(operator,"[+]",(t)->t);
		NonTerminal start=new NonTerminal("START");
		List<ProductionRule> rules=new ArrayList<>();
		rules.add(new ProductionRule(start,new Symbol[]{number},(a)->a[0]));
		rules.add(new ProductionRule(start,new Symbol[]{start,operator,start},(a)->(Integer)a[0]+(Integer)a[2]));
		ContextFreeGrammar grammar=new ContextFreeGrammar(start,rules);
		Parser parser=new NaiveParser(grammar);
		assertParseTo("6",6,parser,lex);
		assertParseTo("6+7",13,parser,lex);
		assertParseTo("6+5+7",18,parser,lex);
	}
	private static void assertParseTo(String code,Object result,ContextFreeGrammar grammar,LexFactory lex){
		assertParseTo(code,result,new NaiveParser(grammar),lex);
	}
	private static void assertParseTo(String code,Object result,Parser parser,LexFactory lex){
		Lex tokenizer=lex.createLex(new IntCheckPointIterator(code.codePoints().iterator()));
		Assert.assertEquals(parser.parse(tokenizer),result);
	}
}
