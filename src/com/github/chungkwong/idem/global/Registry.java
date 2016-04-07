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
package com.github.chungkwong.idem.global;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 * @param <K>
 * @param <V>
 */
public class Registry<K,V> implements Map<K,V>{
	private final HashMap<K,V> mapping=new HashMap<>();
	private final List<MapChangedListener> listeners=new ArrayList<>();
	public static final Registry<KeyStroke,Object> KEY_BINDING=new Registry<>();
	public static final Registry<Object,Action> ACTION=new Registry<>();
	public static final Registry<Action,JMenu> MENU=new Registry<>();
	public Registry(){

	}
	@Override
	public int size(){
		return mapping.size();
	}
	@Override
	public boolean isEmpty(){
		return mapping.isEmpty();
	}
	@Override
	public boolean containsKey(Object key){
		return mapping.containsKey(key);
	}
	@Override
	public boolean containsValue(Object value){
		return mapping.containsValue(value);
	}
	@Override
	public V put(K key,V value){
		V val=mapping.put(key,value);
		if(val!=null)
			listeners.forEach((l)->l.entryRemoved(new EntryRemovalEvent<>(this,key)));
		listeners.forEach((l)->l.entryAdded(new EntryAdditionEvent<>(this,key,value)));
		return val;
	}
	@Override
	public V remove(Object key){
		V val=mapping.remove(key);
		if(val!=null)
			listeners.forEach((l)->l.entryRemoved(new EntryRemovalEvent<>(this,key)));
		return val;
	}
	@Override
	public void putAll(Map m){
		m.entrySet().forEach(entry->put(((Map.Entry<K,V>)entry).getKey(),((Map.Entry<K,V>)entry).getValue()));
	}
	@Override
	public void clear(){
		listeners.forEach(l->mapping.forEach((key,value)->l.entryRemoved(new EntryRemovalEvent(this,key))));
		mapping.clear();
	}
	@Override
	public Set keySet(){
		return mapping.keySet();
	}
	@Override
	public Collection values(){
		return mapping.values();
	}
	@Override
	public Set entrySet(){
		return mapping.entrySet();
	}
	@Override
	public V get(Object key){
		return mapping.get(key);
	}
}
