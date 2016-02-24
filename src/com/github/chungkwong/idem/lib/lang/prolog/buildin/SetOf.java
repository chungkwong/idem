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
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class SetOf extends BuildinPredicate{
	public static final SetOf INSTANCE=new SetOf();
	public static final Predicate pred=new Predicate("setof",3);
	private static final Constant EMPTY_LIST=Lists.EMPTY_LIST;
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Set<Variable> freevars=arguments.get(1).getVariableSet();
		freevars.removeAll(arguments.get(1).getExistentialVariableSet());
		freevars.removeAll(arguments.get(0).getVariableSet());
		Term witness=new CompoundTerm("witness",new ArrayList<>(freevars)),instances=arguments.get(2);
		Variable lst=Variable.InternalVariable.newInstance();
		arguments.set(0,new CompoundTerm("+",witness,arguments.get(0)));
		arguments.set(1,arguments.get(1).toIteratedTerm());
		arguments.set(2,lst);
		if(FindAll.INSTANCE.activate(arguments,exec)){
			Term s=exec.getCurrentSubst().findRoot(lst);
			while(!s.equals(EMPTY_LIST)){
				CompoundTerm wt=(CompoundTerm)((CompoundTerm)s).getArguments().get(0);
				Term tLst=EMPTY_LIST,next=EMPTY_LIST,iter=s;
				while(!iter.equals(EMPTY_LIST)){
					CompoundTerm wwtt=(CompoundTerm)((CompoundTerm)s).getArguments().get(0);
					if(wwtt.getArguments().get(0).isVariantOf(wt.getArguments().get(0))){
						tLst=new CompoundTerm(".",wwtt.getArguments().get(1),tLst);
					}else{
						next=new CompoundTerm(".",((CompoundTerm)s).getArguments().get(1),next);
					}
					iter=((CompoundTerm)s).getArguments().get(1);
				}
				sort(next);
				s=next;
				if(instances.unities(tLst,exec.getCurrentSubst()))
					return true;
			}
			return false;
		}else
			return false;
	}
	private void sort(Term t){
		boolean changed=true;
		while(changed){
			changed=false;
			Term iter=t;
			while(iter instanceof CompoundTerm&&((CompoundTerm)iter).getArguments().get(1) instanceof CompoundTerm){
				Term prec=((CompoundTerm)iter).getArguments().get(0);
				Term succ=((CompoundTerm)((CompoundTerm)iter).getArguments().get(1)).getArguments().get(0);
				if(TermComparator.INSTANCE.compare(prec,succ)>0){
					((CompoundTerm)((CompoundTerm)iter).getArguments().get(1)).getArguments().set(0,prec);
					((CompoundTerm)iter).getArguments().set(0,succ);
					changed=true;
				}
				iter=((CompoundTerm)iter).getArguments().get(1);
			}
		}
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}