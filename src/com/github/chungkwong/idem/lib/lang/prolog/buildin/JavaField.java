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
import com.github.chungkwong.idem.lib.lang.prolog.InstantiationException;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.util.*;
/**
 * java_field(value,object,field)
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class JavaField extends BuildinPredicate{
	public static final JavaField INSTANCE=new JavaField();
	public static final Predicate PREDICATE=new Predicate("java_field",3);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term ret=arguments.get(0),object=arguments.get(1),field=arguments.get(2);
		expectConstant(object);
		expectConstant(field);
		Object obj=((Constant)arguments).getValue();
		String fieldName=((Constant)field).getValue().toString();
		Object retValue;
		try{
			retValue=obj.getClass().getField(fieldName).get(obj);
		}catch(NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException ex){
			throw new JavaException(ex,exec.getCurrentActivator());
		}
		return ret.unities(new Constant(retValue),exec.getCurrentSubst());
	}
	private static void expectConstant(Term t){
		if(t instanceof Variable)
			throw new InstantiationException((Variable)t);
		else if(t instanceof CompoundTerm)
			throw new TypeException("constant",t);
	}
	@Override
	public Predicate getPredicate(){
		return PREDICATE;
	}
}