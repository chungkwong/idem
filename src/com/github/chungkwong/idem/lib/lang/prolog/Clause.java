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
 * Clause of user-defined procedures
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Clause{
	private final Predication head,body;
	/**
	 * Construct a clause
	 * @param head the head
	 * @param body the body
	 */
	public Clause(Predication head,Predication body){
		this.head=head;
		this.body=body;
	}
	/**
	 * @return the head of the clause
	 */
	public Predication getHead(){
		return head;
	}
	/**
	 * @return the body of the clause
	 */
	public Predication getBody(){
		return body;
	}
	private static Term predicationToTerm(Predication pred){
		if(pred.getPredicate().getArity()==0)
			return new Constant(pred.getPredicate().getFunctor());
		else
			return pred;
	}
	/**
	 * @return clause(head,body)
	 */
	public Term asTerm(){
		return new CompoundTerm("clause",head,body).renameAllVariable();
	}
	/**
	 * @return return a renamed copy of the clause
	 */
	public Clause rename(){
		HashMap<Variable,Variable> renameTo=new HashMap<>();
		return new Clause((Predication)head.renameAllVariable(renameTo),body.renameAllVariable(renameTo));
	}
	@Override
	public String toString(){
		return head.toString()+":-"+body.toString();
	}
}