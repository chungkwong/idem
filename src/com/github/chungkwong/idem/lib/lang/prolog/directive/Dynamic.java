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
package com.github.chungkwong.idem.lib.lang.prolog.directive;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.math.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Dynamic implements Directive{
	public static final Dynamic INSTANCE=new Dynamic();
	public static final Predicate pred=new Predicate("dynamic",1);
	@Override
	public Predicate getPredicate(){
		return pred;
	}
	@Override
	public void process(List<Term> arguments,Database db){
		List<Term> PI=((CompoundTerm)arguments.get(0)).getArguments();
		UserPredicate.DYNAMIC_PREDICATES.add(new Predicate(PI.get(0).toString()
				,((BigInteger)Helper.getConstantValue(PI.get(1))).intValueExact()));
	}
}
