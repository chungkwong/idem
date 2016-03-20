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
package com.github.chungkwong.idem.lib.lang.prolog.buildin;
import com.github.chungkwong.idem.lib.lang.prolog.*;
import java.math.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class AtomLength extends BuildinPredicate{
	public static final AtomLength INSTANCE=new AtomLength();
	public static final Predicate pred=new Predicate("atom_length",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Term atom=arguments.get(0),len=arguments.get(1);
		if(len instanceof Variable||Helper.isInteger(len))
			return new Constant(BigInteger.valueOf(Helper.getAtomValue(atom).length())).unities(len,exec.getCurrentSubst());
		else
			throw new TypeException("integer",len);
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}