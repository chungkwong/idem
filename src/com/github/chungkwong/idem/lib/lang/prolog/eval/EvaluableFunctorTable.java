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
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class EvaluableFunctorTable{
	private static final HashMap<EvaluableFunctor,Evaluable> table=new HashMap<>();
	static{
		addEvaluableFunctor(Abs.INSTANCE);
		addEvaluableFunctor(Add.INSTANCE);
		addEvaluableFunctor(And.INSTANCE);
		addEvaluableFunctor(Ceiling.INSTANCE);
		addEvaluableFunctor(Complement.INSTANCE);
		addEvaluableFunctor(Divide.INSTANCE);
		addEvaluableFunctor(Float.INSTANCE);
		addEvaluableFunctor(Floor.INSTANCE);
		addEvaluableFunctor(FractionPart.INSTANCE);
		addEvaluableFunctor(IntegerDivide.INSTANCE);
		addEvaluableFunctor(IntegerPart.INSTANCE);
		addEvaluableFunctor(Mod.INSTANCE);
		addEvaluableFunctor(Multiply.INSTANCE);
		addEvaluableFunctor(Negate.INSTANCE);
		addEvaluableFunctor(Or.INSTANCE);
		addEvaluableFunctor(Remainder.INSTANCE);
		addEvaluableFunctor(Round.INSTANCE);
		addEvaluableFunctor(RoundFloat.INSTANCE);
		addEvaluableFunctor(ShiftLeft.INSTANCE);
		addEvaluableFunctor(ShiftRight.INSTANCE);
		addEvaluableFunctor(Sign.INSTANCE);
		addEvaluableFunctor(Subtract.INSTANCE);
		addEvaluableFunctor(Truncate.INSTANCE);
		addEvaluableFunctor(TruncateFloat.INSTANCE);
	}
	public static void addEvaluableFunctor(Evaluable functor){
		table.put(functor.getFunctor(),functor);
	}
	public static Evaluable getEvaluableFunctor(String name){
		return table.get(name);
	}
}
