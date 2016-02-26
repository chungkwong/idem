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
import java.util.*;
/**
 * Constant including atom,integer and floating point number
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Constant<T> extends Predication{
	private final T val;
	/**
	 * Construct a constant
	 * @param val the value of the Constant
	 */
	public Constant(T val){
		this.val=val;
	}
	/**
	 * @return the value of the constant
	 */
	public T getValue(){
		return val;
	}
	@Override
	public boolean equals(Object obj){
		return obj!=null&&obj instanceof Constant&&((Constant)obj).val.equals(val);
	}
	@Override
	public int hashCode(){
		return val.hashCode();
	}
	@Override
	public String toString(){
		return val.toString();
	}
	@Override
	public Set<Variable> getVariableSet(){
		return Collections.emptySet();
	}
	@Override
	public Set<Variable> getExistentialVariableSet(){
		return Collections.emptySet();
	}
	@Override
	public Term toIteratedTerm(){
		return this;
	}
	@Override
	public boolean unities(Term term,Substitution subst){
		if(term instanceof Constant)
			return ((Constant)term).val.equals(val);
		else if(term instanceof Variable)
			return ((Variable)term).isWildcard()||subst.assign((Variable)term,this);
		else
			return false;
	}
	@Override
	public Constant<T> substitute(Substitution subst){
		return this;
	}
	@Override
	public Constant<T> renameAllVariable(HashMap<Variable,Variable> renameTo){
		return this;
	}
	@Override
	public Predicate getPredicate(){
		return new Predicate(val,0);
	}
	@Override
	public List<Term> getArguments(){
		return Collections.emptyList();
	}
	@Override
	public Predication toHead(){
		return this;
	}
	@Override
	public Predication toBody()throws TypeException{
		if(val instanceof Number)
			throw new TypeException("atom",this);
		return this;
	}
	@Override
	public boolean isVariantOf(Term t,Map<Variable,Variable> perm){
		return equals(t);
	}
	@Override
	public Term substitute(Variable var,Term replacement){
		return this;
	}
}