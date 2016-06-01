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
public class CheckPointIterator<T> implements Iterator<T>{
	private final Iterator<T> src;
	private boolean preread=false;
	private List<T> buffer=new ArrayList<>();
	private int offset=0;
	public CheckPointIterator(Iterator<T> src){
		this.src=src;
	}
	public void startPreread(){
		if(preread){
			throw new IllegalStateException();
		}
		preread=true;
	}
	public void endPreread(boolean forward){
		if(!preread){
			throw new IllegalStateException();
		}
		preread=false;
		if(forward){
			buffer.subList(0,offset).clear();
		}
		offset=0;
	}
	@Override
	public boolean hasNext(){
		return src.hasNext()||offset<buffer.size();
	}
	@Override
	public T next(){
		if(offset<buffer.size()){
			return buffer.get(offset++);
		}else{
			T element=src.next();
			if(preread){
				buffer.add(element);
			}
			return element;
		}
	}
}
