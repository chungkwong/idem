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

import java.io.*;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ParserTest{
	public ParserTest(){
	}
	private void assertParse(String in,String out){
		assertEquals(new PrologParser(new PrologLex(new StringReader(in))).getRemaining().toString(),out);
	}
	@Test
	public void testText(){
		assertParse("fact.","[fact]");
		assertParse("a:-b,c.","[:-(a,,(b,c))]");
		assertParse("?-d(X),e(Y,Z).","[?-(,(d(X),e(Y,Z)))]");
	}
	@Test
	public void testBracket(){
		assertParse("[]","[[]]");
		assertParse("[a]","[.(a,[])]");
		assertParse("[a,b]","[.(a,.(b,[]))]");
		assertParse("[a,b,c]","[.(a,.(b,.(c,[])))]");
		assertParse("[a|b]","[.(a,b)]");
		assertParse("[a,b|c]","[.(a,.(b,c))]");
		assertParse("{a}","[{}(a)]");
		assertParse("{}(5)","[{}(5)]");
		assertParse("{hello}","[{}(hello)]");
		assertParse("{{4*5}+1}","[{}(+({}(*(4,5)),1))]");
		assertParse(".(1,2)","[.(1,2)]");
		assertParse("([],[])","[,([],[])]");
		assertParse("(([],[]),[],([]))","[,(,([],[]),,([],[]))]");
		assertParse("f()","[f()]");
		assertParse("sin(x)","[sin(x)]");
		assertParse("sin((x))","[sin(x)]");
		assertParse("sin((x,y))","[sin(,(x,y))]");
		assertParse("sin(x,y,z)","[sin(x,y,z)]");
		assertParse("sin((x,y,z))","[sin(,(x,,(y,z)))]");
	}
	@Test
	public void testOperator(){
		assertParse("1+2","[+(1,2)]");
		assertParse("1+2+3","[+(+(1,2),3)]");
		assertParse("1+(2+3)","[+(1,+(2,3))]");
		assertParse("(1+2)+3","[+(+(1,2),3)]");
		assertParse("((1+2))+3","[+(+(1,2),3)]");
		assertParse("2^3^4","[^(2,^(3,4))]");
		assertParse("1+2*3","[+(1,*(2,3))]");
		assertParse("1*2+3","[+(*(1,2),3)]");
		assertParse("2*(3+4)","[*(2,+(3,4))]");
		assertParse("-1","[-1]");
		assertParse("-X","[-(X)]");
		assertParse("- -X","[-(-(X))]");
		assertParse("- -2","[2]");
		assertParse("1+5^3*2","[+(1,*(^(5,3),2))]");
	}
}
