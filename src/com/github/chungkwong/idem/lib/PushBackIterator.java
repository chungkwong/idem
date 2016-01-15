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
package com.github.chungkwong.idem.lib;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class PushBackIterator<T> implements Iterator<T>{
	LinkedList<T> buffer=new LinkedList<>();
	Iterator<T> iter;
	public PushBackIterator(Iterator<T> iter){
		this.iter=iter;
	}
	public void pushBack(T item){
		buffer.addFirst(item);
	}
	public T peek(){
		if(buffer.isEmpty())
			buffer.addFirst(iter.next());
		return buffer.getFirst();
	}
	@Override
	public boolean hasNext(){
		return iter.hasNext()||!buffer.isEmpty();
	}
	@Override
	public T next(){
		return buffer.isEmpty()?iter.next():buffer.removeFirst();
	}
}