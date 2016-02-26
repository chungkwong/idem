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
package com.github.chungkwong.idem.lib.lang.prolog;
import java.math.*;
import java.util.*;
import java.util.stream.*;
/**
 * Some useful methods to manipulate prolog list
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Lists{
	/**Empty list*/
	public static final Constant EMPTY_LIST=new Constant("[]");
	/**
	 * @param elements
	 * @return A prolog list with given elements
	 */
	public static Term asList(Term... elements){
		int index=elements.length;
		Term list=EMPTY_LIST;
		while(--index>=0){
			list=new CompoundTerm(".",elements[index],list);
		}
		return list;
	}
	/**
	 * @param elements
	 * @return A prolog list with given elements
	 */
	public static Term asList(List<Term> elements){
		ListIterator<Term> iter=elements.listIterator(elements.size());
		Term list=EMPTY_LIST;
		while(iter.hasPrevious()){
			list=new CompoundTerm(".",iter.previous(),list);
		}
		return list;
	}
	/**
	 * @param term a prolog list
	 * @return the length of the list
	 */
	public static int length(Term term){
		int length=0;
		while(term instanceof CompoundTerm){
			++length;
			term=((CompoundTerm)term).getArguments().get(1);
		}
		return length;
	}
	/**
	 * @param term
	 * @return if term is an empty prolog list
	 */
	public static boolean isEmptyList(Term term){
		return term.equals(EMPTY_LIST);
	}
	/**
	 * @param term
	 * @return if term is an nonempty prolog list
	 */
	public static boolean isNonEmptyList(Term term){
		return term instanceof CompoundTerm&&((CompoundTerm)term).getFunctor().equals(".")
				&&((CompoundTerm)term).getArguments().size()==2;
	}
	/**
	 * @param term
	 * @return if term is an prolog list
	 */
	public static boolean isList(Term term){
		return isEmptyList(term)||isNonEmptyList(term);
	}
	/**
	 * @param term
	 * @return if term is an proper prolog list
	 */
	public static boolean isProperList(Term term){
		while(isNonEmptyList(term)){
			term=((CompoundTerm)term).getArguments().get(1);
		}
		return isEmptyList(term);
	}
	/**
	 * @param str
	 * @return the collating sequence
	 */
	public static Term asCodeList(String str){
		return asList(str.codePoints().mapToObj((i)->new Constant(BigInteger.valueOf(i))).collect(Collectors.toList()));
	}
	/**
	 * @param term a prolog list
	 * @return the head of the list
	 */
	public static Term head(Term term){
		return ((CompoundTerm)term).getArguments().get(0);
	}
	/**
	 * @param term a prolog list
	 * @return the tail of the list
	 */
	public static List<Term> tail(Term term){
		List<Term> list=new LinkedList();
		term=((CompoundTerm)term).getArguments().get(1);
		while(term instanceof CompoundTerm){
			list.add(((CompoundTerm)term).getArguments().get(0));
			term=((CompoundTerm)term).getArguments().get(1);
		}
		return list;
	}
}