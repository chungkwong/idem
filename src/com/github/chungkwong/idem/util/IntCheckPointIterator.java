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
public class IntCheckPointIterator implements PrimitiveIterator.OfInt{
	private final PrimitiveIterator.OfInt src;
	boolean preread=false;
	IntList buffer=new DefaultIntList();
	int offset=0;
	public IntCheckPointIterator(PrimitiveIterator.OfInt src){
		this.src=src;
	}
	public void startPreread(){
		if(preread){
			throw new IllegalStateException();
		}
		preread=true;
	}
	public int[] endPrereadForward(){
		if(!preread){
			throw new IllegalStateException();
		}
		preread=false;
		IntList read=buffer.subList(0,offset);
		int[] text=read.toArray();
		read.clear();
		offset=0;
		return text;
	}
	public void endPrereadBackward(){
		if(!preread){
			throw new IllegalStateException();
		}
		preread=false;
		offset=0;
	}
	@Override
	public boolean hasNext(){
		return src.hasNext()||offset<buffer.size();
	}
	@Override
	public int nextInt(){
		if(offset<buffer.size()){
			return buffer.get(offset++);
		}else{
			Integer element=src.next();
			if(preread){
				buffer.add(element);
			}
			return element;
		}
	}
	public int peek(){
		if(preread){
			throw new IllegalStateException();
		}
		startPreread();
		int val=next();
		endPrereadBackward();
		return val;
	}
}