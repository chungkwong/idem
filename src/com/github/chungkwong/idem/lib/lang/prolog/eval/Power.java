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
import com.github.chungkwong.idem.lib.*;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.math.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Power extends Evaluable{
	public static final Power INSTANCE=new Power();
	public Power(){
		super(new EvaluableFunctor("**",2));
	}
	@Override
	protected Term evaluate(Object[] args){
		int sgn=Helper.signum(args[0]);
		if((sgn<0&&!(args[1]instanceof BigInteger))||(sgn==0&&Helper.signum(args[1])<0))
			throw new CalculationException(CalculationException.Type.UNDEFINED);
		return new Atom(BigDecimalMath.pow(Helper.toReal(args[0]),Helper.toReal(args[1]),MathContext.UNLIMITED));
	}
}