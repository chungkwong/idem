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
import java.util.function.Predicate;
/**
 * Prolog flag
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Flag{
	private final String name;
	private Term value;
	private final Predicate checker;
	private final boolean changeable;
	private final String domain;
	/**
	 * Construct a flag
	 * @param name the name
	 * @param value the default value
	 * @param changeable if user can change the value
	 * @param checker decide if a value is acceptable
	 * @param domain indicate the range of value
	 */
	public Flag(String name,Term value,boolean changeable,Predicate<Term> checker,String domain){
		this.name=name;
		this.changeable=changeable;
		this.checker=checker;
		this.domain=domain;
		if(checker.test(value))
			this.value=value;
		else
			throw new DomainException(domain,value);
	}
	public String getName(){
		return name;
	}
	public Term getValue(){
		return value;
	}
	public Predicate getChecker(){
		return checker;
	}
	/**
	 * Set the value of the flag
	 * @param value new value
	 */
	public void setValue(Term value){
		if(changeable&&checker.test(value))
			this.value=value;
		else
			throw new DomainException(domain,new CompoundTerm("+",new Constant(name),value));
	}
	public boolean isChangeable(){
		return changeable;
	}
}