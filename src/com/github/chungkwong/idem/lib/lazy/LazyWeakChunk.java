/*
 * Copyright (C) 2015 Chan Chung Kwong <1m02math@126.com>
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
package com.github.chungkwong.idem.lib.lazy;
import com.github.chungkwong.idem.util.*;
import java.lang.ref.*;
import java.util.concurrent.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 * @param <T>
 */
public class LazyWeakChunk<T> implements Chunk<T>{
	private final Callable<T> proc;
	private SoftReference<Pack<T>> ref;
	public LazyWeakChunk(Callable<T> proc){
		this.proc=proc;
	}
	@Override
	public synchronized T get(){
		Pack<T> buf=ref==null?null:ref.get();
		if(buf==null){
			try{
				T obj=proc.call();
				ref=new SoftReference(new Pack<>(obj));
				return obj;
			}catch(Exception ex){
				throw new RuntimeException(ex);
			}
		}else
			return buf.get();
	}
}