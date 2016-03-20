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
import java.io.*;
import java.util.*;
/**
 * Variable
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Variable extends Term implements Serializable{
	/**Wildcard variable*/
	public static final Variable WILDCARD=new Variable("");
	private final String name;
	/**
	 * @param name the name of the variable
	 */
	public Variable(String name){
		this.name=name;
	}
	/**
	 * @return if this variable is wilcard
	 */
	public boolean isWildcard(){
		return this==WILDCARD;
	}
	@Override
	public boolean equals(Object obj){
		return obj!=null&&obj instanceof Variable&&!(obj instanceof InternalVariable)
				&&((Variable)obj).name.equals(name);
	}
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	@Override
	public String toString(){
		return name;
	}
	@Override
	public Set<Variable> getVariableSet(){
		return Collections.singleton(this);
	}
	@Override
	public Set<Variable> getExistentialVariableSet(){
		return Collections.EMPTY_SET;
	}
	@Override
	public boolean unities(Term term,Substitution subst){
		return this==WILDCARD||term==WILDCARD||equals(term)||subst.assign(this,term);
	}
	@Override
	public Term substitute(Substitution subst){
		return subst.findRoot(this);
	}
	@Override
	public Term renameAllVariable(HashMap<Variable,Variable> renameTo){
		if(renameTo.containsKey(this))
			return renameTo.get(this);
		Variable fresh=InternalVariable.newInstance();
		renameTo.put(this,fresh);
		return fresh;
	}
	@Override
	public Predication toHead(){
		throw new InstantiationException(this);
	}
	@Override
	public Predication toBody(){
		return new CompoundTerm("call",this);
	}
	@Override
	public Term toIteratedTerm(){
		return this;
	}
	@Override
	protected boolean isVariantOf(Term t,Map<Variable,Variable> this2other,Set<Variable> other2this){
		if(t instanceof Variable){
			if(this2other.containsKey((Variable)t)){
				return this2other.get(this).equals(t);
			}else if(other2this.contains((Variable)t)){
				return false;
			}else{
				this2other.put(this,(Variable)t);
				other2this.add((Variable)t);
				return true;
			}
		}else
			return false;
	}
	@Override
	public Term substitute(Variable var,Term replacement){
		if(equals(var))
			return replacement;
		else
			return this;
	}
	/**
	 * Variable that are different from other variables
	 */
	public static class InternalVariable extends Variable{
		static int used=0;
		int id;
		private InternalVariable(){
			super('_'+Integer.toString(used));
			id=used++;
		}
		/**
		 * @return a new InternalVariable
		 */
		public static InternalVariable newInstance(){
			return new InternalVariable();
		}
		@Override
		public boolean equals(Object obj){
			return obj instanceof InternalVariable&&((InternalVariable)obj).id==id;
		}
		@Override
		public int hashCode(){
			int hash=7;
			hash=11*hash+this.id;
			return hash;
		}
	}
}