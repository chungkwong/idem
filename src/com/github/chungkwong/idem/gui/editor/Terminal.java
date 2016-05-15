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
package com.github.chungkwong.idem.gui.editor;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Terminal implements Symbol{
	private final String id,name;
	public Terminal(String name){
		this(name,name);
	}
	public Terminal(String id,String name){
		this.id=id;
		this.name=name;
	}
	@Override
	public String getID(){
		return id;
	}
	@Override
	public String getName(){
		return name;
	}
	@Override
	public boolean equals(Object obj){
		return obj instanceof Terminal&&id.equals(((Terminal)obj).id);
	}
	@Override
	public int hashCode(){
		int hash=5;
		hash=53*hash+Objects.hashCode(this.id);
		return hash;
	}
}