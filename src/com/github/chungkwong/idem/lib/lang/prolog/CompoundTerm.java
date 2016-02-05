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
	public Set<Variable> getExistentialVariableSet(){
		if(functor.equals("^")&&argments.size()==2){
			Set<Variable> vars=new HashSet<>();
			vars.addAll(argments.get(0).getVariableSet());
			vars.addAll(argments.get(1).getExistentialVariableSet());
			return vars;
		}else
			return Collections.EMPTY_SET;
	}
	@Override
	public Term toIteratedTerm(){
		if(functor.equals("^")&&argments.size()==2){
			return argments.get(1).toIteratedTerm();
		}else
			return this;
	}
	@Override
	public boolean unities(Term term,Substitution subst){
		if(term instanceof Atom)
			return false;
		else if(term instanceof Variable){
			return ((Variable)term).isWildcard()||subst.assign((Variable)term,this);
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
	public CompoundTerm renameAllVariable(HashMap<Variable,Variable> renameTo){
		return new CompoundTerm(functor,argments.stream().map((t)->t.renameAllVariable(renameTo)).collect(Collectors.toList()));
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
	public Predication toBody()throws TypeException{
		if(functor.equals(",")||functor.equals(";")||functor.equals("->"))
			return new CompoundTerm(functor,Arrays.asList(argments.get(0).toBody(),argments.get(1).toBody()));
		return this;
	}
	@Override
	public boolean isVariantOf(Term t,Map<Variable,Variable> perm){
		if(t instanceof CompoundTerm&&((CompoundTerm)t).getFunctor().equals(functor)
				&&((CompoundTerm)t).getArguments().size()==argments.size()){
			for(int i=0;i<argments.size();i++)
				if(!argments.get(i).isVariantOf(((CompoundTerm)t).getArguments().get(i),perm))
					return false;
			return true;
		}else
			return false;
	}
}