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
public class InvertibleMap<K,V> implements Map<K,V>{
	private final HashMap<K,V> directMap=new HashMap<>();
	private final HashMap<V,K> inverseMap=new HashMap<>();

	@Override
	public int size(){
		return directMap.size();
	}
	@Override
	public boolean isEmpty(){
		return directMap.isEmpty();
	}
	@Override
	public boolean containsKey(Object key){
		return directMap.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value){
		return inverseMap.containsKey(value);
	}
	@Override
	public V get(Object key){
		return directMap.get(key);
	}
	public K getKey(Object key){
		return inverseMap.get(key);
	}
	@Override
	public V put(K key,V value){
		V val=directMap.put(key,value);
		inverseMap.put(val,key);
		return val;
	}
	@Override
	public V remove(Object key){
		V val=directMap.remove(key);
		inverseMap.remove(val);
		return val;
	}
	public K removeValue(Object key){
		K val=inverseMap.remove(key);
		directMap.remove(val);
		return val;
	}
	@Override
	public void putAll(Map<? extends K,? extends V> m){
		directMap.putAll(m);
		m.entrySet().stream().forEach((entry)->inverseMap.put(entry.getValue(),entry.getKey()));
	}
	@Override
	public void clear(){
		directMap.clear();
		inverseMap.clear();
	}
	@Override
	public Set<K> keySet(){
		return directMap.keySet();
	}
	@Override
	public Collection<V> values(){
		return inverseMap.keySet();
	}
	@Override
	public Set<Entry<K,V>> entrySet(){
		return directMap.entrySet();
	}
}
