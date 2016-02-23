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
 * Compound term
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CompoundTerm extends Predication{
	private final Object functor;
	private final List<Term> arguments;
	/**
	 * Constant a compound term
	 * @param functor the functor of the compound term
	 * @param arguments the arguments of the compound term
	 */
	public CompoundTerm(Object functor,List<Term> arguments){
		this.functor=functor;
		this.arguments=arguments;
	}
	/**
	 * Constant a compound term
	 * @param functor the functor of the compound term
	 * @param arguments the arguments of the compound term
	 */
	public CompoundTerm(Object functor,Term... arguments){
		this.functor=functor;
		this.arguments=Arrays.asList(arguments);
	}
	/**
	 * @return the functor of the compound term
	 */
	public Object getFunctor(){
		return functor;
	}
	@Override
	public List<Term> getArguments(){
		return arguments;
	}
	@Override
	public String toString(){
		return arguments.stream().map(Term::toString).collect(Collectors.joining(",",functor+"(",")"));
	}
	@Override
	public Set<Variable> getVariableSet(){
		return arguments.stream().map(Term::getVariableSet).flatMap(Set::stream).collect(Collectors.toSet());
	}
	@Override
	public Set<Variable> getExistentialVariableSet(){
		if(functor.equals("^")&&arguments.size()==2){
			Set<Variable> vars=new HashSet<>();
			vars.addAll(arguments.get(0).getVariableSet());
			vars.addAll(arguments.get(1).getExistentialVariableSet());
			return vars;
		}else
			return Collections.EMPTY_SET;
	}
	@Override
	public Term toIteratedTerm(){
		if(functor.equals("^")&&arguments.size()==2){
			return arguments.get(1).toIteratedTerm();
		}else
			return this;
	}
	@Override
	public boolean unities(Term term,Substitution subst){
		if(term instanceof Constant)
			return false;
		else if(term instanceof Variable){
			return ((Variable)term).isWildcard()||subst.assign((Variable)term,this);
		}else if(term instanceof CompoundTerm){
			CompoundTerm t=(CompoundTerm)term;
			if(!t.functor.equals(functor)||t.arguments.size()!=arguments.size())
				return false;
			int len=arguments.size();
			for(int i=0;i<len;i++)
				if(!t.arguments.get(i).unities(arguments.get(i),subst))
					return false;
			return true;
		}else
			return false;
	}
	@Override
	public CompoundTerm renameAllVariable(HashMap<Variable,Variable> renameTo){
		return new CompoundTerm(functor,arguments.stream().map((t)->t.renameAllVariable(renameTo)).collect(Collectors.toList()));
	}
	@Override
	public CompoundTerm substitute(Substitution subst){
		return new CompoundTerm(functor,arguments.stream().map((t)->t.substitute(subst)).collect(Collectors.toList()));
	}
	@Override
	public Predicate getPredicate(){
		return new Predicate(functor,arguments.size());
	}
	@Override
	public Predication toHead(){
		return this;
	}
	@Override
	public Predication toBody()throws TypeException{
		if(functor.equals(",")||functor.equals(";")||functor.equals("->"))
			return new CompoundTerm(functor,arguments.get(0).toBody(),arguments.get(1).toBody());
		return this;
	}
	@Override
	public boolean isVariantOf(Term t,Map<Variable,Variable> perm){
		if(t instanceof CompoundTerm&&((CompoundTerm)t).getFunctor().equals(functor)
				&&((CompoundTerm)t).getArguments().size()==arguments.size()){
			for(int i=0;i<arguments.size();i++)
				if(!arguments.get(i).isVariantOf(((CompoundTerm)t).getArguments().get(i),perm))
					return false;
			return true;
		}else
			return false;
	}
}