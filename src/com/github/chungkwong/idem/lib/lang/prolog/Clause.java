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
public class Clause{
	private Predication head,body;
	/*public Clause(Predication head){
		this.head=head;
		this.body=True.TRUE;
	}*/
	public Clause(Predication head,Predication body){
		this.head=head;
		this.body=body;
	}
	public Predication getHead(){
		return head;
	}
	public Term getHeadAsTerm(){
		if(head.getPredicate().getArity()==0)
			return new Atom(head.getPredicate());
		else
			return new CompoundTerm(body.getPredicate().getFunctor(),body.getArguments()).renameAllVariable(new HashMap<>());
	}
	public Predication getBody(){
		return body;
	}
	public Term getBodyAsTerm(){
		if(body.getPredicate().getArity()==0)
			return new Atom(body.getPredicate());
		return new CompoundTerm(body.getPredicate().getFunctor(),body.getArguments()).renameAllVariable(new HashMap<>());
	}
	public Clause rename(){
		HashMap<Variable,Variable> renameTo=new HashMap<>();
		return new Clause(head.renameAllVariable(renameTo),body.renameAllVariable(renameTo));
	}
	@Override
	public String toString(){
		return head.toString()+":-"+body.toString();
	}
}