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

import static com.github.chungkwong.idem.lib.lang.prolog.ProcessorTest.assertGoalError;
import static com.github.chungkwong.idem.lib.lang.prolog.ProcessorTest.assertGoalFail;
import static com.github.chungkwong.idem.lib.lang.prolog.ProcessorTest.assertGoalSuccess;
import java.math.*;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.*;

/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class MiscTest{
    @Test
	public void testUnification(){
		assertTrue(new Constant(3).unities(new Constant(3),new Substitution()));
		assertTrue(new Constant(BigInteger.valueOf(3)).unities(new Constant(BigInteger.valueOf(3)),new Substitution()));
		assertFalse(new Constant(3).unities(new Constant(4),new Substitution()));
		assertTrue(new Variable("X").unities(new Variable("Y"),new Substitution()));
		assertTrue(new Variable("X").unities(new Variable("X"),new Substitution()));
		assertFalse(new Constant(3).unities(new CompoundTerm("f",new Variable("X")),new Substitution()));
		assertFalse(new CompoundTerm("f",new Variable("X")).unities(new Constant(3),new Substitution()));
		assertFalse(new CompoundTerm("f",new Variable("X")).unities(new CompoundTerm("g",Arrays.asList(new Variable("X"))),new Substitution()));
		assertFalse(new CompoundTerm("f",new Variable("X"),new Variable("X"),new Variable("X"))
				.unities(new CompoundTerm("f",new Variable("Y"),new CompoundTerm("g",new Variable("Y")),new Constant("a")),new Substitution()));
		assertTrue(new CompoundTerm("f",new Variable("X")).unities(new CompoundTerm("f",new Variable("X")),new Substitution()));
		assertTrue(new CompoundTerm("f",new Variable("X"),new Variable("Y"))
				.unities(new CompoundTerm("f",new Variable("Y"),new Constant("a")),new Substitution()));
	}
    @Test
	public void testFact(){
		assertGoalSuccess("m(pete).","m(pete).");
	}
    @Test
	public void testRule(){
		assertGoalError("p(X,Y).","p(M,W):-m(M),f(W).");
	}
	@Test
	public void testCorner(){
		assertGoalFail("p(c).","p(a).p(b).");
		assertGoalError("p(X,Y).","p(M,W):-m(M),f(W).");
	}
	@Test
	public void testEvaluale(){
		assertGoalError("X is 3+Y.","");
		assertGoalError("X is hello(7).","");
	}
}
