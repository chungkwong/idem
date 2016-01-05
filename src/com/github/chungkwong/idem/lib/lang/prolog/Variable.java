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
 *
 * @author kwong
 */
public class Variable implements Term{
	public static final Variable WILDCARD=new Variable("");
	String name;
	public Variable(String name){
		this.name=name;
	}
	public boolean isWildcard(){
		return this==WILDCARD;
	}
	public static Variable getNewVariable(){
		return InternalVariable.newInstance();
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
	public boolean unities(Term term,Substitution subst){
		if(!isWildcard()){
			if(term instanceof Atom){
				subst.assign(this,term);
			}else if(term instanceof Variable){
				if(!((Variable)term).isWildcard()){
					subst.assign(this,term);
				}
			}else if(term instanceof CompoundTerm){
				if(term.getVariableSet().contains(this)){
					return false;
				}
				subst.assign(this,term);
			}
		}
		return true;
	}
	@Override
	public Term renameVariable(Variable org,Variable then){
		return equals(org)?then:this;
	}
	@Override
	public Term substitute(Variable org,Term then){
		return equals(org)?then:this;
	}
	@Override
	public Term substitute(Substitution subst){
		return subst.findRoot(this);
	}
	@Override
	public Term renameAllVariable(HashMap<Variable,Variable> renameTo){
		if(renameTo.containsKey(this))
			return renameTo.get(this);
		Variable fresh=Variable.getNewVariable();
		renameTo.put(this,fresh);
		return fresh;
	}
	@Override
	public Predication toHead(){
		throw new UnsupportedOperationException("Variable cannot be converted to head.");
	}
	@Override
	public Predication toBody(){
		return new CompoundTerm("call",Collections.singletonList(this));
	}
}
class InternalVariable extends Variable{
	static int used=0;
	int id;
	private InternalVariable(){
		super(Integer.toString(used));
		id=used++;
	}
	public static InternalVariable newInstance(){
		return new InternalVariable();
	}
	@Override
	public boolean equals(Object obj){
		return obj instanceof InternalVariable&&((InternalVariable)obj).id==id;
	}
}