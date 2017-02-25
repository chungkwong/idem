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
	public void testCharacterSet(){
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
	}
    @Test
	public void testMatch(){
		assertMatch("","");
		assertMatch("a","a");
		assertMatch("aabc","aabc");
		assertNotMatch("a","");
		assertNotMatch("","a");
		assertNotMatch("b","a");
		assertNotMatch("aa","a");
		assertNotMatch("aa","aabc");
		assertNotMatch("abc","aabc");
	}
	private static void assertMatch(String str,String regex){
		Assert.assertTrue(isMatch(str,regex));
	}
	private static void assertNotMatch(String str,String regex){
		Assert.assertFalse(isMatch(str,regex));
	}
	private static boolean isMatch(String str,String regex){
		return RegularExpression.parseRegularExpression(regex).toNFA().isAccepted(new IntCheckPointIterator(str.codePoints().iterator()));
	}
}
