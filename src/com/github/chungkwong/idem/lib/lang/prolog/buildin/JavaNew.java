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
 * java_construct(object,class,listOfarguments)
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class JavaNew extends BuildinPredicate{
	public static final JavaNew INSTANCE=new JavaNew();
	public static final Predicate PREDICATE=new Predicate("java_new",3);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term clsName=arguments.get(1);
		if(clsName instanceof Variable){
			throw new InstantiationException((Variable)clsName);
		}else if(clsName instanceof Constant&&((Constant)clsName).getValue()instanceof String){
			try{
				Class cls=Class.forName((String)((Constant)clsName).getValue());
				List<Object> args=Lists.extractJavaArguments(arguments.get(2));
				Constructor constructor=cls.getConstructor(args.stream().map((arg)->arg.getClass()).toArray(Class[]::new));
				return arguments.get(0).unities(new Constant(constructor.newInstance(args.toArray())),exec.getCurrentSubst());
			}catch(ClassNotFoundException|NoSuchMethodException|SecurityException
					|java.lang.InstantiationException|IllegalAccessException
					|IllegalArgumentException|InvocationTargetException ex){
				throw new JavaException(ex,exec.getCurrentActivator());
			}
		}else{
			throw new TypeException("atom",clsName);
		}
	}
	@Override
	public Predicate getPredicate(){
		return PREDICATE;
	}
}