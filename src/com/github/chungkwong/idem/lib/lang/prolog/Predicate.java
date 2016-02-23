/*
 * Copyright (C) 2015 Chan Chung Kwong <1m02math@126.com>
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
import java.math.*;
import java.util.*;
public class Predicate{
	private final Object functor;
	private final int arity;
	public Predicate(Object functor,int arity){
		this.functor=functor;
		this.arity=arity;
	}
	public Object getFunctor(){
		return functor;
	}
	public int getArity(){
		return arity;
	}
	public CompoundTerm getIndicator(){
		return new CompoundTerm("/",Arrays.asList(new Constant(functor),new Constant(BigInteger.valueOf(arity))));
	}
	@Override
	public boolean equals(Object obj){
		return obj instanceof Predicate&&((Predicate)obj).functor.equals(functor)&&((Predicate)obj).arity==arity;
	}
	@Override
	public int hashCode(){
		int hash=3;
		hash=37*hash+Objects.hashCode(this.functor);
		hash=37*hash+this.arity;
		return hash;
	}
	@Override
	public String toString(){
		return functor+"/"+arity;
	}
}