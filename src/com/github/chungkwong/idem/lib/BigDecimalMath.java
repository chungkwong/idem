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
package com.github.chungkwong.idem.lib;
import java.math.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class BigDecimalMath{//TODO
	public static final  BigDecimal sin(BigDecimal x,MathContext prec){
		return BigDecimal.valueOf(Math.sin(x.doubleValue()));
	}
	public static final BigDecimal cos(BigDecimal x,MathContext prec){
		return BigDecimal.valueOf(Math.cos(x.doubleValue()));
	}
	public static final BigDecimal atan(BigDecimal x,MathContext prec){
		return BigDecimal.valueOf(Math.atan(x.doubleValue()));
	}
	public static final BigDecimal exp(BigDecimal x,MathContext prec){
		return BigDecimal.valueOf(Math.exp(x.doubleValue()));
	}
	public static final BigDecimal log(BigDecimal x,MathContext prec){
		return BigDecimal.valueOf(Math.log(x.doubleValue()));
	}
	public static final BigDecimal sqrt(BigDecimal x,MathContext prec){
		return BigDecimal.valueOf(Math.sqrt(x.doubleValue()));
	}
	public static final BigDecimal pow(BigDecimal x,BigDecimal y,MathContext prec){
		return BigDecimal.valueOf(Math.pow(x.doubleValue(),y.doubleValue()));
	}
}
