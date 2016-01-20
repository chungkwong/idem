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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PrologProcessorTest{

	public PrologProcessorTest(){
	}
	public void checkFail(String query,String data){
		Database db=new Database();
		new PrologParser(new PrologLex(data)).getRemaining().stream().forEach((pred)->db.addPredication(pred));
		Substitution subst=new Processor(new PrologParser(new PrologLex(query)).next(),db).getSubstitution();
		assertNull(subst);
	}
	public void checkSuccess(String query,String data){
		Database db=new Database();
		new PrologParser(new PrologLex(data)).getRemaining().stream().forEach((pred)->db.addPredication(pred));
		Substitution subst=new Processor(new PrologParser(new PrologLex(query)).next(),db).getSubstitution();
		assertNotNull(subst);
	}
    @Test
	public void testUnification(){
		assertTrue(new Atom(3).unities(new Atom(3),new Substitution()));
		assertFalse(new Atom(3).unities(new Atom(4),new Substitution()));
		assertTrue(new Variable("X").unities(new Variable("Y"),new Substitution()));
		assertTrue(new Variable("X").unities(new Variable("X"),new Substitution()));
		assertFalse(new Atom(3).unities(new CompoundTerm("f",Arrays.asList(new Variable("X"))),new Substitution()));
		assertFalse(new CompoundTerm("f",Arrays.asList(new Variable("X"))).unities(new Atom(3),new Substitution()));
		assertFalse(new CompoundTerm("f",Arrays.asList(new Variable("X"))).unities(new CompoundTerm("g",Arrays.asList(new Variable("X"))),new Substitution()));
		assertFalse(new CompoundTerm("f",Arrays.asList(new Variable("X"),new Variable("X"),new Variable("X")))
				.unities(new CompoundTerm("f",Arrays.asList(new Variable("Y"),new CompoundTerm("g",Arrays.asList(new Variable("Y"))),new Atom("a"))),new Substitution()));
		assertTrue(new CompoundTerm("f",Arrays.asList(new Variable("X"))).unities(new CompoundTerm("f",Arrays.asList(new Variable("X"))),new Substitution()));
		assertTrue(new CompoundTerm("f",Arrays.asList(new Variable("X"),new Variable("Y")))
				.unities(new CompoundTerm("f",Arrays.asList(new Variable("Y"),new Atom("a"))),new Substitution()));
	}
    @Test
	public void testFact(){
		checkSuccess("m(pete).","m(pete).");
	}
    @Test
	public void testRule(){
		checkFail("p(X,Y).","p(M,W):-m(M),f(W).");
	}
    @Test
	public void testControl(){
		checkSuccess("true.","");
		checkFail("fail.","");
		checkFail("call(fail).","");
		checkFail("call(fail,X).","");
	}
}
