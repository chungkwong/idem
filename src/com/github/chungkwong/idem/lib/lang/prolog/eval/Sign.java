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
public class Sign extends Evaluable{
	public static final Sign INSTANCE=new Sign();
	public Sign(){
		super(new EvaluableFunctor("sign",1));
	}
	@Override
	protected Term evaluate(Object[] args){
		if(args[0] instanceof BigInteger)
			return pack(((BigInteger)args[0]).signum());
		else if(args[0] instanceof BigDecimal)
			return pack(((BigDecimal)args[0]).signum());
		else
			throw new TypeException(Number.class,new Atom(args[0]));
	}
	private Atom pack(int sign){
		if(sign==0)
			sign=1;
		return new Atom(BigInteger.valueOf(sign));
	}
}