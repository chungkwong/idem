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

import java.math.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PrologLexTest{

	public PrologLexTest(){
	}
	public Object getSingleToken(String input){
		PrologLex lex=new PrologLex(input);
		Object ret=lex.next();
		assertNull(lex.next());
		return ret;
	}
	private void expectSingle(String input,Object output){
		assertEquals(getSingleToken(input),output);
	}
	private void expectSingleValue(String input,Comparable output){
		assertTrue(((Comparable)getSingleToken(input)).compareTo(output)==0);
	}
	@Test
	public void testBackquoteToken() throws Exception{
		expectSingle(("``"),"");
		expectSingle(("`hello`"),"hello");
		expectSingle(("`guess\\\nwhat`"),"guesswhat");
		expectSingle(("`guess\\\rwhat`"),"guesswhat");
		expectSingle(("`guess\\\r\nwhat`"),"guesswhat");
		expectSingle(("`guess\nwhat`"),"guess\nwhat");
		expectSingle(("````"),"`");
	}
	@Test
	public void testDoublequoteToken() throws Exception{
		expectSingle(("\"\""),"");
		expectSingle(("\"hello\""),"hello");
		expectSingle(("\"guess\\\nwhat\""),"guesswhat");
		expectSingle(("\"guess\\\rwhat\""),"guesswhat");
		expectSingle(("\"guess\\\r\nwhat\""),"guesswhat");
		expectSingle(("\"guess\nwhat\""),"guess\nwhat");
		expectSingle(("\"\"\"\""),"\"");
	}
	@Test
	public void testSinglequoteToken() throws Exception{
		expectSingle(("\'\'"),"");
		expectSingle(("\'hello\'"),"hello");
		expectSingle(("\'guess\\\nwhat\'"),"guesswhat");
		expectSingle(("\'guess\\\rwhat\'"),"guesswhat");
		expectSingle(("\'guess\\\r\nwhat\'"),"guesswhat");
		expectSingle(("\'guess\nwhat\'"),"guess\nwhat");
		expectSingle(("\'\'\'\'"),"\'");
	}
	@Test
	public void testFloatToken() throws Exception{
		expectSingleValue(("2."),new BigDecimal("2"));
		expectSingleValue(("2.5"),new BigDecimal("2.5"));
		expectSingleValue(("12.056"),new BigDecimal("12.056"));
		expectSingleValue(("2.5e4"),new BigDecimal("2.5e4"));
		expectSingleValue(("2.5E4"),new BigDecimal("2.5E4"));
		expectSingleValue(("2.5E0"),new BigDecimal("2.5"));
		expectSingleValue(("2.5e-3"),new BigDecimal("2.5e-3"));
		expectSingleValue(("0.6e+4"),new BigDecimal("0.6e+4"));
		expectSingleValue(("0.02e10"),new BigDecimal("0.02e10"));
	}
	@Test
	public void testIntegerToken() throws Exception{
		expectSingleValue(("0"),new BigInteger("0"));
		expectSingleValue(("9"),new BigInteger("9"));
		expectSingleValue(("923"),new BigInteger("923"));
		expectSingleValue(("0923"),new BigInteger("923"));
		expectSingleValue(("0b01101"),new BigInteger("13"));
		expectSingleValue(("0o107"),new BigInteger("71"));
		expectSingleValue(("0xF3"),new BigInteger("243"));
		expectSingleValue(("0\'\'\'"),BigInteger.valueOf('\''));
		expectSingleValue(("0\'a"),BigInteger.valueOf('a'));
		expectSingleValue(("0\'人"),BigInteger.valueOf('人'));
		expectSingleValue(("0\'\\\\"),BigInteger.valueOf('\\'));
		expectSingleValue(("0\'\\a"),BigInteger.valueOf('\u0007'));
		expectSingleValue(("0\'\\b"),BigInteger.valueOf('\b'));
		expectSingleValue(("0\'\\f"),BigInteger.valueOf('\f'));
		expectSingleValue(("0\'\\n"),BigInteger.valueOf('\n'));
		expectSingleValue(("0\'\\r"),BigInteger.valueOf('\r'));
		expectSingleValue(("0\'\\t"),BigInteger.valueOf('\t'));
		expectSingleValue(("0\'\\v"),BigInteger.valueOf('\u000B'));
		expectSingleValue(("0\'\\65\\"),BigInteger.valueOf(065));
		expectSingleValue(("0\'\\xF2E0\\"),BigInteger.valueOf(0xF2E0));
		expectSingleValue(("0\'\n"),BigInteger.valueOf('\n'));
	}
	@Test
	public void testVariable() throws Exception{
		expectSingle(("_"),Variable.WILDCARD);
		expectSingle(("_32d5E"),new Variable("_32d5E"));
		expectSingle(("_f32d5E"),new Variable("_f32d5E"));
		expectSingle(("_K32d5E"),new Variable("_K32d5E"));
		expectSingle(("Animal"),new Variable("Animal"));
		expectSingle(("Ani5mal"),new Variable("Ani5mal"));
	}
	@Test
	public void testIdentifier() throws Exception{
		expectSingle(("!"),"!");
		expectSingle((";"),";");
		expectSingle(("a4Tgssw"),"a4Tgssw");
		expectSingle(("k"),"k");
		expectSingle(("g65"),"g65");
		String graphic="\\#$&*+-./:<=>?@^~";
		for(int i=0;i<graphic.length();i++){
			String c=graphic.substring(i,i+1);
			expectSingle((c),c);
			for(int j=0;j<graphic.length();j++){
				String d=graphic.substring(j,j+1);
				expectSingle((d),d);
			}
		}
	}
	@Test
	public void testComment()throws Exception{
		assertNull(getSingleToken(" "));
		assertNull(getSingleToken("\r"));
		assertNull(getSingleToken("\n"));
		assertNull(getSingleToken("\r\n"));
		assertNull(getSingleToken("%fyr8tg 5y*/8"));
		assertNull(getSingleToken("\n%fyr8tg 5y*/8\n"));
		assertNull(getSingleToken("/*/*/"));
		assertNull(getSingleToken("/***/"));
		assertNull(getSingleToken("/**fhe 4/u%yy*/"));
		assertNull(getSingleToken("/*\ng4g\r\n*/"));
		expectSingle(("%hdfhui\nhello"),"hello");
		expectSingle(("%hdfhui\rhello"),"hello");
		expectSingle(("%hdfhui\r\nhello"),"hello");
		expectSingle(("hello%hdfhui"),"hello");
		expectSingle(("hello/*gdf*/"),"hello");
		expectSingle(("/*g\ndf*/hello"),"hello");
	}
}