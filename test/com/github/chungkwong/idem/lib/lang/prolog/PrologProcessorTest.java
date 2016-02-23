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
import static org.junit.Assert.assertTrue;
import org.junit.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PrologProcessorTest{

	public PrologProcessorTest(){
	}
	private List<Substitution> multiquery(String query,String data,String mode){
		Database db=new Database();
		db.getFlag("undefined_predicate").setValue(new Constant(mode));
		new PrologParser(new PrologLex(data)).getRemaining().stream().forEach((pred)->db.addPredication(pred));
		List<Substitution> substs=new ArrayList<>();
		Processor processor=new Processor(new PrologParser(new PrologLex(query)).next(),db);
		while(processor.getSubstitution()!=null){
			substs.add(processor.getSubstitution());
			processor.reexecute();
		}
		//System.out.println(substs);
		return substs;
	}
	private List<Substitution> multiquery(String query,String data){
		return multiquery(query,data,"error");
	}
	private void assertSuccessCount(String query,String data,int count){
		Assert.assertEquals(multiquery(query,data).size(),count);
	}
	private void assertGoalFail(String query,String data){
		assertSuccessCount(query,data,0);
	}
	private void assertGoalSuccess(String query,String data){
		Assert.assertTrue(multiquery(query,data).size()!=0);
	}
	private void assertGoalError(String query,String data){
		Database db=new Database();
		new PrologParser(new PrologLex(data)).getRemaining().stream().forEach((pred)->db.addPredication(pred));
		try{
			Substitution subst=new Processor(new PrologParser(new PrologLex(query)).next(),db).getSubstitution();
			Assert.assertTrue(false);
		}catch(Exception ex){

		}
	}
    @Test
	public void testUnification(){
		assertTrue(new Constant(3).unities(new Constant(3),new Substitution()));
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
	public void testTrue(){
		assertGoalSuccess("true.","");
	}
	@Test
	public void testFail(){
		assertGoalFail("fail.","");
	}
    @Test
	public void testCall(){
		assertGoalSuccess("true.","");
		assertGoalFail("fail.","");
		assertGoalFail("call(fail).","");
		assertGoalFail("call((fail,X)).","");
		assertGoalFail("call((fail,call(1))).","");
		assertGoalError("b(_).","b(X):-Y=(write(X),X),call(Y).");
		assertGoalError("b(3).","b(X):-Y=(write(X),X),call(Y).");
		assertGoalError("call((write(3),X)).","");
		assertGoalError("call((write(3),call(1))).","");
		assertGoalError("call(X).","");
		assertGoalError("call(1).","");
		assertGoalError("call((fail,1)).","");
		assertGoalError("call((write(3),1)).","");
		assertGoalError("call((1;true)).","");
		assertSuccessCount("Z=! ,call((Z=!,a(X),Z)).","a(1).a(2).",1);
		assertSuccessCount("call((Z=!,a(X),Z)).","a(1).a(2).",2);
	}
	@Test
	public void testCut(){
		assertGoalSuccess("!.","");
		assertGoalFail("(!,fail;true).","");
		assertGoalSuccess("(call(!),fail;true).","");
	}
	@Test
	public void testConjunction(){
		//assertGoalFail("X=1,var(X).","");
		//assertGoalSuccess("var(X),X=1.","");
		assertGoalSuccess("X=foo,call(X).","foo.");
		assertGoalSuccess("X=true,call(X).","");
	}
	@Test
	public void testDisjunction(){
		assertGoalSuccess("true;fail.","");
		assertGoalFail("(!,fail);true.","");
		assertGoalSuccess("!;call(3).","foo.");
		assertGoalSuccess("(X=2,!);X=2.","");
	}
	@Test
	public void testIf(){
		assertGoalSuccess("\'->\'(true,true).","");
		assertGoalFail("\'->\'(true,fail).","");
		assertGoalFail("\'->\'(fail,true).","");
		assertGoalSuccess("\'->\'(true,X=1).","");
		assertSuccessCount("\'->\'(true,(X=1;X=2))","",2);
		assertSuccessCount("\'->\'((X=1;X=2),true).","",1);
	}
	@Test
	public void testIfThenElse(){
		assertGoalSuccess("\';\'(\'->\'(true,true),fail).","");
		assertGoalSuccess("\';\'(\'->\'(fail,true),true).","");
		assertGoalFail("\';\'(\'->\'(true,fail),fail).","");
		assertGoalFail("\';\'(\'->\'(fail,true),fail).","");
		assertGoalSuccess("\';\'(\'->\'(true,X=1),X=2).","");
		assertGoalSuccess("\';\'(\'->\'(fail,X=1),X=2).","");
		assertGoalSuccess("\';\'(\'->\'(\';\'(X=1,X=2),true),true).","");
	}
	@Test
	public void testThrow(){
		//assertGoalSuccess("catch(foo(5),test(Y),true).","foo(X):-Y is X*2,throw(test(Y)).");
		assertGoalSuccess("catch(true,Z,true).","");
		assertGoalFail("catch(fail,Z,true).","");
		assertGoalSuccess("catch(bar(3),Z,true).","bar(X):-X=Y,throw(Y).");
		assertGoalError("catch(true,C,write(demoen)),throw(bla).","");
		assertGoalSuccess("catch(coo(X),Y,true).","coo(X):-throw(X).");
		assertGoalSuccess("catch(car(X),Y,true).","car(X):-X=1,throw(X).");
		assertGoalError("catch(throw(b),a(C),true).","");
	}
	@Test public void testCorner(){
		assertTrue(multiquery("p(X,Y).","p(M,W):-m(M),f(W).","fail").isEmpty());
		assertGoalSuccess("p(c).","p(a).p(b).");
	}
}
