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
import com.github.chungkwong.idem.lib.*;
/**
 *
 * @author kwong
 */
public class Context{
	Partition<Term> bindings;
	public Context(){
		bindings=new Partition<>();
	}
	public Context(Context parent){
		this.bindings=parent.bindings;
	}
	public Partition<Term> getBindings(){
		return bindings;
	}
	public void addBinding(Variable var,Term term){
		bindings.makeSet(var);
		bindings.makeSet(term);
		bindings.union(var,term);
	}
	@Override
	public String toString(){
		return bindings.toString();
	}
	@Override
	public boolean equals(Object obj){
		return obj instanceof Context&&((Context)obj).bindings.equals(bindings);
	}
}