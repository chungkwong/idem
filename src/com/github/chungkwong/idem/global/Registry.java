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
import static com.github.chungkwong.idem.global.PreferenceManager.getPreference;
import com.github.chungkwong.idem.gui.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 * @param <K>
 * @param <V>
 */
public class Registry<K,V> implements Map<K,V>{
	private final Map<K,V> mapping=new HashMap<>();
	private final List<MapChangedListener> listeners=new ArrayList<>();
	public static final Registry<KeyStroke,Object> KEY_BINDING=new Registry<>();
	public static final Registry<Object,Action> ACTION=new Registry<>();
	public static final Registry<String,DataLoader> MIME2LOADER=Registry.createObjectRegistry(
			getPreference("MIME2DataLoader","/com/github/chungkwong/idem/resources/MIME.xml"),DataLoader.class);
	public static final Registry<String,String> SUFFIX2MIME=Registry.createStringRegistry(
			getPreference("suffix","/com/github/chungkwong/idem/resources/FILE_SUFFIX.xml"));
	static{

	}
	public Registry(){

	}
	public static Registry<String,String> createStringRegistry(Preferences pref){
		Registry<String,String> registry=new Registry<>();
		try{
			for(String k:pref.keys()){
				registry.put(k,pref.get(k,null));
			}
		}catch(BackingStoreException ex){
			Log.LOG.throwing("Registry","createRegister",ex);
		}
		return registry;
	}
	public static Registry<String,Class> createClassRegistry(Preferences pref,boolean init){
		Registry<String,Class> registry=new Registry<>();
		ClassLoader loader=ClassLoader.getSystemClassLoader();
		try{
			for(String k:pref.keys()){
				String cls=pref.get(k,null);
				if(cls!=null)
					try{
						registry.put(k,Class.forName(cls,init,loader));
					}catch(ClassNotFoundException ex){
						Log.LOG.throwing("Registry","createRegister",ex);
					}
			}
		}catch(BackingStoreException ex){
			Log.LOG.throwing("Registry","createRegister",ex);
		}
		return registry;
	}
	public static <V> Registry<String,V> createObjectRegistry(Preferences pref,Class<V> type){
		Registry<String,V> registry=new Registry<>();
		try{
			for(String k:pref.keys()){
				String cls=pref.get(k,null);
				if(cls!=null)
					try{
						registry.put(k,(V)Class.forName(cls).newInstance());
					}catch(ClassNotFoundException|InstantiationException|IllegalAccessException ex){
						Log.LOG.throwing("Registry","createRegister",ex);
					}
			}
		}catch(BackingStoreException ex){
			Log.LOG.throwing("Registry","createRegister",ex);
		}
		return registry;
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
		Iterator<Map.Entry<K,V>> iter=mapping.entrySet().iterator();
		while(iter.hasNext()){
			Entry<K,V> entry=iter.next();
			iter.remove();
			listeners.forEach(l->l.entryRemoved(new EntryRemovalEvent(this,entry.getKey())));
		}
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
	public V get(K key,V defaultValue){
		return mapping.containsKey(key)?mapping.get(key):defaultValue;
	}
}
