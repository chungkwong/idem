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
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Lists{
	public static Term emptyList(){
		return new Atom(Collections.EMPTY_LIST);
	}
	public static Term asList(Term... elements){
		int index=elements.length;
		Term list=emptyList();
		while(--index>=0){
			list=new CompoundTerm(".",Arrays.asList(elements[index],list));
		}
		return list;
	}
	public static int length(Term term){
		int length=0;
		while(term instanceof CompoundTerm){
			++length;
			term=((CompoundTerm)term).getArguments().get(1);
		}
		return length;
	}
	public static boolean isEmptyList(Term term){
		return term instanceof Atom&&((Atom)term).getValue().equals(Collections.EMPTY_LIST);
	}
	public static boolean isNonEmptyList(Term term){
		return term instanceof CompoundTerm&&((CompoundTerm)term).getFunctor().equals(".");
	}
	public static boolean isList(Term term){
		return isEmptyList(term)||isNonEmptyList(term);
	}
	public static Term head(Term term){
		return ((CompoundTerm)term).getArguments().get(0);
	}
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