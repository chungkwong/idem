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
public class SimpleIteratorWraper<T> implements Iterator<T>{
	private final SimpleIterator<T> iter;
	private T buffer;
	private boolean ended=false;
	public SimpleIteratorWraper(SimpleIterator<T> iter){
		this.iter=iter;
	}
	@Override
	public boolean hasNext(){
		if(ended)
			return false;
		else if(buffer!=null)
			return true;
		else{
			buffer=iter.next();
			ended=(buffer==null);
			return !ended;
		}
	}
	@Override
	public T next(){
		if(hasNext()){
			T tmp=buffer;
			buffer=null;
			return tmp;
		}else
			throw new NoSuchElementException();
	}
}
