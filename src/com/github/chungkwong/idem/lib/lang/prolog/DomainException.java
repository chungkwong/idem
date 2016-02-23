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
/**
 * domain_error in Prolog
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class DomainException extends PrologException{
	static final String FUNCTOR="domain_error";
	private final String expected;
	private final Term argument;
	/**
	 * Construct a DomainException
	 * @param expected expected type
	 * @param argument the term that cause this error
	 */
	public DomainException(String expected,Term argument) {
		this.expected=expected;
		this.argument=argument;
	}
	@Override
	public String getMessage(){
		return argument+" cannot be casted to "+expected;
	}
	/**
	 * @return expected type
	 */
	public String getExpected(){
		return expected;
	}
	/**
	 * @return the term that cause this error
	 */
	public Term getArgument(){
		return argument;
	}
	@Override
	public Term getErrorTerm(){
		return new CompoundTerm(FUNCTOR,new Constant(expected),argument);
	}
}
