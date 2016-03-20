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
import java.math.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class SubAtom extends ReexecutableBuildinPredicate{
	public static final SubAtom INSTANCE=new SubAtom();
	public static final Predicate pred=new Predicate("sub_atom",4);
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	@Override
	public void firstActivate(List<Term> arguments,Processor exec,Variable var){
		Term atom=arguments.get(0),start=arguments.get(1),length=arguments.get(2),subatom=arguments.get(3);
		Term lst=Lists.EMPTY_LIST;
		String str=Helper.getAtomValue(atom);
		String sub=subatom instanceof Variable?null:Helper.getAtomValue(subatom);
		int lenMin=length instanceof Variable?0:Math.max(Helper.getIntegerValue(length).intValueExact(),0);
		int lenMax=length instanceof Variable?str.length():Helper.getIntegerValue(length).intValueExact();
		int begin=start instanceof Variable?0:Math.max(Helper.getIntegerValue(start).intValueExact()-1,0);
		int end=start instanceof Variable?str.length():Helper.getIntegerValue(start).intValueExact()-1;
		for(int i=begin;i<=end;i++){
			for(int l=lenMin;l<=lenMax&&i+l<=str.length();l++){
				String substr=str.substring(i,i+l);
				if(sub==null||substr.equals(sub)){
					Term entry=new CompoundTerm("match",new Constant(BigInteger.valueOf(i+1)),
							new Constant(BigInteger.valueOf(l)),new Constant(substr));
					lst=new CompoundTerm(".",entry,lst);
				}
			}
		}
		exec.getCurrentSubst().assign(var,lst);
	}
	@Override
	public boolean againActivate(List<Term> arguments,Processor exec,Variable var){
		Substitution subst=exec.getStack().get(exec.getStack().size()-2).getSubst();
		Term lst=subst.findRoot(var);
		if(lst instanceof CompoundTerm){
			CompoundTerm item=(CompoundTerm)((CompoundTerm)lst).getArguments().get(0);
			arguments.get(1).unities(item.getArguments().get(0),exec.getCurrentSubst());
			arguments.get(2).unities(item.getArguments().get(1),exec.getCurrentSubst());
			arguments.get(3).unities(item.getArguments().get(2),exec.getCurrentSubst());
			subst.removeEquation(var);
			subst.assign(var,((CompoundTerm)lst).getArguments().get(1));
			return true;
		}else{
			return false;
		}
	}
}