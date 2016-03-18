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
import java.util.*;
import org.junit.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PrologProcessorTest{
	public static List<Substitution> multiquery(String query,String data,String mode){
		Database db=new Database(new StringReader(data));
		db.getFlag("undefined_predicate").setValue(new Constant(mode));
		List<Substitution> substs=new ArrayList<>();
		Processor processor=new Processor(new PrologParser(new PrologLex(new StringReader(query))).next(),db);
		while(processor.getSubstitution()!=null){
			substs.add(processor.getSubstitution());
			processor.reexecute();
		}
		//System.out.println(substs);
		return substs;
	}
	public static List<Substitution> multiquery(String query,String data){
		return multiquery(query,data,"error");
	}
	public static void assertSuccessCount(String query,String data,int count){
		Assert.assertEquals(multiquery(query,data).size(),count);
	}
	public static void assertGoalFail(String query,String data){
		assertSuccessCount(query,data,0);
	}
	public static void assertGoalSuccess(String query,String data){
		Assert.assertTrue(!multiquery(query,data).isEmpty());
	}
	public static void assertGoalError(String query,String data){
		Database db=new Database(new StringReader(data));
		try{
			Substitution subst=new Processor(new PrologParser(new PrologLex(new StringReader(query))).next(),db).getSubstitution();
			Assert.assertTrue(false);
		}catch(Exception ex){

		}
	}
	@Test
	public void test(){

	}
}