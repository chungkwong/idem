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
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class UnifyWithOccurCheck extends BuildinPredicate{
	public static final Var INSTANCE=new Var();
	public static final Predicate pred=new Predicate("unify_with_occurs_check",2);
	@Override
	public boolean activate(List<Term> arguments,Processor exec){
		Substitution subst=new Substitution();
		return arguments.get(0).unities(arguments.get(1),subst)&&!subst.occurCheck();
	}
	@Override
	public Predicate getPredicate(){
		return pred;
	}
}