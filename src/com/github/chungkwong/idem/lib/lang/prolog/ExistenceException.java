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
 * existence_error
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ExistenceException extends PrologException{
	static final String FUNCTOR="existence_error";
	public static final String OPERATOR="operator";
	public static final String PAST_END_OF_STREAM="past_end_of_stream";
	public static final String PROCEDURE="procedure";
	public static final String STATIC_PROCEDURE="static_procedure";
	public static final String SOURCE_SINK="source_sink";
	public static final String STREAM="stream";
	private final String type;
	private final Term argument;
	/**
	 * Construct a ExistenceException
	 * @param type type expected
	 * @param argument the object that do not exists
	 */
	public ExistenceException(String type,Term argument){
		this.type=type;
		this.argument=argument;
	}
	@Override
	public String getMessage(){
		return "No"+getType()+" found:"+getArgument();
	}
	@Override
	public Term getErrorTerm(){
		return new CompoundTerm(FUNCTOR,new Constant(getType()),getArgument());
	}
	public String getType(){
		return type;
	}
	public Term getArgument(){
		return argument;
	}
}
