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
package com.github.chungkwong.idem.lib.lang.common.parser;
import java.util.*;
import java.util.function.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ProductionRule{
	private final NonTerminal target;
	private final Symbol[] member;
	private final Function<Object[],Object> action;
	public ProductionRule(NonTerminal target,Symbol[] member,Function<Object[],Object> action){
		this.target=target;
		this.member=member;
		this.action=action;
	}
	public Symbol[] getMember(){
		return member;
	}
	public NonTerminal getTarget(){
		return target;
	}
	public boolean isApplicable(Symbol... comp){
		if(comp.length!=member.length)
			return false;
		for(int i=0;i<comp.length;i++)
			if(!comp[i].getID().equals(member[i].getID()))
				return false;
		return true;
	}
	public SymbolInstance apply(SymbolInstance... comp){
		return new SymbolInstance(target,action.apply(Arrays.stream(comp).map(SymbolInstance::getSemanticValue).toArray()));
	}
}