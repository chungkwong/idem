/*
 * Copyright (C) 2015 kwong
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
import java.util.stream.*;
/**
 *
 * @author kwong
 */
public class CompoundTerm extends Predication{
	Object functor;
	List<Term> argments=new ArrayList<>();
	public CompoundTerm(Object functor,List<Term> argments){
		this.functor=functor;
		this.argments=argments;
	}
	public Object getFunctor(){
		return functor;
	}
	@Override
	public List<Term> getArguments(){
		return argments;
	}
	public String toString(){
		return argments.stream().map(Term::toString).collect(Collectors.joining(",",functor+"(",")"));
	}
	@Override
	public Set<Variable> getVariableSet(){
		return argments.stream().map(Term::getVariableSet).flatMap(Set::stream).collect(Collectors.toSet());
	}
	@Override
	public boolean unities(Term term,Substitution subst){
		if(term instanceof Atom)
			return false;
		else if(term instanceof Variable){
			if(getVariableSet().contains((Variable)term))
				return false;
			if(!((Variable)term).isWildcard())
				subst.assign((Variable)term,this);
			return true;
		}else if(term instanceof CompoundTerm){
			CompoundTerm t=(CompoundTerm)term;
			if(!t.functor.equals(functor)||t.argments.size()!=argments.size())
				return false;
			int len=argments.size();
			for(int i=0;i<len;i++)
				if(!t.argments.get(i).unities(argments.get(i),subst))
					return false;
			return true;
		}else
			return false;
	}
	@Override
	public CompoundTerm renameVariable(Variable org,Variable then){
		return new CompoundTerm(functor,argments.stream().map((t)->t.renameVariable(org,then)).collect(Collectors.toList()));
	}
	@Override
	public CompoundTerm renameAllVariable(HashMap<Variable,Variable> renameTo){
		return new CompoundTerm(functor,argments.stream().map((t)->t.renameAllVariable(renameTo)).collect(Collectors.toList()));
	}
	@Override
	public CompoundTerm substitute(Variable org,Term then){
		return new CompoundTerm(functor,argments.stream().map((t)->t.substitute(org,then)).collect(Collectors.toList()));
	}
	@Override
	public CompoundTerm substitute(Substitution subst){
		return new CompoundTerm(functor,argments.stream().map((t)->t.substitute(subst)).collect(Collectors.toList()));
	}
	@Override
	public Predicate getPredicate(){
		return new Predicate(functor,argments.size());
	}
	@Override
	public Predication toHead(){
		return this;
	}
	@Override
	public Predication toBody(){
		return this;
	}
}