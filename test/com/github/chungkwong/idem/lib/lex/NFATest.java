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
package com.github.chungkwong.idem.lib.lex;

import com.github.chungkwong.idem.lib.lang.common.lex.*;
import com.github.chungkwong.idem.util.*;
import org.junit.*;

/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class NFATest{
	@Test
	public void testBasicCharacterSet(){
		assertMatch("=",".");
		assertMatch(".",".");
		assertMatch("\\","\\\\");
		assertMatch("\t","\\t");
		assertMatch("\n","\\n");
		assertMatch("\r","\\r");
		assertMatch("\f","\\f");
		assertMatch("\u0007","\\a");
		assertMatch("\u001B","\\e");
		assertMatch("3","\\d");
		assertMatch("a","\\D");
		assertMatch(" ","\\h");
		assertMatch("a","\\H");
		assertMatch("\n","\\s");
		assertMatch("a","\\S");
		assertMatch("\n","\\v");
		assertMatch(" ","\\V");
		assertMatch("a","\\w");
		assertMatch("A","\\w");
		assertMatch("8","\\w");
		assertMatch("=","\\W");
		assertMatch("a","\\p{Lower}");
		assertMatch("Z","\\p{Upper}");
		assertMatch("]","\\p{ASCII}");
		assertMatch("d","\\p{Alpha}");
		assertMatch("S","\\p{Alpha}");
		assertMatch("0","\\p{Digit}");
		assertMatch("9","\\p{Alnum}");
		assertMatch("t","\\p{Alnum}");
		assertMatch("U","\\p{Alnum}");
		assertMatch("1","\\p{Graph}");
		assertMatch("e","\\p{Graph}");
		assertMatch("Y","\\p{Graph}");
		assertMatch("!","\\p{Graph}");
		assertMatch("\u0020","\\p{Print}");
		assertMatch("?","\\p{Punct}");
		assertMatch(" ","\\p{Blank}");
		assertMatch("\t","\\p{Blank}");
		assertMatch("\t","\\p{Space}");
		assertMatch("F","\\p{XDigit}");
		assertMatch("a","\\p{XDigit}");
		assertMatch("8","\\p{XDigit}");
		assertNotMatch("g","\\p{XDigit}");
		assertMatch("\u007f","\\p{Cntrl}");
		assertMatch("\u001f","\\p{Cntrl}");
		assertMatch("\u001a","\\p{Cntrl}");
		assertMatch("\u0000","\\p{Cntrl}");
		assertMatch("b","\\p{IsAlphabetic}");
		assertMatch("P","\\p{IsAlphabetic}");
		assertMatch("b","\\p{IsLetter}");
		assertMatch("P","\\p{IsLetter}");
		assertMatch("P","\\p{IsUppercase}");
		assertNotMatch("p","\\p{IsUppercase}");
		assertMatch("q","\\p{IsLowercase}");
		assertNotMatch("Q","\\p{IsLowercase}");
		assertNotMatch("Q","\\p{IsTitlecase}");
		assertNotMatch("r","\\p{IsTitlecase}");
		assertNotMatch("r","\\p{IsPunctuation}");
		assertMatch("!","\\p{IsPunctuation}");
		assertMatch("\u200C","\\p{IsJoin_Control}");
		assertMatch("\u200D","\\p{IsJoin_Control}");
		assertMatch("\f","\\p{IsControl}");
		assertMatch(" ","\\p{IsWhite_Space}");
		assertMatch("3","\\p{IsDigit}");
		assertNotMatch("a","\\p{IsDigit}");
		assertMatch("a","\\p{IsHex_Digit}");
		assertNotMatch("G","\\p{IsHex_Digit}");
		assertMatch("，","\\p{InHALFWIDTH_AND_FULLWIDTH_FORMS}");
		assertMatch("，","\\p{block=HALFWIDTH_AND_FULLWIDTH_FORMS}");
		assertMatch("，","\\p{blk=HALFWIDTH_AND_FULLWIDTH_FORMS}");
		assertNotMatch(",","\\p{InHALFWIDTH_AND_FULLWIDTH_FORMS}");
		assertMatch("，","\\p{IsCOMMON}");
		assertMatch("，","\\p{script=COMMON}");
		assertMatch("，","\\p{sc=COMMON}");
		assertNotMatch("一","\\p{IsCOMMON}");
		assertMatch("'","\\p{IsPo}");
		assertNotMatch("‘","\\p{IsPo}");
		assertMatch("\u0045","\\x45");
		assertMatch("\u045d","\\x{45d}");
		assertMatch("\u1ed0","\\u1ed0");
		assertMatch("\u0021","\\ca");
		assertMatch(" ","\\040");
	}
	@Test
	public void testComplexCharacterSet(){
		assertMatch("a","[a-c]");
		assertMatch("b","[a-c]");
		assertMatch("c","[a-c]");
		assertNotMatch("d","[a-c]");
		assertMatch("a","[a]");
		assertNotMatch("d","[a]");
		assertMatch("a","[ak]");
		assertMatch("k","[ak]");
		assertNotMatch("d","[ak]");
		assertMatch("a","[aeiou]");
		assertMatch("u","[aeiou]");
		assertMatch("o","[aeiou]");
		assertNotMatch("b","[aeiou]");
		assertMatch("a","[ax-y]");
		assertMatch("x","[ax-y]");
		assertMatch("y","[ax-y]");
		assertNotMatch("w","[ax-y]");
		assertMatch("a","[a-btx-y]");
		assertMatch("t","[a-btx-y]");
		assertMatch("y","[a-btx-y]");
		assertNotMatch("u","[a-btx-y]");
		assertMatch("c","[a-d&&[c-z]]");
		assertMatch("d","[a-d&&[c-z]]");
		assertNotMatch("e","[a-d&&[c-z]]");
		assertNotMatch("b","[a-d&&[c-z]]");
		assertMatch("c","[^abx-z]");
		assertMatch("w","[^abx-z]");
		assertNotMatch("y","[^abx-z]");
		assertNotMatch("b","[^abx-z]");
	}
	@Test
	public void testQuantity(){
		assertMatch("","a*");
		assertMatch("a","a*");
		assertMatch("aa","a*");
		assertMatch("aaa","a*");
		assertNotMatch("","a+");
		assertMatch("a","a+");
		assertMatch("aa","a+");
		assertMatch("aaa","a+");
		assertMatch("aa","a{2}");
		assertNotMatch("","a{2}");
		assertNotMatch("a","a{2}");
		assertNotMatch("aaa","a{2}");
		assertMatch("aa","a{2,}");
		assertNotMatch("","a{2,}");
		assertNotMatch("a","a{2,}");
		assertMatch("aaa","a{2,}");
		assertMatch("aaaa","a{2,}");
		assertMatch("aa","a{2,3}");
		assertNotMatch("","a{2,3}");
		assertNotMatch("a","a{2,3}");
		assertMatch("aaa","a{2,3}");
		assertNotMatch("aaaa","a{2,3}");
		assertMatch("a","a?");
		assertMatch("","a?");
		assertNotMatch("aa","a?");
	}

    @Test
	public void testOther(){
		assertMatch("","");
		assertMatch("a","a");
		assertMatch("aabc","aabc");
		assertMatch("aa","aa|bc");
		assertMatch("bc","aa|bc");
		assertNotMatch("ac","aa|bc");
		assertNotMatch("a","");
		assertNotMatch("","a");
		assertNotMatch("b","a");
		assertNotMatch("aa","a");
		assertNotMatch("aa","aabc");
		assertNotMatch("abc","aabc");
		assertMatch("ab","(ab)+");
		assertMatch("abab","(ab)+");
		assertNotMatch("aba","(ab)+");
		assertNotMatch("","(ab)+");
	}
	private static void assertMatch(String str,String regex){
		Assert.assertTrue(isMatch(str,regex));
	}
	private static void assertNotMatch(String str,String regex){
		Assert.assertFalse(isMatch(str,regex));
	}
	private static boolean isMatch(String str,String regex){
		NFA machine=RegularExpression.parseRegularExpression(regex).toNFA();
		machine.prepareForRun();
		return machine.isAccepted(new IntCheckPointIterator(str.codePoints().iterator()));
	}
}
