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
package com.github.chungkwong.idem.lib.lang.prolog.buildin;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.util.*;
/**
 * java_cast(converted,to_convert,new_type)
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class JavaCast extends BuildinPredicate{
	public static final JavaCast INSTANCE=new JavaCast();
	public static final Predicate PREDICATE=new Predicate("java_cast",3);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Object casted;
		try{
			casted=Class.forName(Helper.getConstantValue(arguments.get(2)).toString())
					.cast(Helper.getConstantValue(arguments.get(1)));
		}catch(ClassNotFoundException ex){
			throw new JavaException(ex,exec.getCurrentActivator());
		}
		return arguments.get(0).unities(new Constant(casted),exec.getCurrentSubst());
	}
	@Override
	public Predicate getPredicate(){
		return PREDICATE;
	}
}