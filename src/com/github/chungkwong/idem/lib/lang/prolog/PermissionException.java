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

package com.github.chungkwong.idem.lib.lang.prolog;
/**
 * permission_error
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PermissionException extends PrologException{
	private final Term operation,objectType,culprit;
	/**
	 * Construct a permission_error
	 * @param operation the operation refused
	 * @param objectType type of the object involve
	 * @param culprit the argment or one of its components which cause the error
	 */
	public PermissionException(Term operation,Term objectType,Term culprit){
		this.operation=operation;
		this.objectType=objectType;
		this.culprit=culprit;
	}
	@Override
	public Term getErrorTerm(){
		return new CompoundTerm("permission_error",operation,objectType,culprit);
	}
}