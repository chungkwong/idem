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
/**
 * instantiation_error
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class InstantiationException extends PrologException{
	static String FUNCTOR="instantiation_error";
	private final Variable var;
	/**
	 * Construct a instantiation_error
	 * @param var the variable which is not instantiated
	 */
	public InstantiationException(Variable var) {
		this.var=var;
	}
	/**
	 * @return the variable causing the instantiation_error
	 */
	public Variable getVar(){
		return var;
	}
	@Override
	public String getMessage(){
		return var+" is not instantiated";
	}
	@Override
	public Term getErrorTerm(){
		return new CompoundTerm(FUNCTOR,var);
	}
}