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
/**
 * Operator
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Operator{
	public enum Class{PREFIX,INFIX,POSTFIX}
	public enum Associativity{LEFT,RIGHT,NO}
	private final String token;
	private final int priority;
	private final Class cls;
	private final Associativity associativity;
	/**
	 * Construct a operator
	 * @param token the specifier of the operator
	 * @param priority the priority of the operator should between 1 and 1201
	 * @param cls the class of the operator
	 * @param associativity the associativity of the operator
	 */
	public Operator(String token,int priority,Class cls,Associativity associativity){
		this.token=token;
		this.priority=priority;
		this.cls=cls;
		this.associativity=associativity;
	}
	public String getToken(){
		return token;
	}
	public int getPriority(){
		return priority;
	}
	public Class getCls(){
		return cls;
	}
	public Associativity getAssociativity(){
		return associativity;
	}
	@Override
	public String toString(){
		return token;
	}
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Operator))
			return false;
		Operator o=(Operator)obj;
		return o.priority==priority&&o.token.equals(token)&&o.cls.equals(cls)
				&&o.associativity.equals(associativity);
	}
	@Override
	public int hashCode(){
		int hash=7;
		hash=47*hash+Objects.hashCode(this.token);
		hash=47*hash+this.priority;
		hash=47*hash+Objects.hashCode(this.cls);
		hash=47*hash+Objects.hashCode(this.associativity);
		return hash;
	}
}