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
import java.math.*;
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
	public void testComparison(){
		assertGoalSuccess("'=\\\\='(0,1).","");
		assertGoalSuccess("'<'(0,1).","");
		assertGoalSuccess("'=<'(0,1).","");
		assertGoalSuccess("'=:='(1.0,1).","");
		assertGoalSuccess("'>='(1.0,1).","");
		assertGoalSuccess("'=<'(1.0,1).","");
		assertGoalSuccess("'=:='(3*2,7-1).","");
		assertGoalSuccess("'=<'(3*2,7-1).","");
		assertGoalSuccess("'>='(3*2,7-1).","");
		assertGoalFail("'=:='(0,1).","");
		assertGoalFail("'>'(0,1).","");
		assertGoalFail("'>='(0,1).","");
		assertGoalFail("'=\\\\='(1.0,1).","");
		assertGoalFail("'>'(1.0,1).","");
		assertGoalFail("'<'(1.0,1).","");
		assertGoalFail("'=\\\\='(3*2,7-1).","");
		assertGoalFail("'<'(3*2,7-1).","");
		assertGoalFail("'>'(3*2,7-1).","");
		assertGoalError("X=:=5 .","");
		assertGoalError("X<5 .","");
		assertGoalError("X>5 .","");
		assertGoalError("X>=5 .","");
		assertGoalError("X=<5 .","");
	}
	@Test
	public void testUnify(){
		assertGoalSuccess("'='(1,1).","");
		assertGoalSuccess("'='(X,1).","");
		assertGoalSuccess("'='(X,Y).","");
		assertGoalSuccess("'='(_,_).","");
		assertGoalSuccess("'='(X,Y),'='(X,abc).","");
		assertGoalSuccess("'='(X,def),'='(def,Y).","");
		assertGoalFail("'='(1,2).","");
		assertGoalFail("'='(1,1.0).","");
		assertGoalFail("'='(g(X),f(f(X))).","");
		assertGoalFail("'='(g(X),f(f(X))).","");
		assertGoalFail("'='(f(X,1),f(a(X))).","");
		assertGoalFail("'='(f(X,Y,X),f(a(X),a(Y),Y,2)).","");
	}
	@Test
	public void testUnifyWithOccurCheck(){
		assertGoalSuccess("unify_with_occurs_check(1,1).","");
		assertGoalSuccess("unify_with_occurs_check(X,1).","");
		assertGoalSuccess("unify_with_occurs_check(X,Y).","");
		assertGoalSuccess("unify_with_occurs_check(_,_).","");
		assertGoalSuccess("unify_with_occurs_check(X,Y),unify_with_occurs_check(X,abc).","");
		assertGoalSuccess("unify_with_occurs_check(X,def),unify_with_occurs_check(def,Y).","");
		assertGoalFail("unify_with_occurs_check(1,2).","");
		assertGoalFail("unify_with_occurs_check(1,1.0).","");
		assertGoalFail("unify_with_occurs_check(g(X),f(f(X))).","");
		assertGoalFail("unify_with_occurs_check(g(X),f(f(X))).","");
		assertGoalFail("unify_with_occurs_check(f(X,1),f(a(X))).","");
		assertGoalFail("unify_with_occurs_check(f(X,Y,X),f(a(X),a(Y),Y,2)).","");
		assertGoalFail("unify_with_occurs_check(X,a(X)).","");
		assertGoalFail("unify_with_occurs_check(f(X,1),f(a(X),1)).","");
		assertGoalFail("unify_with_occurs_check(f(1,X),f(1,a(X))).","");
		assertGoalFail("unify_with_occurs_check(f(1,X,1),f(2,a(X),2)).","");
		assertGoalFail("unify_with_occurs_check(f(1,X,1),f(2,a(X),2)).","");
		assertGoalFail("unify_with_occurs_check(f(X,Y,a(X)),f(Z,Z,Z)).","");
	}
	@Test
	public void testNotUnify(){
		assertGoalFail("'\\\\='(1,1).","");
		assertGoalFail("'\\\\='(X,1).","");
		assertGoalFail("'\\\\='(X,Y).","");
		assertGoalFail("'\\\\='(_,_).","");
		assertGoalFail("'\\\\='(X,Y),'='(X,abc).","");
		assertGoalFail("'\\\\='(X,def),'='(def,Y).","");
		assertGoalSuccess("'\\\\='(1,2).","");
		assertGoalSuccess("'\\\\='(1,1.0).","");
		assertGoalSuccess("'\\\\='(g(X),f(f(X))).","");
		assertGoalSuccess("'\\\\='(g(X),f(f(X))).","");
		assertGoalSuccess("'\\\\='(f(X,1),f(a(X))).","");
		assertGoalSuccess("'\\\\='(f(X,Y,X),f(a(X),a(Y),Y,2)).","");
	}
	@Test
	public void testVar(){
		assertGoalSuccess("var(Foo).","");
		assertGoalSuccess("var(_).","");
		assertGoalFail("var(foo).","");
		assertGoalFail("var(f(X)).","");
		assertGoalFail("Foo=foo,var(Foo).","");
	}
	@Test
	public void testAtom(){
		assertGoalSuccess("atom(atom).","");
		assertGoalSuccess("atom('string').","");
		assertGoalSuccess("atom([]).","");
		assertGoalFail("atom(6).","");
		assertGoalFail("atom(3.3).","");
		assertGoalFail("atom(X).","");
		assertGoalFail("atom(a(b)).","");
	}
	@Test
	public void testInteger(){
		assertGoalSuccess("integer(3).","");
		assertGoalSuccess("integer(-3).","");
		assertGoalFail("integer(3.3).","");
		assertGoalFail("integer(0.0).","");
		assertGoalFail("integer(X).","");
		assertGoalFail("integer(atom).","");
		assertGoalFail("integer(a(b)).","");
	}
	@Test
	public void testReal(){
		assertGoalSuccess("real(3.3).","");
		assertGoalSuccess("real(-3.3).","");
		assertGoalFail("real(3).","");
		assertGoalFail("real(atom).","");
		assertGoalFail("real(Var).","");
		assertGoalFail("real(a(b)).","");
	}
	@Test
	public void testAtomic(){
		assertGoalSuccess("atomic(atom).","");
		assertGoalSuccess("atomic(3.3).","");
		assertGoalSuccess("atomic(-3).","");
		assertGoalFail("atomic(Var).","");
		assertGoalFail("atomic(a(b)).","");
	}
	@Test
	public void testCompound(){
		assertGoalSuccess("compound([a]).","");
		assertGoalSuccess("compound(a(b)).","");
		assertGoalSuccess("compound(-a).","");
		assertGoalFail("compound(33.3).","");
		assertGoalFail("compound(-33.3).","");
		assertGoalFail("compound(_).","");
		assertGoalFail("compound(atom).","");
	}
	@Test
	public void testNonVar(){
		assertGoalSuccess("nonvar(33.3).","");
		assertGoalSuccess("nonvar(foo).","");
		assertGoalSuccess("nonvar(a(b)).","");
		assertGoalSuccess("foo=Foo,nonvar(Foo).","");
		assertGoalFail("nonvar(_).","");
		assertGoalFail("nonvar(Var).","");
	}
	@Test
	public void testNumber(){
		assertGoalSuccess("number(3).","");
		assertGoalSuccess("number(33.3).","");
		assertGoalSuccess("number(-3).","");
		assertGoalFail("number(a).","");
		assertGoalFail("number(X).","");
	}
	@Test
	public void testEqual(){
		assertGoalSuccess("'=='(1,1).","");
		assertGoalSuccess("'=='(X,X).","");
		assertGoalSuccess("'=='(f(a,g(a)),f(a,g(a))).","");
		assertGoalFail("'=='(1,2).","");
		assertGoalFail("'=='(X,1).","");
		assertGoalFail("'=='(X,Y).","");
		assertGoalFail("'=='(_,1).","");
		assertGoalFail("'=='(_,_).","");
		assertGoalFail("'=='(X,a(X)).","");
	}
	@Test
	public void testNotEqual(){
		assertGoalSuccess("'\\\\=='(1,2).","");
		assertGoalSuccess("'\\\\=='(X,1).","");
		assertGoalSuccess("'\\\\=='(_,_).","");
		assertGoalSuccess("'\\\\=='(X,a(X)).","");
		assertGoalFail("'\\\\=='(1,1).","");
		assertGoalFail("'\\\\=='(X,X).","");
		assertGoalFail("'\\\\=='(f(a,g(a)),f(a,g(a))).","");
	}
	@Test
	public void testTermLessThan(){
		assertGoalSuccess("'@<'(1.0,1).","");
		assertGoalSuccess("'@<'(aardvark,zebra).","");
		assertGoalSuccess("'@<'(short,shorter).","");
		assertGoalSuccess("'@<'(foo(a,X),foo(b,Y)).","");
		assertGoalFail("'@<'(short,short).","");
		assertGoalFail("'@<'(foo(b),foo(a)).","");
		assertGoalFail("'@<'(foo(a,b),north(a)).","");
		assertGoalFail("'@<'(X,X).","");
	}
	@Test
	public void testTermLessEqual(){
		assertGoalSuccess("'@=<'(1.0,1).","");
		assertGoalSuccess("'@=<'(aardvark,zebra).","");
		assertGoalSuccess("'@=<'(short,short).","");
		assertGoalSuccess("'@=<'(short,shorter).","");
		assertGoalSuccess("'@=<'(X,X).","");
		assertGoalSuccess("'@=<'(foo(a,X),foo(b,Y)).","");
		assertGoalFail("'@=<'(foo(b),foo(a)).","");
	}
	@Test
	public void testTermGreaterThan(){
		assertGoalSuccess("'@>'(foo(b),foo(a)).","");
		assertGoalFail("'@>'(1.0,1).","");
		assertGoalFail("'@>'(aardvark,zebra).","");
		assertGoalFail("'@>'(short,shorter).","");
		assertGoalFail("'@>'(foo(a,X),foo(b,Y)).","");
		assertGoalFail("'@>'(short,short).","");
		assertGoalFail("'@>'(X,X).","");
	}
	@Test
	public void testTermGreaterEqual(){
		assertGoalSuccess("'@>='(short,short).","");
		assertGoalSuccess("'@>='(X,X).","");
		assertGoalSuccess("'@>='(foo(b),foo(a)).","");
		assertGoalFail("'@>'(1.0,1).","");
		assertGoalFail("'@>'(aardvark,zebra).","");
		assertGoalFail("'@>'(short,shorter).","");
		assertGoalFail("'@>'(foo(a,X),foo(b,Y)).","");
	}
	@Test
	public void testFunctor(){
		assertGoalSuccess("functor(foo(a,b,c),foo,3).","");
		assertGoalSuccess("functor(foo(a,b,c),X,Y).","");
		assertGoalSuccess("functor(X,foo,3).","");
		assertGoalSuccess("functor(X,foo,0).","");
		assertGoalSuccess("functor(1,X,Y).","");
		assertGoalSuccess("functor(X,1.1,0).","");
		assertGoalSuccess("functor([_|_],'.',2).","");
		assertGoalSuccess("functor([],[],0).","");
		assertGoalFail("functor(foo(a),foo,2).","");
		assertGoalFail("functor(foo(a),fo,1).","");
		assertGoalError("functor(F,foo(a),1).","");
		assertGoalError("functor(X,Y,3).","");
		assertGoalError("functor(X,foo,N).","");
		assertGoalError("functor(X,foo,a).","");
	}
	@Test
	public void testArg(){
		assertGoalSuccess("arg(1,foo(a,b),a).","");
		assertGoalSuccess("arg(1,foo(a,b),X).","");
		assertGoalSuccess("arg(1,foo(X,b),a).","");
		assertGoalSuccess("arg(1,foo(X,b),Y).","");
		assertGoalFail("arg(1,foo(a,b),b).","");
		assertGoalFail("arg(0,foo(a,b),foo).","");
		assertGoalFail("arg(3,foo(3,4),N).","");
		assertGoalError("arg(X,foo(a,b),a).","");
		assertGoalError("arg(1,X,a).","");
		assertGoalError("arg(0,atom,A).","");
		assertGoalError("arg(0,3,A).","");
	}
	@Test
	public void testUniv(){
		assertGoalSuccess("'=..'(foo(a,b),[foo,a,b]).","");
		assertGoalSuccess("'=..'(X,[foo,a,b]),X==foo(a,b).","");
		assertGoalSuccess("'=..'(foo(a,b),L),L==[foo,a,b].","");
		assertGoalSuccess("'=..'(foo(X,b),[foo,a,Y]),X==a,Y==b.","");
		assertGoalSuccess("'=..'(1,[1]).","");
		assertGoalFail("'=..'(foo(a,b),[foo,b,a]).","");
		assertGoalError("'=..'(X,[foo,a|Y]).","");
		assertGoalError("'=..'(X,[foo|bar]).","");
		assertGoalError("'=..'(X,[Foo,bar]).","");
		assertGoalError("'=..'(X,[3,1]).","");
		assertGoalError("'=..'(X,[1.1,foo]).","");
		assertGoalError("'=..'(X,[a(b),1]).","");
		assertGoalError("'=..'(X,4).","");
	}
	@Test
	public void testCopyTerm(){
		assertGoalSuccess("copy_term(X,Y).","");
		assertGoalSuccess("copy_term(X,3).","");
		assertGoalSuccess("copy_term(_,a).","");
		assertGoalSuccess("copy_term(a+X,X+b).","");
		assertGoalSuccess("copy_term(_,_).","");
		assertGoalSuccess("copy_term(X+X+Y,A+B+B).","");
		assertGoalFail("copy_term(a,b).","");
		assertGoalFail("copy_term(a+X,X+b),copy_term(a+X,X+b).","");
	}
	@Test
	public void testAsserta(){
		assertGoalSuccess("asserta(legs(octopus,8)).","legs(X,6):-insect(X).");
		assertGoalSuccess("asserta((legs(X,4):-animal(X))).","legs(X,6):-insect(X).");
		assertGoalError("asserta(_).","");
		assertGoalError("asserta(4).","");
		assertGoalError("asserta((foo:-4)).","");
		assertGoalError("asserta((atom(_):-true)).","");
	}
	@Test
	public void testAssertz(){
		assertGoalSuccess("assertz(legs(octopus,8)).","legs(X,6):-insect(X).");
		assertGoalSuccess("assertz((legs(X,4):-animal(X))).","legs(X,6):-insect(X).");
		assertGoalError("assertz(_).","");
		assertGoalError("assertz(4).","");
		assertGoalError("assertz((foo:-4)).","");
		assertGoalError("assertz((atom(_):-true)).","");
	}
	@Test
	public void testRetract(){
		assertGoalSuccess("retract(legs(X,6):-T).","legs(X,6):-insect(X).legs(octopus,8).insect(ant).");
		assertGoalSuccess("retract(legs(octopus,8)),legs(ant,6).","legs(X,6):-insect(X).legs(octopus,8).insect(ant).");
		assertGoalFail("retract(legs(octopus,8)),legs(octopus,8).","legs(X,6):-insect(X).legs(octopus,8).insect(ant).");
		assertGoalFail("retract(legs(spider,6)).","legs(X,6):-insect(X).legs(octopus,8).insect(ant).");
		assertGoalFail("retract(foo).","");
		Assert.assertTrue(multiquery("retract(legs(X,Y):-T).","legs(X,6):-insect(X).legs(octopus,8).insect(ant).").size()==2);
		assertGoalError("retract(X:-hello).","");
		assertGoalError("retract(4:-X).","");
		assertGoalError("retract(atom(X):=X=='[]').","");
	}
	@Test
	public void testAbolish(){
		assertGoalSuccess("abolish(foo/2)","");
		assertGoalSuccess("abolish(foo/2),foo(_).","foo(X).");
		assertGoalError("abolish(foo/2),foo(a,a).","foo(X,Y):-X=Y.foo(X,Y):-X==Y");
		assertGoalError("abolish(X).","");
		assertGoalError("abolish(foo/_).","");
		assertGoalError("abolish(foo/a).","");
		assertGoalError("abolish(4/2).","");
		assertGoalError("abolish(abolish/1).","");
	}
	@Test
	public void testFailIf(){
		assertGoalSuccess("fail_if((!,fail)).","");
		assertGoalSuccess("fail_if(4=5).","");
		Assert.assertTrue(multiquery("(X=1;X=2),fail_if((!,fail)).","").size()==2);
		assertGoalFail("fail_if(true).","");
		assertGoalFail("fail_if(!).","");
		assertGoalError("fail_if(3).","");
		assertGoalError("fail_if(X).","");
	}
	@Test
	public void testOnce(){
		assertGoalSuccess("once(!).","");
		Assert.assertTrue(multiquery("once(!),(X=1;X=2).","").size()==2);
		assertGoalSuccess("once(repeat).","");
		assertGoalFail("once(fail).","");
		assertGoalError("once(3).","");
		assertGoalError("once(X).","");
	}
	@Test
	public void testRepeat(){
		assertGoalFail("repeat,!,fail.","");
	}
	@Test
	public void testSetPrologFlag(){
		assertGoalSuccess("set_prolog_flag(undefined_predicate,fail).","");
		assertGoalFail("set_prolog_flag(undefined_predicate,fail),g(a).","");
		assertGoalError("set_prolog_flag(X,off).","");
		assertGoalError("set_prolog_flag(X,off).","");
		assertGoalError("set_prolog_flag(5,decimal).","");
		assertGoalError("set_prolog_flag(date,'july 1988').","");
		assertGoalError("set_prolog_flag(debug,trace).","");
	}
	@Test
	public void testCurrentPrologFlag(){
		assertGoalSuccess("current_prolog_flag(debug,off).","");
		Assert.assertTrue(multiquery("current_prolog_flag(F,V).","").size()==5);
		assertGoalError("current_prolog_flag(5,_).","");
	}
	@Test
	public void testHalt(){
		boolean halted=false;
		try{
			new Processor(new Constant("halt"),new Database());
		}catch(HaltException ex){
			halted=true;
		}
		Assert.assertTrue(halted);
		BigInteger ret=null;
		try{
			new Processor(new CompoundTerm("halt",new Constant(BigInteger.ONE)),new Database());
		}catch(HaltException ex){
			ret=ex.getExitCode();
		}
		Assert.assertEquals(BigInteger.ONE,ret);
		assertGoalError("halt(_).","");
		assertGoalError("halt(a).","");
	}
	@Test
	public void testFindAll(){
		assertGoalSuccess("findall(X,(X=1;X=2),S),S=[1,2].","");
		assertGoalSuccess("findall(X,(X=1;X=1),S),S=[1,1].","");
		assertGoalSuccess("findall(X+Y,(X=1),S),S=[1+_].","");
		assertGoalSuccess("findall(X,fail,S),S=[].","");
		assertGoalError("findall(X,Goal,S).","");
		assertGoalError("findall(X,4,S).","");
	}
	@Test
	public void testBagOf(){
		assertGoalSuccess("bagof(X,(X=1;X=2),S),S=[1,2].","");
		assertGoalSuccess("bagof(X,(X=1;X=2),X),X=[1,2].","");
		assertGoalSuccess("bagof(f(X,Y),(X=a;Y=b),L),L=[_,_].","");
		assertGoalSuccess("bagof(X,Y^((X=1,Y=1);(X=2,Y=2)),L),L=[1,2].","");
		assertGoalSuccess("bagof(X,Y^((X=1;Y=1);(X=2,Y=2)),L),L=[1,_,2].","");
		assertGoalSuccess("bagof(X,a(X,Y),L),L=[1,2].","a(1,(f(_))).a(2,(f(_))).");
		Assert.assertTrue(multiquery("bagof(1,(X=1;X=2),L),L=[1].","").size()==2);
		assertGoalFail("bagof(X,fail,S).","");
		assertGoalError("bagof(X,1,S).","");
		assertGoalError("bagof(X,Y^Z,S).","");
	}
	@Test
	public void testSetOf(){
		assertGoalSuccess("setof(X,(X=1;X=2),S),S=[1,2].","");
		assertGoalSuccess("setof(X,(X=2;X=1),S),S=[1,2].","");
		assertGoalSuccess("setof(X,(X=2;X=2),S),S=[2].","");
		assertGoalSuccess("setof(X,(X=1;X=2),X),X=[1,2].","");
		assertGoalSuccess("setof(f(X,Y),(X=a;Y=b),L),L=[_,_].","");
		assertGoalSuccess("setof(X,Y^((X=1,Y=1);(X=2,Y=2)),L),L=[1,2].","");
		assertGoalSuccess("setof(X,Y^((X=1;Y=1);(X=2,Y=2)),L),L=[_,1,2].","");
		assertGoalSuccess("setof(X,a(X,Y),L),L=[1,2].","a(1,(f(_))).a(2,(f(_))).");
		Assert.assertTrue(multiquery("setof(1,(X=1;X=2),L),L=[1].","").size()==2);
		assertGoalFail("setof(X,fail,S).","");
		assertGoalError("setof(X,1,S).","");
		assertGoalError("setof(X,Y^Z,S).","");
	}
	@Test
	public void testAtomLength(){
		assertGoalSuccess("atom_length('enchanted evening',17).","");
		assertGoalSuccess("atom_length('enchanted \\\nevening',17).","");
		assertGoalSuccess("atom_length('',A),A=:=0.","");
		assertGoalFail("atom_length(scarlet,5).","");
		assertGoalError("atom_length(Atom,4).","");
		assertGoalError("atom_length(1.25,4).","");
		assertGoalError("atom_length(atom,'4').","");
	}
	@Test
	public void testSubAtom(){
		assertGoalSuccess("sub_atom('banana',4,2,'an').","");
		assertGoalSuccess("sub_atom('banana',4,2,S),S='an'.","");
		Assert.assertTrue(multiquery("sub_atom('bananas',_,3,S).","").size()==5);
		Assert.assertTrue(multiquery("sub_atom('banana',4,_,_).","").size()==4);
		Assert.assertTrue(multiquery("sub_atom('ab',_,_,_).","").size()==6);
		assertGoalError("sub_atom(abc,a,1,'a').","");
		assertGoalError("sub_atom(abc,2,sin(X),'a').","");
		assertGoalError("sub_atom(abc,2,1,6).","");
		assertGoalError("sub_atom(12,_,_,_).","");
	}
	@Test
	public void testAtomChars(){
		assertGoalSuccess("atom_chars('',L),L=[].","");
		assertGoalSuccess("atom_chars([],['[',']']).","");
		assertGoalSuccess("atom_chars('''',['''']).","");
		assertGoalSuccess("atom_chars('ant',['a','n','t']).","");
		assertGoalFail("atom_chars(soap,['s','o','p']).","");
		assertGoalError("atom_chars(3,[]).","");
		assertGoalError("atom_chars(X,[3]).","");
		assertGoalError("atom_chars(X,Y).","");
	}
	@Test
	public void testAtomCodes(){
		assertGoalSuccess("atom_codes('',L),L=[].","");
		assertGoalSuccess("atom_codes([],[0'[,0']]).","");
		assertGoalSuccess("atom_codes('''',[39]).","");
		assertGoalSuccess("atom_codes('ant',[0'a,0'n,0't]).","");
		assertGoalFail("atom_codes(soap,[55,56,57]).","");
		assertGoalError("atom_codes(3,[1]).","");
		assertGoalError("atom_codes(X,[a]).","");
		assertGoalError("atom_codes(X,Y).","");
	}
	@Test
	public void testCharCode(){
		assertGoalSuccess("char_code('a',Code),Code=a.","");
		assertGoalSuccess("char_code(Str,99).","");
		assertGoalSuccess("char_code(Str,163).","");
		assertGoalSuccess("char_code('b',0'b).","");
		assertGoalFail("char_code('b',84).","");
		assertGoalError("char_code('ab',84).","");
		assertGoalError("char_code('a',a).","");
		assertGoalError("char_code(C,I).","");
	}
	@Test
	public void testNumberChars(){
		assertGoalSuccess("number_chars(33,L),L=['3','3'].","");
		assertGoalSuccess("number_chars(33.0,L).","");
		assertGoalSuccess("number_chars(33,['3','3']).","");
		assertGoalSuccess("number_chars(3.3,['3','.','3','E','+','0']).","");
		//assertGoalSuccess("number_chars(A,['-','2','5']),A=(-25).","");
		assertGoalSuccess("number_chars(A,['0','x','f']),A=:=15.","");
		assertGoalFail("number_chars(34,['3','3']).","");
		assertGoalError("number_chars(A,B).","");
		assertGoalError("number_chars(a,['3']).","");
		assertGoalError("number_chars(a,A).","");
		assertGoalError("number_chars(A,['a']).","");
		assertGoalError("number_chars(A,a).","");
	}
	@Test
	public void testNumberCodes(){
		assertGoalSuccess("number_codes(33,L),L=[51,51].","");
		assertGoalSuccess("number_codes(34,[51,52]).","");
		assertGoalSuccess("number_codes(-32.5,[45,51,50,46,53]).","");
		assertGoalSuccess("number_codes(A,[45,51,50,46,53]),A=:=(-32.5).","");
		assertGoalFail("number_codes(34,[50,50]).","");
		assertGoalError("number_codes(A,B).","");
		assertGoalError("number_codes(a,['51']).","");
		assertGoalError("number_codes(a,A).","");
		assertGoalError("number_codes(A,['a']).","");
		assertGoalError("number_codes(A,a).","");
		assertGoalError("number_codes(5,a).","");
	}
}