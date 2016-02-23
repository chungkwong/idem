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
public class Helper{
	public static final BigDecimal toReal(Object o){
		if(o instanceof BigDecimal)
			return (BigDecimal)o;
		else if(o instanceof BigInteger)
			return new BigDecimal((BigInteger)o);
		else
			throw new TypeException("number",new Constant(o));
	}
	public static final int signum(Object o){
		if(o instanceof BigInteger)
			return ((BigInteger)o).signum();
		else if(o instanceof BigDecimal)
			return ((BigDecimal)o).signum();
		else
			throw new TypeException("number",new Constant(o));
	}
}
