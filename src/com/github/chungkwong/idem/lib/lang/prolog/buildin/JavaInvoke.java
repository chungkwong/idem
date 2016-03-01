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
import java.lang.reflect.*;
import java.util.*;
/**
 * java_invoke(return_value,object,method,list_of_argments);
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class JavaInvoke extends BuildinPredicate{
	public static final JavaInvoke INSTANCE=new JavaInvoke();
	public static final Predicate PREDICATE=new Predicate("java_invoke",4);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term ret=arguments.get(0),object=arguments.get(1),method=arguments.get(2);
		expectConstant(object);
		expectConstant(method);
		Object obj=((Constant)object).getValue();
		String methodName=((Constant)method).getValue().toString();
		return invoke(ret,obj.getClass(),obj,methodName,arguments.get(3),exec);
	}
	protected boolean invoke(Term ret,Class cls,Object obj,String method,Term arguments,Processor exec){
		List<Object> args=Lists.extractJavaArguments(arguments);
		Object retValue;
		try{
			retValue=cls.getMethod(method,args.stream().map((arg)->arg.getClass()).toArray(Class[]::new))
					.invoke(obj,args.toArray());
		}catch(NoSuchMethodException|SecurityException|IllegalAccessException
				|IllegalArgumentException|InvocationTargetException ex){
			throw new JavaException(ex,exec.getCurrentActivator());
		}
		return ret.unities(new Constant(retValue),exec.getCurrentSubst());
	}
	protected static void expectConstant(Term t){
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