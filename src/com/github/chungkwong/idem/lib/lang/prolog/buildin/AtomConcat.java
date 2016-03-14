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
import com.github.chungkwong.idem.lib.lang.prolog.InstantiationException;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class AtomConcat extends ReexecutableBuildinPredicate{
	public static final AtomConcat INSTANCE=new AtomConcat();
	public static final Predicate pred=new Predicate("atom_concat",3);
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	@Override
	public void firstActivate(List<Term> arguments,Processor exec,Variable var){
		Term atom1=arguments.get(0),atom2=arguments.get(1),atom12=arguments.get(2);
		Term lst=Lists.EMPTY_LIST;
		if(atom12 instanceof Variable){
			if(atom1 instanceof Variable||atom2 instanceof Variable)
				throw new InstantiationException((Variable)atom12);
			else if(!Helper.isAtom(atom1)){
				throw new TypeException("atom",atom1);
			}else if(!Helper.isAtom(atom2)){
				throw new TypeException("atom",atom2);
			}else{
				String concat=((Constant)atom1).getValue().toString()+((Constant)atom2).getValue().toString();
				lst=new CompoundTerm(".",new CompoundTerm("concat",atom1,atom2,new Constant(concat)),lst);
			}
		}else if(Helper.isAtom(atom12)){
			if(Helper.isAtom(atom1)){
				if(Helper.isAtom(atom2))
					lst=new CompoundTerm(".",new CompoundTerm("concat",atom1,atom2,new Constant(atom1.toString()+atom2.toString())),lst);
				else if(atom2 instanceof Variable){
					if(atom12.toString().length()>=atom1.toString().length()){
						String suffix=atom12.toString().substring(atom1.toString().length());
						lst=new CompoundTerm(".",new CompoundTerm("concat",atom1,new Constant(suffix),atom12),lst);
					}
				}else
					throw new TypeException("atom",atom2);
			}else if(atom1 instanceof Variable){
				if(Helper.isAtom(atom2)){
					if(atom12.toString().length()>=atom2.toString().length()){
						String prefix=atom12.toString().substring(0,atom12.toString().length()-atom2.toString().length());
						lst=new CompoundTerm(".",new CompoundTerm("concat",new Constant(prefix),atom2,atom12),lst);
					}
				}else if(atom2 instanceof Variable){
					String str=atom12.toString();
					for(int i=0;i<=str.length();i++)
						lst=new CompoundTerm(".",new CompoundTerm("concat",new Constant(str.substring(0,i)),new Constant(str.substring(i)),atom12),lst);
				}else
					throw new TypeException("atom",atom2);
			}else{
				throw new TypeException("atom",atom1);
			}
		}else{
			throw new TypeException("atom",atom12);
		}
		exec.getCurrentSubst().assign(var,lst);
	}
	@Override
	public boolean againActivate(List<Term> arguments,Processor exec,Variable var){
		Substitution subst=exec.getStack().get(exec.getStack().size()-2).getSubst();
		Term lst=subst.findRoot(var);
		if(lst instanceof CompoundTerm){
			CompoundTerm concat=(CompoundTerm)((CompoundTerm)lst).getArguments().get(0);
			boolean succeed=arguments.get(0).unities(concat.getArguments().get(0),exec.getCurrentSubst())
					&&arguments.get(1).unities(concat.getArguments().get(1),exec.getCurrentSubst())
					&&arguments.get(2).unities(concat.getArguments().get(2),exec.getCurrentSubst());
			subst.unassign(var);
			subst.assign(var,((CompoundTerm)lst).getArguments().get(1));
			return succeed;
		}else{
			return false;
		}
	}
}