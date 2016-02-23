/*
 * Copyright (C) 2015 Chan Chung Kwong <1m02math@126.com>
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
package com.github.chungkwong.idem.lib.lang.prolog;
import java.util.*;
/**
 * Predication, known subclass including Constant and CompoundTerm
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public abstract class Predication implements Term{
	/**
	 * @return the predicate corresponsing to the predication
	 */
	public abstract Predicate getPredicate();
	/**
	 * @return the arguments of the predication
	 */
	public abstract List<Term> getArguments();
	/**
	 * @param renameTo user should pass a<code>new HashMap<>()</code>
	 * @return a renamed predication
	 */
	public abstract Predication renameAllVariable(HashMap<Variable,Variable> renameTo);
	/**
	 * @param subst substitution to be used
	 * @return substituted copy of the predication
	 */
	public abstract Predication substitute(Substitution subst);
}