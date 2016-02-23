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
public class Subtract extends Evaluable{
	public static final Subtract INSTANCE=new Subtract();
	public Subtract(){
		super(new EvaluableFunctor("-",2));
	}
	@Override
	protected Term evaluate(Object[] args){
		if(args[0] instanceof BigInteger&&args[1] instanceof BigInteger)
			return new Constant(((BigInteger)args[0]).subtract((BigInteger)args[1]));
		else
			return new Constant(Helper.toReal(args[0]).subtract(Helper.toReal(args[1])));
	}
}