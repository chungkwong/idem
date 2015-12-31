package com.github.chungkwong.idem.test;
import java.util.*;
public final class ExternalEnvironment{
	HashMap<String,Object> objects=new HashMap<String,Object>();
	HashSet<Class> classes=new HashSet<Class>();
	public ExternalEnvironment(){

	}
	public void addObject(String key,Object value){
		objects.put(key,value);
	}
	public void getObject(String key){
		objects.get(key);
	}
	public void removeObject(String key){
		objects.remove(key);
	}
	public void addClass(Class c){
		classes.add(c);
	}
	public void removeClass(Class c){
		classes.remove(c);
	}


}
