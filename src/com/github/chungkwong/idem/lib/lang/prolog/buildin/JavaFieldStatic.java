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
 * java_field_static(value,class,field)
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class JavaFieldStatic extends JavaField{
	public static final JavaFieldStatic INSTANCE=new JavaFieldStatic();
	public static final Predicate PREDICATE=new Predicate("java_field_static",3);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Class cls;
		try{
			cls=Class.forName(Helper.getConstantValue(arguments.get(1)).toString());
		}catch(ClassNotFoundException ex){
			throw new JavaException(ex,exec.getCurrentActivator());
		}
		return extractField(arguments.get(0),cls,null,Helper.getConstantValue(arguments.get(2)).toString(),exec);
	}
	@Override
	public Predicate getPredicate(){
		return PREDICATE;
	}
}