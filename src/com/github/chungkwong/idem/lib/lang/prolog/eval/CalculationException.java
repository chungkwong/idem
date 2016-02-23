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

package com.github.chungkwong.idem.lib.lang.prolog.eval;
import com.github.chungkwong.idem.lib.lang.prolog.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CalculationException extends PrologException{
	public static enum Type{
		ZERO_DIVISOR(new CompoundTerm("calculation_error",new Constant("zero_divisor"))),
		OVER_FLOW(new CompoundTerm("calculation_error",new Constant("overflow"))),
		UNDER_FLOW(new CompoundTerm("calculation_error",new Constant("underflow"))),
		UNDEFINED(new CompoundTerm("calculation_error",new Constant("undefined")));
		Term errorTerm;
		Type(Term errorTerm){
			this.errorTerm=errorTerm;
		}
	}
	Type type;
	public CalculationException(Type type){
		this.type=type;
	}
	@Override
	public Term getErrorTerm(){
		return type.errorTerm;
	}
}
