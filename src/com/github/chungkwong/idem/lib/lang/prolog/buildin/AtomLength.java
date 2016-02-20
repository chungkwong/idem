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
import com.github.chungkwong.idem.lib.lang.prolog.InstantiationException;
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
	public boolean activate(List<Term> argments,Processor exec){
		Term atom=argments.get(0),len=argments.get(1);
		if(atom instanceof Variable)
			throw new InstantiationException((Variable)atom);
		else if(atom instanceof Atom&&((Atom)atom).getValue()instanceof String){
			if(len instanceof CompoundTerm||(len instanceof Atom&&!(((Atom)len).getValue()instanceof BigInteger)))
				throw new TypeException("integer",len);
			else
				return new Atom(BigInteger.valueOf(((Atom)atom).getValue().toString().length())).unities(len,exec.getCurrentSubst());
		}else
			throw new TypeException("atom",atom);
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}