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
import static com.github.chungkwong.idem.lib.lang.prolog.PrologProcessorTest.assertGoalError;
import static com.github.chungkwong.idem.lib.lang.prolog.PrologProcessorTest.assertGoalFail;
import static com.github.chungkwong.idem.lib.lang.prolog.PrologProcessorTest.assertGoalSuccess;
import static com.github.chungkwong.idem.lib.lang.prolog.PrologProcessorTest.multiquery;
import static org.junit.Assert.assertTrue;
import org.junit.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PrologPredicateTest{
	@Test
	public void testJavaInteraction(){
		assertGoalSuccess("is_null(X).","");
		assertGoalFail("is_null('hello').","");
		assertGoalSuccess("java_new(X,'java.lang.String',['hello']).","");
		assertGoalSuccess("java_field_static(X,'java.lang.Math','E').","");
		assertGoalSuccess("java_field(X,4,'ONE').","");
		assertGoalSuccess("java_invoke_static(X,'java.lang.Integer','parseInt',['230']).","");
		assertGoalSuccess("java_invoke(X,'hello','replaceAll',['l','kk']).","");
		assertGoalSuccess("java_cast(X,'hello','java.lang.Object').","");
	}
	@Test
	public void testRule(){
		assertGoalError("p(X,Y).","p(M,W):-m(M),f(W).");
	}
	@Test
	public void testCorner(){
		assertTrue(multiquery("p(X,Y).","p(M,W):-m(M),f(W).","fail").isEmpty());
		assertGoalFail("p(c).","p(a).p(b).");
	}
}
