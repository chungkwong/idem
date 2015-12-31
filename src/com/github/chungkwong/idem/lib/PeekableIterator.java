/*
 * Copyright (C) 2015 kwong
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

public class PeekableIterator<T> implements ListIterator<T>{
	public static final PeekableIterator EMPTY=new PeekableIterator(Collections.EMPTY_LIST.listIterator());
	ListIterator<T> iter;
	public PeekableIterator(ListIterator<T> iter){
		this.iter=iter;
	}
	public T peek(){
		T curr=iter.next();
		iter.previous();
		return curr;
	}
	@Override
	public boolean hasNext(){
		return iter.hasNext();
	}
	@Override
	public T next(){
		return iter.next();
	}
	@Override
	public boolean hasPrevious(){
		return iter.hasPrevious();
	}
	@Override
	public T previous(){
		return iter.previous();
	}
	@Override
	public int nextIndex(){
		return iter.nextIndex();
	}
	@Override
	public int previousIndex(){
		return iter.previousIndex();
	}
	@Override
	public void remove(){
		iter.remove();
	}
	@Override
	public void set(T e){
		iter.set(e);
	}
	@Override
	public void add(T e){
		iter.add(e);
	}
}