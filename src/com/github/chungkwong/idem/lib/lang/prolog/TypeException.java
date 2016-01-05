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
public class TypeException extends PrologException{
	static String FUNCTOR="type_error";
	private final Class expected;
	private final Term argument;
	public TypeException(Class expected,Term argument) {
		this.expected=expected;
		this.argument=argument;
	}
	@Override
	public String getMessage(){
		return argument+" cannot be casted to "+expected;
	}
	public Class getExpected(){
		return expected;
	}
	public Term getArgument(){
		return argument;
	}
	@Override
	public Term getErrorTerm(){
		return new CompoundTerm(FUNCTOR,Arrays.asList(new Atom(expected),argument));
	}
}
