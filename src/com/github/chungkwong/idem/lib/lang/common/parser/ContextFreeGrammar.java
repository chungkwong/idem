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
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ContextFreeGrammar{
	private final Symbol startSymbol;
	private final List<ProductionRule> rules;
	public ContextFreeGrammar(Symbol startSymbol,List<ProductionRule> rules){
		this.startSymbol=startSymbol;
		this.rules=rules;
	}
	public Symbol getStartSymbol(){
		return startSymbol;
	}
	public List<ProductionRule> getRules(){
		return rules;
	}
	@Override
	public String toString(){
		return rules.stream().map(ProductionRule::toString).collect(Collectors.joining("\n",startSymbol.getName()+"\n",""));
	}

}
