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
 * java_field(value,object,field)
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class JavaField extends BuildinPredicate{
	public static final JavaField INSTANCE=new JavaField();
	public static final Predicate PREDICATE=new Predicate("java_field",3);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Object obj=Helper.getConstantValue(arguments.get(1));
		return extractField(arguments.get(0),obj.getClass(),obj,Helper.getConstantValue(arguments.get(2)).toString(),exec);
	}
	protected boolean extractField(Term ret,Class cls,Object obj,String field,Processor exec){
		Object retValue;
		try{
			retValue=cls.getField(field).get(obj);
		}catch(NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException ex){
			throw new JavaException(ex,exec.getCurrentActivator());
		}
		return ret.unities(new Constant(retValue),exec.getCurrentSubst());
	}
	@Override
	public Predicate getPredicate(){
		return PREDICATE;
	}
}