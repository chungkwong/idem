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
import java.math.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class IntegerDivide extends Evaluable{
	public static final IntegerDivide INSTANCE=new IntegerDivide();
	public IntegerDivide(){
		super(new EvaluableFunctor("//",2));
	}
	@Override
	protected Term evaluate(Object[] args){
		if(!(args[0] instanceof BigInteger))
			throw new TypeException("integer",new Constant(args[0]));
		if(!(args[1] instanceof BigInteger))
			throw new TypeException("integer",new Constant(args[1]));
		if(args[1].equals(BigInteger.ZERO))
			throw new CalculationException(CalculationException.Type.ZERO_DIVISOR);
		return new Constant(((BigInteger)args[0]).divide((BigInteger)args[1]));
	}
}