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
 * Term
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public abstract class Term{
	/**
	 * @return variable set of the term
	 * @see 7.1.1.1 of the standard
	 */
	public abstract Set<Variable> getVariableSet();
	/**
	 * @return existential variable set of the term
	 * @see 7.1.1.3 of the standard
	 */
	public abstract Set<Variable> getExistentialVariableSet();
	/**
	 * @return iterated-goal term of the term
	 * @see 7.1.6.3 of the standard
	 */
	public abstract Term toIteratedTerm();
	/**
	 * @param t
	 * @return if t is variant of the term
	 * @see 7.1.6.1 of the standard
	 */
	public boolean isVariantOf(Term t){
		return isVariantOf(t,new HashMap<>());
	}
	protected abstract boolean isVariantOf(Term t,Map<Variable,Variable> perm);
	/**
	 * Unitify the term with term
	 * @param term
	 * @param subst
	 * @return if the term can unify with term
	 */
	public abstract boolean unities(Term term,Substitution subst);
	/**
	 * @return a renamed copy of the term
	 */
	public Term renameAllVariable(){
		return renameAllVariable(new HashMap<>());
	}
	protected abstract Term renameAllVariable(HashMap<Variable,Variable> renameTo);
	/**
	 * Do substitution
	 * @param subst
	 * @return the resulting term
	 */
	public abstract Term substitute(Substitution subst);
	/**
	 * Replace var with replacement
	 * @param var
	 * @param replacement
	 * @return the resulting term
	 */
	public abstract Term substitute(Variable var,Term replacement);
	/**
	 * @return head of the term
	 * @see 7.6.1 of the standard
	 */
	public abstract Predication toHead();
	/**
	 * @return body of the term
	 * @see 7.6.2 of the standard
	 */
	public abstract Predication toBody();
}
