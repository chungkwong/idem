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
import static com.github.chungkwong.idem.lib.lang.prolog.PrologProcessorTest.assertGoalSuccess;
import org.junit.*;

/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class EvaluableFunctorTest{
	public EvaluableFunctorTest(){
	}
	@Test
	public void testAdd(){
		assertGoalSuccess("X is 7+35,X=42 .","");
		assertGoalSuccess("X is 0+(3+11),X=14 .","");
		assertGoalSuccess("X is 0+(3.2+11),X=14.2 .","");
		assertGoalError("X is 77+N .","");
		assertGoalError("X is N+77 .","");
		assertGoalError("X is foo+77 .","");
		assertGoalError("X is 77+foo .","");
	}
	@Test
	public void Negate(){
		assertGoalSuccess("X is '-'(7),X= -7 .","");
		assertGoalSuccess("X is -(3-11),X=8 .","");
		assertGoalSuccess("X is -(3.2-11),X=7.8 .","");
		assertGoalError("X is -N .","");
		assertGoalError("X is -foo .","");
	}
	@Test
	public void testSubtract(){
		assertGoalSuccess("X is 7-35,X= -28 .","");
		assertGoalSuccess("X is '-'(20,3+11),X=6 .","");
		assertGoalSuccess("X is '-'(0,3.2+11),X= -14.2 .","");
		assertGoalError("X is '-'(77,N).","");
		assertGoalError("X is '-'(foo,77).","");
	}
	@Test
	public void testMultiply(){
		assertGoalSuccess("X is 7*35,X=245 .","");
		assertGoalSuccess("X is '*'(1.5,3.2+11),X=21.30 .","");
		assertGoalSuccess("X is '*'(0,3+11),X=0 .","");
		assertGoalError("X is '*'(77,N).","");
		assertGoalError("X is '*'(foo,77).","");
	}
	@Test
	public void testDivide(){
		assertGoalSuccess("X is 7/35,X=:=0 .","");
		assertGoalSuccess("X is 7.0/35,X=:=0.2 .","");
		assertGoalSuccess("X is 140/(3+11),X=:=10 .","");
		assertGoalError("X is '/'(77,N).","");
		assertGoalError("X is '/'(foo,77).","");
		assertGoalError("X is '/'(3,0).","");
	}
	@Test
	public void testMod(){
		assertGoalSuccess("X is mod(7,3),X=1 .","");
		assertGoalSuccess("X is mod(0,3+11),X=0 .","");
		assertGoalError("X is mod(77,N).","");
		assertGoalError("X is mod(foo,77).","");
		assertGoalError("X is mod(7.5,2).","");
		assertGoalError("X is mod(7,0).","");
	}
	@Test
	public void testFloor(){
		assertGoalSuccess("X is floor(7.4),X=7 .","");
		assertGoalSuccess("X is floor(0.6),X= 0 .","");
		assertGoalSuccess("X is floor(-0.04),X= -1 .","");
		assertGoalSuccess("X is floor(-1),X= -1 .","");
		assertGoalSuccess("X is floor(0),X= 0 .","");
	}
	@Test
	public void testRound(){
		assertGoalSuccess("X is round(7.5),X=8 .","");
		assertGoalSuccess("X is round(7.6),X=8 .","");
		assertGoalSuccess("X is round(-0.6),X= -1 .","");
		assertGoalSuccess("X is round(7.4),X=7 .","");
		assertGoalSuccess("X is round(0.6),X=1 .","");
		assertGoalSuccess("X is round(-0.04),X=0 .","");
		assertGoalSuccess("X is round(-1),X= -1 .","");
		assertGoalSuccess("X is round(0),X= 0 .","");
		assertGoalError("X is round(N).","");
	}
	@Test
	public void testCeiling(){
		assertGoalSuccess("X is ceiling(7.4),X=8 .","");
		assertGoalSuccess("X is ceiling(0.6),X=1 .","");
		assertGoalSuccess("X is ceiling(-0.04),X=0 .","");
		assertGoalSuccess("X is ceiling(-1),X= -1 .","");
		assertGoalSuccess("X is ceiling(0),X= 0 .","");
	}
	@Test
	public void testTruncate(){
		assertGoalSuccess("X is truncate(7.4),X=7 .","");
		assertGoalSuccess("X is truncate(0.6),X=0 .","");
		assertGoalSuccess("X is truncate(-0.04),X=0 .","");
		assertGoalSuccess("X is truncate(-1),X= -1 .","");
		assertGoalSuccess("X is truncate(0),X= 0 .","");
		assertGoalSuccess("X is truncate(-0.5),X=0 .","");
		assertGoalError("X is truncate(foo).","");
	}
	@Test
	public void testFloat(){
		assertGoalSuccess("X is float(7.3),X=:=7.3 .","");
		assertGoalSuccess("X is float(7),X=:=7.0 .","");
		assertGoalSuccess("X is float(5/3),X=:=1.0 .","");
		assertGoalError("X is float(foo).","");
		assertGoalError("X is float(N).","");
	}
	@Test
	public void testAbs(){
		assertGoalSuccess("X is abs(7),X=7 .","");
		assertGoalSuccess("X is abs(3-11),X=8 .","");
		assertGoalSuccess("X is abs(3.2-11.0),X=7.8 .","");
		assertGoalError("X is abs(foo).","");
		assertGoalError("X is abs(N).","");
	}
	@Test
	public void testSqrt(){
		assertGoalSuccess("X is sqrt(0),X=:=0 .","");
		assertGoalSuccess("X is sqrt(4.0),X=:=2.0 .","");
		assertGoalSuccess("X is sqrt(0.0),X=:=0.0 .","");
		assertGoalSuccess("X is sqrt(1.0),X=:=1.0 .","");
		assertGoalError("X is abs(foo).","");
		assertGoalError("X is abs(N).","");
		assertGoalError("X is abs(N-1.","");
	}
	@Test
	public void testPow(){
		assertGoalSuccess("X is '**'(5,3),X=:=125 .","");
		assertGoalSuccess("X is '**'(5,3.0),X=:=125 .","");
		assertGoalSuccess("X is '**'(-5.0,3),X=:= -125 .","");
		assertGoalSuccess("X is '**'(5,-1),X=:=0.2 .","");
		assertGoalSuccess("X is '**'(0.0,0),X=:=1.0 .","");
		assertGoalError("X is '**'(77,N).","");
		assertGoalError("X is '**'(foo,2).","");
		assertGoalError("X is '**'(foo,2).","");
	}
	@Test
	public void testSin(){
		assertGoalSuccess("X is sin(0.0),X=:=0.0 .","");
		assertGoalSuccess("X is sin(0),X=:=0.0 .","");
		assertGoalError("X is sin(N).","");
		assertGoalError("X is sin(foo).","");
	}
	@Test
	public void testCos(){
		assertGoalSuccess("X is cos(0.0),X=:=1.0 .","");
		assertGoalSuccess("X is cos(0),X=:=1.0 .","");
		assertGoalError("X is cos(N).","");
		assertGoalError("X is cos(foo).","");
	}
	@Test
	public void testAtan(){
		assertGoalSuccess("X is atan(0.0),X=:=0.0 .","");
		assertGoalSuccess("X is atan(1),sin(X)=:=cos(X) .","");
		assertGoalError("X is atan(N).","");
		assertGoalError("X is atan(foo).","");
	}
	@Test
	public void testExp(){
		assertGoalSuccess("X is exp(0.0),X=:=1 .","");
		assertGoalSuccess("X is exp(0),X=:=1 .","");
		assertGoalSuccess("X is exp(1),X=:=2.718 .","");
		assertGoalError("X is exp(N).","");
		assertGoalError("X is exp(foo).","");
	}
	@Test
	public void testLog(){
		assertGoalSuccess("X is log(1.0),X=:=0.0 .","");
		//assertGoalSuccess("X is log(2.718),X=:=1.0 .","");
		assertGoalError("X is log(N).","");
		assertGoalError("X is log(foo).","");
		assertGoalError("X is log(0).","");
		assertGoalError("X is log(0.0).","");
	}
	@Test
	public void testShiftRight(){
		assertGoalSuccess("X is '>>'(16,2),X=:=4 .","");
		assertGoalSuccess("X is '>>'(19,2),X=:=4 .","");
		assertGoalSuccess("X is '<<'(-16,2).","");
		assertGoalError("X is '>>'(77,N) .","");
		assertGoalError("X is '>>'(foo,2) .","");
	}
	@Test
	public void testShiftLeft(){
		assertGoalSuccess("X is '<<'(16,2),X=:=64 .","");
		assertGoalSuccess("X is '<<'(19,2),X=:=76 .","");
		assertGoalSuccess("X is '<<'(-19,2).","");
		assertGoalError("X is '<<'(77,N) .","");
		assertGoalError("X is '<<'(foo,2) .","");
	}
	@Test
	public void testAnd(){
		assertGoalSuccess("X is /\\(10,12),X=:=8 .","");
		assertGoalSuccess("X is '/\\\\'(10,12),X=:=8 .","");
		assertGoalSuccess("X is '/\\\\'(17*256+125,255),X=:=125 .","");
		assertGoalSuccess("X is '/\\\\'(-10,12).","");
		assertGoalError("X is '/\\\\'(77,N) .","");
		assertGoalError("X is '/\\\\'(foo,2) .","");
	}
	@Test
	public void testOr(){
		assertGoalSuccess("X is \\/(10,12),X=:=14 .","");
		assertGoalSuccess("X is '\\\\/'(10,12),X=:=14 .","");
		assertGoalSuccess("X is '\\\\/'(125,255),X=:=255 .","");
		assertGoalSuccess("X is '\\\\/'(-10,10) .","");
		assertGoalError("X is '\\\\/'(77,N) .","");
		assertGoalError("X is '\\\\/'(foo,2) .","");
	}
	@Test
	public void testComplement(){
		assertGoalSuccess("X is '\\\\'('\\\\'(10)),X=:=10 .","");
		assertGoalSuccess("X is \\( \\(10)),X=:=10 .","");
		assertGoalSuccess("X is \\(10).","");
		assertGoalError("X is \\(N).","");
		assertGoalError("X is \\(foo).","");
	}
}
