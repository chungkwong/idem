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
/**
 *
 * @author kwong
 */
public class Substitution{
	HashMap<Term,Term> assignment;
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
	public void assign(Term var,Term val){
		Term root=findRoot(var);
		if(root!=var){
			if(!(root instanceof Variable)){
				if(root.equals(val))
					return;
				else
					throw new RuntimeException(var+" already instantiated");
			}
		}
		assignment.put(root,val);
	}
	@Override
	public String toString(){
		StringBuilder buf=new StringBuilder();
		buf.append("{");
		for(Map.Entry<Term,Term> assign:assignment.entrySet())
			buf.append(assign.getKey()).append('=').append(assign.getValue()).append(',');
		buf.append('}');
		return buf.toString();
	}
}