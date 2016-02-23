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
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Substitution{
	HashMap<Variable,Term> assignment;
	public Substitution(){
		assignment=new HashMap<>();
	}
	public Substitution(Substitution parent){
		assignment=new HashMap<>(parent.assignment);
	}
	public Term findRoot(Term obj){
		while(assignment.containsKey(obj)){
			obj=assignment.get(obj);
		}
		return obj;
	}
	public boolean assign(Variable var,Term val){
		Term root=findRoot(var);
		if(root instanceof Variable){
			if(!var.equals(val))
				assignment.put((Variable)root,val);
			return true;
		}else{
			if(val.unities(root,this)){
				val=val.substitute(this);
				if(val.getVariableSet().contains(root)){
					return false;
				}else{
					for(Map.Entry<Variable,Term> entry:assignment.entrySet())
						entry.setValue(entry.getValue().substitute(this));
					return true;
				}
			}else
				return false;
		}
	}
	public void unassign(Variable var){
		assignment.remove(var);
	}
	public boolean occurCheck(){
		return assignment.entrySet().stream().anyMatch((entry)->entry.getValue().getVariableSet().contains(entry.getKey()));
	}
	@Override
	public String toString(){
		StringBuilder buf=new StringBuilder();
		buf.append("{");
		assignment.entrySet().stream().forEach((assign)->{
			buf.append(assign.getKey()).append('=').append(assign.getValue()).append(',');
		});
		buf.append('}');
		return buf.toString();
	}
}