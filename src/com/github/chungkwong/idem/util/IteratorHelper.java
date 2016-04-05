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
public class IteratorHelper{
	public static boolean isEmpty(Iterator<?> iter){
		return !iter.hasNext();
	}
	public static Object get(Iterator<?> iter,int index){
		while(--index>=0)
			iter.next();
		return iter.next();
	}
	public static int indexOf(Iterator<?> iter,Object obj){
		int index=0;
		while(iter.hasNext())
			if(iter.next().equals(obj))
				return index;
			else
				++index;
		return -1;
	}
	public static int length(Iterator<?> iter){
		int len=0;
		while(iter.hasNext()){
			iter.next();
			++len;
		}
		return len;
	}
	public static Enumeration toEnumeration(Iterator<?> iter){
		return new Enumeration(){
			@Override
			public boolean hasMoreElements(){
				return iter.hasNext();
			}
			@Override
			public Object nextElement(){
				return iter.next();
			}
		};
	}
}