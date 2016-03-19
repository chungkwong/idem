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
import org.junit.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class DirectiveTest extends ProcessorTest{
	@Test
	public void testCharConversion(){
		assertGoalSuccess("X is 2+3,X=:=6 .","char_conversion('+','*').");
	}
	@Test
	public void testDiscontigous(){
		assertGoalSuccess("insect(ann).","discontiguous(insect/1).insect(ann).");
		assertGoalSuccess("insect(ann).","cat.discontiguous(insect/1).discontiguous(insect/1).insect(ann).");
	}
	@Test
	public void testDynamic(){
		assertGoalSuccess("clause(cat,true).","dynamic(cat/0).cat.");
		assertGoalError("clause(cat,true).","cat.");
	}
	@Test
	public void testEnsureLoaded(){
		String load="ensure_loaded('src/com/github/chungkwong/idem/lib/lang/prolog/directive/IncludeTest.prolog').";
		assertGoalSuccess("dog.",load);
		assertGoalSuccess("double(3,Y),Y=:=6 .",load);
		assertGoalSuccess("double(3,Y),Y=:=6 .",load+load);
		assertGoalSuccess("dog.",load+load);
		assertGoalError("dog.","");
	}
	@Test
	public void testInclude(){
		String load="include('src/com/github/chungkwong/idem/lib/lang/prolog/directive/IncludeTest.prolog').";
		assertGoalSuccess("dog.",load);
		assertGoalSuccess("double(3,Y),Y=:=6 .",load);
		assertGoalSuccess("double(3,Y),Y=:=9 .",load+load);
		assertGoalError("dog.","");
	}
	@Test
	public void testInitialization(){
		assertGoalFail("dog.","initialization(set_prolog_flag(undefined_predicate,fail)).");
		assertGoalError("dog.","");
		try{
			assertGoalFail("dog.","initialization(4).");
		}catch(Throwable t){
			return;
		}
		Assert.assertTrue(false);
	}
	@Test
	public void testMultiFile(){
		assertGoalSuccess("insect(ann).","multifile(insect/1).insect(ann).");
		assertGoalSuccess("insect(ann).","cat.multifile(insect/1).multifile(insect/1).insect(ann).");
	}
	@Test
	public void testOp(){
		assertGoalSuccess("X is 2+3*4,X=:=20 .","op(400,yfx,'+').");
		assertGoalSuccess("X is 2+3*4,X=:=20 .","op(500,yfx,'*').");
		assertGoalSuccess("X is 2**2**3,X=:=64 .","op(200,yfx,'**').");
	}
}
