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
package com.github.chungkwong.idem.util;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Pair<K,V>{
	private final K first;
	private final V second;
	public Pair(K first,V second){
		this.first=first;
		this.second=second;
	}
	public K getFirst(){
		return first;
	}
	public V getSecond(){
		return second;
	}
	@Override
	public boolean equals(Object obj){
		return obj instanceof Pair&&((Pair)obj).first.equals(first)&&((Pair)obj).second.equals(second);
	}
	@Override
	public int hashCode(){
		int hash=5;
		hash=89*hash+Objects.hashCode(this.first);
		hash=89*hash+Objects.hashCode(this.second);
		return hash;
	}
	@Override
	public String toString(){
		return "("+first+","+second+")";
	}

}